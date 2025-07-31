import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class WaitingScreen {
    private final Scene scene;
    private String name;

    public WaitingScreen(NetworkMain network, GameMode mode, String name) {
        this.name = name;

        Label msg = new Label("Waiting for opponent...");
        msg.getStyleClass().add("waiting-message");

        Label playerLabel = new Label(name);
        playerLabel.getStyleClass().add("player-label");

        ProgressIndicator progress = new ProgressIndicator();
        progress.getStyleClass().add("waiting-progress");

        Label hi = new Label("Life is like a game of CHESS, OFFER DRAW !");
        hi.getStyleClass().add("waiting-message");

        ImageView chessPiece = new ImageView(new Image("file:resources/img/waiting_icon.png"));
        chessPiece.setFitHeight(100);
        chessPiece.setFitWidth(100);
        chessPiece.getStyleClass().add("waiting-icon");

        VBox root = new VBox(20, msg, playerLabel, progress);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("waiting-root");

        scene = new Scene(root, 720, 720);
        scene.getStylesheets().add("file:resources/css/style_wait.css");

        new Thread(() -> {
            try {

                network.awaitOpponentAndStart();

                Platform.runLater(() -> {
                    GameScreen game = new GameScreen(network, mode, name);
                    Main.instance.setScene(game.getScene());
                });
            } catch (IOException ex) {
                Platform.runLater(() ->
                        AlertUtil.showAlert("Network error", ex.getMessage()));
            }
        }).start();
    }

    public Scene getScene() {
        return scene;
    }
}