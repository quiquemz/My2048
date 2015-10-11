/*
Name: Saul Mendez
Login: cs8bapm
Date: March 5, 2015
File: README
Sources of Help: http://vim.wikia.com/wiki/Vim_Tips_Wiki

Description: 
This program is designed to create a graphical interface of the game 2048. Its
main purpose is to show the game in a nice and friendly way for any person to 
use it. The program will show a board with different rectangles and numbers 
which you will be able to combine depending on the numbers to each other. The 
rules are explained on the README file.
*/

/** Gui2048.java */
/** PA8 Release */

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

public class Gui2048 extends Application
{
  private String outputBoard; // The filename for where to save the Board.
  private Board board; // The 2048 Game Board.

  // Original colors.
  /*
  private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
  private static final Color COLOR_2 = Color.rgb(238, 228, 218);
  private static final Color COLOR_4 = Color.rgb(237, 224, 200);
  private static final Color COLOR_8 = Color.rgb(242, 177, 121);
  private static final Color COLOR_16 = Color.rgb(245, 149, 99);
  private static final Color COLOR_32 = Color.rgb(246, 124, 95);
  private static final Color COLOR_64 = Color.rgb(246, 94, 59);
  private static final Color COLOR_128 = Color.rgb(237, 207, 114);
  private static final Color COLOR_256 = Color.rgb(237, 204, 97);
  private static final Color COLOR_512 = Color.rgb(237, 200, 80);
  private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
  private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
  private static final Color COLOR_OTHER = Color.BLACK;
  private static final Color COLOR_GAME_OVER = Color.rgb(238,228, 218, 0.73);
  // For tiles >= 8
  private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242);
  // For tiles < 8
  private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
  */

  // Colors determined by my imagination.
  private static final Color COLOR_EMPTY = Color.WHITE;
  private static final Color COLOR_2 = Color.WHITE;
  private static final Color COLOR_4 = Color.WHITE;
  private static final Color COLOR_8 = Color.WHITE;
  private static final Color COLOR_16 = Color.RED;
  private static final Color COLOR_32 = Color.RED;
  private static final Color COLOR_64 = Color.RED;
  private static final Color COLOR_128 = Color.YELLOW;
  private static final Color COLOR_256 = Color.YELLOW;
  private static final Color COLOR_512 = Color.YELLOW;
  private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
  private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
  private static final Color COLOR_OTHER = Color.BLACK;
  private static final Color COLOR_GAME_OVER = Color.rgb(255,255, 0, 0.8);
  // For tiles >= 8
  private static final Color COLOR_VALUE_LIGHT = Color.WHITE;
  // For tiles < 8
  private static final Color COLOR_VALUE_DARK = Color.BLACK;
  
  /** Instance Variables here */
  private static final int TWO = 2;
  private static final int FOUR = 4;
  private static final int EIGHT = 8;
  private static final int TEN = 10;
  private static final int SIXTEEN = 16;
  private static final int THIRTY_TWO = 32;
  private static final int SIXTY_FOUR = 64;
  private static final int HUNDRED = 100;
  private static final int ONE_TWO_EIGHT = 128;
  private static final int TWO_FIFTY_SIX = 256;
  private static final int FIVE_TWELVE = 512;
  private static final int TEN_TWO_FOUR = 1024;
  private static final int TWENTY_FOUR_EIGHT = 2048;
  private final int InitialSize = 400; //Size for initial board.
  private IntegerProperty size1 = new SimpleIntegerProperty(40);
  private IntegerProperty size2 = new SimpleIntegerProperty(30);

  private int count = 0;
  private int GRID_SIZE; //The board.getGrid().length value.
  private GridPane gridPane; //The pane where the Tiles are created.
  private Tile[][] tiles; //Array of tiles to save specific tiles.
  private Text[] textArray; //Array of text to update score (2048 score: 4).
  private Scene scene; //The Scene where everything will be set.
  private StackPane sPane; //The StackPane.

