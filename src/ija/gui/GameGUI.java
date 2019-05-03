package ija.gui;

import ija.figures.*;
import ija.game.Board;
import ija.game.Chess;
import ija.game.Field;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The interface of a single game of Chess
 */
public class GameGUI extends Pane implements EventHandler<ActionEvent> {

    private Pane layout;

    private Button saveGameButton;
    private Button restartGame;
    private Button stepBack;
    private Button stepForward;
    private Button move;
    private Button loadMoves;
    private TextArea moveLog;
    private TextField speedInput;
    private RadioButton modeAuto;
    private RadioButton modeManual;
    private ToggleGroup buttonGroup;
    private RadioButton shortNotation;
    private RadioButton longNotation;
    private ToggleGroup notation;
    private Label buttonText;
    private Label speedText;
    private Label boardID;
    private Label active;
    private Label activePlayerColor;

    private Rectangle[][] boardField;
    private Image[][] images;
    private ImageView[][] imageView;
    private Image empty;
    private ImageView store;

    private Board chessBoard;
    private Chess chessGame;

    private Field sourceField;
    private Field destField;
    private Field currentField;
    private Field original;
    private String dir;
    private boolean knight;

    private int moveCounter;
    private boolean moveFinished;
    private String halfMove;
    private String output;

    private int activePlayer;

    private File loadFile;

    private File movesFile;
    private List<String> movesList;
    private int movesListLocation;
    private String[] splitMove;
    private boolean white;

    /**
     * The constructor for the GameGUI class
     *
     * @param loadFile a game save file to be loaded
     */
    public GameGUI(File loadFile) {
        this.loadFile = loadFile;
    }

    /**
     * Method load a chess game from the read file
     *
     * @param file file to load the chess game from
     */
    private void loadGame(File file) {
        images = new Image[8][8];
        imageView = new ImageView[8][8];

        placeImageViews(false);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line = reader.readLine();
            if (line.equals("1")) {
                changeActivePlayer();
            }
            line = reader.readLine();
            while (line != null) {
                placeFigure(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("ERROR!");
        }
    }

    /**
     * Method places a figure from the loaded save file onto the chess board
     *
     * @param line the line read from the save file
     */
    private void placeFigure(String line) {
        String color;
        int col;
        int row;
        boolean isWhite;

        color = Character.toString(line.charAt(2));
        if (color.equals("W")) {
            isWhite = true;
        } else {
            isWhite = false;
        }
        col = Integer.parseInt(Character.toString(line.charAt(4)));
        row = Integer.parseInt(Character.toString(line.charAt(6)));

        // if the line defines a pawn
        if (line.matches("^P.*$")) {
            chessBoard.getField(col, row).put(new Pawn(isWhite, col, row));
            if (isWhite) {
                images[col - 1][row - 1] = new Image("file:lib/res/white_pawn.png");
            } else {
                images[col - 1][row - 1] = new Image("file:lib/res/black_pawn.png");
            }
            makeView(col - 1, row - 1,row - 1, false);
        }
        // if the line defines a tower
        else if (line.matches("^V.*$")) {
            chessBoard.getField(col, row).put(new Tower(isWhite, col, row));
            if (isWhite) {
                images[col - 1][row - 1] = new Image("file:lib/res/white_tower.png");
            } else {
                images[col - 1][row - 1] = new Image("file:lib/res/black_tower.png");
            }
            makeView(col - 1, row - 1,row - 1, false);
        }
        // if the line defines a knight
        else if (line.matches("^J.*$")) {
            chessBoard.getField(col, row).put(new Knight(isWhite, col, row));
            if (isWhite) {
                images[col - 1][row - 1] = new Image("file:lib/res/white_knight.png");
            } else {
                images[col - 1][row - 1] = new Image("file:lib/res/black_knight.png");
            }
            makeView(col - 1, row - 1,row - 1, false);
        }
        // if the line defines a bishop
        else if (line.matches("^S.*$")) {
            chessBoard.getField(col, row).put(new Bishop(isWhite, col, row));
            if (isWhite) {
                images[col - 1][row - 1] = new Image("file:lib/res/white_bishop.png");
            } else {
                images[col - 1][row - 1] = new Image("file:lib/res/black_bishop.png");
            }
            makeView(col - 1, row - 1,row - 1, false);
        }
        // if the line defines a king
        else if (line.matches("^K.*$")) {
            chessBoard.getField(col, row).put(new King(isWhite, col, row));
            if (isWhite) {
                images[col - 1][row - 1] = new Image("file:lib/res/white_king.png");
            } else {
                images[col - 1][row - 1] = new Image("file:lib/res/black_king.png");
            }
            makeView(col - 1, row - 1,row - 1, false);
        }
        // if the line defines a queen
        else if (line.matches("^D.*$")) {
            chessBoard.getField(col, row).put(new Queen(isWhite, col, row));
            if (isWhite) {
                images[col - 1][row - 1] = new Image("file:lib/res/white_queen.png");
            } else {
                images[col - 1][row - 1] = new Image("file:lib/res/black_queen.png");
            }
            makeView(col - 1, row - 1,row - 1, false);
        }
        // if the line is not compatible
        else {

        }
    }

    /**
     * Method loads the moves from the move file into a list of strings
     *
     * @param file the move file containing the moves
     */
    private void storeMoves(File file) {
        movesList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line = reader.readLine();
            while (line != null) {
                movesList.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("ERROR!");
        }
    }

    /**
     * Method for configuring the radio buttons on the game ui
     *
     * @param button the radio button to configure
     * @param height the desired Y coordinate of the button
     * @param width the desired X coordinate of the button
     * @param selected the default state of the button (selected or not)
     * @param group the group the radio button belongs to
     */
    private void configureRadioButtons(RadioButton button, double height, double width, boolean selected, ToggleGroup group) {
        button.setToggleGroup(group);
        button.setSelected(selected);
        button.setLayoutX(width);
        button.setLayoutY(height);
        button.setOnAction(this);
    }

    /**
     * Method for configuring the buttons present on the game ui
     *
     * @param button the button to configure
     * @param width the desired width of the button
     * @param height the desired height of the button
     * @param x the X coordinate of the button
     * @param y the Y coordinate of the button
     */
    private void configureButtons(Button button, double width, double height, double x, double y) {
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefSize(width, height);
        button.setOnAction(this);
    }

    /**
     * Method displays the labels with numbers of rows and letters of cols of the chess board
     */
    private void setPosLabels() {
        char c = 'A';

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if (col == 0) {
                    String id = String.valueOf(c);
                    boardID = new Label(id);
                    boardID.setFont(Font.font(22));
                    boardID.setLayoutX(row * 70 + 80);
                    boardID.setLayoutY(15);
                    layout.getChildren().add(boardID);
                    boardID = new Label(id);
                    boardID.setFont(Font.font(22));
                    boardID.setLayoutX(row * 70 + 80);
                    boardID.setLayoutY(620);
                    layout.getChildren().add(boardID);
                    c++;
                }
            }
        }

