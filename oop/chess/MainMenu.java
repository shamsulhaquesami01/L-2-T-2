import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;

public class MainMenu {
    private Scene scene;
    private String name;

    public MainMenu(String name) {
        this.name = name;
        initializeUI();
    }

    private void initializeUI() {
        ImageView avatar = new ImageView(new Image("file:resources/img/logo2.png")); // Replace with your image
        avatar.setFitHeight(120);
        avatar.setFitWidth(150);
        avatar.setPreserveRatio(true);
        avatar.getStyleClass().add("avatar");
        Button startButton = new Button("♟️ Start Game");
        Button gameHistoryButton = new Button("📊 Game History");
        Button aboutButton = new Button("ℹ️ About");
        Button chatButton = new Button("💬 Gemini AI");

        startButton.getStyleClass().add("menu-button");
        gameHistoryButton.getStyleClass().add("menu-button");
        aboutButton.getStyleClass().add("menu-button");
        chatButton.getStyleClass().add("menu-button");

        startButton.setOnAction(e -> {
            ConnectionScreen connect = new ConnectionScreen(name);
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(connect.getScene());
        });

        gameHistoryButton.setOnAction(e -> {
            GameHistoryScreen historyScreen = new GameHistoryScreen(name);
            Stage stage = (Stage) gameHistoryButton.getScene().getWindow();
            stage.setScene(historyScreen.getScene());
        });

        aboutButton.setOnAction(e -> {
            AboutScreen about = new AboutScreen(name);
            Stage stage = (Stage) aboutButton.getScene().getWindow();
            stage.setScene(about.getScene());
        });
        chatButton.setOnAction(e -> {
            ChatWithGemini chat = new ChatWithGemini(name);
            Stage stage = (Stage) chatButton.getScene().getWindow();
            stage.setScene(chat.getScene());
        });

        VBox vbox = new VBox(20, avatar, startButton, gameHistoryButton, aboutButton, chatButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(410);
        vbox.setLayoutY(160);

        // 3. Status Bar
        Label statusLabel = new Label("Logged in as " + name );
        statusLabel.getStyleClass().add("status-bar");
        HBox statusBar = new HBox(statusLabel);
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(15));
        statusBar.setLayoutX(280);
        statusBar.setLayoutY(600);
        Pane root = new Pane(vbox,statusBar);
        root.getStyleClass().add("root");
        List<Button> buttons = Arrays.asList(startButton, gameHistoryButton, aboutButton, chatButton);
        for (Button btn : buttons) {
            btn.setFocusTraversable(true);
            btn.setOnKeyPressed(event -> {
                int idx = buttons.indexOf(btn);
                if (event.getCode() == KeyCode.UP && idx > 0) {
                    buttons.get(idx - 1).requestFocus();
                } else if (event.getCode() == KeyCode.DOWN && idx < buttons.size() - 1) {
                    buttons.get(idx + 1).requestFocus();
                } else if (event.getCode() == KeyCode.ENTER) {
                    btn.fire();
                }
            });
        }

        scene = new Scene(root, 720, 720);
        scene.getStylesheets().add("file:resources/css/style.css");
    }

    public Scene getScene() {
        return scene;
    }
}