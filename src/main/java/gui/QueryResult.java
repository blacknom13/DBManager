package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

public class QueryResult {

    private JTabbedPane tabbedPane;
    private JFrame frame;
    private Vector<Vector<Vector<String>>> results;
    private Vector<Vector<String>> columnNames= new Vector <>();
    private Vector<Vector<Class<?>>> types;
    boolean isAlive;

    /**
     * Launch the application.
     */

    /**
     * Create the application.
     */
    public QueryResult() {
        initialize();
//		frame.pack();
        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        results=new Vector<>();
        columnNames=new Vector<>();
        types=new Vector<>();
        isAlive=true;

        frame = new JFrame();
        frame.setBounds(100, 100, 496, 641);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowIconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                isAlive=false;
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowActivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }
        });

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

    }

    public void insertNewTab(Vector<Vector<String>> tempResults, String query, Vector<Class<?>> columnTypes) {
        // TODO Auto-generated method stub
        JTable results_T;
        JPanel panel;
        JScrollPane results_SP;
        JTextArea query_TA;
        JScrollPane query_SP;
        Vector<Vector<String>> tempVector=new Vector<>();

        if (tempResults!=null && tempResults.size()>0) {
            columnNames.add(tempResults.elementAt(0));
            for (int i=1;i<tempResults.size();i++)
                tempVector.add(tempResults.elementAt(i));
            results.add(tempVector);
            types.add(columnTypes);

            panel = new JPanel(new BorderLayout());
            tabbedPane.addTab("Запрос № "+tabbedPane.getTabCount(), null, panel, null);

            results_SP = new JScrollPane();
            panel.add(results_SP,BorderLayout.NORTH);

            results_T = new JTable(results.elementAt(results.size()-1),
                    columnNames.elementAt(columnNames.size()-1)) {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                public boolean isCellEditable(int row,int column) {
                    return false;
                };
            };
            results_SP.setViewportView(results_T);

            query_SP = new JScrollPane();
            panel.add(query_SP,BorderLayout.SOUTH);

            query_TA = new JTextArea(query);
            query_TA.setRows(4);
            query_TA.setFont(new Font("Monospaced", Font.PLAIN, 20));
            query_TA.setEditable(false);
            query_SP.setViewportView(query_TA);
            frame.repaint();
        }
    }

    public boolean isWindowAlive() {
        return isAlive;
    }

}
