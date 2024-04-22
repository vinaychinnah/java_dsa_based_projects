import java.util.*;

public class OptimizedSudokuSolver {

    private static int[][] board;
    private static int boardSize;
    private static List<Integer>[][] candidates;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the size of the Sudoku board (e.g., 4 for 4x4, 9 for 9x9): ");
        boardSize = scanner.nextInt();

        board = new int[boardSize][boardSize];
        candidates = new ArrayList[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                candidates[i][j] = new ArrayList<>();
            }
        }

        System.out.println("Enter the Sudoku board values (use 0 for empty cells):");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int value = scanner.nextInt();
                board[i][j] = value;
                if (value == 0) {
                    for (int num = 1; num <= boardSize; num++) {
                        candidates[i][j].add(num);
                    }
                }
            }
        }

        if (solveSudoku()) {
            System.out.println("\nSudoku board solved:");
            printBoard();
        } else {
            System.out.println("\nNo solution exists for the given Sudoku board.");
        }
    }

    private static boolean solveSudoku() {
        while (!isSolved()) {
            if (!propagateConstraints()) {
                return false; // Unsatisfiable board
            }
            if (!makeMove()) {
                return false; // Stuck, need to backtrack
            }
        }
        return true; // Sudoku solved
    }

    private static boolean isSolved() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean propagateConstraints() {
        boolean changed = false;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    changed |= propagateConstraintsForCell(i, j);
                }
            }
        }
        return changed;
    }

    private static boolean propagateConstraintsForCell(int row, int col) {
        boolean changed = false;
        List<Integer> candidateList = candidates[row][col];
        for (int candidate : new ArrayList<>(candidateList)) {
            if (!isValidMove(row, col, candidate)) {
                candidateList.remove((Integer) candidate);
                changed = true;
            }
        }
        return changed;
    }

    private static boolean makeMove() {
        boolean progress = false;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0 && candidates[i][j].size() == 1) {
                    int value = candidates[i][j].get(0);
                    board[i][j] = value;
                    candidates[i][j].clear();
                    progress = true;
                }
            }
        }
        return progress;
    }

    private static boolean isValidMove(int row, int col, int num) {
        for (int i = 0; i < boardSize; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        int subgridSize = (int) Math.sqrt(boardSize);
        int startRow = row - row % subgridSize;
        int startCol = col - col % subgridSize;
        for (int i = 0; i < subgridSize; i++) {
            for (int j = 0; j < subgridSize; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
