package ija.gui;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface of a single game of Chess
 */
public class GameGUI extends Pane {

    private Pane layout;
    private Label buttonText;
    private TextArea moveLog;
    private RadioButton modeAuto;
    private RadioButton modeManual;
    private ToggleGroup buttonGroup;
    private Rectangle boardField;

    public GameGUI() {

    }

    public void configureRadioButtons(RadioButton button, double height, boolean selected) {
        button.setToggleGroup(buttonGroup);
        button.setSelected(selected);
        button.setLayoutX(800);
        button.setLayoutY(height);
    }

    private void createBoard() {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                boardField = new Rectangle();

                boardField.setWidth(70);
                boardField.setHeight(70);
                boardField.setX(col * 70 + 50);
                boardField.setY(row * 70 + 50);
                boardField.setStroke(Color.TRANSPARENT);
                boardField.setStrokeType(StrokeType.INSIDE);
                boardField.setStrokeWidth(1);

                if (col % 2 == 0 && row % 2 == 1) {
                    boardField.setFill(Color.WHITE);
                }
                else if (col % 2 == 0 && row % 2 == 0) {
                    boardField.setFill(Color.BLACK);
                }
                else if (col % 2 == 1 && row % 2 == 1) {
                    boardField.setFill(Color.BLACK);
                }
                else if (col % 2 == 1 && row % 2 == 0) {
                    boardField.setFill(Color.WHITE);
                }

                layout.getChildren().add(boardField);
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
        parent.setContent(layout);
    }
}
