package server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOGGER = LogManager.getLogger(ChatServer.class.getName());

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            LOGGER.error("Ошибка при создании соединения с клиентом");
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
            }catch (SQLException e){
                e.printStackTrace();
            }
    }

    public String getName() {
        return name;
    }

    private void doAuthentication() {
        try {
            socket.setSoTimeout(120000);
            performAuthentication();
            socket.setSoTimeout(0);
        }catch (SocketTimeoutException se) {
            LOGGER.error("Истекло время для авторизации клиента!");
            throw new RuntimeException("Timeout - 120s for authentication complete!");
        }catch (SQLException e){
            e.getStackTrace();
        } catch (IOException ex) {
            LOGGER.error("Что-то пошло не так при подключении клиента");
            throw new RuntimeException("Something went wrong during a client authentication.",ex);
        }
    }

    private void performAuthentication() throws IOException, SQLException {
        while (true) {
            sendMessage("Please log in: -auth login password");
            String inboundMessage = in.readUTF();
            if (inboundMessage.startsWith("-auth")) {
                // valid request sample: -auth l1 p1
                String[] credentials = inboundMessage.split("\\s");

                AtomicBoolean isSuccess = new AtomicBoolean(false);
                Optional<String> optionalUsername = server.getUsername(credentials[1], credentials[2]);
                if (optionalUsername.isPresent()) {
                    String username = optionalUsername.get();
                    if (!server.isUsernameOccupied(username)) {
                        server.broadcastMessage(String.format("User[%s] is logged in", username));
                        name = username;
                        server.addClient(this);
                        isSuccess.set(true);
                        sendMessage("[SERVER]Welcome to chat, " + credentials[1]);
                    } else {
                        LOGGER.info(username + " уже авторизован!");
                        sendMessage("Current username is already occupied.");
                    }
                } else {
                    LOGGER.info("Пользователь ввел неверные данные для авторизации");
                    sendMessage("Bad credentials.");
                }
                if (isSuccess.get()) break;
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

    public void readMessage(String input) throws IOException {
            LOGGER.info("Пользователь " + this.name + " отправил сообщение: " + input);
            server.broadcastMessage(String.format("[%s]: %s", this.name,input));
    }

    public void listenMessages() throws IOException, SQLException {
        while (true) {
            String inboundMessage = in.readUTF();
            if (inboundMessage.startsWith("-cname")) {
                // valid request sample: -cname newusername
                String[] credentials = inboundMessage.split("\\s");
                server.changeUserName(name,credentials[1]);
                name = credentials[1];
            }else {
                readMessage(inboundMessage);
            }
        }
    }


}