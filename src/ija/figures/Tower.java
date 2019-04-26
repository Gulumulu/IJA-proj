package ija.figures;

public class Tower implements Figure {

    private boolean isWhite;
    private int col;
    private int row;

    /**
     * Initializes the tower
     * Sets the parameter for the color of the tower
     *
     * @param isWhite the tower is white if true, false if it is black
     */
    public Tower(boolean isWhite, int col, int row) {
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
    }

    /**
     * Finds out the color of the tower
     *
     * @return true if the tower is white, false if the tower is black
     */
    public boolean isWhite() {
        return isWhite;
    }

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
        return "V[" + colour + "]" + this.col + ":" + this.row;
    }

}
