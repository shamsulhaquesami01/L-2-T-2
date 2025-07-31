import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class JoinGameDialog extends Stage {

    private TextField ipAddressField;
    private Button joinButton;
    private Button cancelButton;
    private Optional<String> result = Optional.empty();

    public JoinGameDialog(Stage owner) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        // Title Label
        Label titleLabel = new Label("Join Game");
        titleLabel.getStyleClass().add("dialog-title");

        // Header
        Label headerLabel = new Label("Enter Server IP Address:");
        headerLabel.getStyleClass().add("dialog-header-text");

        // IP Address
        ipAddressField = new TextField("127.0.0.1"); // Default IP
        ipAddressField.setPromptText("e.g., 192.168.1.100");
        ipAddressField.getStyleClass().add("dialog-text-field");

        // Buttons
        joinButton = new Button("Join");
        joinButton.getStyleClass().add("dialog-action-button");
        joinButton.setDefaultButton(true);

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("dialog-cancel-button");
        cancelButton.setCancelButton(true);

        // Button Layout
        HBox buttonBar = new HBox(15, joinButton, cancelButton);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        // Main Layout
        VBox dialogLayout = new VBox(15, titleLabel, headerLabel, ipAddressField, buttonBar);
        dialogLayout.setPadding(new Insets(30));
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.getStyleClass().add("custom-dialog-pane");
        Scene scene = new Scene(dialogLayout);
        scene.getStylesheets().add("file:resources/css/style_dialog.css"); // Link to new dialog CSS
        setScene(scene);

        // Event Handlers
        joinButton.setOnAction(e -> {
            result = Optional.ofNullable(ipAddressField.getText());
            close();
        });

        cancelButton.setOnAction(e -> {
            result = Optional.empty(); // Explicitly empty if cancelled
            close();
        });
        final Delta dragDelta = new Delta();
        dialogLayout.setOnMousePressed(mouseEvent -> {
            dragDelta.x = getX() - mouseEvent.getScreenX();
            dragDelta.y = getY() - mouseEvent.getScreenY();
        });
        dialogLayout.setOnMouseDragged(mouseEvent -> {
            setX(mouseEvent.getScreenX() + dragDelta.x);
            setY(mouseEvent.getScreenY() + dragDelta.y);
        });
    }

    public Optional<String> showAndGetResult() {
        showAndWait();
        return result;
    }
    private static class Delta { double x, y; }
}