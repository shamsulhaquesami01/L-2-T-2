import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Piece {
    private boolean isWhite;
    private final ImageView imageView;
    private boolean hasMoved;

    public Piece(boolean isWhite, String typeName) {
        this.isWhite = isWhite;
        String color = isWhite ? "white" : "black";
        String path = "file:resources/pieces/" + color + "_" + typeName + ".png";
        Image img = new Image(path);
        this.imageView = new ImageView(img);
        this.hasMoved=false;
    }
    public PieceColor getColor() {
        return isWhite() ? PieceColor.WHITE : PieceColor.BLACK;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public ImageView getImage() {
        return imageView;
    }

    public abstract boolean isValidMove(int srcRow, int srcCol, int destRow, int destCol, Piece[][] board);

    public void setColor(PieceColor pieceColor) {
        if(pieceColor==PieceColor.BLACK) isWhite=false;
        else isWhite=true;
    }

    public boolean hasMoved(){
        return hasMoved;
    }

    public void setMoved(boolean moved)
    {
        this.hasMoved=moved;
    }


}
