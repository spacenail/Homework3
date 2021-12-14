package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class ChatFrame {

    private final JFrame mainFrame;
    private final ChattingFrame chattingFrame;
    private final SendingFrame sendingFrame;
    private Consumer<Boolean> exitApp;

    public ChatFrame(Consumer<String> onSubmit, Consumer<Boolean> exitApp) {
        mainFrame = new JFrame();
        this.exitApp = exitApp;
        chattingFrame = new ChattingFrame();
        sendingFrame = new SendingFrame(onSubmit);
        init();
    }

    private void init() {
        // задаем параметры окна
        mainFrame.setBounds(new Rectangle(400, 500));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApp.accept(true);
                e.getWindow().dispose();
            }
        });
        mainFrame.setTitle("Chat v2.0");
        // добавляем поле вывода во все окно и строку ввода с кнопкой в нижней части
        mainFrame.add(chattingFrame.getFrame(), BorderLayout.CENTER);
        mainFrame.add(sendingFrame.getFrame(), BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    public Consumer<String> onReceive() {
        return chattingFrame.getOnReceive();
    }
}