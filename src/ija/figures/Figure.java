package ija.figures;

/**
 * The interface for chess figures
 *
 * @author xquirs00 Gabriel Quirschfeld
 * @author xjendr03 Martina Jendralova
 */
public interface Figure {

    /**
     * Finds out the color of the tower
     *
     * @return true if the tower is white, false if the tower is black
     */
    boolean isWhite();

    /**
     * Update the location of the tower
     *
     * @param col the new col number
     * @param row the new row number
     */
    void updateState(int col, int row);

    /**
     * Determines the colour and the position of a figure
     * @return the information about a figure in a form {kind}[{colour}]{column}:{row}
     */
    String getState();

}
