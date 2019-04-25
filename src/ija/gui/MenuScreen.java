package ija.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MenuScreen extends Application {

    Button newGameButton;

    public MenuScreen() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("CHESS GAME for IJA");

        newGameButton = new Button();
        newGameButton.setText("New Game");

        StackPane layout = new StackPane();
        layout.getChildren().add(newGameButton);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
