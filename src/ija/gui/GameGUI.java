package ija.gui;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.transform.Translate;


/**
 * The interface of a single game of Chess
 */
public class GameGUI extends Pane {

    private Translate pos;
    private Pane layout;
    private Label buttonText;
    private TextArea moveLog;
    private RadioButton modeAuto;
    private RadioButton modeManual;
    private ToggleGroup buttonGroup;
    private Rectangle[][] boardField;
    private Label boardID;
    private Image[][] images;
    private ImageView[][] imageView;

    public GameGUI() {

    }

    public void configureRadioButtons(RadioButton button, double height, boolean selected) {
        button.setToggleGroup(buttonGroup);
        button.setSelected(selected);
        button.setLayoutX(800);
        button.setLayoutY(height);
    }

    private void setPosLabels() {
        char c = 'A';

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row <= 8; row++) {
                if (row == 0) {
                    String id = String.valueOf(col + 1);
                    boardID = new Label(id);
                    boardID.setFont(Font.font(22));
                    boardID.setLayoutX(15);
                    boardID.setLayoutY((col + 1) * 70);
                    layout.getChildren().add(boardID);
                } else if (col == 0) {
                    String id = String.valueOf(c);
                    boardID = new Label(id);
                    boardID.setFont(Font.font(22));
                    boardID.setLayoutX(row * 70 + 10);
                    boardID.setLayoutY(15);
                    layout.getChildren().add(boardID);
                    c++;
                }
            }
        }
    }

    private void createBoard() {
        setPosLabels();

        boardField = new Rectangle[8][8];

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                boardField[col][row] = new Rectangle();
                boardField[col][row].setWidth(70);
                boardField[col][row].setHeight(70);
                boardField[col][row].setStroke(Color.TRANSPARENT);
                boardField[col][row].setStrokeType(StrokeType.INSIDE);
                boardField[col][row].setStrokeWidth(1);
                boardField[col][row].setX(col * 70 + 50);
                boardField[col][row].setY(row * 70 + 50);
                if (col % 2 == 0 && row % 2 == 1) {
                    boardField[col][row].setFill(Color.GRAY);
                }
                else if (col % 2 == 0 && row % 2 == 0) {
                    boardField[col][row].setFill(Color.WHITE);
                }
                else if (col % 2 == 1 && row % 2 == 1) {
                    boardField[col][row].setFill(Color.WHITE);
                }
                else if (col % 2 == 1 && row % 2 == 0) {
                    boardField[col][row].setFill(Color.GRAY);
                }

                layout.getChildren().add(boardField[col][row]);
            }
        }
    }

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

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if (row == 0 || row == 1 || row == 6 || row == 7) {
                    imageView[col][row] = new ImageView();
                    imageView[col][row].setImage(images[col][row]);
                    imageView[col][row].setFitHeight(70);
                    imageView[col][row].setFitWidth(50);
                    imageView[col][row].setPreserveRatio(true);
                    imageView[col][row].setSmooth(true);
                    imageView[col][row].setCache(true);
                    imageView[col][row].setX(col * 70 + 60);
                    imageView[col][row].setY(row * 70 + 60);
                    layout.getChildren().add(imageView[col][row]);
                }
            }
        }
    }

    public void createUI(Tab parent) {
        layout = new Pane();

        moveLog = new TextArea();
        moveLog.setLayoutX(800);
        moveLog.setLayoutY(250);
        moveLog.setPrefWidth(450);
        moveLog.setPrefHeight(350);
        moveLog.setDisable(true);

        buttonText = new Label("Select game mode:");
        buttonText.setFont(new Font("Arial", 22));
        buttonText.setLayoutX(800);
        buttonText.setLayoutY(160);

        buttonGroup = new ToggleGroup();

        modeManual = new RadioButton("Manual Mode");
        configureRadioButtons(modeManual, 195, true);

        modeAuto = new RadioButton("Automatic Mode");
        configureRadioButtons(modeAuto, 220, false);

        layout.getChildren().addAll(moveLog, modeAuto, modeManual, buttonText);

        createBoard();
        initializeImages();

        parent.setContent(layout);
    }
}
