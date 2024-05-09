package sudoku;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class PencilAndPaper {
    private int[][] puzzle;
    private int counter;
    private boolean isValidInput;
    private SudokuSolver gui;

    public PencilAndPaper(int[][] puzzle, SudokuSolver gui) {
        this.puzzle = puzzle;
        this.counter = 0;
        this.isValidInput = true;
        this.gui = gui;
        solve();
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public int getCounter() {
        return counter;
    }

    private void solve() {
        isValidInput = true; // Reset isValidInput to true for each new puzzle

        if (!isValidInput) {
            Object[] options = { "Retry" };
            int choice = JOptionPane.showOptionDialog(null, "Invalid input entered. Please enter numbers between 1 and 9.", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                System.out.println("Retry button clicked. Resetting puzzle...");
                clearPuzzle();
            }
            return;
        }

        if (solveSudoku(puzzle)) {
            System.out.println("Sudoku puzzle solved successfully.");
        } else {
            System.out.println("No solution found for the Sudoku puzzle.");
        }
    }

    private void clearPuzzle() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                puzzle[i][j] = 0;
            }
        }
        gui.updateTextFields(puzzle);
    }

    private boolean solveSudoku(int[][] grid) {
        ArrayList<Integer> emptyCell = findEmptyCell(grid);
        int row = emptyCell.get(0);
        int col = emptyCell.get(1);

        if (row == -1 || col == -1) {
            return true; // All cells are filled, puzzle solved
        }

        for (int num = 1; num <= 9; num++) {
            if (isValidMove(grid, row, col, num)) {
                grid[row][col] = num;
                counter++;

                if (solveSudoku(grid)) {
                    return true; // Puzzle solved recursively
                }

                grid[row][col] = 0; // Backtrack
            }
        }

        return false; // No valid number for this cell
    }

    private ArrayList<Integer> findEmptyCell(int[][] grid) {
        ArrayList<Integer> cell = new ArrayList<>();
        cell.add(-1);
        cell.add(-1);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    cell.set(0, i);
                    cell.set(1, j);
                    return cell;
                }
            }
        }

        return cell;
    }

    private boolean isValidMove(int[][] grid, int row, int col, int num) {
        if (num < 1 || num > 9 || isRepeatedInRow(grid, row, num) || isRepeatedInColumn(grid, col, num) || isRepeatedInBox(grid, row - row % 3, col - col % 3, num)) {
            isValidInput = false;
            return false;
        }
        return true;
    }

    private boolean isRepeatedInRow(int[][] grid, int row, int num) {
        for (int col = 0; col < grid.length; col++) {
            if (grid[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean isRepeatedInColumn(int[][] grid, int col, int num) {
        for (int row = 0; row < grid.length; row++) {
            if (grid[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean isRepeatedInBox(int[][] grid, int boxStartRow, int boxStartCol, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (grid[row + boxStartRow][col + boxStartCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }
}
