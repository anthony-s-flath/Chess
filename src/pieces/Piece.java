package pieces;

import game.Board;

public abstract class Piece {

    public static final int DIMENSION = 8;

    private final String color;
    private int position;

    public Piece(String inColor, int inPosition) {
        color = inColor;
        position = inPosition;
    }

    public boolean moveValid(int target, Board board) {
        System.out.println("Error... Piece.moveValid was called");
        return false;
    }

    public void movePiece(int pos) {
        position = pos;
    }

    public String getColor() {
        return color;
    }

    public int getPosition() {
        return position;
    }

    public Piece copy() {
        System.out.println("Error... Piece.copy() was called");
        return new Bishop("ERROR", -1);
    }

    public String getName() {
        System.out.println("Error... Piece.getName() was called");
        return "ERROR";
    }

}
