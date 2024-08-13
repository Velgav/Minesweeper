package Minesweeper;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
  private static final int SIZE = 10;
  private static final int MINES = 10;
  private static final char MINE = '*';
  private static final char EMPTY = '0';
  private static final char UNREVEALED = '.';
  private static final char FLAG = 'F';

  private char[][] actualBoard;
  private char[][] displayBoard;
  private boolean[][] revealed;
  private boolean gameIsOver;
  private int cellsRevealed;

  public Minesweeper() {
    actualBoard = new char[SIZE][SIZE];
    displayBoard = new char[SIZE][SIZE];
    revealed = new boolean[SIZE][SIZE];
    gameIsOver = false;
    cellsRevealed = 0;

    boardInitialization();
    findPlacesOfMines();
    calculateNumbersOfMines();
  }

  private void boardInitialization() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        actualBoard[i][j] = EMPTY;
        displayBoard[i][j] = UNREVEALED;
        revealed[i][j] = false;
      }
    }
  }

  private void findPlacesOfMines() {
    Random random = new Random();
    int minesPlaced = 0;

    while (minesPlaced < MINES) {
      int x = random.nextInt(SIZE);
      int y = random.nextInt(SIZE);

      if (actualBoard[x][y] != MINE) {
        actualBoard[x][y] = MINE;
        minesPlaced++;
      }
    }
    
  }

  private void calculateNumbersOfMines() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        if (actualBoard[i][j] == MINE) {
          continue;
        }

        int mineCount = 0;

        for (int x = -1; x <= 1; x++) {
          for (int y = -1; y <= 1; y++) {
            int storeX = i + x;
            int storeY = j + y;

            if (storeX >= 0 && storeX < SIZE && storeY >= 0 && storeY < SIZE &&
                actualBoard[storeX][storeY] == MINE) {
              mineCount++;
            }
          }
        }

        actualBoard[i][j] = (char) (mineCount + '0');
      }
    }
  }

  public void playTheGame() {
    Scanner scanner = new Scanner(System.in);

    while (!gameIsOver) {
      printBoard();

      try {
        System.out.print("Enter coordinates (row and column) and action (R for reveal, F for flag/unflag): ");
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        char action = scanner.next().toUpperCase().charAt(0);

        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
          System.out.println("Invalid coordinates. Try again.");
          continue;
        }

        if (action == 'R') {
          if (displayBoard[row][col] == FLAG) {
            System.out.println("This cell is flagged. Unflag it before revealing.");
            continue;
          }

          if (revealed[row][col]) {
            System.out.println("This cell is already revealed. Try again.");
            continue;
          }

          if (actualBoard[row][col] == MINE) {
            gameIsOver = true;
            System.out.println("Boom! You hit a mine. Game over.");
            revealAllMines();
            printBoard();
            break;
          } else {
            revealCell(row, col);

            if (cellsRevealed == SIZE * SIZE - MINES) {
              System.out.println("Congratulations! You've revealed all non-mine cells. You win!");
              gameIsOver = true;
              printBoard();
            }
          }
        } else if (action == 'F') {
          if (displayBoard[row][col] == FLAG) {
            displayBoard[row][col] = UNREVEALED;
          } else if (displayBoard[row][col] == UNREVEALED) {
            displayBoard[row][col] = FLAG;
          } else {
            System.out.println("This cell is already revealed. You cannot flag it.");
          }
        } else {
          System.out.println("Invalid action. Use R for reveal and F for flag/unflag.");
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter integers for coordinates.");
        scanner.next();
      }
    }

    scanner.close();
  }

  private void revealCell(int row, int col) {
    if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col]) {
      return;
    }

    revealed[row][col] = true;
    displayBoard[row][col] = actualBoard[row][col];
    cellsRevealed++;

    if (actualBoard[row][col] == '0') {
      for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
          if (x != 0 || y != 0) {
            revealCell(row + x, col + y);
          }
        }
      }
    }
  }

  private void revealAllMines() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        if (actualBoard[i][j] == MINE) {
          displayBoard[i][j] = MINE;
        }
      }
    }
  }

  private void printBoard() {
    System.out.println("  0 1 2 3 4 5 6 7 8 9");

    for (int i = 0; i < SIZE; i++) {
      System.out.print(i + " ");

      for (int j = 0; j < SIZE; j++) {
        System.out.print(displayBoard[i][j] + " ");
      }

      System.out.println();
    }
    
    
  }

}