  @Override
  public void start(Stage primaryStage)
  {
    //Processes command line arguments and initializes the Game Board.
    processArgs(getParameters().getRaw().toArray(new String[0]));

    /**Add your Code for the GUI Here*/
  
    //Initializing GridPane related variables.
    GRID_SIZE = board.getGrid().length; //Saving grid's length.
    gridPane = new GridPane(); //Initializing a GridPane object.
    sPane = new StackPane(gridPane); //Initializing a StackPane object.
    
    //Setting the GridPane properties.
    gridPane.setAlignment(Pos.CENTER); 
    //gridPane.setStyle("-fx-background-color: rgb(187, 173, 160)");
    gridPane.setStyle("-fx-background-color: rgb(255, 255, 255)");
    gridPane.setHgap(TEN); //Setting the tiles' horizontal margin.
    gridPane.setVgap(TEN); //Settign the tiles' vertical margin.
    
    //Initializing Scene object & setting pane in the scene
    scene = new Scene(sPane, InitialSize, InitialSize);
    
    initTextArray(); //Initiating Text array
    initTileArray(); //Initiating Tile array
    updateGrid(); //Creating grid with tiles for the first time

    KeyHandler handler1 = new KeyHandler(); //Initiating KeyHandler objects
    KeyHandlerWhenOver handler2 = new KeyHandlerWhenOver(); 
    scene.setOnKeyPressed( handler1 );//Setting keyHandler when key is pressed
    scene.setOnKeyReleased( handler2 );//Setting keyHandler when key is release

    primaryStage.setTitle("Gui2048"); //Setting the Stage's title
    primaryStage.setScene(scene); //Setting scene on the Stage
    primaryStage.show(); //Shows the stage
  }

  /** Add your own Instance Methods Here */
  /* Name: Tile 
   * Purpose: Tile Class with Rectangle and Text objects to be able to save 
   *          each tile in an array.
   * Parameters: none
   * Return: none
   */
  private class Tile {
    private Rectangle rectangle;
    private Text value;
  }

  /* Name: initTextArray()
   * Purpose: Initiate the text array and adds text values to the GridPane.
   * Parameters: none
   * Return: none
   */
  private void initTextArray() {
    textArray = new Text[GRID_SIZE]; //initiating array
    int mid = GRID_SIZE / TWO; // getting the midle value of the grid
    int size1 = 40;
    int size2 = 15;  

    // Inserting Text objects into textArray.
    // Adds the Text objects to the grid with the number of columns to span
    // being mid+1
    for (int x = 0; x < GRID_SIZE; x += mid) { 

      textArray[x] = new Text();
      if (GRID_SIZE > FOUR )
        gridPane.add( textArray[x], x, 0, mid+1, 1 ); 
      else 
        gridPane.add( textArray[x], x, 0, mid+1, 1 ); 

    }

    //Setting text properties for the text "2048".
    textArray[0].setFont(Font.font
        ( "Helvetica Neue", FontWeight.BOLD, size1 ));
    textArray[0].setText("2048");

    //Setting text proprties for the score.
    textArray[mid].setFont(Font.font
        ( "Helvetica Neue", FontWeight.BOLD, size2 ));
    textArray[mid].setText("Score: " + board.getScore());
  }

  /* Name: initTextArray()
   * Purpose: Initiate the tile array and add tile objects to the GridPane.
   * Parameters: none
   * Return: none
   */
  private void initTileArray() {
    tiles = new Tile[GRID_SIZE][GRID_SIZE]; //Initializing tile array

    for (int x = 1; x < GRID_SIZE + 1; x++) { 
      for (int y = 0; y < GRID_SIZE; y++) {

        tiles[x-1][y] = new Tile(); //Initializing tiles in the array

        //Creating rectangle objects and adding them to the GridPane
        tiles[x-1][y].rectangle = new Rectangle();
        Rectangle r = tiles[x-1][y].rectangle; 
        r.heightProperty().bind( 
            scene.heightProperty().divide(GRID_SIZE+TWO));
        r.widthProperty().bind( 
            scene.widthProperty().divide(GRID_SIZE+TWO));
        gridPane.add( tiles[x-1][y].rectangle, y, x); 

        //Creating Text objects and adding them to the GridPane
        tiles[x-1][y].value = new Text("");
        gridPane.add( tiles[x-1][y].value, y, x );

      }
    }
  }

  /* Name: initTextArray()
   * Purpose: updates the score in the GridPane 
   * Parametesr: none
   * Return: none
   */
  private void updateScore() {
    textArray[GRID_SIZE/TWO].setText("Score: " + board.getScore());
  }

