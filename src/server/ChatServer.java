package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChatServer {

    private final ServerSocket socket;
    private Set<ClientHandler> connectedUsers;

    public ChatServer() {
        try {
            DB.connect();
            connectedUsers = new HashSet<>();
            this.socket = new ServerSocket(8888);

            while (true) {
                System.out.println("Waiting for a new connection...");
                Socket client = socket.accept();
                new Thread(() -> new ClientHandler(client, this)).start();
                System.out.println("Client accepted.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during connection establishing.", e);
        }finally {
            DB.disconnect();
        }
    }

    public synchronized void addClient(ClientHandler client) {
        connectedUsers.add(client);
        DB.addUserToLoggedUsers(client.getName());
    }

    public synchronized void removeClient(ClientHandler client) {
        connectedUsers.remove(client);
        DB.deleteUserFromLoggedUsers(client.getName());
    }

    public boolean isUsernameOccupied(String username) {
    return DB.isUsernameOccupied(username);
    }

    public synchronized void changeUserName(String username,String newUserName) {
        DB.changeUsername(username, newUserName);
    }

    public Optional<String> getUsername(String login, String password){
        return DB.getUsernameByLoginAndPassword(login,password);
    }


    public synchronized void broadcastMessage(String message) {
        connectedUsers.forEach(ch -> ch.sendMessage(message));
    }
}