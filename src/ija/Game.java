package ija;

import ija.gui.TopBar;
import javafx.application.Application;

/**
 * Main class of the project, starts with the program
 *
 * @author xquirs00 Gabriel Quirschfeld
 */
public class Game {

    public static void main(String[] args) {
        TopBar menu = new TopBar();
        Application.launch(TopBar.class, args);
    }

}
