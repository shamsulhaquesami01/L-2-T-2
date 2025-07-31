import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PlayerPanel extends VBox {
    private final Label nameLabel = new Label();
    private final Label moveStatusLabel = new Label();

    public PlayerPanel(String name, String profileImagePath, boolean isCurrentTurn) {
        setSpacing(5);
        setPadding(new Insets(5));
        setAlignment(Pos.CENTER);
        getStyleClass().add("player-panel");

        ImageView profileImage = new ImageView();
        try {
            profileImage.setImage(new Image(profileImagePath));
        } catch (Exception e) {
            // fallback
            profileImage.setImage(new Image("file:resources/img/profile_you.png"));
        }

        profileImage.setFitWidth(50);
        profileImage.setFitHeight(50);

        nameLabel.setText(name);
        nameLabel.getStyleClass().add("player-name");

        moveStatusLabel.getStyleClass().add("move-status");
        setMoveStatus(isCurrentTurn);

        getChildren().addAll(profileImage, nameLabel, moveStatusLabel);
    }

    public void setMoveStatus(boolean isYourTurn) {
        moveStatusLabel.setText(isYourTurn ? "Your move" : "Opponent's move");
    }

    public void setName(String name) {
        nameLabel.setText(name);
    }
}
