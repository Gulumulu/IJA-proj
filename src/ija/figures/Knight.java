package ija.figures;

/**
 * The class of a knight chess figure
 *
 * @author xquirs00 Gabriel Quirschfeld
 * @author xjendr03 Martina Jendralova
 */
public class Knight implements Figure {

    private boolean isWhite;
    private int col;
    private int row;

    /**
     * Initializes the knight
     * Sets the parameter for the color of the knight
     *
     * @param isWhite the knight is white if true, false if it is black
     */
    public Knight(boolean isWhite, int col, int row) {
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    /**
     * Finds out the color of the knight
     *
     * @return true if the knight is white, false if the knight is black
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Update the location of the knight
     *
     * @param col the new col number
     * @param row the new row number
     */
    public void updateState(int col, int row) {
        this.col = col;
        this.row = row;
    }

    /**
     * Determines the colour and the position of a figure
     * @return the information about a figure in a form {kind}[{colour}]{column}:{row}
     */
    public String getState() {
        String colour;

        if (this.isWhite()) {
            colour = "W";
        }
        else {
            colour = "B";
        }
        return "J[" + colour + "]" + this.col + ":" + this.row;
    }

}
