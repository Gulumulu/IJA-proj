package ija.figures;

public class King {

    private boolean isWhite;
    private int col;
    private int row;

    /**
     * Initializes the king
     * Sets the parameter for the color of the king
     *
     * @param isWhite the king is white if true, false if it is black
     */
    public King(boolean isWhite, int col, int row) {
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    /**
     * Finds out the color of the king
     *
     * @return true if the king is white, false if the king is black
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Update the location of the king
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
