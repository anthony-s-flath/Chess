// creates starting pieces
// creates pieces
// holds Pieces[] in board
// handles moving pieces
// handles if there is check in changeBoard()

package game;
import pieces.*;

public class Board {

    public static final int NUM_PIECES = 64;

    // to transfer to another platform, change what the array objects are
    private Piece[] board = new Piece[NUM_PIECES];

    private String turn;

    // creates starting board
    public Board(String topColor, String bottomColor) {
        // real setup of board
        newPiece(0, new Rook(topColor, 0, true));
        newPiece(1, new Knight(topColor, 1));
        newPiece(2, new Bishop(topColor, 2));
        newPiece(3, new Queen(topColor, 3));
        newPiece(4, new King(topColor, 4, true));
        newPiece(5, new Bishop(topColor, 5));
        newPiece(6, new Knight(topColor, 6));
        newPiece(7, new Rook(topColor, 7, true));
        for (int i = 0; i < 8; ++i) {
            newPiece(8 + i, new Pawn(topColor, 8 + i,
                    true, true));
            newPiece(48 + i, new Pawn(bottomColor, 48 + i,
                    true, false));
        }
        newPiece(56, new Rook(bottomColor, 56, true));
        newPiece(57, new Knight(bottomColor, 57));
        newPiece(58, new Bishop(bottomColor, 58));
        newPiece(59, new Queen(bottomColor, 59));
        newPiece(60, new King(bottomColor, 60, true));
        newPiece(61, new Bishop(bottomColor, 61));
        newPiece(62, new Knight(bottomColor, 62));
        newPiece(63, new Rook(bottomColor, 63, true));


        /*
        newPiece(8, new Queen(bottomColor, 8));
        newPiece(0, new Queen(bottomColor, 0));
        newPiece(60, new King(bottomColor, 60, true));

        //newPiece(3, new Queen(topColor, 3));
        newPiece(4, new King(topColor, 4, true));

         */

        /*
        // castling tests
        newPiece(59, new Queen(bottomColor, 59));
        newPiece(60, new King(bottomColor, 60,
                true));

        newPiece(56, new Rook(bottomColor, 56, true));
        newPiece(63, new Rook(bottomColor, 63, true));

        newPiece(0, new Rook(topColor, 0, true));
        newPiece(7, new Rook(topColor, 7, true));

        newPiece(3, new Queen(topColor, 3));
        newPiece(4, new King(topColor, 4, true));

         */
    }

    // safe copy of this
    public Board(Piece[] inBoard) {
        board = cloneBoard(inBoard);
    }

    // used in gui.Load
    public Board(int[] position, String[] color, String[] piece,
                 boolean[] firstMove, boolean[] isTop, boolean[] enPassant) {
        // counters are to traverse the non-indexed by position arrays
        int firstMoveCounter = 0;
        int pawnCounter = 0;
        for (int i = 0; i < position.length; ++i) {
            switch (piece[i]) {
                case "PAWN" -> {
                    newPiece(position[i], new Pawn(color[i], position[i],
                            firstMove[firstMoveCounter],
                            isTop[pawnCounter],
                            enPassant[pawnCounter]));
                    ++firstMoveCounter;
                    ++pawnCounter;
                }
                case "KING" -> {
                    newPiece(position[i], new King(color[i], position[i],
                            firstMove[firstMoveCounter]));
                    ++firstMoveCounter;
                }
                case "KNIGHT" -> newPiece(position[i],
                        new Knight(color[i], position[i]));
                case "BISHOP" -> newPiece(position[i],
                        new Bishop(color[i], position[i]));
                case "QUEEN" -> newPiece(position[i],
                        new Queen(color[i], position[i]));
                case "ROOK" -> {
                    newPiece(position[i], new Rook(color[i], position[i],
                            firstMove[firstMoveCounter]));
                    ++firstMoveCounter;
                }
            }
        }
    }


