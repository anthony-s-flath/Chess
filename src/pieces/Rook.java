package pieces;

import game.Board;

public class Rook extends Piece {

    private static final String name = "ROOK";

    private boolean firstMove;

    public Rook(String inColor, int inPosition, boolean inFirstMove) {
        super(inColor, inPosition);
        firstMove = inFirstMove;
    }
    // target = designated position
    // checks if piece can move to target
    public boolean moveValid(int target, Board board) {
        int position = getPosition();

        // collisionHelper helps check board is empty leading to target
        // // iterates i for collision
        // // if negative, position is lower on board
        int collisionHelper = target - position;
        if (collisionHelper < 0) {
            collisionHelper = -1;
        } else {
            collisionHelper = 1;
        }

        // helper helps make upperLimitRow and lowerLimitRow
        int helper = DIMENSION - position % DIMENSION;
        int upperLimitRow = position + helper;
        int lowerLimitRow = position - DIMENSION + helper;

        // checks if every Piece on board leading vertically to target is empty
        if (target % DIMENSION == position % DIMENSION) {
            for (int i = position + DIMENSION * collisionHelper;
                 i != target; i += DIMENSION * collisionHelper) {
                if (board.getPiece(i) != null) {
                    return false;
                }
            }
            return true;
        // checks if every Piece on board leading horizontally to target is empty
        } else if (target >= lowerLimitRow && target <= upperLimitRow) {
                for (int i = position + collisionHelper;
                     i < target; i += collisionHelper) {
                    if (board.getPiece(i) != null) {
                        return false;
                    }
                }
                return true;
        }
        else {
            return false;
        }
    }

    public boolean getFirstMove() {
        return firstMove;
    }

    public void setFirstMove() {
        firstMove = false;
    }

    @Override
    public Rook copy() {
        return new Rook(getColor(), getPosition(), firstMove);
    }

    @Override
    public String getName() {
        return name;
    }
}
