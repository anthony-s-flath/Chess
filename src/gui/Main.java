// runs primaryStage

package gui;

import display.PiecesDisplay;
import display.RunGame;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileNotFoundException;


public class Main extends Application {

    // index 1 is a group of 3 children (see RunGame line 28)
    // index 2 is a VBox containing the menu
    private final Group root = new Group();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Settings settings = new Settings();
        RunGame runGame = new RunGame();
        runGame.newGame(settings);
        root.getChildren().add(runGame.getGroup());

        // everything runs through menu
        root.getChildren().add(Menu.getMenu(runGame));

        Scene scene = new Scene(root, Layout.SCENE_SIZE_X,
                Layout.SCENE_SIZE_Y);
        primaryStage.setScene(scene);
        primaryStage.setTitle("chess");
        primaryStage.show();
    }

    // if i would like to implement a nice background
    private void createBackground() throws FileNotFoundException {
        // loading screen
        Image backgroundImage = new Image(
                PiecesDisplay.getImage("background.png"));
        ImageView backgroundView = new ImageView();
        backgroundView.setImage(backgroundImage);
        backgroundView.setFitHeight(Layout.SCENE_SIZE_Y);
        backgroundView.setFitWidth(Layout.SCENE_SIZE_X);
        backgroundView.setX(0);
        backgroundView.setY(0);
        root.getChildren().add(backgroundView);
    }

    public static void main(String[] args) {
        launch(args);
    }

}