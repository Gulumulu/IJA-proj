package ija.board;

import ija.figures.Figure;

/**
 * Interface of a space on the board
 */
public interface Field {

    /**
     * The directions on the board
     */
    public static enum Direction {
        U,
        D,
        L,
        R,
        LU,
        LD,
        RU,
        RD
    }

    /**
     * Adds a neighboring field to the fields array
     * Implemented in the BoardField class
     *
     * @param dirs direction in which the neighboring field is
     * @param field the neighboring field
     */
    void addNextField(Field.Direction dirs, Field field);

    int getColumn();

    int getRow();

    /**
     * Gets the disk from the field
     * Implemented in the BoardField class
     *
     * @return null if no disk is on the field, otherwise returns the disk
     */
    Figure get();

    /**
     * Checks if the field has a disk on it
     * Implemented in the BoardField class
     *
     * @return true if no disk is on the field, false if there is a disk
     */
    boolean isEmpty();

    /**
     * Gets a neighboring field in a direction
     * Implemented in the BoardField class
     *
     * @param dirs the direction in which the neighboring field is located
     * @return the sought neighboring field
     */
    Field nextField(Field.Direction dirs);

    /**
     * Puts a disk on a field if the field is empty
     * Implemented in the BoardField class
     *
     * @param disk the disk to be put on the field
     * @return true if disk was put on the field, otherwise false
     */
    boolean put(Figure disk);

    /**
     * Removes a disk from a field
     * Implemented in the BoardField class
     *
     * @param disk the disk to be removed
     * @return true if disk was removed, false if no disk was on the field or there is a different disk on the field
     */
    boolean remove(Figure disk);

}
