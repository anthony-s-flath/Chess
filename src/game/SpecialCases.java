// used in special cases such as
// // castling, promotion, end of game, en passant

package game;

import pieces.*;

public abstract class SpecialCases {

    // checks if piece moves is pawn at end of board
    // switches pawn with desired piece (queen)
    public static void checkPawnSwitch(Board board,
                                       String turn, int piecePosition) {
        if (board.nameEquals(piecePosition, "PAWN")) {
            if (((Pawn) board.getPiece(piecePosition)).getIsTop()) {
                if (piecePosition >= 56 && piecePosition <= 63) {
                    board.newPiece(piecePosition,
                            new Queen(turn, piecePosition));
                }
            } else {
                if (piecePosition >= 0 && piecePosition <= 7) {
                    board.newPiece(piecePosition,
                            new Queen(turn, piecePosition));
                }
            }
        }
    }

    // checks if a castling occurs and moves pieces accordingly
    // if piece moved is rook or king, sets firstMove as false\
    // conditions for castling:
    // 1. king and rook have not moved
    // 2. king not in check
    // 3. king does not pass through check
    // 4. no pieces between king and rook
    public static void checkCastle(Board board, int movedPiece) {
        if (board.nameEquals(movedPiece, "KING")) {
            ((King) board.getPiece(movedPiece)).setFirstMove();
            if (((King) board.getPiece(movedPiece)).getCastled()) {
                // positions of possible rooks
                int leftRook = Piece.DIMENSION * (movedPiece / Piece.DIMENSION);
                int rightRook = leftRook + 7;
                if (((King) board.getPiece(movedPiece)).getLeftCastled()) {
                    // moves left rook to movedPiece + 1
                    Board.movePiece(leftRook, movedPiece + 1,
                            board.getBoard());
                    ((Rook) board.getPiece(movedPiece + 1))
                            .setFirstMove();
                } else {
                    // moved right rook to movedPiece - 1
                    Board.movePiece(rightRook, movedPiece - 1,
                            board.getBoard());
                    ((Rook) board.getPiece(movedPiece - 1))
                            .setFirstMove();
                }

                ((King) board.getPiece(movedPiece)).setCastled();
            }
        } else if (board.nameEquals(movedPiece, "ROOK")) {
            ((Rook) board.getPiece(movedPiece)).setFirstMove();
        }
    }

    // checks if en passant occurs
    public static void checkEnPassant(Board board) {
        for (int i = 0; i < Board.NUM_PIECES; ++i) {
            if (board.getPiece(i) != null
                    && board.nameEquals(i, "PAWN")) {
                ((Pawn) (board.getPiece(i))).enPassantRefresh();
                if ((((Pawn) board.getPiece(i)).isEnPassantCaptured())) {
                    board.newPiece(i, null);
                }
            }
        }
    }

    // checks for only checkmate or stalemate
    // returns if either happens
    public static boolean checkEnd(Board board, String nextTurn) {
        boolean canNoPieceMove = true;
        // checks if any piece can move
        for (int i = 0; i < Board.NUM_PIECES; ++i) {
            if (board.getPiece(i) != null
                    && board.getPiece(i).getColor().equals(nextTurn)) {
                for (int j = 0; j < Board.NUM_PIECES; ++j) {
                    Board newBoard = new Board(board.getBoardClone());
                    if (newBoard.changeBoard(i, j, nextTurn, false)) {
                        canNoPieceMove = false;
                    }
                }
            }
        }
        return canNoPieceMove;
    }
}
