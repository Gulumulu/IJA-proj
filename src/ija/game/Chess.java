package ija.game;

import ija.figures.*;

import java.util.Stack;

public class Chess {

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
     * Checks whether the figure can go to the destination field
     *
     * @param dest the possible destination field for the figure
     */
    public boolean checkDestField(Field src, Field dest) {
        Figure figure = src.get();

        // if the destination figure is the same color as the source figure
        if (dest.get() != null) {
            if ((figure.isWhite() && dest.get().isWhite()) || (!figure.isWhite() && !dest.get().isWhite())) {
                return false;
            }
        }

        if (figure instanceof Pawn) {
            // if the pawn moves in a straight line
            if (src.getColumn() == dest.getColumn()) {
                // if the pawn is white
                if (figure.isWhite()) {
                    // if the pawn moves at the start by 2 fields
                    if (src.getRow() == 2 && dest.getRow() == 4) {
                        return true;
                    }
                    // if the pawn moves by 1 field
                    else if (src.getRow() == dest.getRow() - 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
                // if the pawn is black
                else {
                    // if the pawn moves as the start by 2 fields
                    if (src.getRow() == 7 && dest.getRow() == 5) {
                        return true;
                    }
                    // if the pawn moves by 1 field
                    else if (src.getRow() == dest.getRow() + 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            // if the pawn moves in a diagonal line
            else if ((src.getColumn() == dest.getColumn() - 1) || (src.getColumn() == dest.getColumn() + 1)) {
                // if the pawn is white
                if (figure.isWhite()) {
                    // if the pawn has a black figure to destroy
                    if (dest.get() != null && !dest.get().isWhite()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    // if the pawn has a white figure to destroy
                    if (dest.get() != null && dest.get().isWhite()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else if (figure instanceof Bishop) {
            if (!checkBishop(src, dest)) {
                return false;
            }
        } else if (figure instanceof Knight) {
            boolean colLong = false;
            boolean rowLong = false;
            boolean colShort = false;
            boolean rowShort = false;

            // if the knight is moving to the right by 2 fields
            if (src.getColumn() == dest.getColumn() - 2) {
                colLong = true;
            }
            // if the knight is moving to the left by 2 fields
            else if (src.getColumn() == dest.getColumn() + 2) {
                colLong = true;
            }
            // if the knight is moving to the right by 1 field
            else if (src.getColumn() == dest.getColumn() - 1) {
                colShort = true;
            }
            // if the knight is moving to the left by 1 field
            else if (src.getColumn() == dest.getColumn() + 1) {
                colShort = true;
            }

            // if the knight is moving up by 2 fields
            if (src.getRow() == dest.getRow() - 2) {
                rowLong = true;
            }
            // if the knight is moving down by 2 fields
            else if (src.getRow() == dest.getRow() + 2) {
                rowLong = true;
            }
            // if the knight is moving up by 1 space
            else if (src.getRow() == dest.getRow() - 1) {
                rowShort = true;
            }
            // if the knight is moving down by 1 space
            else if (src.getRow() == dest.getRow() + 1) {
                rowShort = true;
            }

            if (!((rowLong && colShort) || (rowShort && colLong))) {
                return false;
            }
            return true;
        } else if (figure instanceof Tower) {
            if (!checkTower(src, dest)) {
                return false;
            }
        } else if (figure instanceof King) {
            boolean row = false;
            boolean col = false;

            // if the king is moving up
            if (src.getRow() == dest.getRow() - 1) {
                row = true;
            }
            // if the king is moving down
            else if (src.getRow() == dest.getRow() + 1) {
                row = true;
            }
            // if the king is not moving in a column
            else if (src.getRow() == dest.getRow()) {
                row = true;
            } else {
                row = false;
            }

            // if the king is moving to the right
            if (src.getColumn() == dest.getColumn() - 1) {
                col = true;
            }
            // if the king is moving to the left
            else if (src.getColumn() == dest.getColumn() + 1) {
                col = true;
            }
            // if the king is not moving is a row
            else if (src.getColumn() == dest.getColumn()) {
               col = true;
            } else {
                col = false;
            }

            return row && col;
        } else if (figure instanceof Queen) {
            int colSub = src.getColumn() - dest.getColumn();
            int rowSub = src.getRow() - dest.getRow();

            if (Math.abs(colSub) - Math.abs(rowSub) == 0) {
                if (!checkBishop(src, dest)) {
                    return false;
                }
            } else if (src.getRow() == dest.getRow() || src.getColumn() == dest.getColumn()) {
                if (!checkTower(src, dest)) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private boolean checkTower(Field src, Field dest) {
        // if the tower moves is a column
        if (src.getColumn() == dest.getColumn()) {
            // if the tower is supposed to move up
            if (src.getRow() < dest.getRow()) {
                if (!checkRowLine(src, dest)) {
                    return false;
                }
            }
            // if the tower is supposed to move down
            else if (src.getRow() > dest.getRow()) {
                if (!checkRowLine(dest, src)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        // if the tower moves in a row
        else if (src.getRow() == dest.getRow()) {
            // if the tower is supposed to move to the right
            if (src.getColumn() < dest.getColumn()) {
                if (!checkColLine(src, dest)) {
                    return false;
                }
            }
            // if the tower is supposed to move to the left
            else if (src.getColumn() > dest.getColumn()) {
                if (!checkColLine(dest, src)) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private boolean checkBishop(Field src, Field dest) {
        int colSub = src.getColumn() - dest.getColumn();
        int rowSub = src.getRow() - dest.getRow();

        boolean right = false;
        boolean left = false;
        boolean up = false;
        boolean down = false;

        // if the bishop is moving in a diagonal
        if (Math.abs(colSub) - Math.abs(rowSub) == 0) {
            // if the bishop is moving up
            if (src.getRow() < dest.getRow()) {
                up = true;
            }
            // if the bishop is moving down
            else if (src.getRow() > dest.getRow()) {
                down = true;
            }

            // if the bishop is moving to the right
            if (src.getColumn() < dest.getColumn()) {
                right = true;
            }
            // if the bishop is moving to the left
            else if (src.getColumn() > dest.getColumn()) {
                left = true;
            }

            if (up && right) {
                checkURDiagonal(src, dest);
            } else if (up && left) {
                checkULDiagonal(src, dest);
            } else if (down && right) {
                checkDRDiagonal(src, dest);
            } else if (down && left) {
                checkDLDiagonal(src, dest);
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Method checks if the row is empty so that the figure can move
     *
     * @param from field from which the figure wants to move
     * @param to field to which the figure wants to move
     * @return true if empty
     */
    private boolean checkRowLine(Field from, Field to) {
        int row = from.getRow();
        row++;

        for (; row < to.getRow(); row++) {
            if (board.getField(from.getColumn(), row).get() != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Method checks if the column is empty so that the figure can move
     *
     * @param from field from which the figure wants to move
     * @param to field to which the figure wants to move
     * @return true if empty
     */
    private boolean checkColLine(Field from, Field to) {
        int col = from.getColumn();
        col++;

        for (; col < to.getColumn(); col++) {
            if (board.getField(col, from.getRow()).get() != null) {
                return false;
            }
        }

        return true;
    }

    private boolean checkURDiagonal(Field src, Field dest) {
        int fromCol = src.getColumn();
        int fromRow = src.getRow();
        int toCol = dest.getColumn();
        int toRow = dest.getRow();

        fromCol++;
        fromRow++;
        toCol--;
        toRow--;

        for (; fromCol <= toCol; fromCol++) {
            for (; fromRow <= toRow; fromRow++) {
                if (Math.abs(fromCol - toCol) - Math.abs(fromRow - toRow) == 0) {
                    if (board.getField(fromCol, fromRow).get() != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean checkULDiagonal(Field src, Field dest) {
        int fromCol = src.getColumn();
        int fromRow = src.getRow();
        int toCol = dest.getColumn();
        int toRow = dest.getRow();

        fromCol--;
        fromRow++;
        toCol++;
        toRow--;

        for (; fromCol >= toCol; fromCol--) {
            for (; fromRow <= toRow; fromRow++) {
                if (Math.abs(fromCol - toCol) - Math.abs(fromRow - toRow) == 0) {
                    if (board.getField(fromCol, fromRow).get() != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean checkDRDiagonal(Field src, Field dest) {
        int fromCol = src.getColumn();
        int fromRow = src.getRow();
        int toCol = dest.getColumn();
        int toRow = dest.getRow();

        fromCol++;
        fromRow--;
        toCol--;
        toRow++;

        for (; fromCol <= toCol; fromCol++) {
            for (; fromRow >= toRow; fromRow--) {
                if (Math.abs(fromCol - toCol) - Math.abs(fromRow - toRow) == 0) {
                    if (board.getField(fromCol, fromRow).get() != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean checkDLDiagonal(Field src, Field dest) {
        int fromCol = src.getColumn();
        int fromRow = src.getRow();
        int toCol = dest.getColumn();
        int toRow = dest.getRow();

        fromCol--;
        fromRow--;
        toCol++;
        toRow++;

        for (; fromCol >= toCol; fromCol--) {
            for (; fromRow >= toRow; fromRow--) {
                if (Math.abs(fromCol - toCol) - Math.abs(fromRow - toRow) == 0) {
                    if (board.getField(fromCol, fromRow).get() != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public Field movePawn(Field src, Field dest) {
        Field field;

        // if the pawn will move in a column
        if (src.getColumn() == dest.getColumn()) {
            // if the white pawn will move by 2 spaces
            if (src.getRow() == 2 && dest.getRow() == 4) {
                field = board.getField(src.getColumn(), 3);
                field.put(src.get());
                field.get().updateState(field.getColumn(), field.getRow());
                src.remove(src.get());
            }
            // if the black pawn will move by 2 spaces
            else if (src.getRow() == 7 && dest.getRow() == 5) {
                field = board.getField(src.getColumn(), 6);
                field.put(src.get());
                field.get().updateState(field.getColumn(), field.getRow());
                src.remove(src.get());
            } else {
                field = dest;
                field.put(src.get());
                field.get().updateState(field.getColumn(), field.getRow());
                src.remove(src.get());
            }
        }
        // if the pawn will move diagonally
        else {
            field = dest;
            field.remove(field.get());
            field.put(src.get());
            field.get().updateState(field.getColumn(), field.getRow());
            src.remove(src.get());
        }

        return field;
    }

    public Field moveTower(Field src, Field dest) {
        Field field;

        // if the tower will move in a row
        if (src.getRow() == dest.getRow()) {
            // if the tower is moving to the right
            if (src.getColumn() < dest.getColumn()) {
                field = board.getField(src.getColumn() + 1, src.getRow());
            }
            // if the tower is moving to the left
            else {
                field = board.getField(src.getColumn() - 1, src.getRow());
            }

        }
        // if the tower will move in a column
        else {
            // if the tower is moving up
            if (src.getRow() < dest.getRow()) {
                field = board.getField(src.getColumn(), src.getRow() + 1);
            }
            // if the tower is moving down
            else {
                field = board.getField(src.getColumn(), src.getRow() - 1);
            }
        }

        field = performMove(src, field);

        return field;
    }

    /*public Field moveKnight(Field src, Field dest) {
        Field field;



        return field;
    }*/

    public Field moveBishop(Field src, Field dest) {
        Field field;

        boolean right = false;
        boolean left = false;
        boolean up = false;
        boolean down = false;

        // if the bishop is moving to the right
        if (src.getColumn() < dest.getColumn()) {
            right = true;
        }
        // if the bishop is moving to the left
        else if (src.getColumn() > dest.getColumn()) {
            left = true;
        }

        // if the bishop is moving up
        if (src.getRow() < dest.getRow()) {
            up = true;
        }
        // if the bishop is moving down
        else if (src.getRow() > dest.getRow()) {
            down = true;
        }

        if (right && up) {
            field = board.getField(src.getColumn() + 1, src.getRow() + 1);
        } else if (right && down) {
            field = board.getField(src.getColumn() + 1, src.getRow() - 1);
        } else if (left && up) {
            field = board.getField(src.getColumn() - 1, src.getRow() + 1);
        } else {
            field = board.getField(src.getColumn() - 1, src.getRow() - 1);
        }

        field = performMove(src, field);

        return field;
    }

    public Field moveKing(Field src, Field dest) {
        Field field;

        boolean right = false;
        boolean left = false;
        boolean up = false;
        boolean down = false;

        // if the king is moving to the right
        if (src.getColumn() < dest.getColumn()) {
            right = true;
        }
        // if the king is moving to the left
        else if (src.getColumn() > dest.getColumn()) {
            left = true;
        }

        // if the king is moving up
        if (src.getRow() < dest.getRow()) {
            up = true;
        }
        // if the king is moving down
        else if (src.getRow() > dest.getRow()) {
            down = true;
        }

        if (right && up) {
            field = board.getField(src.getColumn() + 1, src.getRow() + 1);
        } else if (right && down) {
            field = board.getField(src.getColumn() + 1, src.getRow() - 1);
        } else if (left && up) {
            field = board.getField(src.getColumn() - 1, src.getRow() + 1);
        } else if (left && down) {
            field = board.getField(src.getColumn() - 1, src.getRow() - 1);
        } else if (left) {
            field = board.getField(src.getColumn() - 1, src.getRow());
        } else if (right) {
            field = board.getField(src.getColumn() + 1, src.getRow());
        } else if (up) {
            field = board.getField(src.getColumn(), src.getRow() + 1);
        } else {
            field = board.getField(src.getColumn(), src.getRow() - 1);
        }

        field = performMove(src, field);

        return field;
    }

    private Field performMove(Field src, Field field) {
        if (field.get() != null) {
            field.remove(field.get());
            field.put(src.get());
            field.get().updateState(field.getColumn(), field.getRow());
            src.remove(src.get());
        } else {
            field.put(src.get());
            field.get().updateState(field.getColumn(), field.getRow());
            src.remove(src.get());
        }

        return field;
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
