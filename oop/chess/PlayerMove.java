import java.util.Objects;

enum MoveType {
    NORMAL,
    CAPTURE,
    KINGSIDE_CASTLE,
    QUEENSIDE_CASTLE,
    PROMOTION,
    EN_PASSANT ;
}
class PlayerMove {
    private int srcRow, srcCol, destRow, destCol;
    private MoveType moveType;

    public PlayerMove(int srcRow, int srcCol, int destRow, int destCol, MoveType moveType) {
        this.srcRow = srcRow;
        this.srcCol = srcCol;
        this.destRow = destRow;
        this.destCol = destCol;
        this.moveType = moveType;
    }
    public int getSrcRow() { return srcRow; }
    public int getSrcCol() { return srcCol; }
    public int getDestRow() { return destRow; }
    public int getDestCol() { return destCol; }
    public MoveType getMoveType() { return moveType; }

    @Override
    public String toString() {
        String moveDesc = "";
        switch (moveType) {
            case KINGSIDE_CASTLE:
                moveDesc = "O-O";
                break;
            case QUEENSIDE_CASTLE:
                moveDesc = "O-O-O";
                break;
            default:
                char srcFile = (char)('a' + srcCol);
                char destFile = (char)('a' + destCol);
                int srcRank = 8 - srcRow;
                int destRank = 8 - destRow;
                moveDesc = srcFile + "" + srcRank + "-" + destFile + "" + destRank;
                if (moveType == MoveType.CAPTURE) {
                    moveDesc += "x";
                }
                break;
        }
        return moveDesc;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayerMove move = (PlayerMove) obj;
        return srcRow == move.srcRow && srcCol == move.srcCol &&
                destRow == move.destRow && destCol == move.destCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcRow, srcCol, destRow, destCol);
    }
}