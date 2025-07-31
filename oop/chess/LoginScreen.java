import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.animation.FadeTransition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.*;

public class LoginScreen {
    private Scene scene;

    public LoginScreen() {
        Label title = new Label("Login");
        title.getStyleClass().add("label-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("text-field");


        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("password-field");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("login-message");
        messageLabel.getStyleClass().add("msg");

        Button loginButton = new Button("🔑 Login");
        Button registerButton = new Button("✏️ Register");

        loginButton.getStyleClass().add("button");
        registerButton.getStyleClass().add("button");

        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> loginButton.fire());
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                usernameField.requestFocus();
            }
        });
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                passwordField.requestFocus();
            }
        });

        loginButton.setOnAction(e -> {
            try {
                String path = getClass().getResource("/sound/click.wav").toExternalForm();
                Media sound = new Media(path);
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            } catch (Exception ex) {
                System.err.println("Could not play sound: " + ex.getMessage());
            }

            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (authenticate(username, password)) {
                messageLabel.setText("Login successful!");
                messageLabel.getStyleClass().add("success");
                MainMenu menu = new MainMenu(username);
                Main.instance.setScene(menu.getScene());
            } else {
                messageLabel.setText("Login failed. Invalid username or password.");
                messageLabel.getStyleClass().remove("success");
            }
        });

        registerButton.setOnAction(e -> {
            try {
                String path = getClass().getResource("/sound/login.mp3").toExternalForm();
                Media sound = new Media(path);
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            } catch (Exception ex) {
                System.err.println("Could not play sound: " + ex.getMessage());
            }

            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Username and password cannot be empty!");
                messageLabel.getStyleClass().remove("success");
                return;
            }

            if (registerUser(username, password)) {
                messageLabel.setText("Registration successful! Logging in...");
                messageLabel.getStyleClass().add("success");
                MainMenu menu = new MainMenu(username);
                Main.instance.setScene(menu.getScene());
            } else {
                messageLabel.setText("Registration failed. Username already taken or file error.");
                messageLabel.getStyleClass().remove("success");
            }
        });

        Image logoImage = new Image(getClass().getResourceAsStream("/img/logo2.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);

        VBox layout = new VBox(15,  logoView, usernameField, passwordField, loginButton, registerButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("root");

        scene = new Scene(layout, 720, 720);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), layout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        scene.getStylesheets().add("file:resources/css/style_login.css");
    }

    public Scene getScene() {
        return scene;
    }

    private boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
        return false;
    }

    private boolean registerUser(String username, String password) {
        File file = new File("users.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2 && parts[0].equals(username)) {
                        return false;
                    }
                }
            }
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(username + ":" + password + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}