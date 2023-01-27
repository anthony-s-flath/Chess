package gui;

import display.RunGame;
import game.Board;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Load extends Layout {

    private static final File saveFile= new File(
            System.getProperty("user.dir") + "//saves");

    public static Button createLoadButton(RunGame runGame) {
        Button loadButton = new Button("Load");
        loadButton.setPrefWidth(MENU_SIZE);
        loadButton.setOnMouseClicked(loadButtonEvent(runGame));

        return loadButton;
    }

    private static EventHandler<MouseEvent> loadButtonEvent(RunGame runGame) {
        return event -> {
            VBox saveList = new VBox();
            saveList.setStyle("-fx-background-color: Pink");
            saveList.setSpacing(GRID_PADDING);
            saveList.setAlignment(Pos.TOP_CENTER);
            saveList.setPadding(new Insets(GRID_PADDING));
            Scene scene = new Scene(saveList);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Load Game");
            stage.setAlwaysOnTop(true);

            Label label = new Label("Save names:");
            label.setStyle("-fx-font-size: 17");
            saveList.getChildren().add(label);

            ChoiceBox<String> choices = new ChoiceBox<>();
            choices.setPrefWidth(7 * GRID_PADDING);
            displaySaves(saveList, choices);


            HBox enterSave = new HBox();
            enterSave.setPadding(new Insets(GRID_PADDING));
            enterSave.setSpacing(GRID_PADDING);
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnMouseClicked( buttonClicked -> stage.close());
            enterSave.getChildren().add(cancelButton);

            Button loadButton = new Button("Load");
            loadButton.setOnMouseClicked( buttonClicked -> {
                loadSave(runGame, choices);
                stage.close();
            });
            enterSave.getChildren().add(loadButton);

            saveList.getChildren().add(enterSave);


            stage.show();
        };
    }

    private static void displaySaves(VBox saveList, ChoiceBox<String> choices) {
        String[] stringArray = saveFile.list();
        if (stringArray.length != 0) {
            String[] saveStringArray = new String[stringArray.length];
            for (int i = 0; i < stringArray.length; ++i) {
                saveStringArray[i] = stringArray[i].substring(
                        0, stringArray[i].length() - 4);
            }
            choices.setItems(FXCollections.observableArrayList(saveStringArray));
            choices.setValue(saveStringArray[0]);
            saveList.getChildren().add(choices);
        }
    }

    private static void loadSave(RunGame runGame, ChoiceBox<String> choices) {
        String choice = choices.getSelectionModel().getSelectedItem();
        Path path = Paths.get(saveFile + "//"  + choice + ".txt");
        try {
            List<String> lines = Files.readAllLines(path);
            Settings settings = new Settings();
            settings.setTopColor(lines.get(0));
            settings.setTurnOrder(lines.get(1).equals("true"));
            settings.setFirstTurn(lines.get(2));

            // 3 is a new line
            //System.out.println(lines.get(3));

            // position
            // color
            // piece
            // king/rook/pawn values
            int[] position = new int[lines.size() - 4];
            String[] color = new String[lines.size() - 4];
            String[] piece = new String[lines.size() - 4];
            ArrayList<Boolean> firstMoveList = new ArrayList<>();
            ArrayList<Boolean> isTopList = new ArrayList<>();
            ArrayList<Boolean> enPassantList = new ArrayList<>();

            // starts in lines where the line where piece data starts
            for (int i = 0; i < lines.size() - 4; ++i) {
                String[] pieceVar = lines.get(i + 4).split(" ");
                position[i] = Integer.parseInt(pieceVar[0]);
                color[i] = pieceVar[1];
                piece[i] = pieceVar[2];
                if (pieceVar.length >= 4) {
                    // for rook, king, pawn
                    firstMoveList.add(Boolean.parseBoolean(pieceVar[3]));
                    if (pieceVar.length == 6) {
                        // for pawn
                        isTopList.add(Boolean.parseBoolean(pieceVar[4]));
                        enPassantList.add(Boolean.parseBoolean(pieceVar[5]));
                    }
                }
            }

            boolean[] firstMove = toNativeBoolArray(
                    firstMoveList.toArray(new Boolean[0]));
            boolean[] isTop = toNativeBoolArray(
                    isTopList.toArray(new Boolean[0]));
            boolean[] enPassant = toNativeBoolArray(
                    enPassantList.toArray(new Boolean[0]));

            Board board = new Board(position, color, piece, firstMove, isTop, enPassant);
            runGame.loadGame(settings, board);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean[] toNativeBoolArray(Boolean[] boolArray) {
        boolean[] nativeArray = new boolean[boolArray.length];
        for (int i = 0; i < boolArray.length; ++i) {
            nativeArray[i] = boolArray[i];
        }
        return nativeArray;
    }

}
