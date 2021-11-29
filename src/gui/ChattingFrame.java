package gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
// окно чата для вывода сообщений
public class ChattingFrame {

    private final JPanel frame;
    private final JTextArea messageArea;
    private final Consumer<String> onReceive;
// создание окна и запрет на его редактирование
    public ChattingFrame() {
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        /* реализация интерфейса onReceive - переданное в него сообщение
        отображается в окне вывода клиентской программы + перевод на следующую строку
         */
        this.onReceive = new Consumer<String>() {
            @Override
            public void accept(String message) {
                messageArea.append(message);
                messageArea.append("\n");
            }
        };

        frame = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.add(messageArea);
    }

    public JPanel getFrame() {
        return frame;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public Consumer<String> getOnReceive() {
        return onReceive;
    }
}