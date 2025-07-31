import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Optional;

public class ConnectionScreen {
    private Scene scene;
    private ToggleGroup gameModeToggleGroup;
    private GameMode selectedGameMode = GameMode.CLASSIC;
    public static boolean isBlackConnected=false;

    public ConnectionScreen(String name) {
        Label title = new Label("Select Game Mode");
        title.getStyleClass().add("screen-title");

        Label modeSelectionLabel = new Label("");
        modeSelectionLabel.getStyleClass().add("game-label");
        gameModeToggleGroup = new ToggleGroup();

        RadioButton classicMode = new RadioButton("Classic Chess (Untimed)");
        classicMode.setToggleGroup(gameModeToggleGroup);
        classicMode.setSelected(true);
        classicMode.getStyleClass().add("radio-button");
        classicMode.setUserData(GameMode.CLASSIC);

        RadioButton quickMode = new RadioButton("Quick Chess (Timed)");
        quickMode.setToggleGroup(gameModeToggleGroup);
        quickMode.getStyleClass().add("radio-button");
        quickMode.setUserData(GameMode.QUICK_CHESS);

        gameModeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGameMode = (GameMode) newValue.getUserData();
                System.out.println("Selected Game Mode: " + selectedGameMode);
            }
        });

        Button hostButton = new Button("⛳️ Host Game ");
        Button joinButton = new Button("🎮 Join Game ");
        Button vsComputerButton = new Button(" 🤖Play with BOT");
        Button backButton = new Button("Back");

        hostButton.setOnAction(e -> {
            new Thread(() -> {
                try {
                    NetworkMain connection = Client.connect("127.0.0.1", 4558, name);
                    Platform.runLater(() -> {
                        Main.instance.setScene(new WaitingScreen(connection, selectedGameMode, name).getScene());
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> showAlert("Connection Failed",
                            "Could not connect to server. Make sure the server is running.\nError: " + ex.getMessage()));
                }
            }).start();
        });

        joinButton.setOnAction(e -> {
            Stage ownerStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
            JoinGameDialog dialog = new JoinGameDialog(ownerStage);

            Optional<String> ipResult = dialog.showAndGetResult();

            ipResult.ifPresent(ip -> {
                new Thread(() -> {
                    try {
                        NetworkMain connection = Client.connect(ip, 4558, name);
                        connection.awaitOpponentAndStart();

                        Platform.runLater(() -> {
                            GameScreen gameScreen = new GameScreen(connection, selectedGameMode, name);
                            Main.instance.setScene(gameScreen.getScene());
                        });
                    } catch (Exception ex) {
                        Platform.runLater(() -> showAlert("Connection Failed",
                                "Could not connect to server at " + ip + "\nError: " + ex.getMessage()));
                    }
                }).start();
            });
        });

        vsComputerButton.setOnAction(e -> {
            DifficultySelectionScreen diffScreen = new DifficultySelectionScreen(name);
            Main.instance.setScene(diffScreen.getScene());
        });

        backButton.setOnAction(e -> {
            MainMenu menu = new MainMenu(name);
            Main.instance.setScene(menu.getScene());
        });
        HBox buttonRow1 = new HBox(10, hostButton, joinButton);
        buttonRow1.setAlignment(Pos.CENTER);
        HBox buttonRow2 = new HBox(10, vsComputerButton);
        buttonRow2.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, title, modeSelectionLabel, classicMode, quickMode,
                buttonRow1, buttonRow2, backButton);

        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("root");
        layout.setPadding(new Insets(30));
        layout.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                backButton.fire();
            }
        });
        hostButton.getStyleClass().add("menu-button");
        joinButton.getStyleClass().add("join-button");
        vsComputerButton.getStyleClass().add("bot-button");
        backButton.getStyleClass().add("back-button");

        scene = new Scene(layout, 720, 720);
        scene.getStylesheets().add("file:resources/css/style_game.css");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Connection Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}