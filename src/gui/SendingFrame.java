package gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
// окно ввода сообщений
public class SendingFrame {

    private final JPanel frame;
    private final JButton submitBtn;
    private final JTextField writingField;
    private final Consumer<String> onSubmit;
/* создание однострочного текстового поля для ввода информации.
Добавление действия при нажатии Enter  - в интерфейс OnSubmit передается текст из строки
и окно ввода обнуляется
 */
    public SendingFrame(Consumer<String> onSubmit) {
        this.onSubmit = onSubmit;
        writingField = new JTextField();
        writingField.addActionListener(e->{
            onSubmit.accept(writingField.getText());
            writingField.setText(null);
        });
/* создание кнопки для отправки сообщения.
Добавление действия при нажатии на кнопку  - в OnSubmit передается текст из строки и окно ввода обнуляется
*/
        submitBtn = new JButton("Send");
        submitBtn.addActionListener(e -> {
            onSubmit.accept(writingField.getText());
            writingField.setText(null);
        });
// компоновка кнопки и поля для ввода
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