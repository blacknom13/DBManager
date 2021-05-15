package gui;

import logic.DBConnect;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Welcome {

    private JFrame frame;
    private JTextField dataBase_TF;
    private JTextField user_TF;
    private JTextField password_TF;
    private JTextField path_TF;
    private JFileChooser chooser;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Welcome window = new Welcome();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public Welcome() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.getContentPane().setFont(new Font("Times New Roman", Font.PLAIN, 20));
        frame.setTitle(
                "\u0422\u0435\u0441\u0442\u043E\u0432\u044B\u0439 \u0440\u0435\u0434\u0430\u043A\u0442\u043E\u0440 \u0411\u0414\r\n");
        frame.setBounds(100, 100, 581, 463);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(4, 2, 0, 0));

        JLabel label_1 = new JLabel("\u041F\u0443\u0442\u044C");
        label_1.setLayout(new BorderLayout());
        label_1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        panel.add(label_1);

        JButton browse_B = new JButton("...");
        browse_B.addActionListener(e -> {
            // TODO Auto-generated method stub
            FileNameExtensionFilter fdbFilter=
                    new FileNameExtensionFilter("\u0424\u0430\u0439\u043B\u044B \u0431\u0430\u0437 \u0434\u0430\u043D\u043D\u044B\u0445(*.fdb)", "fdb");

            chooser = new JFileChooser();
            chooser.setFileFilter(fdbFilter);
            chooser.showDialog(frame, "\u0412\u044B\u0431\u0440\u0430\u0442\u044C");
            File x = chooser.getSelectedFile();
            if (x != null) {
                String filePath=x.getAbsolutePath();
                path_TF.setText(filePath.substring(0,filePath.lastIndexOf('\\')+1));
                dataBase_TF.setText(filePath.substring(filePath.lastIndexOf('\\')+1));
            }
        });
        label_1.add(browse_B, BorderLayout.EAST);

        path_TF = new JTextField();
        path_TF.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        path_TF.setText("f:\\data\\");
        panel.add(path_TF);
        path_TF.setColumns(10);

        JLabel label = new JLabel(
                "\u041D\u0430\u0438\u043C\u0435\u043D\u043E\u0432\u0430\u043D\u0438\u0435 \u0411\u0414");
        label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        panel.add(label);

        dataBase_TF = new JTextField();
        dataBase_TF.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        dataBase_TF.setText("test.fdb");
        panel.add(dataBase_TF);
        dataBase_TF.setColumns(10);

        JLabel lblNewLabel = new JLabel(
                "\u0418\u043C\u044F \u041F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044F");
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        panel.add(lblNewLabel);

        user_TF = new JTextField();
        user_TF.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        user_TF.setText("sysdba");
        panel.add(user_TF);
        user_TF.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("\u041F\u0430\u0440\u043E\u043B\u044C\r\n");
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        panel.add(lblNewLabel_1);

        password_TF = new JTextField();
        password_TF.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        password_TF.setText("masterkey");
        panel.add(password_TF);
        password_TF.setColumns(10);

        JPanel panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new FlowLayout());

        JButton connectToDB_B = new JButton(
                "\u0423\u0441\u0442\u0430\u043D\u043E\u0432\u0438\u0442\u044C \u0441\u043E\u0435\u0434\u0438\u043D\u0435\u043D\u0438\u0435");
        connectToDB_B.addActionListener(arg0 -> {
            if (DBConnect.initConnection(path_TF.getText() + dataBase_TF.getText(),
                    user_TF.getText(), password_TF.getText())) {
                EventQueue.invokeLater(() -> {
                    try {
                        @SuppressWarnings("unused")
                        InsertValuesWindow x = new InsertValuesWindow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                frame.dispose();
            }
        });
        frame.dispose();
        connectToDB_B.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        panel_1.add(connectToDB_B);
    }
}
