package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class SudokuSolver {
    private JFrame frame;
    private JPanel panel;
    private JTextField[][] textFields;
    private JButton solveButton;
    private JButton clearButton;
    private PencilAndPaper pencilAndPaper;

    public SudokuSolver() {
        frame = new JFrame("Sudoku Solver");
        panel = new JPanel(new GridLayout(3, 3));
        textFields = new JTextField[9][9];
        solveButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        pencilAndPaper = null;

        // Initialize tfs
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                textFields[i][j] = new JTextField(1);
                textFields[i][j].setFont(new Font("Arial", Font.BOLD, 50));
                textFields[i][j].setBackground(Color.RED); 
                textFields[i][j].setForeground(new Color(128, 0, 128)); 
                textFields[i][j].setHorizontalAlignment(JTextField.CENTER); 
                // Limit
                PlainDocument doc = new PlainDocument();
                doc.setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                            throws BadLocationException {
                        if (fb.getDocument().getLength() + string.length() <= 1) {
                            super.insertString(fb, offset, string, attr);
                        }
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                            throws BadLocationException {
                        if (fb.getDocument().getLength() + text.length() - length <= 1) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }
                });

                textFields[i][j].setDocument(doc);
                //key navigation
                textFields[i][j].addKeyListener(new ArrowKeyListener());
                panel.add(textFields[i][j]);
            }
        }

        // bg
        panel.setBackground(new Color(240, 240, 240)); 

        // border for 3x3 grids
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JPanel subPanel = new JPanel(new GridLayout(3, 3));
                subPanel.setBorder(blackBorder);
                for (int k = i * 3; k < i * 3 + 3; k++) {
                    for (int l = j * 3; l < j * 3 + 3; l++) {
                        subPanel.add(textFields[k][l]);
                    }
                }
                panel.add(subPanel);
            }
        }

        //solve button action listener
        solveButton.addActionListener(e -> {
            int[][] puzzle = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    try {
                        String input = textFields[i][j].getText().trim();
                        if (input.isEmpty()) {
                            puzzle[i][j] = 0;
                        } else {
                            puzzle[i][j] = Integer.parseInt(input);
                            if (puzzle[i][j] < 1 || puzzle[i][j] > 9) {
                                throw new NumberFormatException();
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid input entered. Please enter a number between 1 and 9.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            pencilAndPaper = new PencilAndPaper(puzzle,this);
            int[][] solvedPuzzle = pencilAndPaper.getPuzzle();
            updateTextFields(solvedPuzzle);
        });

        //clear button action listener
        clearButton.addActionListener(e -> {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    textFields[i][j].setText("");
                }
            }
        });

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateTextFields(int[][] puzzle) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                textFields[i][j].setText(puzzle[i][j] == 0 ? "" : String.valueOf(puzzle[i][j]));
            }
        }
    }

    public static void main(String[] args) {
        new SudokuSolver();
    }

    public class ArrowKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int row = -1;
            int col = -1;
            outerloop:
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (textFields[i][j] == e.getSource()) {
                        row = i;
                        col = j;
                        break outerloop;
                    }
                }
            }

            if (row != -1 && col != -1) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        row = Math.max(0, row - 1);
                        break;
                    case KeyEvent.VK_DOWN:
                        row = Math.min(8, row + 1);
                        break;
                    case KeyEvent.VK_LEFT:
                        col = Math.max(0, col - 1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        col = Math.min(8, col + 1);
                        break;
                }
                textFields[row][col].requestFocus();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
