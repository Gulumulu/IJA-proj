package ija.gui;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * The interface of a single game of Chess
 */
public class GameGUI extends Pane {

    private Label buttonText;
    private TextArea moveLog;
    private RadioButton modeAuto;
    private RadioButton modeManual;
    private Pane layout;
    private ToggleGroup buttonGroup;

    public GameGUI() {

    }

    public void configureRadioButtons(RadioButton button, double height, boolean selected) {
        button.setToggleGroup(buttonGroup);
        button.setSelected(selected);
        button.setLayoutX(700);
        button.setLayoutY(height);
    }

    public void createUI(Tab parent) {
        layout = new Pane();

        moveLog = new TextArea();
        moveLog.setLayoutX(700);
        moveLog.setLayoutY(250);
        moveLog.setPrefWidth(450);
        moveLog.setPrefHeight(320);
        moveLog.setDisable(true);

        buttonText = new Label("Select game mode:");
        buttonText.setFont(new Font("Arial", 22));
        buttonText.setLayoutX(700);
        buttonText.setLayoutY(160);

        buttonGroup = new ToggleGroup();

        modeManual = new RadioButton("Manual Mode");
        configureRadioButtons(modeManual, 195, true);

        modeAuto = new RadioButton("Automatic Mode");
        configureRadioButtons(modeAuto, 220, false);

        layout.getChildren().addAll(moveLog, modeAuto, modeManual, buttonText);
        parent.setContent(layout);
    }
}
