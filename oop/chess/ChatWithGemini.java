import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ChatWithGemini {
    private Scene scene;
    private VBox messageContainer;
    private ScrollPane scrollPane;
    private String name;
    public ChatWithGemini(String name) {
        Label title = new Label("Chat with Gemini AI");
        title.getStyleClass().add("title-label");
        this.name=name;
        messageContainer = new VBox(5);
        messageContainer.setPadding(new Insets(10));
        messageContainer.getStyleClass().add("message-container");
        messageContainer.setAlignment(Pos.TOP_LEFT);

        scrollPane = new ScrollPane();
        scrollPane.setContent(messageContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("chat-scroll-pane");

        messageContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue(1.0);
        });

        TextField inputField = new TextField();
        inputField.setPromptText("Ask me anything...");
        inputField.getStyleClass().add("text-field");

        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("button");

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("back-button");

        HBox.setHgrow(inputField, Priority.ALWAYS);
        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getStyleClass().add("input-box");

        VBox layout = new VBox(10, title, scrollPane, inputBox, backButton);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(800, 600);
        layout.getStyleClass().add("chat-root");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendButton.fire();
            }
        });
        sendButton.setOnAction(e -> {
            String userMessage = inputField.getText().trim();
            if (!userMessage.isEmpty()) {
                addMessageToChat("You: " + userMessage, "user");
                inputField.clear();

                new Thread(() -> {
                    String reply = GeminiAPI.askGemini(userMessage);
                    Platform.runLater(() -> {
                        addMessageToChat("Gemini: " + reply, "gemini");
                    });
                }).start();
            }
        });
        layout.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                backButton.fire();
            }
        });
        backButton.setOnAction(e -> {
            MainMenu menu = new MainMenu(name);
            Stage stage = (Stage) layout.getScene().getWindow();
            stage.setScene(menu.getScene());
        });

        scene = new Scene(layout);
        scene.getStylesheets().add("file:resources/css/style_chat.css");
    }

    private void addMessageToChat(String message, String senderType) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("chat-message");

        HBox messageWrapper = new HBox();
        messageWrapper.getChildren().add(messageLabel);
        HBox.setHgrow(messageLabel, Priority.ALWAYS);
        messageLabel.setMaxWidth(Double.MAX_VALUE);

        if ("user".equals(senderType)) {
            messageLabel.getStyleClass().add("user-message");
            messageWrapper.setAlignment(Pos.CENTER_RIGHT);
            messageWrapper.setPadding(new Insets(0, 0, 0, 50));
        } else if ("gemini".equals(senderType)) {
            messageLabel.getStyleClass().add("gemini-message");
            messageWrapper.setAlignment(Pos.CENTER_LEFT);
            messageWrapper.setPadding(new Insets(0, 50, 0, 0));
        }

        messageContainer.getChildren().add(messageWrapper);
        Platform.runLater(() -> {
            double availableWidth = scrollPane.getViewportBounds().getWidth();
            messageLabel.setMaxWidth(availableWidth * 0.70);
        });
    }

    public Scene getScene() {
        return scene;
    }
}