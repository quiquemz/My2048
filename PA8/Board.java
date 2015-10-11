//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  Brandon Williams                                        //
// Date:    1/17/15                                                 //
//------------------------------------------------------------------//

/**     Sample Board
 *
 *      0   1   2   3
 *  0   -   -   -   -
 *  1   -   -   -   -
 *  2   -   -   -   -
 *  3   -   -   -   -
 *
 *  The sample board shows the index values for the columns and rows
 */

/*Name: Saul Mendez 
Login: cs8bapm
Date: February 3, 2015
File: Board.java 
Sources of Help: - Introduction to Java Programming by Daniel Lang (10th ed.)
                 - http://docs.oracle.com/javase/7/docs/api/
Description: this program is to create Board objects. It either creates a new
grid with its n number of tiles, two random numbers, and score; or it creates
an existing grid with its existing numbers, and score. It uses certain methods 
that let you play the game 2048.
*/ 
import java.util.*;
import java.io.*;

public class Board
{
  public final int NUM_START_TILES = 2;
  public final int TWO_PROBABILITY = 90;
  public final int GRID_SIZE;
  public final int HIGH_PROB_NUM = 2;
  public final int LOW_PROB_NUM = 4;
  public final int PERCENTAGE = 100;
  public final int NUM_TO_WIN = 2048;

  private final Random random;
  private int[][] grid;
  private int score;


  /* Name: Board(int boardSize, Random random)
   * Purpose:Constructs a fresh board with random tiles.
   * Parameters: boardSize - integer the board size
   *             random - Random object to create random numbers.
   * Return: -
   * */
  public Board(int boardSize, Random random)
  {
    this.random = random; 
    GRID_SIZE = boardSize;
    score = 0;
    grid = new int[GRID_SIZE][GRID_SIZE];
    for (int i = 0; i < NUM_START_TILES; i++){
      addRandomTile();
    }
  }

  // PA3
  /* Name: Board(String inputBoard, Random random)   
   * Purpose: Construct a board based off of an input file 
   * Parameters: inputBoard - board file that has been previously saved.
   *             random - Random object to create random numbers.
   * Return: -
   * */
  public Board(String inputBoard, Random random) throws IOException
  {
    this.random = random;
    File sourceFile = new File(inputBoard);
    Scanner input = new Scanner(sourceFile);
    GRID_SIZE = input.nextInt();
    score = input.nextInt();
    grid = new int[GRID_SIZE][GRID_SIZE];

    // for loops to go through the values of the existing grid 
    for (int row = 0; row < grid.length; row++){
      for (int column = 0; column < grid[row].length; column++){
        grid[row][column] = input.nextInt();
      }  
    }
    input.close();
  }

  // PA3
  /* Name: saveBoard(String outputBoard)  
   * Purpose: Saves the current board to a file
   * Parameters: outputBoard - String where the file will be saved.
   * Return: void
   * */
  public void saveBoard(String outputBoard) throws IOException
  { 
    PrintWriter output = new PrintWriter(new File (outputBoard));
    output.println(GRID_SIZE);
    output.println(score);
    // for loops to go through the values of the existing grid
    for (int row = 0; row < GRID_SIZE; row++){
      for (int column = 0; column < GRID_SIZE; column++){
        output.print(grid[row][column] + " ");
      }
      output.println();
    }
    output.close();
  }

  // TODO PA3
  /* Name: saveBoard(String outputBoard)  
   * Purpose: Adds a Random Tile (of value 2 or 4) to a Random Empty space on 
   * the board.
   * Parameters: None
   * Return: void
   * */
  public void addRandomTile(){
    int location = 0;
    int value = 0;
    int count = 0; 
    ArrayList<Integer> gridArray = new ArrayList<Integer>();
    int arraySize = GRID_SIZE * GRID_SIZE;

    //for loop to get the values from the existing grid
    for (int i = 0; i < arraySize; i++){
      // if statement to check if the value in the grid equals to 0
      if (grid[i/GRID_SIZE][i%GRID_SIZE] == 0){
        gridArray.add(i);
      }  
    }
    // if statement to check if there are any values in the ArrayList
    if (gridArray.size() > 0){ 
      count = random.nextInt(gridArray.size());
      location = gridArray.get(count);
      value = random.nextInt(PERCENTAGE); //Percentage is the number 100
      if (value < TWO_PROBABILITY){
        grid[ location/GRID_SIZE ][ location%GRID_SIZE ] = HIGH_PROB_NUM;// = 2
      }
      else {
        grid[ location/GRID_SIZE ][ location%GRID_SIZE ] = LOW_PROB_NUM;// = 4
      }
    } 
  }

