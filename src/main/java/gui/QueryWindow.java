package gui;

import logic.DBConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class QueryWindow {

    Vector<Class<?>> types;
    QueryResult queryResultFrame;
    private String presetSql;
    private JFrame frame;

    /**
     * Launch the application.
     */

    /**
     * Create the application.
     */
    public QueryWindow() {
        initialize();
        // frame.pack();
        frame.setVisible(true);
    }

    public QueryWindow(String sql) {
        presetSql=sql;
        initialize();
        // frame.pack();
        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 541, 471);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane query_SP = new JScrollPane();

        frame.getContentPane().add(query_SP, BorderLayout.NORTH);

        JTextArea query_TA = new JTextArea(7, 30);
        query_TA.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        query_TA.setText(presetSql);
        query_SP.setViewportView(query_TA);

        JLabel label = new JLabel(
                "\u041D\u0430\u043F\u0438\u0448\u0438\u0442\u0435 \u0441\u0432\u043E\u0439 \u0437\u0430\u043F\u0440\u043E\u0441 \u043D\u0438\u0436\u0435");
        label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        query_SP.setColumnHeaderView(label);

        JScrollPane message_SP = new JScrollPane();

        frame.getContentPane().add(message_SP, BorderLayout.SOUTH);

        JTextArea dataBaseMessage_TA = new JTextArea(4, 30);
        dataBaseMessage_TA.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        dataBaseMessage_TA.setEditable(false);
        message_SP.setViewportView(dataBaseMessage_TA);

        JLabel label2 = new JLabel(
                "\u041E\u0448\u0438\u0431\u043A\u0438 \u0438 \u043E\u043F\u043E\u0432\u0435\u0449\u0435\u043D\u0438\u044F");
        label2.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        message_SP.setColumnHeaderView(label2);

        JButton queryExecute_B = new JButton(
                "\u0412\u044B\u043F\u043E\u043B\u043D\u0438\u0442\u044C \u0437\u0430\u043F\u0440\u043E\u0441");
        queryExecute_B.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        queryExecute_B.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                StringBuilder message = new StringBuilder ("");
                boolean empty = false;
                Vector<Vector<String>> tempResults;
                types=new Vector<>();
                tempResults = DBConnect.executeQuery(query_TA.getText(), message, types);

                if (message.toString().equals(""))
                    message.append("Операция выполнена удачно");
                dataBaseMessage_TA.setText(message.toString());

                empty=(tempResults.isEmpty()||tempResults==null);
                if (!empty)
                    for (int i = 0; i < tempResults.elementAt(0).size(); i++) {
                        if (tempResults.elementAt(0).elementAt(i) == null
                                || tempResults.elementAt(0).elementAt(i).equals("")) {
                            empty = true;
                            break;
                        }
                    }
                if (!empty) {
                    if (queryResultFrame == null || !queryResultFrame.isWindowAlive())
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    queryResultFrame = new QueryResult();
                                    queryResultFrame.insertNewTab(tempResults, query_TA.getText(), types);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    else
                        queryResultFrame.insertNewTab(tempResults, query_TA.getText(), types);
                }
            }
        });
        frame.getContentPane().add(queryExecute_B, BorderLayout.WEST);

        JButton newQuery_B = new JButton("Написать новый запрос");
        newQuery_B.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                query_TA.setText("");
            }
        });
        newQuery_B.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        frame.getContentPane().add(newQuery_B, BorderLayout.EAST);

    }

    public void setQuery(String query) {
        presetSql=query;
    }

}
