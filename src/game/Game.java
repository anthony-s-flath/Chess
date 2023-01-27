// controls Board
// Interacts with GUI through makeMove()


package game;

import gui.Settings;
import pieces.Piece;

import java.io.FileNotFoundException;

public class Game {

    private final Board board;
    // if turn order should be followed
    private final boolean turnOrder;
    // current color turn
    private String turn;
    private boolean isEnd = false;

    // starts instance
    // top color for what color pieces are on top of board
    // first turn to determine what color moves first
    public Game(Settings settings) {
        board = new Board(settings.getTopColor(),
                getOppositeColor(settings.getTopColor()));
        turnOrder = settings.getTurnOrder();
        turn = settings.getFirstTurn();
    }

    public Game(Settings settings, Board inBoard) {
        board = inBoard;
        turnOrder = settings.getTurnOrder();
        turn = settings.getFirstTurn();
    }

    // helpers when making selection
    private Integer selection1 = null;
    private Integer selection2 = null;
    public boolean makeSelection(int selection) {
        boolean moveMade = false;
        // if first selection is not placed, place it
        if (selection1 == null && getBoard().getPiece(selection) != null) {
            selection1 = selection;
            System.out.println("Game.selection1: " + selection1);
        }
        // else makes second selection
        else if (selection1 != null && selection2 == null) {
            selection2 = selection;
            System.out.println("Game.selection2: " + selection2);
            // if true, the board is changed
            if (!selection1.equals(selection2)
                    && makeMove(selection1, selection2)) {
                System.out.println("move made\n");
                moveMade = true;
            } else {
                System.out.println("not valid\n");
            }
            // restart selection process
            selection1 = null;
            selection2 = null;
        }
        return moveMade;
    }

    // if the move is valid:
    // // it will change the board
    // // return true
    // // call runEndOfTurn
    // if the move is not valid, return false
    private boolean makeMove(int pieceIndex, int targetIndex) {
        if (turnOrder) {
            boolean boardChanged = board.changeBoard(pieceIndex, targetIndex,
                    turn, true);
            endOfTurn(targetIndex, boardChanged);
            return boardChanged;
        } else {
            String nonOrderTurn = board.getPiece(pieceIndex).getColor();
            boolean boardChanged = board.changeBoard(pieceIndex, targetIndex,
                    nonOrderTurn, true);
            endOfTurn(targetIndex, boardChanged);
            return boardChanged;
        }
    }

    // if board is changed, called checkPawnSwitch(), checkCastleEvent()
    // always calls checkEnd() for stalemate or checkmate
    private void endOfTurn(int movedPiece, boolean boardChanged) {
        if (boardChanged) {
            isEnd = SpecialCases.checkEnd(board, getOppositeColor(turn));
            SpecialCases.checkPawnSwitch(board, turn, movedPiece);
            SpecialCases.checkCastle(board, movedPiece);
            SpecialCases.checkEnPassant(board);
            changeTurn();
        }
    }

    private String getOppositeColor(String inColor) {
        if (inColor.equals("WHITE")) {
            return "BLACK";
        } else {
            return "WHITE";
        }
    }

    private void changeTurn() {
        if (turn.equals("WHITE")) {
            turn = "BLACK";
        } else {
            turn = "WHITE";
        }
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    public Board getBoard() {
        return board;
    }

}
