package ija.game;

import ija.figures.*;

import java.util.Stack;

public class Chess{

    private Board board;
    private Stack<Board> prevBoards;
    private Stack<Field> currField;
    private Stack<Field> prevField;
    private Stack<Field> removed;

    /**
     * Initializes the game of chess
     * Prepares the needed stacks for undo operations and sets the game
     *
     * @param board the game to play on
     */
    public Chess(Board board) {
        this.board = board;
        this.prevBoards = new Stack<Board>();
        this.prevBoards.push(board);
        this.currField = new Stack<Field>();
        this.prevField = new Stack<Field>();
        this.removed = new Stack<Field>();
    }

    /**
     * Allows the pawns to move in a column either up if white or down if black
     * Also allows the tower to move in a column either up or down regardless of its colour
     * If the destination field is occupied with a different colour figure, it is eliminated
     *
     * @param figure the figure to be moved
     * @param field the field the figure is supposed to move to
     * @return successfulness of the operation
     */
    public boolean move(Figure figure, Field field) {
        int size = this.board.getSize();
        Field original = null;
        boolean result = false;

        // getting the field the figure is originally on
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (this.board.getField(col + 1, row + 1).get() == figure) {
                    original = this.board.getField(col + 1, row + 1);
                }
            }
        }

        if (original != null) {
            this.prevField.push(original);
            Field tmp = new BoardField(field.getColumn(), field.getRow());
            tmp.put(field.get());
            this.removed.push(tmp);
            int currentRow = original.getRow();
            int currentCol = original.getColumn();
            boolean colour = figure.isWhite();

            // if the figure is a pawn
            if (figure instanceof Pawn) {
                // if the white pawn is moving
                if (colour && (field.getRow() == currentRow + 1 || (currentRow == 2 && field.getRow() == currentRow + 2))) {
                    result = movePawn(currentCol, field, original, figure);
                }
                // if the black pawn is moving
                else if (!colour && (field.getRow() == currentRow - 1 || (currentRow == 7 && field.getRow() == currentRow - 2))) {
                    result = movePawn(currentCol, field, original, figure);
                }
                // if the conditions for moving aren't met
                else {
                    return false;
                }
            }
            // if the figure is a tower
            else if (figure instanceof Tower) {
                // if the white tower is moving in a row
                if (colour && currentCol == field.getColumn()) {
                    result = moveRow(currentCol, currentRow, field, original, figure);
                }
                // if the white tower is moving the a column
                else if (colour && currentRow == field.getRow()) {
                    result = moveCol(currentCol, currentRow, field, original, figure);
                }
                // if the black tower is moving in a row
                else if (currentCol == field.getColumn()) {
                    result = moveRow(currentCol, currentRow, field, original, figure);
                }
                // if the black tower is moving the a column
                else if (currentRow == field.getRow()) {
                    result = moveCol(currentCol, currentRow, field, original, figure);
                }
                // if the movement conditions aren't met
                else {
                    return false;
                }
            }
        } else {
            return false;
        }

        try {
            this.currField.push(field);
            this.prevBoards.push(board);
            if (!result) {
                this.currField.pop();
                this.prevField.pop();
                this.prevBoards.pop();
                this.removed.pop();
            }
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    /**
     * Perform the movement of the pawn
     *
     * @param currentCol current location of the pawn
     * @param dest the field the pawn is supposed to move to
     * @param origin current location of the pawn
     * @param figure the pawn itself
     * @return successfulness of the operation
     */
    private boolean movePawn(int currentCol, Field dest, Field origin, Figure figure) {
        // if the destination and origin col doesn't change
        if (currentCol == dest.getColumn()) {
            // if the destination is empty
            if (dest.get() == null) {
                origin.remove(figure);
                dest.put(figure);
                figure.updateState(dest.getColumn(), dest.getRow());
                return true;
            } else {
                return false;
            }
        }
        // if the destination is in a different col than the origin
        else {
            return false;
        }
    }

    /**
     * Perform the movement of the tower
     *
     * @param dest the field the tower is supposed to move to
     * @param origin current location of the tower
     * @param figure the tower itself
     * @return successfulness of the operation
     */
    private boolean moveTower(Field dest, Field origin, Figure figure) {
        // if the destination field is empty
        if (dest.isEmpty()) {
            origin.remove(figure);
            dest.put(figure);
            figure.updateState(dest.getColumn(), dest.getRow());
            this.removed.pop();
            return true;
        }
        // if the destination is occupied with a different colour figure
        else if (dest.get().isWhite() != figure.isWhite()) {
            origin.remove(figure);
            dest.remove(dest.get());
            dest.put(figure);
            figure.updateState(dest.getColumn(), dest.getRow());
            return true;
        }
        // if the destination is occupied with the same colour figure
        else {
            return false;
        }
    }

    /**
     * Tests the movement of the tower in a column
     *
     * @param currentCol current column of the pawn
     * @param currentRow current row of the pawn
     * @param dest the field the tower is supposed to move to
     * @param origin current location of the tower
     * @param figure the tower itself
     * @return successfulness of the operation
     */
    private boolean moveCol(int currentCol, int currentRow, Field dest, Field origin, Figure figure) {
        // if the tower is supposed to move to the left
        if (currentCol < dest.getColumn()) {
            // checking the spaces in between the destination and the origin
            for (int col = currentCol + 1; col < dest.getColumn(); col++) {
                // if another figure blocks the move
                if (!this.board.getField(col, currentRow).isEmpty()) {
                    return false;
                }
            }
        }
        // if the figure is supposed to move the the right
        else if (currentCol > dest.getColumn()) {
            // checking the spaces in between the destination and the origin
            for (int col = dest.getColumn(); col < currentCol; col++) {
                // if another figure blocks the move
                if (!this.board.getField(col, currentRow).isEmpty()) {
                    return false;
                }
            }
        }
        // if the destination and origin is the same field
        else {
            return false;
        }
        return moveTower(dest, origin, figure);
    }

    /**
     * Tests the movement of the tower in a row
     *
     * @param currentCol current column of the pawn
     * @param currentRow current row of the pawn
     * @param dest the field the tower is supposed to move to
     * @param origin current location of the tower
     * @param figure the tower itself
     * @return successfulness of the operation
     */
    private boolean moveRow(int currentCol, int currentRow, Field dest, Field origin, Figure figure) {
        // if the figure is supposed to move up
        if (currentRow < dest.getRow()) {
            // checking the spaces in between the destination and the origin
            for (int row = currentRow + 1; row < dest.getRow(); row++) {
                // if another figure blocks the move
                if (!this.board.getField(currentCol, row).isEmpty()) {
                    return false;
                }
            }
        }
        // if the figure is supposed to move down
        else if (currentRow > dest.getRow()) {
            // checking the spaces in between the destination and the origin
            for (int row = dest.getRow(); row < currentRow; row++) {
                // if another figure blocks the move
                if (!this.board.getField(currentCol, row).isEmpty()) {
                    return false;
                }
            }
        }
        // if the destination and origin is the same field
        else {
            return false;
        }
        return moveTower(dest, origin, figure);
    }

    /**
     * Reverts back to the previous state before a movement was performed
     */
    public void undo() {
        this.board = this.prevBoards.pop();
        Field tmp1 = this.currField.pop();
        Field tmp2 = this.prevField.pop();
        Field rem = this.removed.peek();
        this.board.getField(tmp2.getColumn(), tmp2.getRow()).put(tmp1.get());
        this.board.getField(tmp1.getColumn(), tmp1.getRow()).remove(tmp1.get());
        if (tmp1.getColumn() == rem.getColumn() && tmp1.getRow() == rem.getRow()) {
            rem = this.removed.pop();
            this.board.getField(tmp1.getColumn(), tmp1.getRow()).put(rem.get());
            tmp1.get().updateState(tmp1.getColumn(), tmp1.getRow());
        }
        tmp2.get().updateState(tmp2.getColumn(), tmp2.getRow());
    }

}