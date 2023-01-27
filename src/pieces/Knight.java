package pieces;

import game.Board;

public class Knight extends Piece {
    private static final String name = "KNIGHT";

    public Knight(String inColor, int inPosition) {
        super(inColor, inPosition);
    }


    public boolean moveValid(int pos, Board board) {
        int position = getPosition();

        boolean topLeft = pos == position - 2 * DIMENSION - 1;
        boolean topRight = pos == position - 2 * DIMENSION + 1;
        boolean midTopLeft = pos == position - DIMENSION - 2;
        boolean midTopRight = pos == position - DIMENSION + 2;
        boolean midBottomLeft = pos == position + DIMENSION - 2;
        boolean midBottomRight = pos == position + DIMENSION + 2;
        boolean bottomLeft = pos == position + 2 * DIMENSION - 1;
        boolean bottomRight = pos == position + 2 * DIMENSION + 1;

        return topLeft || topRight || midTopLeft || midTopRight || midBottomLeft
                || midBottomRight || bottomLeft || bottomRight;
    }

    @Override
    public Knight copy() {
        return new Knight(getColor(), getPosition());
    }

    @Override
    public String getName() {
        return name;
    }
}
