package gui;

import display.RunGame;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public abstract class NewGame extends Layout {


    private static final double GRID_WIDTH = SCENE_SIZE_X / 2
                                    - 2 * GRID_PADDING;
    private static final double GRID_HEIGHT = SCENE_SIZE_Y / 2.5
                                    - 2 * GRID_PADDING;

    public static Button createNewGameButton(RunGame runGame) {
        Button newGameButton = new Button("New Game");
        newGameButton.setPrefWidth(MENU_SIZE);
        newGameButton.setOnMouseClicked(newGameEvent(runGame));
        return newGameButton;
    }

    private static EventHandler<MouseEvent> newGameEvent(RunGame runGame) {
        return mouseEvent -> {
            Settings settings = new Settings();
            GridPane gridPane = createGridPane();
            Scene scene = new Scene(gridPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("New Game");
            stage.setAlwaysOnTop(true);
            stage.show();

            // index 0
            gridPane.add(createRectangle(GRID_WIDTH, GRID_HEIGHT),
                    0, 0, 1, 5);

            Text text = new Text("Settings:");
            text.setFill(Color.WHITE);
            text.setStroke(Color.BLACK);
            text.setStyle("-fx-font: bold 36px verdana;");
            GridPane.setHalignment(text, HPos.CENTER);
            // index 1
            gridPane.add(text, 0, 0, 3, 1);

            Button newGameButton = new Button("New Game");
            newGameButton.setOnAction(newGameEvent -> {
                try {
                    runGame.newGame(settings);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            // index 2
            gridPane.add(newGameButton, 2, 4);

            Button cancelButton = new Button("Cancel");
            cancelButton.setOnMouseClicked( buttonClicked -> stage.close());
            GridPane.setHalignment(cancelButton, HPos.CENTER);
            // index 3
            gridPane.add(cancelButton, 1, 4);


            // index 4-6
            createTopColorOption(gridPane, settings);
            // index 7-9
            createTurnOrderOption(gridPane, settings);

            stage.show();
        };
    }

    private static GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: White");
        gridPane.setPadding(new Insets(GRID_PADDING));

        // constraints added together are equal to dimensions of gridPane
        ColumnConstraints firstColumn = new ColumnConstraints(
                2 * GRID_WIDTH / 5);
        ColumnConstraints otherColumns = new ColumnConstraints(
                3 * (GRID_WIDTH) / 10);
        gridPane.getColumnConstraints().add(firstColumn);
        gridPane.getColumnConstraints().add(otherColumns);
        gridPane.getColumnConstraints().add(otherColumns);

        RowConstraints rowConstraints = new RowConstraints(GRID_HEIGHT / 5);
        for (int i = 0; i < 5; ++i) {
            gridPane.getRowConstraints().add(rowConstraints);
        }

        return gridPane;
    }

    // automizes font size and alignment for labels when adding to gridPane
    private static void addToGridPane(GridPane gridPane, Node node,
                                      int colIndex, int rowIndex) {
        if (node instanceof Label) {
            GridPane.setHalignment(node, HPos.CENTER);
        }
        node.setStyle("-fx-font-size: 17");
        gridPane.add(node, colIndex, rowIndex);
    }

    private static void createTopColorOption(GridPane gridPane,
                                             Settings settings) {
        Label topColorLabel = new Label("Color on top:");
        addToGridPane(gridPane, topColorLabel, 0, 1);
        CheckBox blackTopColor = new CheckBox("Black");
        addToGridPane(gridPane, blackTopColor, 1, 1);
        CheckBox whiteTopColor = new CheckBox("White");
        addToGridPane(gridPane, whiteTopColor, 2, 1);
        if (settings.getTopColor().equals("BLACK")) {
            blackTopColor.setSelected(true);
        } else {
            whiteTopColor.setSelected(true);
        }
        blackTopColor.setOnMouseClicked(mouseEvent -> {
            whiteTopColor.setSelected(false);
            settings.setTopColor("BLACK");
        });
        whiteTopColor.setOnMouseClicked(mouseEvent -> {
            blackTopColor.setSelected(false);
            settings.setTopColor("WHITE");
        });
    }

    private static void createTurnOrderOption(GridPane gridPane,
                                              Settings settings) {
        Label turnsLabel = new Label("Track turns:");
        addToGridPane(gridPane, turnsLabel, 0, 2);
        CheckBox onBox = new CheckBox("On");
        addToGridPane(gridPane, onBox, 1, 2);
        CheckBox offBox = new CheckBox("Off");
        addToGridPane(gridPane, offBox, 2, 2);
        if (settings.getTurnOrder()) {
            createFirstTurnOption(gridPane, settings);
            onBox.setSelected(true);
        } else {
            offBox.setSelected(true);
        }
        onBox.setOnMouseClicked(mouseEvent -> {
            offBox.setSelected(false);
            settings.setTurnOrder(true);
            if (gridPane.getChildren().size() < 11) {
                // index 10-12
                createFirstTurnOption(gridPane, settings);
            }
        });
        offBox.setOnMouseClicked(mouseEvent -> {
            onBox.setSelected(false);
            settings.setTurnOrder(false);

            // firstTurnOption nodes are put in index 10-12
            for (int i = 0; i < 3; ++i) {
                if (gridPane.getChildren().size() >= 11) {
                    gridPane.getChildren().remove(10);
                }
            }
        });
    }

    private static void createFirstTurnOption(GridPane gridPane,
                                              Settings settings) {
        Label firstTurn = new Label("First turn:");
        addToGridPane(gridPane, firstTurn, 0, 3);
        CheckBox blackFirstTurn = new CheckBox("Black");
        addToGridPane(gridPane, blackFirstTurn, 1, 3);
        CheckBox whiteFirstTurn = new CheckBox("White");
        addToGridPane(gridPane, whiteFirstTurn, 2, 3);
        if (settings.getFirstTurn().equals("BLACK")) {
            blackFirstTurn.setSelected(true);
        } else {
            whiteFirstTurn.setSelected(true);
        }
        blackFirstTurn.setOnMouseClicked(mouseEvent -> {
            whiteFirstTurn.setSelected(false);
            settings.setFirstTurn("BLACK");
        });
        whiteFirstTurn.setOnMouseClicked(mouseEvent -> {
            blackFirstTurn.setSelected(false);
            settings.setFirstTurn("WHITE");
        });
    }

}
