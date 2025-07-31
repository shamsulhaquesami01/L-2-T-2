import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DifficultySelectionScreen {
    private Scene scene;
    String name;
    public DifficultySelectionScreen(String name) {
        Label title = new Label("Select AI Difficulty");
        title.getStyleClass().add("screen-title");
        this.name =name;
        Button beginnerBtn = new Button("Beginner ");
        Button advancedBtn = new Button("Advanced ");
        Button intermediateBtn= new Button("Intermeidate");
        Button backBtn = new Button("Back");

        beginnerBtn.getStyleClass().add("diff-button");
        intermediateBtn.getStyleClass().add("diff-button");
        advancedBtn.getStyleClass().add("diff-button");
        backBtn.getStyleClass().add("back-button");

        beginnerBtn.setOnAction(e -> {
            GameScreenVsComputer game = new GameScreenVsComputer(GameMode.VS_COMPUTER, ComputerDifficulty.BEGINNER,name);
            Main.instance.setScene(game.getScene());
        });

        intermediateBtn.setOnAction(e -> {
            GameScreenVsComputer game = new GameScreenVsComputer(GameMode.VS_COMPUTER, ComputerDifficulty.INTERMEDIATE,name);
            Main.instance.setScene(game.getScene());
        });

        advancedBtn.setOnAction(e -> {
            GameScreenVsComputer game = new GameScreenVsComputer(GameMode.VS_COMPUTER, ComputerDifficulty.ADVANCED,name);
            Main.instance.setScene(game.getScene());
        });

        backBtn.setOnAction(e -> {
            ConnectionScreen back = new ConnectionScreen(name);
            Main.instance.setScene(back.getScene());
        });

        VBox layout = new VBox(20, title, beginnerBtn,intermediateBtn, advancedBtn, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.getStyleClass().add("root");

        scene = new Scene(layout, 720, 720);
        scene.getStylesheets().add("file:resources/css/style_game.css");
    }

    public Scene getScene() {
        return scene;
    }
}
