package ija.figures;

public class Queen implements Figure {

    private boolean isWhite;
    private int col;
    private int row;

    /**
     * Initializes the queen
     * Sets the parameter for the color of the queen
     *
     * @param isWhite the queen is white if true, false if it is black
     */
    public Queen(boolean isWhite, int col, int row) {
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    /**
     * Finds out the color of the queen
     *
     * @return true if the queen is white, false if the queen is black
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Update the location of the queen
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
        return "D[" + colour + "]" + this.col + ":" + this.row;
    }

}
