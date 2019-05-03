package ija.figures;

/**
 * The class of a pawn chess figure
 *
 * @author xquirs00 Gabriel Quirschfeld
 * @author xjendr03 Martina Jendralova
 */
public class Pawn implements Figure {

    private boolean isWhite;
    private int col;
    private int row;

    /**
     * Initializes the pawn
     * Sets the parameter for the color of the pawn
     *
     * @param isWhite the pawn is white if true, false if it is black
     */
    public Pawn(boolean isWhite, int col, int row) {
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    /**
     * Finds out the color of the pawn
     *
     * @return true if the pawn is white, false if the pawn is black
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Update the location of the pawn
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
        return "P[" + colour + "]" + this.col + ":" + this.row;
    }

}
