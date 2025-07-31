
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.function.Consumer;

public class Tile extends StackPane {
    private final int row, col;
    private final Rectangle background;
    private ImageView piece;
    private final Rectangle highlightOverlay;
    private Consumer<Tile> clickHandler;

    public Tile(int row, int col, int tileSize, Consumer<Tile> clickHandler) {
        this.row = row;
        this.col = col;
        this.clickHandler = clickHandler;

        background = new Rectangle(tileSize, tileSize);
        background.setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.rgb(75, 56, 42));
        background.setStrokeWidth(1);
        background.setStroke(Color.BLACK);
        getChildren().add(background);

        highlightOverlay = new Rectangle(tileSize, tileSize);
        highlightOverlay.setFill(Color.TRANSPARENT);
        highlightOverlay.setStroke(Color.GREEN);
        highlightOverlay.setStrokeWidth(3);
        highlightOverlay.setVisible(false);
        getChildren().add(highlightOverlay);
        setOnMouseClicked(e -> handleClick());
    }
    private void handleClick() {
        if (clickHandler != null) {
            clickHandler.accept(this);
        }
    }

    public void setPiece(ImageView piece) {
        if (this.piece != null) {
            getChildren().remove(this.piece);
        }

        this.piece = piece;
        if (piece != null) {
            piece.setFitWidth(background.getWidth());
            piece.setFitHeight(background.getHeight());
            getChildren().add(piece);
        }
    }

    public ImageView getPiece() {
        return piece;
    }

    public void highlight() {
        highlightOverlay.setVisible(true);
    }

    public void removeHighlight() {
        highlightOverlay.setVisible(false);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}