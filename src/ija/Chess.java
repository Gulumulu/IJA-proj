package ija;

import ija.gui.MenuScreen;
import javafx.application.Application;

/**
 * Main class of the project, starts with the program
 *
 * @author xquirs00 Gabriel Quirschfeld
 */
public class Chess {

    public static void main(String[] args) {
        MenuScreen menu = new MenuScreen();
        Application.launch(MenuScreen.class, args);
    }

}
