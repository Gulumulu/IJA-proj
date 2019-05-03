package ija.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * The class defines the top bar of the GUI and allows for creation of new chess games in the bottom part
 *
 * @author xquirs00 Gabriel Quirschfeld
 * @author xjendr03 Martina Jendralova
 */
public class TopBar extends Application implements EventHandler<ActionEvent> {

    private Button newGameButton;
    private Button loadGameButton;
    private Label appName;
    private SplitPane split;
    private Pane topPanel;
    private TabPane bottomPanel;
    private Scene scene;
    private Image logo;
    private ImageView logoView;

    private int tabCount;
    private File loadFile;

    /**
     * Class constructor, sets the tab counter to 0
     */
    public TopBar() {
        tabCount = 0;
    }

    /**
     * Method for configuring the buttons present on the top bar
     *
     * @param button the button to configure
     * @param width the Y coordinate at which the button should be located
     */
    private void configButtons(Button button, double width) {
        button.setPrefSize(150, 80);
        button.setLayoutX(width);
        button.setLayoutY(10);
        button.setOnAction(this);
    }

    /**
     * Method starts the GUI of the application
     *
     * @param primaryStage the window of the application
     * @throws Exception if any exception is present, detect it
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("CHESS GAME for IJA");

        split = new SplitPane();
        topPanel = new Pane();
        bottomPanel = new TabPane();

        loadFile = null;

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

        appName = new Label("CHESS");

        configButtons(newGameButton, 850);
        configButtons(loadGameButton, 1020);

        appName.setFont(Font.font(60));
        appName.setLayoutX(50);
        appName.setLayoutY(10);

        logo = new Image("file:lib/res/main_ui.png");

        logoView = new ImageView();
        logoView.setImage(logo);
        logoView.setFitHeight(100);
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);
        logoView.setSmooth(true);
        logoView.setCache(true);
        logoView.setX(250);
        logoView.setY(10);

        topPanel.getChildren().addAll(appName, newGameButton, loadGameButton, logoView);

        scene = new Scene(split);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handling button clicks
     *
     * @param event click of the button
     */
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == newGameButton) {
            createTab();
        } else if (event.getSource() == loadGameButton) {
            loadFile();
        }
    }

    /**
     * Method for loading the file when the load button was clicked
     */
    private void loadFile() {
        FileChooser fc = new FileChooser();
        loadFile = fc.showOpenDialog(null);
        if (loadFile.getName().matches("^.*.save$")) {
            createTab();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error while opening a save file!");
            alert.setContentText("Save files are in a .save format.");
            alert.showAndWait();
        }
        loadFile = null;
    }

    /**
     * Method for opening a new game tab with either a new game or a loaded game
     */
    private void createTab() {
        this.tabCount++;
        Tab tab = new Tab("Game " + tabCount);
        GameGUI game = new GameGUI(loadFile);
        game.createUI(tab);
        bottomPanel.getTabs().add(tab);
        bottomPanel.getSelectionModel().select(tab);
        tab.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                tabCount--;
                int index = bottomPanel.getSelectionModel().getSelectedIndex();
                bottomPanel.getSelectionModel().select(index - 1);
            }
        });
    }
}