    // interaction between GUI and game
    // if valid move, makes move and returns true
    // if not valid move, does not make move and returns false
    // checks if pieceIndex moving is correct turnColor
    public boolean changeBoard(int pieceIndex, int targetIndex,
                               String inTurnColor, boolean realBoardMove) {
        turn = inTurnColor;

        // movingPiece is correct turnColor
        if (getPiece(pieceIndex).getColor().equals(turn)) {
            // targetIndex holds a piece, returns if move is valid
            if (getPiece(targetIndex) != null) {
                // if the pieces are not the same color
                // and if move is valid
                if (!getPiece(pieceIndex).getColor().equals(
                        getPiece(targetIndex).getColor())
                        && getPiece(pieceIndex)
                            .moveValid(targetIndex, this)) {
                    // if it moves into check, returns true
                    // if move is valid, makes move and returns false
                    return !isMoveCheck(pieceIndex, targetIndex, realBoardMove);
                } else {
                    return false;
                }
            }
            // targetIndex does not have a piece, checks if valid
             else if (getPiece(pieceIndex).moveValid(targetIndex, this)) {
                // if it moves into check, returns true
                // if move is valid, makes move and returns false
                return !isMoveCheck(pieceIndex, targetIndex, realBoardMove);
            } else {
                return false;
            }
        } else return false;
    }

    // checks if moving the pieceIndex will cause the board to in check
    // if not moving into check, will move board and return false
    public boolean isMoveCheck(int pieceIndex, int targetIndex,
                                boolean realBoardMove) {
        Piece[] newBoard = cloneBoard(board);
        movePiece(pieceIndex, targetIndex, newBoard);
        Integer isCheck = isCheck(newBoard, turn);
        if (isCheck != null) {
            if (realBoardMove) {
                System.out.println("check by: " + isCheck);
            }
            return true;
        } else {
            movePiece(pieceIndex, targetIndex, board);
            return false;
        }
    }
    public static void movePiece(int pieceIndex, int targetIndex,
                                 Piece[] inBoard) {
        inBoard[pieceIndex].movePiece(targetIndex);
        inBoard[targetIndex] = inBoard[pieceIndex];
        inBoard[pieceIndex] = null;
    }

    // determines if king is in check of turn
    // run through each piece of opposite of turn color
    // each piece calls moveValid() to the king
    // if move is valid, returns null
    // else returns position that is causing the check
    private Integer isCheck(Piece[] inBoard, String color) {
        // finds king position
        Integer kingPos = getKingPosition(cloneBoard(inBoard), color);
        // when king is found, checks if king is in check
        for (int i = 0; i < NUM_PIECES; ++i) {

            if (kingPos != null && inBoard[i] != null
                    && !inBoard[i].getColor().equals(color)) {
                if (inBoard[i].moveValid(kingPos, new Board(inBoard))) {
                    return i;
                }
            }
        }
        return null;
    }

    // returns king of specified color in Piece[]
    protected static Integer getKingPosition(Piece[] pieces, String turn) {
        // finds king of turn color
        for (int i = 0; i < NUM_PIECES; ++i) {
            if (pieces[i] != null
                    && pieces[i].getColor().equals(turn)
                    && pieces[i] instanceof King) {
                return i;
            }
        }
        return null;
    }

    public void newPiece(int pos, Piece piece) {
        board[pos] = piece;
    }

    public Piece getPiece(int position) {
        return board[position];
    }

    // safe clone
    public Piece[] getBoardClone() {
        Piece[] newBoard = new Piece[NUM_PIECES];
        for (int i = 0; i < NUM_PIECES; ++i) {
            if (getPiece(i) != null) {
                newBoard[i] = board[i].copy();
            }
        }
        return newBoard;
    }

    public Piece[] getBoard() {
        return board;
    }

    public boolean nameEquals(int position, String name) {
        return getPiece(position).getName().equals(name);
    }

    private Piece[] cloneBoard(Piece[] inBoard) {
        Piece[] newBoard = new Piece[NUM_PIECES];
        for (int i = 0; i < NUM_PIECES; ++i) {
            if (inBoard[i] != null) {
                newBoard[i] = inBoard[i].copy();
            }
        }
        return newBoard;
    }
}
