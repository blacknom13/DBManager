package logic;

import java.sql.*;
import java.util.Vector;

public class DBConnect {

    static String db_URL;

    static {
        db_URL = "jdbc:firebirdsql:localhost/3050:";
    }

    static Connection conn = null;
    static Statement stmn = null;

    public static boolean initConnection(String db_URL, String user, String password) {
        db_URL = DBConnect.db_URL + db_URL+"?encoding=UTF8";
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            System.out.println("Connecting to database");
            conn = DriverManager.getConnection(db_URL, user, password);
            System.out.println("Connection established");
            stmn = conn.createStatement();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Vector<Vector<String>> executeQuery(String query, StringBuilder message, Vector<Class<?>> types) {
        ResultSet s = null;
        int columnCount;
        Vector<Vector<String>> results = new Vector<>();
        Vector<String> temp;
        try {
            stmn.execute(query);
            s = stmn.getResultSet();
            if (s!=null) {
                columnCount = s.getMetaData().getColumnCount();
                temp = new Vector<>();

                for (int i = 0; i < columnCount; i++) {
                    //  System.out.println(toClass(s.getMetaData().getColumnType(i + 1)));
                    types.add(toClass(s.getMetaData().getColumnType(i + 1)));
                    temp.add(s.getMetaData().getColumnLabel(i + 1).trim());
                }
                results.add(temp);
                while (s.next()) {
                    temp = new Vector<>();
                    for (int i = 0; i < columnCount; i++) {
                        if (s.getString(i+1)==null)
                            temp.add(s.getString(i + 1));
                        else
                            temp.add(s.getString(i + 1).trim());
                        // System.out.println(temp.elementAt(i));
                    }
                    results.add(temp);
                }
            }
        } catch (SQLException e) {
            for (StackTraceElement el: e.getStackTrace())
                message.append(el.toString()+"\n");
            e.printStackTrace();
        } catch (Exception ex) {
            // TODO: handle exception
            for (StackTraceElement el: ex.getStackTrace())
                message.append(el.toString()+"\n");
            ex.printStackTrace();
        }

        return results;
    }

    public static Class<?> toClass(int type) {
        Class<?> result = Object.class;

        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                result = String.class;
                break;

            case Types.NUMERIC:
            case Types.DECIMAL:
                result = java.math.BigDecimal.class;
                break;

            case Types.BIT:
                result = Boolean.class;
                break;

            case Types.TINYINT:
                result = Byte.class;
                break;

            case Types.SMALLINT:
                result = Short.class;
                break;

            case Types.INTEGER:
                result = Integer.class;
                break;

            case Types.BIGINT:
                result = Long.class;
                break;

            case Types.REAL:
            case Types.FLOAT:
                result = Float.class;
                break;

            case Types.DOUBLE:
                result = Double.class;
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                result = Byte[].class;
                break;

            case Types.DATE:
                result = Date.class;
                break;

            case Types.TIME:
                result = Time.class;
                break;

            case Types.TIMESTAMP:
                result = Timestamp.class;
                break;
        }

        return result;
    }

    public static void terminateConnection() {
        try {
            if (stmn != null)
                stmn.close();
        } catch (SQLException e) {
            // TODO: handle exception
        }
        try {
            if (conn != null)
                conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

