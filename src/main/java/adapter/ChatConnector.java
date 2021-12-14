package adapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatConnector {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public ChatConnector(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during a connection to the server.", e);
        }
    }

    public void sendMessage(String outboundMessage) {
        try {
            out.writeUTF(outboundMessage);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during sending message to the server.", e);
        }
    }

    public String receiveMessage() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during receiving message from the server.", e);
        }
    }
}