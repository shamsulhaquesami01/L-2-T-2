import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 4558;
    private ServerSocket serverSocket;
    private Socket player1Socket; // White player (host)
    private Socket player2Socket; // Black player (join)
    private DataInputStream player1In;
    private DataOutputStream player1Out;
    private DataInputStream player2In;
    private DataOutputStream player2Out;

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chess Server started on port " + PORT);
            System.out.println("Waiting for players to connect...");

            while (true) {
                waitForTwoPlayers();
                System.out.println("Both players connected! Starting game relay...");
            }
        } catch (IOException e) {
            System.err.println("Server " + e.getMessage());
        }
    }

    private void waitForTwoPlayers() throws IOException {
        player1Socket = serverSocket.accept();
        player1In = new DataInputStream(player1Socket.getInputStream());
        player1Out = new DataOutputStream(player1Socket.getOutputStream());
        String player1Name = player1In.readUTF();
        System.out.println("Player 1 name: " + player1Name);
        player1Out.writeUTF("WHITE");
        player1Out.flush();
        System.out.println("Player 1 (White/Host) " + player1Name + " connected from: " + player1Socket.getInetAddress());
        player2Socket = serverSocket.accept();
        player2In = new DataInputStream(player2Socket.getInputStream());
        player2Out = new DataOutputStream(player2Socket.getOutputStream());
        String player2Name = player2In.readUTF();
        System.out.println("Player 2 name: " + player2Name);
        player2Out.writeUTF("BLACK");
        player2Out.flush();
        System.out.println("Player 2 (Black/Join) " + player2Name + " connected from: " + player2Socket.getInetAddress());
        player1Out.writeUTF(player2Name);
        player1Out.flush();

        player2Out.writeUTF(player1Name);
        player2Out.flush();

        System.out.println("Player names exchanged successfully");

        new GameSession(player1Socket, player2Socket, player1In, player1Out, player2In, player2Out);
    }
}