  /* Name: initTextArray()
   * Purpose: Updates the Tile values in the GridPane with their specified 
   *          properties
   * Parametesr: none
   * Return: none
   */
  private void updateGrid() {
    int size1 = 30;
    int size2 = 25;
    int size3 = 20;
    final int  THOUSAND = 1000;
    int[][] grid = board.getGrid(); //Saving the board's values in an int Array

    //Running through the grid to set the values in the GridPane
    for (int x = 0; x < GRID_SIZE; x++) { 
      for (int y = 0; y < GRID_SIZE; y++) {
        Integer val = grid[x][y]; //Saving the grid's values

        //Setting the rectangles' color to Black if val is greater to 2048
        if (val > TWENTY_FOUR_EIGHT) { 
          tiles[x][y].rectangle.setFill(COLOR_OTHER);
        }

        //Else setting the color to the one that corresponds to the case
        else {
          tiles[x][y].rectangle.setStroke(Color.BLACK);
          switch (val) {
            case 0: tiles[x][y].rectangle.setFill(COLOR_EMPTY);
                    break;
            case TWO: tiles[x][y].rectangle.setFill(COLOR_2);
                    break;
            case FOUR: tiles[x][y].rectangle.setFill(COLOR_4);
                    break;
            case EIGHT: tiles[x][y].rectangle.setFill(COLOR_8);
                    break;
            case SIXTEEN: tiles[x][y].rectangle.setFill(COLOR_16);
                    break;
            case THIRTY_TWO: tiles[x][y].rectangle.setFill(COLOR_32);
                    break;
            case SIXTY_FOUR: tiles[x][y].rectangle.setFill(COLOR_64);
                    break;
            case ONE_TWO_EIGHT: tiles[x][y].rectangle.setFill(COLOR_128);
                    break;
            case TWO_FIFTY_SIX: tiles[x][y].rectangle.setFill(COLOR_256);
                    break;
            case FIVE_TWELVE: tiles[x][y].rectangle.setFill(COLOR_512);
                              break;
            case TEN_TWO_FOUR: tiles[x][y].rectangle.setFill(COLOR_1024);
                               break;
            case TWENTY_FOUR_EIGHT: tiles[x][y].rectangle.setFill(COLOR_2048);
                                    break;
          }
        }

        tiles[x][y].value.setText(""); //Reseting the Text values

        //Setting the new Text values on the grid if val is greaten than 0
        if(val > 0) {
          tiles[x][y].value.setText(val.toString()); //Setting new text

          //Setting the text's type and size
          if ( val < HUNDRED ) {
            tiles[x][y].value.setFont(Font.font( "Helvetica Neue", 
                  FontWeight.BOLD, size1 ));

            //Setting the text's color
            if ( val < 16 )
              tiles[x][y].value.setFill( COLOR_VALUE_DARK );
            else if ( val >= 16 && val < 128 )
              tiles[x][y].value.setFill( COLOR_VALUE_LIGHT );
            else if ( val >= 128 ){
              tiles[x][y].value.setFill( COLOR_VALUE_DARK );}
          }

          //Setting the text's type, size and color
          else if ( val > HUNDRED ) {
            tiles[x][y].value.setFont(Font.font( "Helvetica Neue", 
                  FontWeight.BOLD, size2 ));
            tiles[x][y].value.setFill( COLOR_VALUE_DARK );
          }

          //Setting the text's type, size and color
          else {
            tiles[x][y].value.setFont(Font.font( "Helvetica Neue", 
                  FontWeight.BOLD, size3 ));
            tiles[x][y].value.setFill( COLOR_VALUE_LIGHT );
          }

          //Aligning the Text values to be centered
          gridPane.setValignment( tiles[x][y].value, VPos.CENTER );
          gridPane.setHalignment( tiles[x][y].value, HPos.CENTER );
        }
      }
    }
  }

  /* Name: gameIsOver()
   * Purpose: Shows a message saying that the game is over
   * Parametesr: none
   * Return: none
   */
  private void gameIsOver() {
    int size = 50;
    //Creating rectangle, binding it to the scene, and setting its color
    Rectangle overRect = new Rectangle (); 
    overRect.heightProperty().bind( scene.heightProperty() );
    overRect.widthProperty().bind( scene.widthProperty() );
    overRect.setFill(COLOR_GAME_OVER);

    //Creating text and setting its properties
    Text overText = new Text("GAME OVER");
    overText.setFont(Font.font( "Helvetica Neue", FontWeight.BOLD, size ));
    //overText.fontProperty().bind(
    //   scene.widthProperty());
    gridPane.setHalignment( overText, HPos.CENTER );

    //Setting rectangle and text on the Stack Pane, over the Grid Pane.
    sPane.getChildren().addAll(overRect, overText);
  }

  /* Name: KeyHandler implements EventHandler<KeyEvent>
   * Purpose: Class created to manage the events happening on the scene when a 
   *          key is pressed
   * Parametesr: none
   * Return: none
   */
  private class KeyHandler implements EventHandler<KeyEvent> {

