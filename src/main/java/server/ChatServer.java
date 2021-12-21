package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private final ServerSocket socket;
    private Set<ClientHandler> connectedUsers;
    private static final Logger LOGGER = LogManager.getLogger(ChatServer.class.getName());

    public ChatServer() {
        int procNumber = Integer.parseInt(System.getenv("NUMBER_OF_PROCESSORS"));
        ExecutorService executorService = Executors.newFixedThreadPool(procNumber);
        LOGGER.info("Запущено " + procNumber + " потока(ов)");

        try {
            DB.connect();
            connectedUsers = new HashSet<>();
            this.socket = new ServerSocket(8888);

            while (true) {
                LOGGER.info("Ожидание подключений...");
                Socket client = socket.accept();
                executorService.execute(() -> {
                    LOGGER.info("Установлено соединение с клиентом");
                    new ClientHandler(client, this);
                });
            }
        } catch (IOException e) {
            LOGGER.fatal("Что-то пошло не так при запуске сервера!");
            throw new RuntimeException("Something went wrong during connection establishing.", e);
        }finally {
            DB.disconnect();
            LOGGER.info("Отключение от БД");
            executorService.shutdown();
            LOGGER.info("Остановка потоков");
        }
    }

    public synchronized void addClient(ClientHandler client) {
        connectedUsers.add(client);
        DB.addUserToLoggedUsers(client.getName());
        LOGGER.info("Клиент " + client.getName() + " авторизован");
    }

    public synchronized void removeClient(ClientHandler client) {
        connectedUsers.remove(client);
        DB.deleteUserFromLoggedUsers(client.getName());
        LOGGER.info("Клиент " + client.getName() + " отключился");
    }

    public boolean isUsernameOccupied(String username) {
    return DB.isUsernameOccupied(username);
    }

    public synchronized void changeUserName(String username,String newUserName) {
        if (DB.changeUsername(username, newUserName)) {
            LOGGER.info(username + " изменён на " + newUserName);
        } else {
            LOGGER.info(username + " не удалось изменить имя");
        }
    }

    public Optional<String> getUsername(String login, String password){
        return DB.getUsernameByLoginAndPassword(login,password);
    }


    public synchronized void broadcastMessage(String message) {
        connectedUsers.forEach(ch -> ch.sendMessage(message));
    }
}