  // PA4
  /* Name: isGameOver() 
   * Purpose: Check to see if we have a game over
   * Parameters: None
   * Return: true - if the game is over
   *         false - if the game is not over
   * */
  public boolean isGameOver()
  { 
    // if statement to check if there is no possible move to be made 
    if(canMove(Direction.LEFT) == false && canMove(Direction.RIGHT) == false &&
        canMove(Direction.DOWN) == false && canMove(Direction.UP) == false){
      System.out.println("GAME IS OVER!");
      return true;
    }
    return false;
  }

  /* Name: canMoveLeft()   
   * Purpose: Check to see if you can move left.
   * Parameters: None
   * Return: true - if you can move left
   *         false - if you cannot move left
   * */
  private boolean canMoveLeft()
  { 
    // for loops to run through the whole grid 
    for (int row = 0; row < grid.length; row++){
      for (int column = 0; column < grid[row].length; column++){
        // if statement for no Runtime Error to occur
        if (column > 0){
          int leftTile = grid[row][column-1];
          // if statement to check if the value in the grid is not 0.
          if (grid[row][column] != 0){
            // if statement to check if the value in the grid is the same
            // to the previous one on the left
            if (grid[row][column] == leftTile || leftTile == 0){
              return true;
            }
          }
        }
      } 
    }
    return false;
  }

  /* Name: canMoveRight()  
   * Purpose: Check to see if you can move right.
   * Parameters: None
   * Return: true - if you can move right
   *         false - if you cannot move right
   * */
  private boolean canMoveRight()
  {  
    // for loops to run through the whole grid
    for (int row = 0; row < grid.length; row++){
      for (int column = 0; column < grid[row].length-1; column++){
        // if statement for no Runtime Error to occur
        if (column < grid.length){
          int rightTile = grid[row][column+1];
          // if statement to check if the value in the grid is not 0.
          if (grid[row][column] != 0){
            // if statement to check if the value in the grid is the same
            // to the next one on the right
            if (grid[row][column] == rightTile || rightTile == 0){
              return true;
            }
          }
        }
      } 
    }
    return false;
  }

  /* Name: canMoveUp()  
   * Purpose: Check to see if you can move up.
   * Parameters: None
   * Return: true - if you can move up
   *         false - if you cannot move up
   * */
  private boolean canMoveUp()
  {  
    // for loops to run through the whole grid
    for (int row = 0; row < grid.length; row++){
      for (int column = 0; column < grid[row].length; column++){
        // if statement for no Runtime Error to occur
        if (row > 0){
          int upTile = grid[row-1][column];
          // if statement to check if the value in the grid is not 0.
          if (grid[row][column] != 0){
            // if statement to check if the value in the grid is the same
            // to the one above it
            if (grid[row][column] == upTile || upTile == 0){
              return true;
            }
          }
        }
      } 
    }
    return false;
  }

  /* Name: canMoveDown()  
   * Purpose: Check to see if you can move down.
   * Parameters: None
   * Return: true - if you can move down
   *         false - if you cannot move down
   * */
  private boolean canMoveDown()
  {   
    // for loops to run through the whole grid
    for (int row = 0; row < grid.length-1; row++){
      for (int column = 0; column < grid[row].length; column++){

        // if statement for no Runtime Error to occur
        if (row < grid[row].length){
          int downTile = grid[row+1][column];

          // if statement to check if the value in the grid is not 0.
          if (grid[row][column] != 0){

            // if statement to check if the value in the grid is the same
            // to the one below it
            if (grid[row][column] == downTile || downTile == 0){
              return true;
            }
          }
        }
      } 
    }
    return false;
  }

