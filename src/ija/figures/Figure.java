package ija.figures;

public interface Figure {

    boolean isWhite();

    void updateState(int col, int row);

    String getState();

}
