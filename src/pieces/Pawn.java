package pieces;

import game.Board;

public class Pawn extends Piece {

    private static final String name = "PAWN";
    // opening move
    private Boolean firstMove;
    // starts on top
    private final Boolean isTop;

    // is true if piece can be captured by an en passant
    private boolean canBeEnPassantCaptured = false;
    // is true if piece is captured by an en passant
    // is deleted before end of turn
    private boolean isEnPassantCaptured = false;
    // if piece has moved
    private boolean hasMoved = false;

    public Pawn(String inColor, int inPosition,
                Boolean inFirstMove, Boolean inIsTop) {
        super(inColor,inPosition);
        firstMove = inFirstMove;
        isTop = inIsTop;
    }

    public Pawn(String inColor, int inPosition, Boolean inFirstMove,
                Boolean inIsTop, boolean inCanBeEnPassantCaptured) {
        super(inColor,inPosition);
        firstMove = inFirstMove;
        isTop = inIsTop;
        canBeEnPassantCaptured = inCanBeEnPassantCaptured;
    }


    // will not be called if valid moves on other pieces is same color
    public boolean moveValid(int pos, Board board) {
        int position = getPosition();
        // adding helper to position moves pawn correctly vertically
        // if the pawn is on top, helper is positive
        // if the pawn is on top,  helper is negative
        int helper = DIMENSION;
        if (!isTop) {
            helper *= -1;
        }

        // first move movements
        // firstMove is assigned accordingly in enPassantRefresh()
        if (firstMove && pos == position + 2 * helper
                && board.getPiece(position + 2 * helper) == null) {
            hasMoved = true;
            return true;
        }

        // not firstMove movements
        if (pos == position + helper && board.getPiece(pos) == null) {
            hasMoved = true;
            firstMove = false;
            return true;
        } else if ((pos == position + helper - 1
                || pos == position + helper + 1)) {
            if (board.getPiece(pos) != null) {
                hasMoved = true;
                firstMove = false;
                return true;
            } else if (board.getPiece(pos - helper) != null) {
                // en passant
                if (board.nameEquals(pos - helper, "PAWN")
                        && ((Pawn) board.getPiece(pos - helper))
                        .getCanBeEnPassantCaptured()) {
                    ((Pawn) board.getPiece(pos - helper))
                            .enPassantCapture();
                    return true;
                }
            }
        }
        return false;
    }

    // is ran at end of own color turn
    // updates en passant variables and first move
    // updates canBeEnPassantCaptured at the end of every turn so it is true
    // // only for one more turn
    public void enPassantRefresh() {
        if (hasMoved) {
            // canBeEnPassantCaptured is already true for pieces that can be,
            // but other pieces that have moved must have the variable be false
            canBeEnPassantCaptured = firstMove;
            firstMove = false;
        }
    }

    public boolean getCanBeEnPassantCaptured() {
        return canBeEnPassantCaptured;
    }

    public boolean isEnPassantCaptured() {
        return isEnPassantCaptured;
    }

    public void enPassantCapture() {
        isEnPassantCaptured = true;
    }

    public boolean getIsTop() {
        return isTop;
    }

    public boolean getFirstMove() {
        return firstMove;
    }

    @Override
    public Pawn copy() {
        return new Pawn(getColor(), getPosition(),
                firstMove, isTop, canBeEnPassantCaptured);
    }

    @Override
    public String getName() {
        return name;
    }
}