  // PA4
  /* Name: canMove(Direction direction)   
   * Purpose: Determine if we can move in a given direction
   * Parameters: direction - the direction where to move, either:
   *                          UP, DOWN, LEFT, OR RIGHT
   * Return: true - if you can move in any direction
   *         false - if you cannot move in any direction
   * */      
  public boolean canMove(Direction direction)
  {
    if (direction == Direction.LEFT){
      if (canMoveLeft()){
        return true;
      }
    }

    else if (direction == Direction.RIGHT){
      if (canMoveRight()){
        return true;
      }
    }

    else if (direction == Direction.UP){
      if (canMoveUp()){
        return true;
      }
    }

    else if (direction == Direction.DOWN){
      if (canMoveDown()){
        return true;
      }
    }

    return false;
  }

  /* Name:moveLeft()  
   * Purpose: Moves the values in the grid LEFT, applying the rules of 2048.
   * Parameters: None
   * Return: void
   * */
  private void moveLeft(){
    //for loop to run through the rows 
    for (int row = 0; row < grid.length; row++){
      //ArrayList created to modify row by row
      ArrayList<Integer> rowArray = new ArrayList<Integer>();

      //for loop to run through the columns.
      for (int column = 0; column < grid[row].length; column++){
        // if statement to add the numbers of the row, other than 0, to the
        // ArrayList
        if (grid[row][column] != 0){
          rowArray.add(grid[row][column]);
        }
      }

      //for loop to run through the ArrayList and modify the numbers in it.
      for (int i = 0; i < rowArray.size() - 1; i++){
        // if statement to compare the current value of the ArrayList with the
        // next one to the right
        if (rowArray.get(i).equals(rowArray.get(i+1))){
          int sum = rowArray.get(i) + rowArray.get(i);
          rowArray.set(i, sum);
          rowArray.remove(i+1);
          score = score + sum;
        } 
      }

      //for loop to run through the columns 
      for (int column = 0; column < grid[row].length; column++){
        grid[row][column] = 0; //resets the board
        // if statement created so there is no Runtime Error
        if (column < rowArray.size()){
          grid[row][column] = rowArray.get(column);
        }
      }
    }
  }

  /* Name:moveRight()  
   * Purpose: Moves the values in the grid RIGHT, applying the rules of 2048.
   * Parameters: None
   * Return: void
   * */
  public void moveRight(){
    // for loop to run through the rows of the grid
    for (int row = 0; row < grid.length; row++){ 
      //ArrayList created to modify row by row
      ArrayList<Integer> rowArray = new ArrayList<Integer>();
      
      //for loop to run through the columns.
      for (int column = 0; column < grid[row].length; column++){
        // if statement to add the numbers of the row, other than 0, to the
        // ArrayList
        if (grid[row][column] != 0){
          rowArray.add(grid[row][column]);
        }
      }
      
      //for loop to run through the ArrayList and modify the numbers in it
      for (int i = rowArray.size()-1; i > 0; i--){
        // if statement to compare the current value of the ArrayList with the
        // previous one to the left
        if (rowArray.get(i).equals(rowArray.get(i-1))){
          int sum = rowArray.get(i) + rowArray.get(i);
          rowArray.set(i, sum);
          rowArray.remove(i-1);
          score = score + sum;
          i--;
        } 
      }

      //for loop to run through the columns of the grid and modify them
      for (int column = grid[row].length-1, i=1; column >= 0; column--, i++){
        grid[row][column] = 0; //resets the board
        // if statement created so there is no Runtime Error
        if (i <= rowArray.size()){ 
          grid[row][column] = rowArray.get(rowArray.size()-i);
        }
      }
    }
  } 

