package pieces;

import game.Board;

public class King extends Piece {

    private static final String name = "KING";
    private boolean firstMove;
    // in state of castling
    private boolean leftCastled = false;
    private boolean rightCastled = false;

    public King(String inColor, int inPosition,
                boolean inFirstMove) {
        super(inColor, inPosition);
        firstMove = inFirstMove;
    }

    public boolean moveValid(int target, Board board) {
        int position = getPosition();

        // castling check
        // positions of hypothetical rooks
        int leftRook = DIMENSION * (position / DIMENSION);
        int rightRook = leftRook + 7;
        boolean leftCastle = firstMove && canCastle(position, target, board,
                                                -1, leftRook);
        boolean rightCastle = firstMove && canCastle(position, target, board,
                                                1, rightRook);
        if (leftCastle && board.nameEquals(leftRook, "ROOK")) {
            if (((Rook) board.getPiece(leftRook)).getFirstMove()) {
                firstMove = false;
                leftCastled = true;
                return true;
            }
        } else if (rightCastle
                && board.nameEquals(rightRook, "ROOK")) {
            if (((Rook) board.getPiece(rightRook)).getFirstMove()) {
                firstMove = false;
                rightCastled = true;
                return true;
            }
        }

        // non castling check
        int upRow = position - DIMENSION;
        int downRow = position + DIMENSION;

        boolean zero = target == upRow - 1;
        boolean one = target == upRow;
        boolean two = target == upRow + 1;
        boolean three = target == position - 1;
        boolean five = target == position + 1;
        boolean six = target == downRow - 1;
        boolean seven = target == downRow;
        boolean eight = target == downRow + 1;

        // boarder positions
        if (position % DIMENSION == 7) {
            boolean moveMade = zero || one || three || six || seven;
            if (moveMade) {
                firstMove = false;
            }
            return moveMade;
        }
        if (position % DIMENSION == 0) {
            boolean moveMade = one || two || five || seven || eight;
            if (moveMade) {
                firstMove = false;
            }
            return moveMade;
        }

        // non boarder positions
        boolean moveMade = zero || one || two || three
                        || five || six || seven || eight;
        if (moveMade) {
            firstMove = false;
        }
        return moveMade;
    }

    // determines:
    // // target is correct position for castle
    // // king is not in check and will not move through check
    // // there are no pieces between the rook and king
    // if castling left, side is -1
    // if castling right, side is 1
    private boolean canCastle(int position, int target,
                                  Board board, int side, int rookPosition) {
        // if there is no rook at rookPosition, cannot perform castle
        if (board.getPiece(rookPosition) == null) {
            return false;
        }

        // target is castle position
        boolean castlePosition = target == position + 2 * side;
        if (!castlePosition) {
            return false;
        }

        // not in check and does not pass through check
        for (int i = position; i != target + side; i += side) {
            Board newBoard = new Board(board.getBoardClone());
            if (newBoard.isMoveCheck(position, i, false)) {
                return false;
            }
        }

        // no pieces between position and target
        for (int i = position + side; i != rookPosition; i += side) {
            if (board.getPiece(i) != null) {
                return false;
            }
        }
        return true;
    }

    public void setFirstMove() {
        firstMove = false;
    }

    public boolean getCastled() {
        return leftCastled || rightCastled;
    }

    public boolean getLeftCastled() { return leftCastled; }

    public boolean getFirstMove() { return firstMove; }

    public void setCastled() {
        leftCastled = false;
        rightCastled = false;
    }

    @Override
    public King copy() {
        return new King(getColor(), getPosition(), firstMove);
    }

    @Override
    public String getName() {
        return name;
    }
}
