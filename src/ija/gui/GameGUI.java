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
import java.util.List;

/**
 * The interface of a single game of Chess
 */
public class GameGUI extends Pane implements EventHandler<ActionEvent> {

    private Pane layout;
    private Button saveGameButton;
    private Label buttonText;
    private TextArea moveLog;
    private RadioButton modeAuto;
    private RadioButton modeManual;
    private ToggleGroup buttonGroup;
    private RadioButton shortNotation;
    private RadioButton longNotation;
    private ToggleGroup notation;
    private Button restartGame;
    private Button stepBack;
    private Button stepForward;
    private Label speedText;
    private TextField speedInput;
    private Label boardID;
    private Rectangle[][] boardField;
    private Image[][] images;
    private ImageView[][] imageView;
    private Image empty;
    private ImageView store;
    private Label active;
    private Label activePlayerColor;

    private Board chessBoard;
    private Chess chessGame;

    private Field sourceField;
    private Field destField;
    private Field currentField;
    private String dir;
    private boolean knight;
    private Field original;

    private int moveCounter;
    private boolean moveFinished;
    private String halfMove;
    private String output;

    private int activePlayer;

    private File loadFile;

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
    private boolean moveFigureForward() {
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
    }

    private void moveFigureBack() {

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
            moveLog.appendText(output + "\n");
            System.out.println(output);
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

        store = new ImageView();
        store.setImage(empty);
        store.setFitHeight(70);
        store.setFitWidth(50);
        store.setPreserveRatio(true);
        store.setSmooth(true);

        moveLog = new TextArea();
        moveLog.setLayoutX(800);
        moveLog.setLayoutY(250);
        moveLog.setPrefWidth(350);
        moveLog.setPrefHeight(370);

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
        buttonText.setLayoutY(20);

        buttonGroup = new ToggleGroup();

        modeManual = new RadioButton("Manual Mode");
        configureRadioButtons(modeManual, 60, 800,true, buttonGroup);

        modeAuto = new RadioButton("Automatic Mode");
        configureRadioButtons(modeAuto, 100, 800, false, buttonGroup);

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

        restartGame = new Button("Restart Game");
        configureButtons(restartGame, 140, 50, 800, 185);

        stepBack = new Button("BACK");
        configureButtons(stepBack, 100, 35, 950, 50);

        stepForward = new Button("FORWARD");
        configureButtons(stepForward, 100, 35, 1050, 50);

        saveGameButton = new Button("Save Game");
        configureButtons(saveGameButton, 140, 50, 1010, 185);

        speedText = new Label("Step speed in MS:");
        speedText.setFont(Font.font(14));
        speedText.setLayoutX(950);
        speedText.setLayoutY(100);

        notation = new ToggleGroup();

        shortNotation = new RadioButton("Short Notation");
        configureRadioButtons(shortNotation, 630, 840, true, notation);

        longNotation = new RadioButton("Long Notation");
        configureRadioButtons(longNotation, 630, 1000, false, notation);

        active = new Label("ACTIVE PLAYER:");
        active.setFont(Font.font(20));
        active.setLayoutX(800);
        active.setLayoutY(140);

        activePlayerColor = new Label("white");
        activePlayerColor.setFont(Font.font(18));
        activePlayerColor.setLayoutX(965);
        activePlayerColor.setLayoutY(143);

        speedInput = new TextField();
        speedInput.setLayoutX(1080);
        speedInput.setLayoutY(95);
        speedInput.setPrefWidth(100);
        speedInput.setEditable(false);

        layout.getChildren().addAll(moveLog, modeAuto, modeManual, buttonText, restartGame, speedText, speedInput, stepBack, stepForward, shortNotation, longNotation, active, activePlayerColor, saveGameButton);

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

        } else if (event.getSource() == stepForward) {
            if (sourceField != null && destField != null) {
                moveFigureForward();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Warning while clicking FORWARD!");
                alert.setContentText("You have to select source and destination first.");
                alert.showAndWait();
            }
        } else if (event.getSource() == stepBack) {
            if (sourceField != null && destField != null) {
                moveFigureBack();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Warning while clicking BACK!");
                alert.setContentText("You have to select source and destination first.");
                alert.showAndWait();
            }
        }
    }
}
