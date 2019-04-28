package ija.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;


/**
 * The interface of a single game of Chess
 */
public class GameGUI extends Pane implements EventHandler<ActionEvent> {

    private Pane layout;
    private Label buttonText;
    private TextArea moveLog;
    private RadioButton modeAuto;
    private RadioButton modeManual;
    private ToggleGroup buttonGroup;
    private Button restartGame;
    private Button stepBack;
    private Button stepForward;
    private Label speedText;
    private TextField speedInput;
    private Label boardID;
    private Rectangle[][] boardField;
    private Image[][] images;
    private ImageView[][] imageView;

    public GameGUI() {

    }

    private void configureRadioButtons(RadioButton button, double height, boolean selected) {
        button.setToggleGroup(buttonGroup);
        button.setSelected(selected);
        button.setLayoutX(800);
        button.setLayoutY(height);
        button.setOnAction(this);
    }

    private void configureButtons(Button button, double width, double height, double x, double y) {
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefSize(width,height);
        button.setOnAction(this);
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
                    boardID.setLayoutY((col + 1) * 70 + 5);
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
        buttonText.setFont(Font.font(22));
        buttonText.setLayoutX(800);
        buttonText.setLayoutY(20);

        buttonGroup = new ToggleGroup();

        modeManual = new RadioButton("Manual Mode");
        configureRadioButtons(modeManual, 60, true);

        modeAuto = new RadioButton("Automatic Mode");
        configureRadioButtons(modeAuto, 100, false);

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
        configureButtons(restartGame, 125, 50, 800, 180);

        stepBack = new Button("BACK");
        configureButtons(stepBack, 100, 35, 950, 50);

        stepForward = new Button("FORWARD");
        configureButtons(stepForward, 100, 35, 1050, 50);

        speedText = new Label("Step speed in MS: ");
        speedText.setFont(Font.font(14));
        speedText.setLayoutX(950);
        speedText.setLayoutY(100);

        speedInput = new TextField();
        speedInput.setLayoutX(1080);
        speedInput.setLayoutY(95);
        speedInput.setPrefWidth(100);
        speedInput.setEditable(false);

        layout.getChildren().addAll(moveLog, modeAuto, modeManual, buttonText, restartGame, speedText, speedInput, stepBack, stepForward);

        createBoard();
        initializeImages();

        parent.setContent(layout);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == restartGame) {
            initializeImages();
        } else if (event.getSource() == stepForward) {

        } else if (event.getSource() == stepBack) {
            
        }
    }
}
