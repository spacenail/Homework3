package gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SendingFrame {

    private final JPanel frame;
    private final JButton submitBtn;
    private final JTextField writingField;
    private final Consumer<String> onSubmit;

    public SendingFrame(Consumer<String> onSubmit) {
        this.onSubmit = onSubmit;
        writingField = new JTextField();
        writingField.addActionListener(e->{
            onSubmit.accept(writingField.getText());
            writingField.setText(null);
        });
        submitBtn = new JButton("Send");
        submitBtn.addActionListener(e -> {
            onSubmit.accept(writingField.getText());
            writingField.setText(null);
        });

        frame = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.add(writingField, BorderLayout.CENTER);
        frame.add(submitBtn, BorderLayout.EAST);
    }

    public JPanel getFrame() {
        return frame;
    }

    public JButton getSubmitBtn() {
        return submitBtn;
    }

    public JTextField getWritingField() {
        return writingField;
    }
}