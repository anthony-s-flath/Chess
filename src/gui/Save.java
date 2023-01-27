package gui;

import display.RunGame;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import pieces.Rook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Save extends Layout {

    private static final double GRID_WIDTH = SCENE_SIZE_X / 3;
    private static final double GRID_HEIGHT = SCENE_SIZE_Y / 7;

    public static Button createSaveButton(RunGame runGame) {
        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(MENU_SIZE);
        saveButton.setOnMouseClicked(saveEvent(runGame));
        return saveButton;
    }

    private static EventHandler<MouseEvent> saveEvent(RunGame runGame) {
        return mouseEvent -> {
            GridPane gridPane = createGridPane();
            Scene scene = new Scene(gridPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Save");
            stage.setAlwaysOnTop(true);
            stage.show();

            gridPane.add(createRectangle(GRID_WIDTH + 2 * GRID_PADDING,
                    GRID_HEIGHT), 0, 0, 3, 2);

            Label  label = new Label("Save name:");
            label.setStyle("-fx-font-size: 16");
            gridPane.add(label, 0, 0);
            GridPane.setHalignment(label, HPos.CENTER);

            TextField textField = new TextField();
            textField.setPromptText("Enter save name");
            textField.setMaxWidth(5 * GRID_WIDTH / 8 - GRID_PADDING);
            GridPane.setHalignment(textField, HPos.CENTER);
            gridPane.add(textField, 1, 0, 2, 1);



            Button cancelButton = new Button("Cancel");
            cancelButton.setOnMouseClicked( buttonClicked -> stage.close());
            GridPane.setHalignment(cancelButton, HPos.RIGHT);
            gridPane.add(cancelButton, 1, 1);

            Button newGameButton = new Button("Save");
            newGameButton.setOnAction(newGameEvent -> {
                if (textField.getText().length() > 0) {
                    save(runGame, textField.getText(), stage);
                }
            });
            GridPane.setHalignment(newGameButton, HPos.CENTER);
            gridPane.add(newGameButton, 2, 1);

        };
    }

    private static GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: White");
        gridPane.setPrefSize(GRID_WIDTH, GRID_HEIGHT);
        gridPane.setPadding(new Insets(GRID_PADDING));
        gridPane.setHgap(GRID_PADDING);
        gridPane.getColumnConstraints().add(
                new ColumnConstraints(3 * GRID_WIDTH / 8));
        gridPane.getColumnConstraints().add(
                new ColumnConstraints(3 * GRID_WIDTH / 8));
        gridPane.getColumnConstraints().add(
                new ColumnConstraints(GRID_WIDTH / 4));
        gridPane.getRowConstraints().add(
                new RowConstraints((GRID_HEIGHT - GRID_PADDING) / 2));
        gridPane.getRowConstraints().add(
                new RowConstraints((GRID_HEIGHT - GRID_PADDING) / 2));
        return gridPane;
    }

    // attempts to save game to saveName.txt
    // either opens a stage that announces game was saved
    // or opens a stage
    private static void save(RunGame runGame, String saveName, Stage stage) {
        try {

            File saveFile = new File(System.getProperty("user.dir")
                    + "//saves//" + saveName + ".txt");
            if (saveFile.createNewFile()) {
                saveStage();
                writeSave(saveFile, runGame);
                stage.close();
            } else {
                errorStage(saveFile, runGame, stage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // presents a stage with an ok button announcing that the file was saved
    private static void saveStage() {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: Pink");
        vBox.setPrefSize(GRID_WIDTH / 1.8, GRID_HEIGHT / 1.5);
        vBox.setSpacing(GRID_PADDING);
        vBox.setPadding(new Insets(GRID_PADDING));
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Save");
        stage.setAlwaysOnTop(true);

        Label gameSavedLabel = new Label("Game saved");
        gameSavedLabel.setStyle("-fx-font-size: 17");
        vBox.getChildren().add(gameSavedLabel);

        Button okayButton = new Button("Okay");
        okayButton.setOnMouseClicked(mouseEvent -> stage.close());
        vBox.getChildren().add(okayButton);

        stage.show();
    }

    // asks if the save named saveName should be overwritten with current data
    private static void errorStage(File saveFile, RunGame runGame,
                                   Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(GRID_PADDING));
        gridPane.setVgap(GRID_PADDING);
        gridPane.setStyle("-fx-background-color: Pink");
        Scene scene = new Scene(gridPane);
        Stage errorStage = new Stage();
        errorStage.setScene(scene);
        errorStage.setTitle("Overwrite");
        errorStage.setAlwaysOnTop(true);

        Label gameSavedLabel = new Label("Overwrite save?");
        gameSavedLabel.setStyle("-fx-font-size: 17");
        gridPane.add(gameSavedLabel, 0, 0, 2, 1);

        Button noButton = new Button("No");
        GridPane.setHalignment(noButton, HPos.CENTER);
        noButton.setOnMouseClicked(mouseEvent -> errorStage.close());
        gridPane.add(noButton, 0, 1);

        Button yesButton = new Button("Yes");
        GridPane.setHalignment(yesButton, HPos.CENTER);
        yesButton.setOnMouseClicked(mouseEvent -> {
            writeSave(saveFile, runGame);
            errorStage.close();
            stage.close();
        });
        gridPane.add(yesButton, 1, 1);

        gridPane.getColumnConstraints().add(
                new ColumnConstraints(4 * GRID_PADDING));
        errorStage.show();
    }

    // writes save data to saveName.txt
    // 1 topColor
    // 2 turnOrder
    // 3 firstTurn
    // 4
    // 5 (board positions down)
    private static void writeSave(File saveFile, RunGame runGame) {
        Settings settings = runGame.getSettings();
        Piece[] board = runGame.copyBoardArray();

        try {
            FileWriter saveWriter = new FileWriter(saveFile);
            saveWriter.write(settings.getTopColor() + "\n");
            saveWriter.write(settings.getTurnOrder() + "\n");
            saveWriter.write(settings.getFirstTurn() + "\n");

            saveWriter.write("\n");

            for (int i = 0; i < NUM_SQUARES; ++i) {
                if (board[i] instanceof Pawn) {
                    saveWriter.write(i + " " + board[i].getColor() + " "
                            + board[i].getName() + " "
                            + ((Pawn) board[i]).getFirstMove() + " "
                            + ((Pawn) board[i]).getIsTop() + " "
                            + ((Pawn) board[i]).getCanBeEnPassantCaptured()
                            + "\n");
                } else if (board[i] instanceof King) {
                    saveWriter.write(i + " " + board[i].getColor() + " "
                            + board[i].getName() + " "
                            + ((King) board[i]).getFirstMove() + "\n");
                } else if (board[i] instanceof Rook) {
                    saveWriter.write(i + " " + board[i].getColor() + " "
                            + board[i].getName() + " "
                            + ((Rook) board[i]).getFirstMove() + "\n");
                } else if (board[i] != null) {
                    saveWriter.write(i + " " + board[i].getColor() + " "
                            + board[i].getName() + "\n");
                }
            }
            saveWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
