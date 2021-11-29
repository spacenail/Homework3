package gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ChatFrame {

    private final JFrame mainFrame;
    private final ChattingFrame chattingFrame;
    private final SendingFrame sendingFrame;

    public ChatFrame(Consumer<String> onSubmit) {
        mainFrame = new JFrame();
        chattingFrame = new ChattingFrame();
        sendingFrame = new SendingFrame(onSubmit);
        init();
    }

    private void init() {
        // задаем параметры окна
        mainFrame.setBounds(new Rectangle(400, 500));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setTitle("Chat v1.0");
        // добавляем поле вывода во все окно и строку ввода с кнопкой в нижней части
        mainFrame.add(chattingFrame.getFrame(), BorderLayout.CENTER);
        mainFrame.add(sendingFrame.getFrame(), BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    public Consumer<String> onReceive() {
        return chattingFrame.getOnReceive();
    }
}