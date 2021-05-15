package gui;

import logic.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class InsertValuesWindow {
    private Vector<String> tableNames;

//		private Vector<String> columnNames = new Vector<>(
//				Arrays.asList("Field Name", "Field Type", "Field Length", "Default Value", "Not Null"));

    private HashMap<String, MappingPair> mapEngToRus;

    private int index = 0;

    private class MappingPair {
        String rus;
        String eng;
        String info;

        MappingPair(String f, String s, String info) {
            eng = f;
            rus = s;
            this.info = info;
        }
    }

    private final Vector<MappingPair> ENG_RUS_PAIRS = new Vector<>();

    private void initPairs() {
        // TODO Auto-generated method stub
        ENG_RUS_PAIRS.add(new MappingPair("DEPARTMENT", "Отделы", ""));
        ENG_RUS_PAIRS.add(new MappingPair("DEPT_ID", "ID", "Отдел"));
        ENG_RUS_PAIRS.add(new MappingPair("DEPT_NAME", "Наименование", "Отдел"));
        ENG_RUS_PAIRS.add(new MappingPair("TEL_NUM", "Телефон", "Отдел"));
        ENG_RUS_PAIRS.add(new MappingPair("EMAIL", "Эл. почта", "Отдел"));
        ENG_RUS_PAIRS.add(new MappingPair("SHEF_ID", "ID Начальника отдела", "Отдел"));
        ENG_RUS_PAIRS.add(new MappingPair("SHEF_BONUS_SALARY", "Бонус к зарплате начальника", "Отдел"));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE", "Сотрудники", ""));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE_ID", "ID", "Сотрудник"));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE_NAME", "Имя и фамилия", "Сотрудник"));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE_DATE_OF_BIRTH", "Дата рождения", "Сотрудник"));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE_ADRESS", "Адрес", "Сотрудник"));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE_EMAIL", "Эл. почта", "Сотрудник"));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE_TEL_NUM", "Телефон", "Сотрудник"));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE_DEPT", "ID Отдела", "Сотрудник"));
        ENG_RUS_PAIRS.add(new MappingPair("EMPLOYEE_POSITION", "ID Должности", "Сотрудник"));
        ENG_RUS_PAIRS.add(new MappingPair("POSITION", "Должности", ""));
        ENG_RUS_PAIRS.add(new MappingPair("POSITION_NAME", "Наименование", "Должность"));
        ENG_RUS_PAIRS.add(new MappingPair("POSITION_ID", "ID", "Должность"));
        ENG_RUS_PAIRS.add(new MappingPair("POSITION_SALARY", "Зарплата", "Должность"));
        ENG_RUS_PAIRS.add(new MappingPair("POSITION_JOIN", "Должность", "Должность"));
        ENG_RUS_PAIRS.add(new MappingPair("DEPT_JOIN", "Отдел", "Отдел"));
        ENG_RUS_PAIRS.add(new MappingPair("SHEF_JOIN", "Начальник отдела", "Отдел"));
        ENG_RUS_PAIRS.add(new MappingPair("BONUS_SALARY", "Бонус зарплата", "Отдел"));
    }

    private JFrame frame;
    JButton refresh_B;

    private String globalSql;
    Vector<Vector<String>> tableOfDepts;
    Vector<Vector<String>> tableOfEmp;
    Vector<Vector<String>> tableOfPos;
    Vector<Vector<String>> tableOfAllInfoOfEmp;

    /**
     * Launch the application.
     */

    /**
     * Create the application.
     */
    public InsertValuesWindow() {
        initialize();
        // frame.pack();
        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel_1 = new JPanel(new BorderLayout());
        frame.getContentPane().add(panel_1, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panel_1.add(tabbedPane);
        // tabbedPane.setBounds(0, 0, frame.getBounds().width - 20,
        // frame.getBounds().height - 50);
        tabbedPane.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.getListeners(MouseListener.class);

        mapEngToRus = new HashMap<>();
        // mapBackRusToEng=new HashMap<>();
        initPairs();
        initTheEngToRusMap();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        Vector<Class<?>> typesDept = new Vector<>();
        Vector<Class<?>> typesEmp = new Vector<>();
        Vector<Class<?>> typesPos = new Vector<>();
        Vector<Class<?>> typesAllInfo = new Vector<>();

        StringBuilder message = new StringBuilder("");
        String fetchAllDepts = "SELECT * FROM \"DEPARTMENT\" ORDER BY DEPT_ID";
        String fetchAllEmp = "SELECT * FROM \"EMPLOYEE\" ORDER BY EMPLOYEE_ID";
        String fetchAllPos = "SELECT * FROM \"POSITION\" ORDER BY POSITION_ID";
        String fetchAllInfoOfEmp = "SELECT " + "EMPLOYEE_NAME," + "EMPLOYEE_TEL_NUM," + "EMPLOYEE_EMAIL,"
                + "EMPLOYEE_ADRESS," + "POSITION_NAME as POSITION_JOIN," + "DEPT_NAME as DEPT_JOIN,"
                + "CASE EMPLOYEE_ID " + "WHEN SHEF_ID THEN \'Да\' " + "ELSE null " + "END as SHEF_JOIN,"
                + "POSITION_SALARY, " + "CASE EMPLOYEE_ID " + "WHEN SHEF_ID THEN SHEF_BONUS_SALARY " + "ELSE \'0\' "
                + "END " + "as BONUS_SALARY " + "FROM \"POSITION\" RIGHT JOIN (\"EMPLOYEE\" LEFT JOIN \"DEPARTMENT\" "
                + "ON EMPLOYEE_DEPT=DEPT_ID)" + "ON EMPLOYEE_POSITION=POSITION_ID";

        tableOfDepts = new Vector<>();
        tableOfEmp = new Vector<>();
        tableOfPos = new Vector<>();
        tableOfAllInfoOfEmp = new Vector<>();

        tableOfDepts = DBConnect.executeQuery(fetchAllDepts, message, typesDept);
        tableOfEmp = DBConnect.executeQuery(fetchAllEmp, message, typesEmp);
        tableOfPos = DBConnect.executeQuery(fetchAllPos, message, typesPos);
        tableOfAllInfoOfEmp = DBConnect.executeQuery(fetchAllInfoOfEmp, message, typesAllInfo);

        tabbedPane.addTab("Отделы", null, createTab(tableOfDepts, tabbedPane, typesDept), null);
        tabbedPane.addTab("Должности", null, createTab(tableOfPos, tabbedPane, typesPos), null);
        tabbedPane.addTab("Сотрудники", null, createTab(tableOfEmp, tabbedPane, typesEmp), null);
        tabbedPane.addTab("Полная информация", null, createTab(tableOfAllInfoOfEmp, tabbedPane, typesAllInfo), null);
        tabbedPane.setSelectedIndex(index);

        JPanel control_P = new JPanel(new GridLayout(2, 1, 0, 100));
        JButton queryWindow_B = new JButton("Написать запрос");
        queryWindow_B.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            @SuppressWarnings("unused")
                            QueryWindow x = new QueryWindow();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        control_P.add(queryWindow_B);

        refresh_B = new JButton("Обновить");
        refresh_B.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                index = tabbedPane.getSelectedIndex();
                frame.dispose();
                initialize();
                // frame.pack();
                frame.setVisible(true);
            }
        });
        control_P.add(refresh_B);

        panel_1.add(control_P, BorderLayout.EAST);
    }

    public JPanel createTab(Vector<Vector<String>> table, JTabbedPane tabbedPane, Vector<Class<?>> types) {
        JScrollPane tempScrollPane;
        JPanel contain = new JPanel(new BorderLayout());
        JTable tempTable;
        JCheckBox nulls = new JCheckBox();
        // JComboBox<String> types = new JComboBox<>(availableTypes);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        HashMap<String, Integer> columnMap = new HashMap<>();
        Vector<Vector<String>> tempVector = new Vector<>();
        Vector<String> columnDisplayNames = new Vector<>();
        Vector<String> columnNames = new Vector<>();
        boolean unEditable = false;

        if (table != null && table.size() > 0) {
            for (int i = 0; i < table.elementAt(0).size(); i++) {
                columnDisplayNames.add(mapEngToRus.get(table.elementAt(0).elementAt(i)).rus);
                columnNames.add(mapEngToRus.get(table.elementAt(0).elementAt(i)).eng);
                if (columnNames.elementAt(i).contains("JOIN"))
                    unEditable = true;
                // System.out.println(columnNames.elementAt(i)+"
                // "+types.elementAt(i).toString());
            }

            for (int i = 1; i < table.size(); i++) {
                Vector<String> temp = new Vector<>(table.elementAt(i));
                tempVector.add(temp);
            }
        }
        // System.out.println(table.size());
        if (unEditable) {
            tempTable = new JTable(tempVector, columnDisplayNames) {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    // TODO Auto-generated method stub
                    return false;
                }
            };
        } else {
            tempTable = new JTable(tempVector, columnDisplayNames) {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    // TODO Auto-generated method stub
                    if (column > 0)
                        return true;
                    else
                        return false;
                }
            };
        }
        tempTable.setRowHeight(30);
        tempTable.setFont(new Font("Times New Roman", Font.PLAIN, 16));
