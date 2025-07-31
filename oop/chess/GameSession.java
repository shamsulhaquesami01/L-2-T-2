import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameSession {
    private Socket player1Socket; // White player (host)
    private Socket player2Socket; // Black player (join)
    private DataInputStream player1In;
    private DataOutputStream player1Out;
    private DataInputStream player2In;
    private DataOutputStream player2Out;
    private ExecutorService executorService;

    GameSession(Socket p1, Socket p2, DataInputStream player1In, DataOutputStream player1Out,
                DataInputStream player2In, DataOutputStream player2Out) throws IOException {
        player1Socket = p1;
        player2Socket = p2;
        this.player1In = player1In;
        this.player2In = player2In;
        this.player1Out = player1Out;
        this.player2Out = player2Out;

        executorService = Executors.newFixedThreadPool(12);
        player1Out.writeUTF("START");
        player2Out.writeUTF("START");
        System.out.println("Start sent to both players");
        startGameRelay();
    }

    private void startGameRelay() {
        executorService.submit(() -> relayMessages(player1In, player2Out, "Player1->Player2"));
        executorService.submit(() -> relayMessages(player2In, player1Out, "Player2->Player1"));
        executorService.submit(this::monitorConnections);
    }

    private void relayMessages(DataInputStream from, DataOutputStream to, String direction) {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String messageType = from.readUTF();
                System.out.println(direction + " - Received message: " + messageType);

                if (messageType.equals("MOVE")) {
                    int srcRow = from.readInt();
                    int srcCol = from.readInt();
                    int destRow = from.readInt();
                    int destCol = from.readInt();
                    int promotion = from.readInt();

                    synchronized (to) {
                        to.writeUTF("MOVE");
                        to.writeInt(srcRow);
                        to.writeInt(srcCol);
                        to.writeInt(destRow);
                        to.writeInt(destCol);
                        to.writeInt(promotion);
                        to.flush();
                    }

                    System.out.println(direction + " - Move relayed: (" + srcRow + "," + srcCol + ") -> ("
                            + destRow + "," + destCol + ")" + " with promotion " + promotion);

                } else if (messageType.startsWith("GAME_OVER:")) {
                    synchronized (to) {
                        to.writeUTF(messageType);
                        to.flush();
                    }
                    System.out.println(direction + " - Game over message relayed: " + messageType);
                    break;

                } else if (messageType.equals("DRAW_OFFER")) {
                    synchronized (to) {
                        to.writeUTF("DRAW_OFFER");
                        to.flush();
                    }
                    System.out.println(direction + " - Draw offer relayed");

                } else if (messageType.equals("DRAW_ACCEPT")) {
                    synchronized (to) {
                        to.writeUTF("DRAW_ACCEPT");
                        to.flush();
                    }
                    System.out.println(direction + " - Draw acceptance relayed");
                    broadcastGameOver("DRAW");
                    break;

                } else if (messageType.equals("DRAW_REJECT")) {
                    synchronized (to) {
                        to.writeUTF("DRAW_REJECT");
                        to.flush();
                    }
                    System.out.println(direction + " - Draw rejection relayed");

                } else if (messageType.equals("DRAW")) {
                    synchronized (to) {
                        to.writeUTF("DRAW_OFFER");
                        to.flush();
                    }
                    System.out.println(direction + " - Legacy draw converted to draw offer");

                } else if (messageType.equals("NO")) {
                    synchronized (to) {
                        to.writeUTF("DRAW_REJECT");
                        to.flush();
                    }
                    System.out.println(direction + " - Legacy NO converted to draw reject");

                } else {
                    synchronized (to) {
                        to.writeUTF(messageType);
                        to.flush();
                    }
                    System.out.println(direction + " - Unknown message relayed: " + messageType);
                }
            }
        } catch (IOException e) {
            System.err.println("Relay error " + direction + ": " + e.getMessage());
            handleDisconnection();
        }
    }

    private void broadcastGameOver(String gameOverType) {
        try {
            synchronized (player1Out) {
                player1Out.writeUTF("GAME_OVER:" + gameOverType);
                player1Out.flush();
            }
            synchronized (player2Out) {
                player2Out.writeUTF("GAME_OVER:" + gameOverType);
                player2Out.flush();
            }
            System.out.println("Broadcasted game over: " + gameOverType);
        } catch (IOException e) {
            System.err.println("Error broadcasting game over: " + e.getMessage());
        }
    }

    private void monitorConnections() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (player1Socket.isClosed() || player2Socket.isClosed()) {
                    System.out.println("A player disconnected. Cleaning up...");
                    handleDisconnection();
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleDisconnection() {
        try {
            if (player1Socket != null && !player1Socket.isClosed()) {
                player1Socket.close();
            }
            if (player2Socket != null && !player2Socket.isClosed()) {
                player2Socket.close();
            }
            executorService.shutdownNow();
            System.out.println("Game session ended. Waiting for new players...");
        } catch (IOException e) {
            System.err.println("Error closing sockets: " + e.getMessage());
        }
    }
}