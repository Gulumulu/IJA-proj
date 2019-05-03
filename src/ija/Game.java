package ija;

import ija.gui.TopBar;
import javafx.application.Application;

/**
 * Main class of the project, starts with the program
 *
 * @author xquirs00 Gabriel Quirschfeld
 * @author xjendr03 Martina Jendralova
 */
public class Game {

    /**
     * The main method of the application
     * Starts the UI
     *
     * @param args necessary for java
     */
    public static void main(String[] args) {
        TopBar menu = new TopBar();
        Application.launch(TopBar.class, args);
    }

}
