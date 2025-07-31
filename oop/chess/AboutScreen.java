import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AboutScreen {
    private Scene scene;
private String name;
    public AboutScreen(String name) {
        this.name=name;
        // --- Game Title ---
        ImageView gameLogo = new ImageView();
        try {
            gameLogo.setImage(new Image("file:resources/img/logo2.png"));
        } catch (Exception e) {
            System.out.println("Game logo not found, using default styling.");
        }
        gameLogo.setFitWidth(100);
        gameLogo.setFitHeight(100);
        gameLogo.setPreserveRatio(true);
        gameLogo.getStyleClass().add("game-logo");
        Label creditsHeader = new Label("Developed By");
        creditsHeader.getStyleClass().add("section-header");

        VBox creditsBox = new VBox(5);
        creditsBox.setAlignment(Pos.CENTER);
        creditsBox.getStyleClass().add("credits-box");
        Label gptContribute=new Label("General Consulting when problems Occured");
        gptContribute.getStyleClass().add("credit-role");
        Label samiCredit = new Label("Md Shamsul Haque Sami-2305055");
        samiCredit.getStyleClass().add("credit-name");
        Label samiRole = new Label("CSE,BUET");
        samiRole.getStyleClass().add("credit-role");

        Label muntasirCredit = new Label("Md Muntasir Bin Rafique - 2305033");
        muntasirCredit.getStyleClass().add("credit-name");
        Label muntasirRole = new Label("CSE,BUET");
        muntasirRole.getStyleClass().add("credit-role");

        creditsBox.getChildren().addAll(
                new Region(),
                samiCredit, samiRole,
                new Region(),
                muntasirCredit, muntasirRole
        );

        Label copyrightLabel = new Label("© 2025 Network Chess. All rights reserved.");
        copyrightLabel.getStyleClass().add("copyright-text");

        Button backButton = new Button("⬅️ Back");
        backButton.setOnAction(e -> {
            MainMenu menu = new MainMenu(name);
            Main.instance.setScene(menu.getScene());
        });
        backButton.getStyleClass().add("back-button");

        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.getStyleClass().add("about-root");
        layout.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                backButton.fire();
            }
        });
        layout.getChildren().addAll(
                gameLogo,
                new Region(),
                creditsHeader,
                creditsBox,
                new Region(),
                new Region(),
                copyrightLabel,
                backButton
        );
        VBox.setVgrow(layout.getChildren().get(1), Priority.ALWAYS);
        VBox.setVgrow(layout.getChildren().get(4), Priority.ALWAYS);


        scene = new Scene(layout, 720, 720);
        scene.getStylesheets().add("file:resources/css/style_about.css");
    }

    public Scene getScene() {
        return scene;
    }
}