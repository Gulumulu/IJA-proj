package ija.board;

import ija.figures.Figure;

public class BoardField implements Field {

    private int col; // identifier of the column
    private int row; // identifier of the row
    private Figure figure; // the figure located in this Field
    private Field[] fields;

    /**
     * Initializes the field
     * Sets the figure to null (field has no figure on it at the moment)
     * Creates an array for the neighboring fields
     *
     * @param col the column of the board on which the field is located
     * @param row the row of the board on which the field is located
     */
    public BoardField(int col, int row) {
        this.col = col;
        this.row = row;
        this.figure = null;
        this.fields = new Field[8];
    }

    /**
     * Allows the user to get the column number of the field
     *
     * @return the column number
     */
    public int getColumn() {
        return this.col;
    }

    /**
     * Allows the user to get the row number of the field
     *
     * @return the row number
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Adds a neighboring field to the fields array
     *
     * @param dirs direction in which the neighboring field is
     * @param field the neighboring field
     */
    public void addNextField(Field.Direction dirs, Field field) {
        if (field != null) {
            this.fields[dirs.ordinal()] = field;
        }
    }

    /**
     * Gets the figure from the field
     *
     * @return null if no figure is on the field, otherwise returns the figure
     */
    public Figure get() {
        return this.figure;
    }

    /**
     * Checks if the field has a figure on it
     *
     * @return true if no figure is on the field, false if there is a figure
     */
    public boolean isEmpty() {
        if (this.figure == null) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Gets a neighboring field in a direction
     *
     * @param dirs the direction in which the neighboring field is located
     * @return the sought neighboring field
     */
    public Field nextField(Field.Direction dirs) {
        return this.fields[dirs.ordinal()];
    }

    /**
     * Puts a figure on a field if the field is empty
     *
     * @param figure the figure to be put on the field
     * @return true if figure was put on the field, otherwise false
     */
    public boolean put(Figure figure) {
        if (this.isEmpty()) {
            this.figure = figure;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Removes a figure from a field
     *
     * @param figure the figure to be removed
     * @return true if figure was removed, false if no figure was on the field or there is a different figure on the field
     */
    public boolean remove(Figure figure) {
        if (this.figure != figure || this.figure == null) {
            return false;
        }
        else {
            this.figure = null;
            return true;
        }
    }

}
