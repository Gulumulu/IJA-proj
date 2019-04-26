package ija.board;

public class Board {

    private int size; // dimension of the board (size x size)
    private Field[][] field; // 2-dimensional array of the Fields

    /**
     * Initializing the board
     * Sets the dimension of the board and fills it with empty fields
     * Also sets the neighbouring fields for each field
     *
     * @param size one dimension of the board (board is size x size)
     */
    public Board(int size) {
        this.size = size;
        this.field = new Field[size][size];
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                this.field[col][row] = new BoardField(col + 1, row + 1);
            }
        }
        for (int col = 1; col <= size; col++) {
            for (int row = 1; row <= size; row++) {
                this.getField(col,row).addNextField(Field.Direction.U, this.getField(col, row + 1));
                this.getField(col,row).addNextField(Field.Direction.D, this.getField(col, row - 1));
                this.getField(col,row).addNextField(Field.Direction.L, this.getField(col - 1, row));
                this.getField(col,row).addNextField(Field.Direction.R, this.getField(col + 1, row));
                this.getField(col,row).addNextField(Field.Direction.RU, this.getField(col + 1, row + 1));
                this.getField(col,row).addNextField(Field.Direction.RD, this.getField(col + 1, row - 1));
                this.getField(col,row).addNextField(Field.Direction.LU, this.getField(col - 1, row + 1));
                this.getField(col,row).addNextField(Field.Direction.LD, this.getField(col - 1, row - 1));
            }
        }
    }

    /**
     * Finds the field with a specified location
     *
     * @param col the column in which the sought field is located
     * @param row the row in which the sought field is located
     * @return the found field
     */
    public Field getField(int col, int row) {
        if (col - 1 < this.size && col - 1 >= 0 && row - 1 < this.size && row - 1 >= 0) {
            return this.field[col - 1][row - 1];
        }
        else {
            return null;
        }
    }

    /**
     * Finds out the dimension of the board
     *
     * @return one dimension of the board
     */
    public int getSize() {
        return this.size;
    }

}
