package ija.figures;

public class Bishop {

    private boolean isWhite;
    private int col;
    private int row;

    /**
     * Initializes the bishop
     * Sets the parameter for the color of the bishop
     *
     * @param isWhite the bishop is white if true, false if it is black
     */
    public Bishop(boolean isWhite, int col, int row) {
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    /**
     * Finds out the color of the bishop
     *
     * @return true if the bishop is white, false if the bishop is black
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Update the location of the bishop
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
        return "S[" + colour + "]" + this.col + ":" + this.row;
    }

}
