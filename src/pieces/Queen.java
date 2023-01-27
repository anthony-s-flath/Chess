package pieces;

import game.Board;

public class Queen extends Piece {

    private static final String name = "QUEEN";

    public Queen(String inColor, int inPosition) {
        super(inColor, inPosition);
    }

    // first checks rook move is valid
    // second checks bishop move is valid
    public boolean moveValid(int pos, Board board) {
        Rook rook = new Rook(getColor(), getPosition(), false);
        Bishop bishop = new Bishop(getColor(), getPosition());
        return rook.moveValid(pos, board) || bishop.moveValid(pos, board);
    }

    @Override
    public Queen copy() {
        return new Queen(getColor(), getPosition());
    }

    @Override
    public String getName() {
        return name;
    }
}