//		tempTable.setPreferredScrollableViewportSize(
//				new Dimension(tabbedPane.getSize().width - 10, tabbedPane.getHeight() - 70));
        tempTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tempTable.getColumnCount(); i++)
            tempTable.getColumnModel().getColumn(i).setCellRenderer(center);

        for (int i = 0; i < tempTable.getColumnCount(); i++) {
            columnMap.put(tempTable.getColumnName(0), 0);
        }

        JButton addRow_B = new JButton("Добавить строку");
        addRow_B.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (tempTable.isEditing()) {
                    tempTable.getCellEditor().stopCellEditing();
                }

                DefaultTableModel m = (DefaultTableModel) tempTable.getModel();
                m.addRow(new Vector<String>());
//				if ()
//				tempTable.setValueAt(
//						Integer.parseInt((tempTable.getValueAt(tempTable.getRowCount() - 2, 0).toString())) + 1,
//						tempTable.getRowCount() - 1, 0);
            }
        });

        tempScrollPane = new JScrollPane(tempTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // tempScrollPane.setPreferredSize(new Dimension(tabbedPane.getSize().width -
        // 10, tabbedPane.getHeight() - 70));
        contain.add(tempScrollPane, BorderLayout.NORTH);

        JPanel button_P = new JPanel();
        JButton save_B = new JButton("Сохранить изменения");
        save_B.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                if (tempTable.isEditing()) {
                    tempTable.getCellEditor().stopCellEditing();
                }
                if (tempTable.getRowCount() < 1) {
                    JOptionPane.showMessageDialog(frame, "Невозможно сохранить изменения!");
                } else {
                    remapColumnsWithNames(tempTable, columnMap);

                    boolean allRowsOk = true;
                    boolean updated;
                    String tableName = mapEngToRus.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())).eng;
                    String columns[] = columnNames.toString().replaceAll("\\[|\\]|[A-Z]*_ID,", "").split(",");
