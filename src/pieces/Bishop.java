package pieces;

import game.Board;

public class Bishop extends Piece {
    private static final String name = "BISHOP";

    public Bishop(String inColor, int inPosition) {
        super(inColor, inPosition);
    }

    public boolean moveValid(int pos, Board board) {
        int position = getPosition();
        int colDifference = 1 + Math.abs((position - pos) / DIMENSION);
        int iterations = 0;

        // helps with collision detection
        // pos is in which direction from position:
        boolean topLeftCollision = false;
        boolean topRightCollision = false;
        boolean bottomLeftCollision = false;
        boolean bottomRightCollision = false;

        // repeats for each row above/below possible for bishop
        // checks row above and below i rows above/below
        for (int i = 1; i <= colDifference; ++i) {
            ++iterations;
            // piece cannot move horizontally more than the board allows
            // true if piece has not hit limit
            boolean leftLimit = (i  - 1) < (position % DIMENSION);
            boolean rightLimit = (i - 1) < (DIMENSION - 1
                    - position % DIMENSION);

            int topMove = position - i * DIMENSION;
            int bottomMove = position + i * DIMENSION;
            if (leftLimit && pos == topMove - i) {
                topLeftCollision = true;
                break;
            } else if (rightLimit && pos == topMove + i) {
                topRightCollision = true;
                break;
            } else if (leftLimit && pos == bottomMove - i) {
                bottomLeftCollision = true;
                break;
            } else if (rightLimit && pos == bottomMove + i) {
                bottomRightCollision = true;
                break;
            }
        }

        // collision detection
        // uses same algorithm as above
        for (int i = 1; i < iterations; ++i) {
            int topMove = position - i * DIMENSION;
            int bottomMove = position + i * DIMENSION;
            if (topLeftCollision) {
                if (board.getPiece(topMove - i) != null) {
                    return false;
                }
            } else if (topRightCollision) {
                if (board.getPiece(topMove + i) != null) {
                    return false;
                }
            } else if (bottomLeftCollision) {
                if (board.getPiece(bottomMove - i) != null) {
                    return false;
                }
            } else if (bottomRightCollision) {
                if (board.getPiece(bottomMove + i) != null) {
                    return false;
                }
            }
        }
        return topLeftCollision || topRightCollision
                || bottomLeftCollision || bottomRightCollision;
    }

    @Override
    public Bishop copy() {
        return new Bishop(getColor(), getPosition());
    }

    @Override
    public String getName() {
        return name;
    }

}