    //@Override
    /* Name: handle(KeyEvent e)
     * Purpose: Overriden method to handle certain key event
     * Parametesr: e - the event to be handled
     * Return: none
     */
    public void handle(KeyEvent e){
      //Getting the KeyEvent code to compare it with the key event
      if ( e.getCode() == KeyCode.UP ) {
        // If statement to execute the moveUp() method in board. A random tile
        // is added after the move is done.
        if ( board.canMove( Direction.UP ) ) {
          board.move( Direction.UP );
          board.addRandomTile();
          updateGrid();
          updateScore();
          System.out.println("Moving UP");
        }
      }
      else if ( e.getCode() == KeyCode.DOWN ) {
        // If statement to execute the moveDown() method in board. A random 
        // tile is added after the move is done.
        if ( board.canMove( Direction.DOWN ) ) {
          board.move( Direction.DOWN );
          board.addRandomTile();
          updateGrid();
          updateScore();
          System.out.println("Moving DOWN");
        }
      }
      else if ( e.getCode() == KeyCode.LEFT ) {
        // If statement to execute the moveLeft() method in board. A random 
        // tile is added after the move is done.
        if ( board.canMove( Direction.LEFT ) ) {
          board.move( Direction.LEFT );
          board.addRandomTile();
          updateGrid();
          updateScore();
          System.out.println("Moving LEFT");
        }
      }
      else if ( e.getCode() == KeyCode.RIGHT ) {
        // If statement to execute the moveRight() method in board. A random 
        // tile is added after the move is done.
        if (board.canMove( Direction.RIGHT )) {
          board.move( Direction.RIGHT );
          board.addRandomTile();
          updateGrid();
          updateScore();
          System.out.println("Moving RIGHT");
        }
      }
      else if ( e.getCode() == KeyCode.S ) {
        // Execute the saveBoard() method in board. It saves the board.
        try { 
          board.saveBoard(outputBoard);
          System.out.println("Saving Board");
        }
        catch ( IOException ioCatch ) {
          System.out.println("IOException");
        }
      }
    }
  }

  /* Name: KeyHandlerWhenOver implements EventHandler<KeyEvent>
   * Purpose: Class created to manage the events happening on the scene when a 
   *          key is realeased
   * Parametesr: none
   * Return: none
   */
  private class KeyHandlerWhenOver implements EventHandler<KeyEvent> {

    //@Override
    /* Name: handle(KeyEvent e)
     * Purpose: Overriden method to handle certain key event
     * Parametesr: e - the event to be handled
     * Return: none
     */
    public void handle(KeyEvent e){
      //Getting the KeyEvent code to compare it with a key even
      if ( e.getCode () == KeyCode.UP || e.getCode () == KeyCode.DOWN || 
          e.getCode () == KeyCode.RIGHT || e.getCode () == KeyCode.LEFT ){
        if (count == 0 && board.isGameOver()) {
          gameIsOver();
          count = 1;
        }
      }
    }
  }

  /** DO NOT EDIT BELOW */

  // The method used to process the command line arguments
  private void processArgs(String[] args)
  {
    String inputBoard = null;   // The filename for where to load the Board
    int boardSize = 0;          // The Size of the Board

    // Arguments must come in pairs
    if((args.length % 2) != 0)
    {
      printUsage();
      System.exit(-1);
    }

    // Process all the arguments 
    for(int i = 0; i < args.length; i += 2)
    {
      if(args[i].equals("-i"))
      {   // We are processing the argument that specifies
        // the input file to be used to set the board
        inputBoard = args[i + 1];
      }
      else if(args[i].equals("-o"))
      {   // We are processing the argument that specifies
        // the output file to be used to save the board
        outputBoard = args[i + 1];
      }
      else if(args[i].equals("-s"))
      {   // We are processing the argument that specifies
        // the size of the Board
        boardSize = Integer.parseInt(args[i + 1]);
      }
      else
      {   // Incorrect Argument 
        printUsage();
        System.exit(-1);
      }
    }

    // Set the default output file if none specified
    if(outputBoard == null)
      outputBoard = "2048.board";
    // Set the default Board size if none specified or less than 2
    if(boardSize < 2)
      boardSize = FOUR;

    // Initialize the Game Board
    try{
      if(inputBoard != null)
        board = new Board(inputBoard, new Random());
      else
        board = new Board(boardSize, new Random());
    }
    catch (Exception e)
    {
      System.out.println(e.getClass().getName() + " was thrown while creating a " +
          "Board from file " + inputBoard);
      System.out.println("Either your Board(String, Random) " +
          "Constructor is broken or the file isn't " +
          "formated correctly");
      System.exit(-1);
    }
  }

  // Print the Usage Message 
  private static void printUsage()
  {
    System.out.println("Gui2048");
    System.out.println("Usage:  Gui2048 [-i|o file ...]");
    System.out.println();
    System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
    System.out.println();
    System.out.println("  -i [file]  -> Specifies a 2048 board that should be loaded");
    System.out.println();
    System.out.println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
    System.out.println("                If none specified then the default \"2048.board\" file will be used");
    System.out.println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
    System.out.println("                specified.  If both -s and -i are used, then the size of the board");
    System.out.println("                will be determined by the input file. The default size is 4.");
  }
}