  /* Name:moveUp()  
   * Purpose: Moves the values in the grid UP, applying the rules of 2048.
   * Parameters: None
   * Return: void
   * */
  private void moveUp(){
    // for loop to run through the columns of the grid
    for (int column = 0; column < grid.length; column++){ 
      //ArrayList created to modify column by column
      ArrayList<Integer> columnArray = new ArrayList<Integer>();

      //for loop to run through the rows
      for (int row = 0; row < grid.length; row++){
        // if statement to add the numbers of the column, other than 0, to the
        // ArrayList
        if (grid[row][column] != 0){
          columnArray.add(grid[row][column]);
        }
      }
      
      // for loop to run through the ArrayList and modify the numbers in it
      for (int i = 0; i < columnArray.size()-1; i++){
        // if statement to compare the current value of the ArrayList with the
        // previous one to the right (the one below)
        if (columnArray.get(i).equals(columnArray.get(i+1))){
          int sum = columnArray.get(i) + columnArray.get(i);
          columnArray.set(i, sum);
          columnArray.remove(i+1);
          score = score + sum;
        } 
      }

      //for loop to run through the rows of the grid
      for (int row = 0; row < grid.length; row++){ 
        grid[row][column] = 0; //resets the board
        // if statement created so there is no Runtime Error
        if (row < columnArray.size()){
          grid[row][column] = columnArray.get(row);
        }
      }
    }
  }

  /* Name:moveDown()  
   * Purpose: Moves the values in the grid UP, applying the rules of 2048.
   * Parameters: None
   * Return: void
   * */
  private void moveDown(){
    // for loop to run through the columns of the grid
    for (int column = 0; column < grid.length; column++){ 
      //ArrayList created to modify column by column
      ArrayList<Integer> columnArray = new ArrayList<Integer>();

      //for loop to run through the rows of the grid
      for (int row = 0; row < grid.length; row++){
        // if statement to add the numbers of the column, other than 0, to the
        // ArrayList
        if (grid[row][column] != 0){
          columnArray.add(grid[row][column]);
        }
      }
     // for loop to run through the ArrayList and modify the numbers in it
      for (int i = columnArray.size()-1; i >= 0; i--){
        // if statement to compare the current value of the ArrayList with the
        // previous one to the left (the one above)
        if (i > 0 && columnArray.get(i).equals(columnArray.get(i-1))){
          int sum = columnArray.get(i) + columnArray.get(i);
          columnArray.set(i, sum);
          columnArray.remove(i-1);
          score = score + sum;
          i--;
        } 
      }
      //for loop to run through the rows of the grid and modify them
      for (int row = grid.length-1, i = 1; row >= 0; row--, i++){ 
        grid[row][column] = 0; //resets the board
        // if statement created so there is no Runtime Error
        if (i <= columnArray.size()){
          grid[row][column] = columnArray.get(columnArray.size()-i);
        }
      }
    }
  }

  // PA4
  /* Name: canMove(Direction direction)   
   * Purpose: Perform a move Operation in a given direction
   * Parameters: direction - the direction where to move, either:
   *                          UP, DOWN, LEFT, OR RIGHT
   * Return: true - if you moved in the given direction
   *         false - if you don't move
   * */ 
  public boolean move(Direction direction)
  {
    if (direction == Direction.LEFT){
      moveLeft();
      return true;
    }

    if (direction == Direction.RIGHT){
      moveRight();
      return true;
    }

    if (direction == Direction.UP){
      moveUp();
      return true;
    }

    if (direction == Direction.DOWN){
      moveDown();
      return true;
    }

    return false;
  }

  /* Name: youWon()   
   * Purpose: Displays a message if the user Won the game.
   * Parameters: None
   * Return: true - if the user reached 2048 or won
   *         false - if the user hasn't reached 2048
   * */ 
  public boolean youWon(){
    for (int row = 0; row < grid.length; row++){
      for (int column = 0; column < grid[row].length; column++){
        if (grid[row][column] == NUM_TO_WIN){ // NUM_TO_WIN = 2048
          System.out.println("YOU WON!!!");
          System.out.println("Press q to exit and save the board");
          System.out.println("or ENTER to continue playing.");
          return true;
        }
      }
    }
    return false;
  }

  // Return the reference to the 2048 Grid
  public int[][] getGrid()
  {
    return grid;
  }

  // Return the score
  public int getScore()
  {
    return score;
  }

  @Override
    public String toString()
    {
      StringBuilder outputString = new StringBuilder();
      outputString.append(String.format("Score: %d\n", score));
      for (int row = 0; row < GRID_SIZE; row++)
      {
        for (int column = 0; column < GRID_SIZE; column++)
          outputString.append(grid[row][column] == 0 ? "    -" :
              String.format("%5d", grid[row][column]));

        outputString.append("\n");
      }
      return outputString.toString();
    }
}