        int i = 1;

        for (int col = 7; col >= 0; col--) {
            for (int row = 0; row < 8; row++) {
                if (row == 0) {
                    String id = String.valueOf(i);
                    boardID = new Label(id);
                    boardID.setFont(Font.font(22));
                    boardID.setLayoutX(15);
                    boardID.setLayoutY((col + 1) * 70 + 5);
                    layout.getChildren().add(boardID);
                    boardID = new Label(id);
                    boardID.setFont(Font.font(22));
                    boardID.setLayoutX(630);
                    boardID.setLayoutY((col + 1) * 70 + 5);
                    layout.getChildren().add(boardID);
                    i++;
                }
            }
        }
    }

    /**
     * Method for creating the chess board using rectangles
     */
    private void createBoard() {
        setPosLabels();

        boardField = new Rectangle[8][8];

        int colCount = 7;
        int rowCount = 0;

        for (int col = 7; col >= 0; col--) {
            for (int row = 7; row >= 0; row--) {
                boardField[col][row] = new Rectangle();
                boardField[col][row].setWidth(70);
                boardField[col][row].setHeight(70);
                boardField[col][row].setStroke(Color.TRANSPARENT);
                boardField[col][row].setStrokeType(StrokeType.INSIDE);
                boardField[col][row].setStrokeWidth(1);
                boardField[col][row].setX(colCount * 70 + 50);
                boardField[col][row].setY(rowCount * 70 + 50);
                if (col % 2 == 0 && row % 2 == 1) {
                    boardField[col][row].setFill(Color.WHITE);
                }
                else if (col % 2 == 0 && row % 2 == 0) {
                    boardField[col][row].setFill(Color.GRAY);
                }
                else if (col % 2 == 1 && row % 2 == 1) {
                    boardField[col][row].setFill(Color.GRAY);
                }
                else if (col % 2 == 1 && row % 2 == 0) {
                    boardField[col][row].setFill(Color.WHITE);
                }

                boardField[col][row].setOnMouseClicked(event -> {
                    determinePiece(event);
                });

                layout.getChildren().add(boardField[col][row]);
                rowCount++;
            }
            colCount--;
            rowCount = 0;
        }
    }

    /**
     * Method for loading and displaying the chess pieces onto the board in their default positions
     */
    private void initializeImages() {
        images = new Image[8][8];
        imageView = new ImageView[8][8];

        images[0][0] = new Image("file:lib/res/black_tower.png");
        images[1][0] = new Image("file:lib/res/black_knight.png");
        images[2][0] = new Image("file:lib/res/black_bishop.png");
        images[3][0] = new Image("file:lib/res/black_queen.png");
        images[4][0] = new Image("file:lib/res/black_king.png");
        images[5][0] = new Image("file:lib/res/black_bishop.png");
        images[6][0] = new Image("file:lib/res/black_knight.png");
        images[7][0] = new Image("file:lib/res/black_tower.png");

        for (int col = 0; col < 8; col++) {
            images[col][1] = new Image("file:lib/res/black_pawn.png");
        }

        images[0][7] = new Image("file:lib/res/white_tower.png");
        images[1][7] = new Image("file:lib/res/white_knight.png");
        images[2][7] = new Image("file:lib/res/white_bishop.png");
        images[3][7] = new Image("file:lib/res/white_queen.png");
        images[4][7] = new Image("file:lib/res/white_king.png");
        images[5][7] = new Image("file:lib/res/white_bishop.png");
        images[6][7] = new Image("file:lib/res/white_knight.png");
        images[7][7] = new Image("file:lib/res/white_tower.png");

        for (int col = 0; col < 8; col++) {
            images[col][6] = new Image("file:lib/res/white_pawn.png");
        }

        placeImageViews(true);
    }

    private void placeImageViews(boolean newGame) {
        int rowCount = 7;

        for (int col = 7; col >= 0; col--) {
            for (int row = 0; row < 8; row++) {
                if (newGame && (row == 0 || row == 1 || row == 6 || row == 7)) {
                    makeView(col, rowCount, row, true);
                } else {
                    makeView(col, rowCount, row, true);
                }
                rowCount--;
            }
            rowCount = 7;
        }
    }

    private void makeView(int col, int rowCount, int row, boolean newGame) {
        if (newGame) {
            imageView[col][rowCount] = new ImageView();
            imageView[col][rowCount].setImage(images[col][row]);
            imageView[col][rowCount].setFitHeight(70);
            imageView[col][rowCount].setFitWidth(50);
            imageView[col][rowCount].setPreserveRatio(true);
            imageView[col][rowCount].setSmooth(true);
            imageView[col][rowCount].setX(col * 70 + 60);
            imageView[col][rowCount].setY(row * 70 + 60);
            imageView[col][rowCount].setOnMouseClicked(event -> {
                determinePiece(event);
            });
            layout.getChildren().add(imageView[col][rowCount]);
        } else {
            imageView[col][rowCount].setImage(images[col][row]);
        }
    }

    /**
     * Method for initializing the virtual chess pieces on the chess board
     */
    private void initializeFigures() {
        chessBoard.getField(1, 1).put(new Tower(true, 1, 1));
        chessBoard.getField(2, 1).put(new Knight(true, 2, 1));
        chessBoard.getField(3, 1).put(new Bishop(true, 3, 1));
        chessBoard.getField(4, 1).put(new Queen(true, 4, 1));
        chessBoard.getField(5, 1).put(new King(true, 5, 1));
        chessBoard.getField(6, 1).put(new Bishop(true, 6, 1));
        chessBoard.getField(7, 1).put(new Knight(true, 7, 1));
        chessBoard.getField(8, 1).put(new Tower(true, 8, 1));

        for (int col = 1; col <= 8; col++) {
            chessBoard.getField(col, 2).put(new Pawn(true, col, 2));
        }

        chessBoard.getField(1, 8).put(new Tower(false, 1, 8));
        chessBoard.getField(2, 8).put(new Knight(false, 2, 8));
        chessBoard.getField(3, 8).put(new Bishop(false, 3, 8));
        chessBoard.getField(4, 8).put(new Queen(false, 4, 8));
        chessBoard.getField(5, 8).put(new King(false, 5, 8));
        chessBoard.getField(6, 8).put(new Bishop(false, 6, 8));
        chessBoard.getField(7, 8).put(new Knight(false, 7, 8));
        chessBoard.getField(8, 8).put(new Tower(false, 8, 8));

        for (int col = 1; col <= 8; col++) {
            chessBoard.getField(col, 7).put(new Pawn(false, col, 7));
        }

        for (int col = 1; col <= 8; col++) {
            for (int row = 3; row <= 6; row++) {
                if (chessBoard.getField(col, row).get() != null) {
                    chessBoard.getField(col, row).remove(chessBoard.getField(col, row).get());
                }
            }
        }
    }

    /**
     * Method for determining the source and destination fields
     *
     * @param event click of the primary mouse button
     */
    private void determinePiece(MouseEvent event) {
        int selectedCol = 0;
        int selectedRow = 0;

        // get the column and row of the clicked on field
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if (event.getSource() == boardField[col][row]) {
                    selectedCol = col;
                    selectedRow = row;
                    if (chessBoard.getField(col + 1, row + 1).get() != null) {
                        System.out.println(chessBoard.getField(col + 1, row + 1).get().getState());
                    }
                } else if (event.getSource() == imageView[col][row]) {
                    selectedCol = col;
                    selectedRow = row;
                    if (chessBoard.getField(col + 1, row + 1).get() != null) {
                        System.out.println(chessBoard.getField(col + 1, row + 1).get().getState());
                    }
                }
            }
        }

        try {
            // if the source field was not yet selected do so
            if (sourceField == null) {
                // if the selected source field has a figure on it
                if (chessBoard.getField(selectedCol + 1, selectedRow + 1).get() != null) {
                    // if the active player is 0, the source field has to have a white figure on it
                    if (activePlayer == 0 && chessBoard.getField(selectedCol + 1, selectedRow + 1).get().isWhite()) {
                        boardField[selectedCol][selectedRow].setStroke(Color.RED);
                        sourceField = chessBoard.getField(selectedCol + 1, selectedRow + 1);
                    }
                    // if the active player is 1, the source field has to have a black figure on it
                    else if (activePlayer == 1 && !chessBoard.getField(selectedCol + 1, selectedRow + 1).get().isWhite()) {
                        boardField[selectedCol][selectedRow].setStroke(Color.RED);
                        sourceField = chessBoard.getField(selectedCol + 1, selectedRow + 1);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error!");
                        alert.setHeaderText("Error while selecting a field!");
                        alert.setContentText("The active player has to select their figure.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error while selecting a field!");
                    alert.setContentText("The source field has to have a figure on it.");
                    alert.showAndWait();
                }
            }
            // if the source field is deselected
            else if (sourceField == chessBoard.getField(selectedCol + 1, selectedRow + 1)) {
                boardField[selectedCol][selectedRow].setStroke(Color.TRANSPARENT);
                sourceField = null;
            }
            // if the source field is already selected select the dest field
            else if (destField == null) {
                // if the destination field is incorrect
                if (!chessGame.checkDestField(sourceField, chessBoard.getField(selectedCol + 1, selectedRow + 1))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error while selecting a field!");
                    alert.setContentText("The figure cannot get to the selected destination.");
                    alert.showAndWait();
                } else {
                    boardField[selectedCol][selectedRow].setStroke(Color.RED);
                    destField = chessBoard.getField(selectedCol + 1, selectedRow + 1);
                    // get the first part of the move for text area output
                    getHalfMove();
                }
            }
            // if the dest field is deselected
            else if (destField == chessBoard.getField(selectedCol + 1, selectedRow + 1)) {
                boardField[selectedCol][selectedRow].setStroke(Color.TRANSPARENT);
                destField = null;
                halfMove = "";
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Warning while selecting a field!");
                alert.setContentText("Source and destination fields are already selected.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("ERROR -> " + e);
        }
    }

    /**
     * Method for moving the image when a figure was moved
     *
     * @param from the source image view with the image to be moved
     * @param to the destination image view
     */
    private void moveImage(ImageView from, ImageView to) {
        Image tmp = from.getImage();
        to.setImage(tmp);
        from.setImage(empty);
    }

    /**
     * Method that finishes the move of a figure on the board
     */
    private void moveDest() {
        moveImage(imageView[sourceField.getColumn() - 1][sourceField.getRow() - 1], imageView[destField.getColumn() - 1][destField.getRow() - 1]);
        boardField[destField.getColumn() - 1][destField.getRow() - 1].setStroke(Color.TRANSPARENT);
        boardField[sourceField.getColumn() - 1][sourceField.getRow() - 1].setStroke(Color.TRANSPARENT);
        changeActivePlayer();
        writeMove();
        sourceField = null;
        destField = null;
        currentField = null;
    }

    /**
     * Method that does one step of the figure on the board
     */
    private void moveStep() {
        moveImage(imageView[sourceField.getColumn() - 1][sourceField.getRow() - 1], imageView[currentField.getColumn() - 1][currentField.getRow() - 1]);
        boardField[sourceField.getColumn() - 1][sourceField.getRow() - 1].setStroke(Color.TRANSPARENT);
        sourceField = currentField;
        boardField[sourceField.getColumn() - 1][sourceField.getRow() - 1].setStroke(Color.RED);
    }

    /**
     * Knight figure specific method that moves the knight by one step on the board
     */
    private void moveKnight() {
        Image tmp = imageView[sourceField.getColumn() - 1][sourceField.getRow() - 1].getImage();
        imageView[currentField.getColumn() - 1][currentField.getRow() - 1].setImage(tmp);
        imageView[sourceField.getColumn() - 1][sourceField.getRow() - 1].setImage(store.getImage());
        boardField[sourceField.getColumn() - 1][sourceField.getRow() - 1].setStroke(Color.TRANSPARENT);
        sourceField = currentField;
        boardField[sourceField.getColumn() - 1][sourceField.getRow() - 1].setStroke(Color.RED);
    }

    /**
     * Knight figure specific method that finishes the move on the board
     */
    private void moveKnightDest() {
        Image tmp = imageView[sourceField.getColumn() - 1][sourceField.getRow() - 1].getImage();
        imageView[currentField.getColumn() - 1][currentField.getRow() - 1].setImage(tmp);
        imageView[sourceField.getColumn() - 1][sourceField.getRow() - 1].setImage(store.getImage());
        boardField[destField.getColumn() - 1][destField.getRow() - 1].setStroke(Color.TRANSPARENT);
        boardField[sourceField.getColumn() - 1][sourceField.getRow() - 1].setStroke(Color.TRANSPARENT);
        changeActivePlayer();
        writeMove();
        sourceField = null;
        destField = null;
        currentField = null;
    }

    /**
     * Method changes the active player after a player moves
     */
    private void changeActivePlayer() {
        if (activePlayer == 0) {
            moveFinished = false;
            activePlayer = 1;
            activePlayerColor.setText("black");
        } else {
            moveFinished = true;
            activePlayer = 0;
            activePlayerColor.setText("white");
        }
    }

    /**
     * Method for moving a figure on the board
     * Determines the instance of the figure class and calls the necessary methods for realising the movement
     *
     * @return true if movement was successful
     */
    /*private boolean moveFigure() {
        // if the source figure is a knight
        if (sourceField.get() instanceof Knight || knight) {
            knight = true;
            // set the initial movement direction
            if (sourceField.getRow() == destField.getRow() + 2) {
                dir = "D";
                original = sourceField;
            } else if (sourceField.getRow() == destField.getRow() - 2) {
                dir = "U";
                original = sourceField;
            } else if (sourceField.getColumn() == destField.getColumn() + 2) {
                dir = "L";
                original = sourceField;
            } else if (sourceField.getColumn() == destField.getColumn() - 2) {
                dir = "R";
                original = sourceField;
            }
            if ((dir.equals("D") || dir.equals("U")) && sourceField.getRow() == destField.getRow()) {
                dir = "";
            } else if ((dir.equals("L") || dir.equals("R")) && sourceField.getColumn() == destField.getColumn()) {
                dir = "";
            }
            // call the method that moves the knight in the backend
            currentField = chessGame.moveKnight(sourceField, destField, dir, original);
            // if the next field is the destination field finish the move on the frontend
            if (currentField.getRow() == destField.getRow() && currentField.getColumn() == destField.getColumn()) {
                moveKnightDest();
                store.setImage(empty);
                original = null;
                knight = false;
            }
            // move by one space on the frontend
            else {
                Image tmp = imageView[currentField.getColumn() - 1][currentField.getRow() - 1].getImage();
                moveKnight();
                store.setImage(tmp);
            }
        }
        // if the source figure is a tower
        else if (sourceField.get() instanceof Tower) {
            // call the method that moves the knight in the backend
            currentField = chessGame.moveTower(sourceField, destField);
            // if the next field is the destination field finish the move on the frontend
            if (currentField.getRow() == destField.getRow() && currentField.getColumn() == destField.getColumn()) {
                moveDest();
            }
            // move by one space on the frontend
            else {
                moveStep();
            }
        }
        // if the source figure is a bishop
        else if (sourceField.get() instanceof Bishop) {
            // call the method that moves the knight in the backend
            currentField = chessGame.moveBishop(sourceField, destField);
            // if the next field is the destination field finish the move on the frontend
            if (currentField.getRow() == destField.getRow() && currentField.getColumn() == destField.getColumn()) {
                moveDest();
            }
            // move by one space on the frontend
            else {
                moveStep();
            }
        }
        // if the source figure is a king
        else if (sourceField.get() instanceof King) {
            // call the method that moves the knight in the backend
            currentField = chessGame.moveKing(sourceField, destField);
            // finish the move on the frontend
            moveDest();
        }
        // if the source figure is a queen
        else if (sourceField.get() instanceof Queen) {
            // call the method that moves the knight in the backend
            currentField = chessGame.moveKing(sourceField, destField);
            // if the next field is the destination field finish the move on the frontend
            if (currentField.getRow() == destField.getRow() && currentField.getColumn() == destField.getColumn()) {
                moveDest();
            }
            // move by one space on the frontend
            else {
                moveStep();
            }
        }
        // if the source figure is a pawn
        else if (sourceField.get() instanceof Pawn) {
            // call the method that moves the knight in the backend
            currentField = chessGame.movePawn(sourceField, destField);
            // if the next field is the destination field finish the move on the frontend
            if (currentField.getRow() == destField.getRow() && currentField.getColumn() == destField.getColumn()) {
                moveDest();
            }
            // move by one space on the frontend
            else {
                moveStep();
            }
        }

        return true;
    }*/

    /**
     * Method for getting the col number from a string
     *
     * @param string the string with the col letter
     * @param pos the position of the letter in the string
     * @return the col number
     */
    private int findCol(String string, int pos) {
        return (int) string.charAt(pos) - 96;
    }

    /**
     * Method for getting the row number from a string
     *
     * @param string the string with the row number
     * @param pos the position of the number in the string
     * @return the row number
     */
    private int findRow(String string, int pos) {
        return Character.getNumericValue(string.charAt(pos));
    }

    /**
     * Method for getting the figure letter identifier from a string
     *
     * @param string the string with the figure identifier
     * @param pos the position the the figure identifier in the string
     * @return the figure identifier
     */
    private String findFigure(String string, int pos) {
        return String.valueOf(string.charAt(pos));
    }

    private String source(String figure) {
        int srcCol = sourceField.getColumn();
        srcCol += 96;
        int srcRow = sourceField.getRow();
        return figure + (char) srcCol + srcRow;
    }

    /**
     * Method finds the source field of the pawn that has to move
     *
     * @param destCol the destination field column
     * @param destRow the destination field row
     * @param white true if the sought pawn is white
     * @return the source field for the move operation
     */
    private Field findPawn(int destCol, int destRow, boolean white) {
        Field field;

        if (white) {
            if (chessBoard.getField(destCol, destRow - 1).get() != null && chessBoard.getField(destCol, destRow - 1).get().isWhite() && chessBoard.getField(destCol, destRow - 1).get() instanceof Pawn) {
                field = chessBoard.getField(destCol, destRow - 1);
            } else if (chessBoard.getField(destCol, destRow - 2).get() != null && chessBoard.getField(destCol, destRow - 2).get().isWhite() && chessBoard.getField(destCol, destRow - 2).get() instanceof Pawn) {
                field = chessBoard.getField(destCol, destRow - 2);
            } else if (chessBoard.getField(destCol - 1, destRow - 1).get() != null && chessBoard.getField(destCol - 1, destRow - 1).get().isWhite() && chessBoard.getField(destCol - 1, destRow - 1).get() instanceof Pawn) {
                field = chessBoard.getField(destCol - 1, destRow - 1);
            } else if (chessBoard.getField(destCol + 1, destRow - 1).get() != null && chessBoard.getField(destCol + 1, destRow - 1).get().isWhite() && chessBoard.getField(destCol + 1, destRow - 1).get() instanceof Pawn) {
                field = chessBoard.getField(destCol + 1, destRow - 1);
            } else {
                field = null;
            }
        } else {
            if (chessBoard.getField(destCol, destRow + 1).get() != null && !chessBoard.getField(destCol, destRow + 1).get().isWhite() && chessBoard.getField(destCol, destRow + 1).get() instanceof Pawn) {
                field = chessBoard.getField(destCol, destRow + 1);
            } else if (chessBoard.getField(destCol, destRow + 2).get() != null && !chessBoard.getField(destCol, destRow + 2).get().isWhite() && chessBoard.getField(destCol, destRow + 2).get() instanceof Pawn) {
                field = chessBoard.getField(destCol, destRow + 2);
            } else if (chessBoard.getField(destCol - 1, destRow + 1).get() != null && !chessBoard.getField(destCol - 1, destRow + 1).get().isWhite() && chessBoard.getField(destCol - 1, destRow + 1).get() instanceof Pawn) {
                field = chessBoard.getField(destCol - 1, destRow + 1);
            } else if (chessBoard.getField(destCol + 1, destRow + 1).get() != null && !chessBoard.getField(destCol + 1, destRow + 1).get().isWhite() && chessBoard.getField(destCol + 1, destRow + 1).get() instanceof Pawn) {
                field = chessBoard.getField(destCol + 1, destRow + 1);
            } else {
                field = null;
            }
        }

        return field;
    }

    /**
     * Method finds the source field of the king that has to move
     *
     * @param destCol the destination field column
     * @param destRow the destination field row
     * @param white true if the sought king is white
     * @return the source field for the move operation
     */
    private Field findKing(int destCol, int destRow, boolean white) {
        Field field = null;

        for (int col = destCol - 1; col <= destCol + 1; col++) {
            for (int row = destRow - 1; row <= destRow + 1; row++) {
                if (col >= 1 && col <= 8 && row >= 1 && row <= 8) {
                    if (white) {
                        if (chessBoard.getField(col, row).get() != null && chessBoard.getField(col, row).get() instanceof King && chessBoard.getField(col, row).get().isWhite()) {
                            field = chessBoard.getField(col, row);
                        } else {
                            field = null;
                        }
                    } else {
                        if (chessBoard.getField(col, row).get() != null && chessBoard.getField(col, row).get() instanceof King && !chessBoard.getField(col, row).get().isWhite()) {
                            field = chessBoard.getField(col, row);
                        } else {
                            field = null;
                        }
                    }
                }
            }
        }

        return field;
    }

    /**
     * Method finds the source field of the bishop or the queen that has to move
     *
     * @param destCol the destination field column
     * @param destRow the destination field row
     * @param white true if the sought bishop is white
     * @param queen true if the algorithm is searching for a bishop source field
     * @return the source field for the move operation
     */
    private Field findBishop(int destCol, int destRow, boolean white, boolean queen) {
        Field field = null;

        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
               if (Math.abs(destCol - col) - Math.abs(destRow - row) == 0) {
                   if (chessBoard.getField(col, row).get() != null && chessBoard.getField(col, row).get() instanceof Bishop && !queen) {
                       if (white && chessBoard.getField(col, row).get().isWhite()) {
                           field = chessBoard.getField(col, row);
                       } else if (!white && !chessBoard.getField(col, row).get().isWhite()) {
                           field = chessBoard.getField(col, row);
                       }
                   } else if (chessBoard.getField(col, row).get() != null && chessBoard.getField(col, row).get() instanceof Queen && queen) {
                       if (white && chessBoard.getField(col, row).get().isWhite()) {
                           field = chessBoard.getField(col, row);
                       } else if (!white && !chessBoard.getField(col, row).get().isWhite()) {
                           field = chessBoard.getField(col, row);
                       }
                   }
               }
            }
        }

        return field;
    }

    /**
     * Method finds the source field of the tower or the queen that has to move
     *
     * @param destCol the destination field column
     * @param destRow the destination field row
     * @param white true if the sought tower is white
     * @param queen true if the algorithm is searching for a queen source field
     * @return the source field for the move operation
     */
    private Field findTower(int destCol, int destRow, boolean white, boolean queen) {
        Field field = null;

        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                if (!queen) {
                    if (chessBoard.getField(col, destRow).get() != null && chessBoard.getField(col, destRow).get() instanceof Tower) {
                        if (white && chessBoard.getField(col, destRow).get().isWhite()) {
                            field = chessBoard.getField(col, destRow);
                        } else if (!white && !chessBoard.getField(col, destRow).get().isWhite()) {
                            field = chessBoard.getField(col, destRow);
                        }
                    } else if (chessBoard.getField(destCol, row).get() != null && chessBoard.getField(destCol, row).get() instanceof Tower) {
                        if (white && chessBoard.getField(destCol, row).get().isWhite()) {
                            field = chessBoard.getField(destCol, row);
                        } else if (!white && !chessBoard.getField(destCol, row).get().isWhite()) {
                            field = chessBoard.getField(destCol, row);
                        }
                    }
                } else if (queen) {
                    if (chessBoard.getField(col, destRow).get() != null && chessBoard.getField(col, destRow).get() instanceof Queen) {
                        if (white && chessBoard.getField(col, destRow).get().isWhite()) {
                            field = chessBoard.getField(col, destRow);
                        } else if (!white && !chessBoard.getField(col, destRow).get().isWhite()) {
                            field = chessBoard.getField(col, destRow);
                        }
                    } else if (chessBoard.getField(destCol, row).get() != null && chessBoard.getField(destCol, row).get() instanceof Queen) {
                        if (white && chessBoard.getField(destCol, row).get().isWhite()) {
                            field = chessBoard.getField(destCol, row);
                        } else if (!white && !chessBoard.getField(destCol, row).get().isWhite()) {
                            field = chessBoard.getField(destCol, row);
                        }
                    }
                }
            }
        }

        return field;
    }

    /**
     * Method finds the source field of the knight that has to move
     *
     * @param destCol the destination field column
     * @param destRow the destination field row
     * @param white true if the sought knight is white
     * @return the source field for the move operation
     */
    private Field findKnight(int destCol, int destRow, boolean white) {
        Field field = null;

        if (chessBoard.getField(destCol - 1, destRow - 2).get() != null && chessBoard.getField(destCol - 1, destRow - 2).get() instanceof Knight) {
            if (chessBoard.getField(destCol - 1, destRow - 2).get().isWhite() && white) {
                field = chessBoard.getField(destCol - 1, destRow - 2);
            } else if (!chessBoard.getField(destCol - 1, destRow - 2).get().isWhite() && !white) {
                field = chessBoard.getField(destCol - 1, destRow - 2);
            }
        } else if (chessBoard.getField(destCol + 1, destRow - 2).get() != null && chessBoard.getField(destCol + 1, destRow - 2).get() instanceof Knight) {
            if (chessBoard.getField(destCol + 1, destRow - 2).get().isWhite() && white) {
                field = chessBoard.getField(destCol + 1, destRow - 2);
            } else if (!chessBoard.getField(destCol + 1, destRow - 2).get().isWhite() && !white) {
                field = chessBoard.getField(destCol + 1, destRow - 2);
            }
        } else if (chessBoard.getField(destCol - 1, destRow + 2).get() != null && chessBoard.getField(destCol - 1, destRow + 2).get() instanceof Knight) {
            if (chessBoard.getField(destCol - 1, destRow + 2).get().isWhite() && white) {
                field = chessBoard.getField(destCol - 1, destRow + 2);
            } else if (!chessBoard.getField(destCol - 1, destRow + 2).get().isWhite() && !white) {
                field = chessBoard.getField(destCol - 1, destRow + 2);
            }
        } else if (chessBoard.getField(destCol + 1, destRow + 2).get() != null && chessBoard.getField(destCol + 1, destRow + 2).get() instanceof Knight) {
            if (chessBoard.getField(destCol + 1, destRow + 2).get().isWhite() && white) {
                field = chessBoard.getField(destCol + 1, destRow + 2);
            } else if (!chessBoard.getField(destCol + 1, destRow + 2).get().isWhite() && !white) {
                field = chessBoard.getField(destCol + 1, destRow + 2);
            }
        } else if (chessBoard.getField(destCol - 2, destRow - 1).get() != null && chessBoard.getField(destCol - 2, destRow - 1).get() instanceof Knight) {
            if (chessBoard.getField(destCol - 2, destRow - 1).get().isWhite() && white) {
                field = chessBoard.getField(destCol - 2, destRow - 1);
            } else if (!chessBoard.getField(destCol - 2, destRow - 1).get().isWhite() && !white) {
                field = chessBoard.getField(destCol - 2, destRow - 1);
            }
        } else if (chessBoard.getField(destCol - 2, destRow + 1).get() != null && chessBoard.getField(destCol - 2, destRow + 1).get() instanceof Knight) {
            if (chessBoard.getField(destCol - 2, destRow + 1).get().isWhite() && white) {
                field = chessBoard.getField(destCol - 2, destRow + 1);
            } else if (!chessBoard.getField(destCol - 2, destRow + 1).get().isWhite() && !white) {
                field = chessBoard.getField(destCol - 2, destRow + 1);
            }
        } else if (chessBoard.getField(destCol + 2, destRow - 1).get() != null && chessBoard.getField(destCol + 2, destRow - 1).get() instanceof Knight) {
            if (chessBoard.getField(destCol + 2, destRow - 1).get().isWhite() && white) {
                field = chessBoard.getField(destCol + 2, destRow - 1);
            } else if (!chessBoard.getField(destCol + 2, destRow - 1).get().isWhite() && !white) {
                field = chessBoard.getField(destCol + 2, destRow - 1);
            }
        } else if (chessBoard.getField(destCol + 2, destRow + 1).get() != null && chessBoard.getField(destCol + 2, destRow + 1).get() instanceof Knight) {
            if (chessBoard.getField(destCol + 2, destRow + 1).get().isWhite() && white) {
                field = chessBoard.getField(destCol + 2, destRow + 1);
            } else if (!chessBoard.getField(destCol + 2, destRow + 1).get().isWhite() && !white) {
                field = chessBoard.getField(destCol + 2, destRow + 1);
            }
        }

        return field;
    }

    /**
     * Method calls the needed function for finding a source field based on the type of figure that has to move
     *
     * @param col the destination field column
     * @param row the destination field row
     * @param white true if the figure that is moving is white
     * @param figure the string indicating the type of the figure
     */
    private String getSource(int col, int row, boolean white, String figure) {
        String src = null;

        // set the source field based on the figure
        if (figure.equals("J")) {
            sourceField = findKnight(col, row, white);
        } else if (figure.equals("K")) {
            sourceField = findKing(col, row, white);
        } else if (figure.equals("V")) {
            sourceField = findTower(col, row, white, false);
        } else if (figure.equals("S")) {
            sourceField = findBishop(col, row, white, false);
        } else if (figure.equals("D")) {
            sourceField = findBishop(col, row, white, true);
            if (sourceField == null) {
                sourceField = findTower(col, row, white, true);
            }
        } else {
            sourceField = findPawn(col, row, white);
        }

        if (sourceField != null) {
            if (chessGame.checkDestField(sourceField, destField)) {
                chessGame.performMove(sourceField, destField);
                src = source(figure);
                moveDest();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error while loading a move!");
                alert.setContentText("Could not find the source field.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error while loading a move!");
            alert.setContentText("Could not find the source field.");
            alert.showAndWait();
        }

        return src;
    }

    /**
     * Method performs the movement of a figure that is defined in the moves file
     */
    private void doStepForward() {
        if (white) {
            if (movesListLocation >= movesList.size()) {
                movesListLocation--;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error while loading a move!");
                alert.setContentText("No more moves after this move found.");
                alert.showAndWait();
            } else {
                int pos = 0;
                String figure;
                String string[] = movesList.get(movesListLocation).split("\\s+");

                // find figure identifier
                if ((int) string[1].charAt(pos) < 96) {
                    figure = findFigure(string[1], pos);
                    pos++;
                } else {
                    figure = "";
                }
                // get figure col
                int col = findCol(string[1], pos);
                pos++;
                // get figure row
                int row = findRow(string[1], pos);

                if (string[1].length() <= 3) {
                    // set the destination field
                    destField = chessBoard.getField(col, row);

                    String src = getSource(col, row, white, figure);
                    if (src != null) {
                        src = string[0] + " " + src + string[1] + " " + string[2];
                        movesList.set(movesListLocation, src);
                        System.out.println(movesList.get(movesListLocation));
                    }
                } else {
                    if (figure.equals("")) {
                        pos++;
                    } else {
                        pos += 2;
                    }
                    //get the dest field column
                    int destCol = findCol(string[1], pos);
                    pos++;
                    // get the dest field row
                    int destRow = findRow(string[1], pos);

                    // perform the move
                    sourceField = chessBoard.getField(col, row);
                    destField = chessBoard.getField(destCol, destRow);
                    chessGame.performMove(sourceField, destField);
                    moveDest();
                }
            }
        } else {
            int pos = 0;
            String figure;
            String string[] = movesList.get(movesListLocation).split("\\s+");

            // find figure identifier
            if ((int) string[2].charAt(pos) < 96) {
                figure = findFigure(string[2], pos);
                pos++;
            } else {
                figure = "";
            }
            // get figure col
            int col = findCol(string[2], pos);
            pos++;
            // get figure row
            int row = findRow(string[2], pos);

            if (string[2].length() <= 3) {
                // set the destination field
                destField = chessBoard.getField(col, row);

                String src = getSource(col, row, white, figure);
                if (src != null) {
                    src = string[0] + " " + string[1] + " " + src + string[2];
                    movesList.set(movesListLocation, src);
                    System.out.println(movesList.get(movesListLocation));
                }
            } else {
                if (figure.equals("")) {
                    pos++;
                } else {
                    pos += 2;
                }
                //get the dest field column
                int destCol = findCol(string[2], pos);
                pos++;
                // get the dest field row
                int destRow = findRow(string[2], pos);

                // perform the move
                sourceField = chessBoard.getField(col, row);
                destField = chessBoard.getField(destCol, destRow);
                chessGame.performMove(sourceField, destField);
                moveDest();
            }
            movesListLocation++;
        }
    }

    /**
     * Method rolls back the move of the figure based on the move file
     */
    private void doStepBack() {
        if (!white) {
            movesListLocation--;
            if (movesListLocation < 0) {
                movesListLocation++;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error while loading a move!");
                alert.setContentText("No more moves before this move found.");
                alert.showAndWait();
            } else {
                String string[] = movesList.get(movesListLocation).split("\\s+");

                int pos = 0;
                String figure;

                // find figure identifier
                if ((int) string[2].charAt(pos) < 96) {
                    figure = findFigure(string[2], pos);
                    pos++;
                } else {
                    figure = "";
                }
                int destCol = findCol(string[2], pos);
                pos++;
                int destRow = findRow(string[2], pos);
                if (figure.equals("")) {
                    pos++;
                } else {
                    pos += 2;
                }
                int srcCol = findCol(string[2], pos);
                pos++;
                int srcRow = findRow(string[2], pos);

                sourceField = chessBoard.getField(srcCol, srcRow);
                destField = chessBoard.getField(destCol, destRow);
                chessGame.performMove(sourceField, destField);
                moveDest();
            }
        } else {
            String string[] = movesList.get(movesListLocation).split("\\s+");

            int pos = 0;
            String figure;

            // find figure identifier
            if ((int) string[1].charAt(pos) < 96) {
                figure = findFigure(string[1], pos);
                pos++;
            } else {
                figure = "";
            }
            int destCol = findCol(string[1], pos);
            pos++;
            int destRow = findRow(string[1], pos);
            if (figure.equals("")) {
                pos++;
            } else {
                pos += 2;
            }
            int srcCol = findCol(string[1], pos);
            pos++;
            int srcRow = findRow(string[1], pos);

            sourceField = chessBoard.getField(srcCol, srcRow);
            destField = chessBoard.getField(destCol, destRow);
            chessGame.performMove(sourceField, destField);
            moveDest();
        }
    }

    /**
     * Get the first half of the move for text output
     * This consists of figure type, source field column, source field row, dest field column and dest field row
     */
    private void getHalfMove() {
        int col, row;

        // determine the type of the figure that moves
        if (sourceField.get() instanceof King) {
            halfMove = "K";
        } else if (sourceField.get() instanceof Knight) {
            halfMove = "J";
        } else if (sourceField.get() instanceof Bishop) {
            halfMove = "S";
        } else if (sourceField.get() instanceof Queen) {
            halfMove = "D";
        } else if (sourceField.get() instanceof Tower) {
            halfMove = "V";
        } else {
            halfMove = "";
        }

        // if long notation is selected get the source field info
        if (longNotation.isSelected()) {
            col = sourceField.getColumn();
            col += 96;
            row = sourceField.getRow();
            halfMove = halfMove + (char)col + row;
        }

        // get the dest field info
        col = destField.getColumn();
        col += 96;
        row = destField.getRow();
        halfMove = halfMove + (char)col + row + " ";
    }

    /**
     * Writes the completed move into the move log
     */
    private void writeMove() {
        if (!moveFinished) {
            output = output + halfMove;
            halfMove = "";
        } else {
            moveCounter++;
            output = output + halfMove;
            moveLog.appendText(moveCounter + ". " + output + "\n");
            output = "";
        }
    }

    /**
     * Method creates the UI for the game including the controls and the chess board
     *
     * @param parent the tab the ui is supposed to be on
     */
    public void createUI(Tab parent) {
        layout = new Pane();

        chessBoard = new Board(8);

        chessGame = new Chess(chessBoard);

        empty = new Image("file:lib/res/empty.png");

        activePlayer = 0;
        output = "";
        knight = false;
        moveCounter = 0;
        moveFinished = false;
        movesListLocation = 0;
        white = true;

        store = new ImageView();
        store.setImage(empty);
        store.setFitHeight(70);
        store.setFitWidth(50);
        store.setPreserveRatio(true);
        store.setSmooth(true);

        moveLog = new TextArea();
        moveLog.setLayoutX(800);
        moveLog.setLayoutY(300);
        moveLog.setPrefWidth(350);
        moveLog.setPrefHeight(320);

        moveLog.setOnMouseClicked(evt -> {
            if (evt.getButton() == MouseButton.PRIMARY) {
                Node node = evt.getPickResult().getIntersectedNode();
                while (node != moveLog) {
                    if (node.getStyleClass().contains("content")) {
                        int caretPosition = moveLog.getCaretPosition();
                        String text = moveLog.getText();
                        int break1 = text.lastIndexOf('\n', caretPosition - 1);
                        int break2 = text.indexOf('\n', caretPosition);
                        if (break2 < 0) {
                            break2 = text.length();
                        }
                        moveLog.selectRange(break1, break2);
                        evt.consume();
                        break;
                    }
                    node = node.getParent();
                }
            }
        });

        buttonText = new Label("Select game mode:");
        buttonText.setFont(Font.font(22));
        buttonText.setLayoutX(800);
        buttonText.setLayoutY(70);

        buttonGroup = new ToggleGroup();

        modeManual = new RadioButton("Manual Mode");
        configureRadioButtons(modeManual, 110, 800,true, buttonGroup);

        modeAuto = new RadioButton("Automatic Mode");
        configureRadioButtons(modeAuto, 150, 800, false, buttonGroup);

        buttonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (buttonGroup.getSelectedToggle() == modeAuto) {
                    speedInput.setEditable(true);
                    stepBack.setDisable(true);
                    stepForward.setDisable(true);
                } else if (buttonGroup.getSelectedToggle() == modeManual) {
                    speedInput.setEditable(false);
                    speedInput.setText("");
                    stepBack.setDisable(false);
                    stepForward.setDisable(false);
                }
            }
        });

        stepBack = new Button("BACK");
        configureButtons(stepBack, 100, 35, 940, 100);

        stepForward = new Button("FORWARD");
        configureButtons(stepForward, 100, 35, 1040, 100);

        move = new Button("Move Figure");
        configureButtons(move, 150, 50, 800, 10);

        loadMoves = new Button("Load Moves");
        configureButtons(loadMoves, 115, 50, 800, 240);

        restartGame = new Button("Restart Game");
        configureButtons(restartGame, 115, 50, 915, 240);

        saveGameButton = new Button("Save Game");
        configureButtons(saveGameButton, 115, 50, 1030, 240);

        speedText = new Label("Step speed in MS:");
        speedText.setFont(Font.font(14));
        speedText.setLayoutX(950);
        speedText.setLayoutY(150);

        speedInput = new TextField();
        speedInput.setLayoutX(1080);
        speedInput.setLayoutY(145);
        speedInput.setPrefWidth(100);
        speedInput.setEditable(false);

        notation = new ToggleGroup();

        shortNotation = new RadioButton("Short Notation");
        configureRadioButtons(shortNotation, 630, 840, true, notation);

        longNotation = new RadioButton("Long Notation");
        configureRadioButtons(longNotation, 630, 1000, false, notation);

        active = new Label("ACTIVE PLAYER:");
        active.setFont(Font.font(20));
        active.setLayoutX(800);
        active.setLayoutY(200);

        activePlayerColor = new Label("white");
        activePlayerColor.setFont(Font.font(18));
        activePlayerColor.setLayoutX(965);
        activePlayerColor.setLayoutY(203);

        layout.getChildren().addAll(moveLog, modeAuto, modeManual, buttonText, restartGame, speedText, speedInput, stepBack, stepForward, shortNotation, longNotation, active, activePlayerColor, saveGameButton, loadMoves, move);

        createBoard();

        if (loadFile != null) {
            loadGame(loadFile);
        } else {
            initializeImages();
            initializeFigures();
        }

        parent.setContent(layout);
    }

    /**
     * The click handler for the buttons on the game UI
     * @param event the click of the button
     */
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == restartGame) {
            createBoard();
            initializeImages();
            initializeFigures();
            moveLog.setText("");
            sourceField = null;
            destField = null;
        } else if (event.getSource() == saveGameButton) {
            List<String> save = chessGame.getGameState();
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("SAVE files (*.save)", "*.save");
            fc.getExtensionFilters().add(ext);
            File file = fc.showSaveDialog(null);
            if (file != null) {
                try {
                    PrintWriter writer = new PrintWriter(file);
                    writer.println(activePlayer);
                    for (String string : save) {
                        writer.println(string);
                    }
                    writer.close();
                } catch (IOException e) {
                    System.out.println("ERROR!");
                }
            }

        } else if (event.getSource() == loadMoves) {
            FileChooser fc = new FileChooser();
            movesFile = fc.showOpenDialog(null);
            if (movesFile.getName().matches("^.*.pgn$")) {
                storeMoves(movesFile);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error while opening a moves file!");
                alert.setContentText("Save files are in a .pgn format.");
                alert.showAndWait();
            }
        } else if (event.getSource() == move) {
            if (sourceField != null && destField != null) {
                chessGame.performMove(sourceField, destField);
                moveDest();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Warning while clicking MOVE FIGURE!");
                alert.setContentText("You have to select source and destination first.");
                alert.showAndWait();
            }
        } else if (event.getSource() == stepForward) {
            if (movesFile != null) {
                doStepForward();
                white = !white;
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Warning while clicking FORWARD!");
                alert.setContentText("No moves file is loaded.");
                alert.showAndWait();
            }
        } else if (event.getSource() == stepBack) {
            if (movesFile != null) {
                white = !white;
                doStepBack();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Warning while clicking BACK!");
                alert.setContentText("No moves file is loaded.");
                alert.showAndWait();
            }
        }
    }
}
