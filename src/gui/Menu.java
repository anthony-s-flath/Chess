package gui;

import display.RunGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public abstract class Menu extends Layout {

    // buttons are set with preferred size CoordinateSystem.MENU_SIZE so it
    // // will fill the space until it hits the padding
    public static VBox getMenu(RunGame runGame) {
        VBox menu = new VBox();
        menu.setAlignment(Pos.TOP_LEFT);
        // + 10 accounts for the outline of the board
        menu.setPrefSize(MENU_SIZE + 10, SCENE_SIZE_Y);
        menu.setPadding(new Insets(PADDING_SIZE));
        menu.setSpacing(PADDING_SIZE);


        menu.getChildren().add(NewGame.createNewGameButton(runGame));

        menu.getChildren().add(Save.createSaveButton(runGame));

        menu.getChildren().add(Load.createLoadButton(runGame));

        return menu;
    }

}
