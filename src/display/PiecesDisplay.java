package display;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import game.Game;
import gui.Layout;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pieces.Piece;
import pieces.*;

public class PiecesDisplay extends Layout {
    Game game;

    private final Group groupPieces = new Group();

    private final ImageView[] images = new ImageView[NUM_SQUARES];

    // accesses images in resources
    private final Image whiteRookImage =
            new Image(getImage("whiterook.png"));
    private final Image whiteKnightImage =
            new Image(getImage("whiteknight.png"));
    private final Image whiteBishopImage =
            new Image(getImage("whitebishop.png"));
    private final Image whiteKingImage =
            new Image(getImage( "whiteking.png"));
    private final Image whiteQueenImage =
            new Image(getImage("whitequeen.png"));
    private final Image whitePawnImage =
            new Image(getImage("whitepawn.png"));
    private final Image blackPawnImage =
            new Image(getImage("blackpawn.png"));
    private final Image blackRookImage =
            new Image(getImage("blackrook.png"));
    private final Image blackKnightImage =
            new Image(getImage("blackknight.png"));
    private final Image blackBishopImage =
            new Image(getImage("blackbishop.png"));
    private final Image blackQueenImage =
            new Image(getImage("blackqueen.png"));
    private final Image blackKingImage =
            new Image(getImage("blackking.png"));


    public PiecesDisplay(Game inGame) throws FileNotFoundException {
        game = inGame;
    }

    // renders each piece
    // gets pieces from game.getBoardClone().getPiece(index)
    public void renderBoard() throws FileNotFoundException {
        for (int i = 0; i < NUM_SQUARES; ++i) {
            images[i] = null;
            if (game.getBoard().getPiece(i) != null) {
                renderPiece(i, game.getBoard().getPiece(i));
            }
        }
        refreshGroup();
    }

    // puts piece on the screen
    // puts piece in board
    private void renderPiece(int position, Piece piece)
            throws FileNotFoundException {
        // image will be changed
        // initialized as random picture
        Image image = new Image(getImage("background.png"));
        String color = piece.getColor();
        if (piece instanceof Rook) {
            if (color.equals("WHITE")) {
                image = whiteRookImage;
            } else {
                image = blackRookImage;
            }
        } else if (piece instanceof Knight) {
            if (color.equals("WHITE")) {
                image = whiteKnightImage;
            } else {
                image = blackKnightImage;
            }
        } else if (piece instanceof Bishop) {
            if (color.equals("WHITE")) {
                image = whiteBishopImage;
            } else {
                image = blackBishopImage;
            }
        } else if (piece instanceof Queen) {
            if (color.equals("WHITE")) {
                image = whiteQueenImage;
            } else {
                image = blackQueenImage;
            }
        } else if (piece instanceof King) {
            if (color.equals("WHITE")) {
                image = whiteKingImage;
            } else {
                image = blackKingImage;
            }
        } else if (piece instanceof Pawn) {
            if (color.equals("WHITE")) {
                image = whitePawnImage;
            } else {
                image = blackPawnImage;
            }
        }
        createImage(position, image);
    }

    private void createImage(int pos, Image image) {
        ImageView imageView = new ImageView();
        imageView.setX(getXCoordinate(pos));
        imageView.setY(getYCoordinate(pos));
        imageView.setFitHeight(SQUARE_SIZE);
        imageView.setFitWidth(SQUARE_SIZE);
        imageView.setImage(image);
        images[pos] = imageView;
        groupPieces.getChildren().add(imageView);
    }

    // clears group and adds all in correct order
    private void refreshGroup() {
        groupPieces.getChildren().setAll();
        for (int i = 0; i < NUM_SQUARES; ++i) {
            if (images[i] != null) {
                groupPieces.getChildren().add(images[i]);
            }
         }
    }

    public static FileInputStream getImage(String string)
            throws FileNotFoundException {
        return new FileInputStream(System.getProperty("user.dir")
                + "//resources//" + string);
    }

    public Group getPiecesGroup() {
        return groupPieces;
    }
}
