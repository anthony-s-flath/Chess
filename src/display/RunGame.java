// connects Game, Background, and Piece
// has instance of each
// holds Group with each a group of elements within each instance
// contains EventHandlers
// checks for end of game

package display;

import game.Board;
import gui.Layout;
import gui.Settings;
import game.Game;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import pieces.Piece;

import java.io.FileNotFoundException;
import java.util.Objects;

public class RunGame extends Layout {

    // group holds 4 children
    // index 0 is Group for buttons with event handlers
    // index 1 is Group for background squares
    // index 2 is Group for pieces
    // index 0 is Rectangle for outline
    private final Group group = new Group();

    // connection to game engine
    private Game game;
    // display for pieces on board
    private PiecesDisplay piecesDisplay;
    // has Rectangle[64]
    private Background background;
    // settings for game
    private Settings settings;

    // used by gui.Load
    public void loadGame(Settings inSettings, Board board)
            throws FileNotFoundException {
        settings = inSettings;
        game = new Game(inSettings, board);
        piecesDisplay = new PiecesDisplay(game);
        background = new Background(settings.getTopColor());
        run();
    }

    // used by gui.NewGame
    public void newGame(Settings inSettings) throws FileNotFoundException {
        settings = inSettings;
        game = new Game(settings);
        piecesDisplay = new PiecesDisplay(game);
        background = new Background(settings.getTopColor());
        run();
    }

    public void run() throws FileNotFoundException {
        // displays pieces
        piecesDisplay.renderBoard();
        // add index 0, 1, 2 to group
        group.getChildren().setAll(Background.createOutline(),
                background.getBackgroundGroup(), piecesDisplay.getPiecesGroup());

        // creates index 4 of group
        createButtons();
    }

    // creates a button for each tile on board
    private void createButtons() {
        Group buttons = new Group();
        buttons.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("ButtonStyle.css")).toExternalForm());
        for (int i = 0; i < NUM_SQUARES; ++i) {
            Button button = new Button();
            button.setPrefSize(SQUARE_SIZE, SQUARE_SIZE);
            button.setLayoutX(getXCoordinate(i));
            button.setLayoutY(getYCoordinate(i));
            // add EventHandler to button
            EventHandler<MouseEvent> eventHandler = createEventHandler(i);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
            buttons.getChildren().add(button);
        }
        // index 3 of group
        group.getChildren().add(buttons);
    }
    
    // if click is valid, makes appropriate changes:
    // selection1, selection2, changeBoard(), pieces.movePieces()
    private EventHandler<MouseEvent> createEventHandler(int selection) {
        return mouseEvent -> {
           if (game.makeSelection(selection)) {
               try {
                   piecesDisplay.renderBoard();
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               }
           }
            if (game.getIsEnd()) {
                gameEnd();
            }
        };
    }

    private void gameEnd() {
        System.out.println("End of game");
    }

    public Group getGroup() {
        return group;
    }

    public Settings getSettings() {
        return settings;
    }

    public Piece[] copyBoardArray() {
        return game.getBoard().getBoardClone();
    }

}