//					Vector <String> colunmNames=extractColumNames(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),
//							tempTable);
                    String executeBlock = "EXECUTE BLOCK AS \n BEGIN\n";
                    String insertValues = "";
                    String updateValues = "";

                    int rowCount = tempTable.getRowCount();
                    int colCount = tempTable.getColumnCount();
                    // System.out.println(colCount);
                    for (int i = 1; i < table.size(); i++) {
                        updated = false;
                        for (int j = 1; j < colCount; j++) {
                            Object value = tempTable.getValueAt(i - 1, columnMap.get(columnDisplayNames.elementAt(j)));
                            if (!Objects.equals(value, table.elementAt(i).elementAt(j))) {
                                updateValues += "UPDATE \"" + tableName + "\" SET ";
                                for (int k = 0; k < columns.length; k++) {
                                    value = tempTable.getValueAt(i - 1,
                                            columnMap.get(columnDisplayNames.elementAt(k + 1)));
                                    if (value != null && !value.toString().isEmpty())
                                        updateValues += columns[k] + "= \'" + value.toString() + "\',";
                                    else
                                        updateValues += columns[k] + "= null,";
                                }
                                updateValues = updateValues.substring(0, updateValues.length() - 1);
                                updateValues += "\n WHERE " + table.elementAt(0).elementAt(0) + "="
                                        + tempTable.getValueAt(i - 1, columnMap.get(columnDisplayNames.elementAt(0)))
                                        + ";\n";
                                updated = true;
                                break;
                            }

                        }
                    }

                    for (int i = table.size() - 1; i < rowCount; i++) {
                        String validColumns = "";
                        String values = "";
                        String insertHeader = "INSERT INTO \"" + tableName + "\" (";
                        for (int j = 1; j < colCount; j++) {
                            Object currVal = tempTable.getValueAt(i, columnMap.get(columnDisplayNames.elementAt(j)));
                            validColumns += " " + columns[j - 1] + ",";
                            if (currVal != null && !currVal.toString().isEmpty()) {
                                values += " \'" + currVal.toString() + "\',";
                            } else
                                values += "null,";
                        }
                        if (!validColumns.isEmpty()) {
                            insertValues += insertHeader;
                            insertValues += validColumns.substring(0, validColumns.length() - 1);
                            insertValues += ") VALUES (";
                            insertValues += values.substring(0, values.length() - 1);
                            insertValues += ");\n";
                        }
                    }
                    if (!updateValues.isEmpty() || !insertValues.isEmpty()) {
                        executeBlock += updateValues + insertValues;
                        executeBlock += "END";
                        //System.out.println(executeBlock);
                        globalSql = executeBlock;
                        startExecution();
                    }
                }

            }

        });
        button_P.add(save_B);

        JButton dropField_B = new JButton("Удалить строку");
        dropField_B.setEnabled(table.size() > 1);
        dropField_B.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                remapColumnsWithNames(tempTable, columnMap);
                int fieldID = tempTable.getSelectedRow();

                if (tempTable.isEditing()) {
                    tempTable.getCellEditor().stopCellEditing();
                }
                if (fieldID == -1)
                    JOptionPane.showMessageDialog(frame, "Выберете строку для удаления", "Информация",
                            JOptionPane.INFORMATION_MESSAGE);
                else {
                    String tableName = mapEngToRus.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())).eng;
                    // System.out.println(tableName);
                    // System.out.println(table.elementAt(0).elementAt(0));
                    if (table.size() > 1) {
                        String valueID = tempTable.getValueAt(fieldID, columnMap.get(columnDisplayNames.elementAt(0)))
                                .toString();
                        int choice = JOptionPane.showConfirmDialog(frame, "Удалить строку?");
                        if (choice == JOptionPane.OK_OPTION) {
                            String sqlDeleteValues = "DELETE FROM \"" + tableName + "\" WHERE "
                                    + table.elementAt(0).elementAt(0) + "=" + valueID;
                            globalSql = sqlDeleteValues;
                            startExecution();
                        }
                    }
                }
            }
        });

        save_B.setEnabled(!unEditable);
        addRow_B.setEnabled(!unEditable);
        dropField_B.setEnabled(!unEditable);
        button_P.add(addRow_B);
        button_P.add(dropField_B);

        contain.add(button_P, BorderLayout.SOUTH);

        return contain;
    }

    private Vector<String> extractColumNames(String tableName, JTable table) {
        // TODO Auto-generated method stub
        Vector<String> columnNames = new Vector<>();
        int columns = table.getColumnCount();
        for (int i = 0; i < columns; i++) {
            //System.out.println(table.getColumnName(i) + tableName);
            // columnNames.add(mapEngToRus.get(table.getColumnName(i)+tableName).eng);
        }
        return columnNames;
    }

    public Vector<Vector<Vector<String>>> devideTables(Vector<String> tableNames, Vector<Vector<String>> tableInfo) {
        Vector<Vector<Vector<String>>> dT = new Vector<>();
        Vector<Vector<String>> temp = new Vector<>();
        int j = 0;

        for (int i = 0; i < tableInfo.size(); i++) {
            if (tableNames.elementAt(j).equals(tableInfo.elementAt(i).elementAt(0))) {
                temp.add(tableInfo.elementAt(i));
            } else {
                j++;
                dT.add(temp);
                temp = new Vector<>();
                temp.add(tableInfo.elementAt(i));
            }
        }
        if (temp.size() > 0) {
            dT.add(temp);
        }
        return dT;
    }

    private class CheckBoxInTable extends DefaultCellEditor {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public CheckBoxInTable(JCheckBox checkBox) {
            // TODO Auto-generated constructor stub
            super(checkBox);
            checkBox.setVisible(false);

        }
    }

    private void remapColumnsWithNames(JTable tempTable, HashMap<String, Integer> columnMap) {
        for (int i = 0; i < tempTable.getColumnCount(); i++) {
            columnMap.put(tempTable.getColumnName(i), i);
        }
    }

    private void initTheEngToRusMap() {
        // TODO Auto-generated method stub

        for (int i = 0; i < ENG_RUS_PAIRS.size(); i++) {
            mapEngToRus.put(ENG_RUS_PAIRS.elementAt(i).eng, ENG_RUS_PAIRS.elementAt(i));
            mapEngToRus.put(ENG_RUS_PAIRS.elementAt(i).rus + ENG_RUS_PAIRS.elementAt(i).info,
                    ENG_RUS_PAIRS.elementAt(i));
        }
    }

    private void startExecution() {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					@SuppressWarnings("unused")
//					QueryWindow x = new QueryWindow(globalSql);
//					globalSql = "";
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
        Vector<Class<?>> types = new Vector<>();
        StringBuilder message = new StringBuilder();
        DBConnect.executeQuery(globalSql, message, types);
        if (message.toString().isEmpty())
            message.append("Операция выполнена удачно");
        JOptionPane.showMessageDialog(frame, message);
        refresh_B.doClick();
    }

}
