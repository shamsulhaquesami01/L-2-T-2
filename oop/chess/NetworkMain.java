
import java.io.*;
import java.net.Socket;

public class NetworkMain {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private PieceColor myColor;
    private boolean myTurn;
    private GameScreen gameScreen;
    private String lastReceivedMessage = null;
    private int[] lastMove = null;
    private final Object messageLock = new Object();
    private String opponentName; // Store opponent's name
    private final Object readLock = new Object();

    public NetworkMain(Socket socket, DataInputStream in, DataOutputStream out, PieceColor color, String opponentName) throws IOException {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.myColor = color;
        this.myTurn = (color == PieceColor.WHITE);
        this.opponentName = opponentName;
    }

    public void readOpponentName() throws IOException {
        synchronized (readLock) {
            this.opponentName = in.readUTF();
            System.out.println("Opponent name received: " + this.opponentName);
        }
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public PieceColor getMyColor() {
        return myColor;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean turn) {
        this.myTurn = turn;
    }

    public synchronized void sendMove(int srcRow, int srcCol, int destRow, int destCol, int promotion) throws IOException {
        out.writeUTF("MOVE");
        out.writeInt(srcRow);
        out.writeInt(srcCol);
        out.writeInt(destRow);
        out.writeInt(destCol);
        out.writeInt(promotion);
        out.flush();
    }

    public void sendDraw() throws IOException {
        sendDrawOffer();
    }

    public void sendDrawAcceptance() throws IOException {
        synchronized (out) {
            out.writeUTF("DRAW_ACCEPT");
            out.flush();
            System.out.println("Sent draw acceptance to opponent");
        }
    }

    public synchronized void sendGameOverNotification(String gameOverType, PieceColor loser) throws IOException {
        System.out.println("IN NetworkMain");
        String message = "GAME_OVER:" + gameOverType + ":" + loser.toString();
        out.writeUTF(message);
        out.flush();
    }

    public void rejectDraw() throws IOException {
        sendDrawRejection();
    }

    public void sendDrawOffer() throws IOException {
        synchronized (out) {
            out.writeUTF("DRAW_OFFER");
            out.flush();
            System.out.println("Sent draw offer to opponent");
        }
    }

    public String receiveMessage() throws IOException {
        synchronized (readLock) {
            if (in.available() > 0) {
                String messageType = in.readUTF();

                if (messageType.equals("MOVE")) {
                    int srcRow = in.readInt();
                    int srcCol = in.readInt();
                    int destRow = in.readInt();
                    int destCol = in.readInt();
                    int promotion = in.readInt();

                    lastMove = new int[]{srcRow, srcCol, destRow, destCol, promotion};
                    lastReceivedMessage = "MOVE";

                    System.out.println("Received move: (" + srcRow + "," + srcCol + ") -> ("
                            + destRow + "," + destCol + ") promotion=" + promotion);

                } else if (messageType.equals("DRAW_OFFER")) {
                    lastReceivedMessage = "DRAW_OFFER";
                    System.out.println("Received draw offer from opponent");

                } else if (messageType.equals("DRAW_ACCEPT")) {
                    lastReceivedMessage = "DRAW_ACCEPT";
                    System.out.println("Received draw acceptance from opponent");

                } else if (messageType.equals("DRAW_REJECT")) {
                    lastReceivedMessage = "DRAW_REJECT";
                    System.out.println("Received draw rejection from opponent");

                } else if (messageType.startsWith("GAME_OVER:")) {
                    lastReceivedMessage = messageType;
                    System.out.println("Received game over: " + messageType);
                    String[] parts = messageType.split(":");
                    if (parts.length >= 3) {
                        String gameOverType = parts[1];
                        PieceColor loser = PieceColor.valueOf(parts[2]);
                        throw new GameOverException(gameOverType, loser);
                    } else if (parts.length == 2) {
                        String gameOverType = parts[1];
                        if (gameOverType.equals("DRAW")) {
                            lastReceivedMessage = "GAME_OVER:DRAW";
                        }
                    }
                } else {
                    lastReceivedMessage = messageType;
                    System.out.println("Received unknown message: " + messageType);
                }

                return lastReceivedMessage;
            }
            return null;
        }
    }

    public int[] getLastMove() {
        synchronized (readLock) {
            return lastMove;
        }
    }

    public boolean hasPendingDrawOffer() {
        synchronized (readLock) {
            return "DRAW_OFFER".equals(lastReceivedMessage);
        }
    }

    public void clearLastMessage() {
        synchronized (readLock) {
            lastReceivedMessage = null;
            lastMove = null;
        }
    }

    public int[] receiveMove() throws IOException {
        String message = receiveMessage();
        if (message != null && message.equals("MOVE")) {
            return getLastMove();
        }
        return null;
    }

    public void sendDrawRejection() throws IOException {
        synchronized (out) {
            out.writeUTF("DRAW_REJECT");
            out.flush();
            System.out.println("Sent draw rejection to opponent");
        }
    }

    public void close() throws IOException {
        socket.close();
    }
    public void awaitOpponentAndStart() throws IOException {
        synchronized (readLock) {
            this.opponentName = in.readUTF();
            System.out.println("Opponent name received: " + this.opponentName);
            String msg = in.readUTF();
            if (!"START".equals(msg)) {
                throw new IOException("Protocol error, expected START, got " + msg);
            }
            System.out.println("START message received successfully");
        }
    }
    public void awaitStart() throws IOException {
        awaitOpponentAndStart();
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getOpponentName() {
        return opponentName != null ? opponentName : "Waiting...";
    }
    public static class GameOverException extends IOException {
        private String gameOverType;
        private PieceColor loser;

        public GameOverException(String gameOverType, PieceColor loser) {
            super("Game Over: " + gameOverType);
            this.gameOverType = gameOverType;
            this.loser = loser;
        }

        public String getGameOverType() {
            return gameOverType;
        }

        public PieceColor getLoser() {
            return loser;
        }
    }
}