package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler {

    private final Socket socket;
    private final ChatServer server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String name;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            throw new RuntimeException("Something went wrong during a client connection establishing.");
        }

        doAuthentication();

            try {
                listenMessages();
            }catch (IOException e) {
                server.removeClient(this);
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
    }

    public String getName() {
        return name;
    }

    private void doAuthentication() {
        try {
            socket.setSoTimeout(12000);
            performAuthentication();
            socket.setSoTimeout(0);
        }catch (SocketTimeoutException se) {
            throw new RuntimeException("Timeout - 120s for authentication complete!");
        }catch (SQLException e){
            e.getStackTrace();
        } catch (IOException ex) {
            throw new RuntimeException("Something went wrong during a client authentication.",ex);
        }
    }

    private void performAuthentication() throws IOException, SQLException {
        while (true) {
            String inboundMessage = in.readUTF();
            if (inboundMessage.startsWith("-auth")) {
                // valid request sample: -auth l1 p1
                String[] credentials = inboundMessage.split("\\s");

                AtomicBoolean isSuccess = new AtomicBoolean(false);
                Optional<String> optionalUsername = UsersDB.getUsernameByLoginAndPassword(credentials[1], credentials[2]);
                if (optionalUsername.isPresent()) {
                    String username = optionalUsername.get();
                    if (!server.isUsernameOccupied(username)) {
                        server.broadcastMessage(String.format("User[%s] is logged in", username));
                        name = username;
                        server.addClient(this);
                        isSuccess.set(true);
                        sendMessage("Welcome to chat!");
                    } else {
                        sendMessage("Current username is already occupied.");
                    }
                } else {
                    sendMessage("Bad credentials.");
                }
                if (isSuccess.get()) break;
            } else {
                sendMessage("You need to be logged-in.");
            }
        }
    }

    public void sendMessage(String outboundMessage) {
        try {
            out.writeUTF(outboundMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessage() throws IOException {
            server.broadcastMessage(String.format("[%s]: %s", this.name,in.readUTF()));
    }

    public void listenMessages() throws IOException {
        while (true) {
            readMessage();
        }
    }
}