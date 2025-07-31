import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SplashScreen {
    private Scene scene;

    public SplashScreen() {
        ImageView chessIcon = new ImageView(new Image("file:resources/img/chess.png"));
        chessIcon.setFitHeight(120);
        chessIcon.setPreserveRatio(true);
        chessIcon.getStyleClass().add("splash-icon");

        Label title = new Label("NETWORK CHESS");
        title.getStyleClass().add("splash-title");

        Label loading = new Label("Loading...");
        loading.getStyleClass().add("splash-loading");

        VBox layout = new VBox(15, chessIcon, title, loading);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("splash-root");

        scene = new Scene(layout, 720, 720);
        scene.getStylesheets().add("file:resources/css/style.css");

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            LoginScreen login = new LoginScreen();
            Main.instance.setScene(login.getScene());
        });
        delay.play();
    }

    public Scene getScene() {
        return scene;
    }
}