package ija.gui;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TopBar extends Application {

    Button newGameButton;
    Button loadGameButton;
    Button saveGameButton;
    Label appName;

    public TopBar() {

    }

    private void configButtons(Button button, int width) {
        button.setPrefSize(150, 80);
        button.setLayoutX(width);
        button.setLayoutY(10);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("CHESS GAME for IJA");

        SplitPane split = new SplitPane();
        Pane topPanel = new Pane();
        Pane bottomPanel = new Pane();

        split.setOrientation(Orientation.VERTICAL);
        split.setPrefWidth(1200);

        split.getItems().add(0, topPanel);
        topPanel.setMinHeight(100);
        topPanel.setMaxHeight(100);

        split.getItems().add(1, bottomPanel);
        bottomPanel.setMinHeight(700);
        bottomPanel.setMaxHeight(700);

        newGameButton = new Button("New Game");
        loadGameButton = new Button("Load Game");
        saveGameButton = new Button("Save Game");

        appName = new Label("CHESS");

        topPanel.getChildren().addAll(appName, newGameButton, loadGameButton, saveGameButton);

        configButtons(newGameButton, 600);
        configButtons(loadGameButton, 800);
        configButtons(saveGameButton, 1000);
        appName.setFont(new Font("Arial", 60));
        appName.setLayoutX(50);
        appName.setLayoutY(10);

        Scene scene = new Scene(split);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}