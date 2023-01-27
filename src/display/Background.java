// creates background (Rectangle[])
// also holds createOutline() which returns the outline of the board (Rectangle)

package display;

import gui.Layout;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Background extends Layout {
    private final Rectangle[] squares = new Rectangle[NUM_SQUARES];
    private final Group backgroundGroup = new Group();

    // if black is top color for pieces, pink will be top color for squares
    // color that corresponds to the top
    private Color topColor;
    // color that corresponds to the bottom
    private Color bottomColor;

    public Background(String topColorString) {
        createColors(topColorString);
        createSquares();
    }

    public Group getBackgroundGroup() {
        for (int i = 0; i < NUM_SQUARES; ++i) {
            backgroundGroup.getChildren().add(squares[i]);
        }
        return backgroundGroup;
    }

    private void createSquares() {
        // colorCounter is used to checker the board
        int colorCounter = 0;
        for (int i = 0; i < NUM_SQUARES; ++i) {

            squares[i] = new Rectangle(getXCoordinate(i), getYCoordinate(i),
                                    SQUARE_SIZE, SQUARE_SIZE);
            if (i % 8 == 0 && i != 0) {
                ++colorCounter;
            }

            if (colorCounter % 2 == 0) {
                if (i % 2 == 0) {
                    squares[i].setFill(bottomColor);
                } else {
                    squares[i].setFill(topColor);
                }
            }
            else {
                if (i % 2 == 1) {
                    squares[i].setFill(bottomColor);
                } else {
                    squares[i].setFill(topColor);
                }
            }
        }
    }

    public static Rectangle createOutline() {
        int helper = 10;
        Rectangle outline = new Rectangle(PADDING_SIZE - helper + MENU_SIZE,
                                         PADDING_SIZE - helper,
                                         BOARD_SIZE + 2 * helper,
                                         BOARD_SIZE + 2 * helper);
        outline.setFill(Color.PINK);
        return outline;
    }

    private void createColors(String topColorString) {
        if (topColorString.equals("BLACK")) {
            topColor = Color.PINK;
            bottomColor = Color.WHITE;
        } else {
            topColor = Color.WHITE;
            bottomColor = Color.PINK;
        }
    }
}
