package adapter;

import gui.ChatFrame;
import java.util.function.Consumer;

public class ChatAdapter {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8888;

    private final ChatFrame frame;
    private final ChatConnector connector;
    private ChatHistory chatHistory;

    public ChatAdapter() {
        this.connector = new ChatConnector(HOST, PORT);
        /*
        Анонимный класс имплементирующий интерфейс Consumer,
        в методе accept отсылаем сообщение в DataOutputStream на сервер
         */
        this.frame = new ChatFrame(new Consumer<String>() {
            @Override
            public void accept(String outboundMessage) {
                connector.sendMessage(outboundMessage);
            }
        },
                new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean exitApp) {
                        if (exitApp) {
                            chatHistory.close();
                        }
                    }
                });

        boolean userIsLoggedIn = false;
/* в бесконечном цикле получаем сообщения от сервера и передаем их в GUI, если
пользователь залогинился, то записываем все полученные им сообщения в файл history[login].txt
 */

        while (true) {
            String receiveMessage = connector.receiveMessage();
            if (!userIsLoggedIn) {
                if (receiveMessage.startsWith("[SERVER]Welcome to chat, ")) {
                    chatHistory = new ChatHistory(receiveMessage.split("\\[SERVER\\]Welcome to chat, ")[1]);
                    userIsLoggedIn = true;
                }
            }

            if (chatHistory != null) {
                chatHistory.record(receiveMessage);
            }

            frame.onReceive().accept(receiveMessage);
        }
    }
}