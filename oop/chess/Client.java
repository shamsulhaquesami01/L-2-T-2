
import java.io.*;
import java.net.Socket;

public class Client {
    public static NetworkMain connect(String ip, int port, String playerName) throws IOException {
        Socket socket = new Socket(ip, port);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(playerName);
        out.flush();
        System.out.println("Sent player name: " + playerName);
        String colorStr = in.readUTF();
        PieceColor color = colorStr.equals("BLACK") ? PieceColor.BLACK : PieceColor.WHITE;
        System.out.println("Received color: " + colorStr);
        NetworkMain networkMain = new NetworkMain(socket, in, out, color, null);

        return networkMain;
    }
}