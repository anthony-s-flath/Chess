// helper class that helps positioning on scene

package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Layout {

    // how large the board should be
    public static final double BOARD_SIZE = 700;
    public static final double SQUARE_SIZE = BOARD_SIZE / 8;
    // the padding outside of the board (the outline covers some of it)
    public static final double PADDING_SIZE = 25;
    public static final double MENU_SIZE = BOARD_SIZE / 6 ;

    public static final double SCENE_SIZE_X = BOARD_SIZE + 2 * PADDING_SIZE
                                                + MENU_SIZE;
    public static final double SCENE_SIZE_Y = BOARD_SIZE + 2 * PADDING_SIZE;
    public static final double GRID_PADDING = PADDING_SIZE - 10;

    public static final int NUM_SQUARES = 64;

    public static int getXCoordinate(int position) {

        // for padding
        int x = (int) PADDING_SIZE;
        int leftover = position % 8;
        x += leftover * SQUARE_SIZE;

        return x + (int) MENU_SIZE;
    }

    public static int getYCoordinate(int position) {

        // for padding
        int y = (int) PADDING_SIZE;
        int rowNum = position / 8;
        y += rowNum * SQUARE_SIZE;

        return y;
    }

    public static Rectangle createRectangle(double width, double height) {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setFill(Color.PINK);
        return rectangle;
    }
}
