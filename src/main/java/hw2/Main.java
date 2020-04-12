package hw2;

import java.sql.*;

public class Main {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement pstmt;


    public static void main(String[] args) throws SQLException {

        try {
            connect();
            //addDataToTable("students", "Sarah",777);
            //getDataByName("students", "Sarah");
            //changeValuesById("students", 1, "name", "Robert");
            //deleteRow("students", 4);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }

    private static void createNewTable(String tableName) {
        try {
            stmt.executeUpdate("CREATE TABLE " + tableName + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "score INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:myDB.db");
        stmt = connection.createStatement();
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getDataByName(String tableName, String name) {
        try {
            if (connection.isClosed()) return;
            pstmt = connection.prepareStatement("SELECT id, name, score FROM " + tableName + " WHERE name = ?;");
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            //String sql = String.format("SELECT id, name, score FROM " + tableName + " where name = '%s'", name);
            //ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString("name") + " " + rs.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addDataToTable(String tableName, String name, int score) {
        try {
            if (connection.isClosed()) return;
            pstmt = connection.prepareStatement("INSERT INTO " + tableName + " (name,score) VALUES (?,?);");
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
            //stmt.executeUpdate("INSERT INTO students (name, score) VALUES ('" + name + "', " + score + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRow(String tableName, int id) {
        try {
            if (connection.isClosed()) return;
            pstmt = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            //stmt.executeUpdate("DELETE FROM " + tableName + " WHERE id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void changeValuesById(String tableName, int id, String columnName, String newValue) {
        try {
            if (connection.isClosed()) return;
            pstmt = connection.prepareStatement("UPDATE " + tableName + " SET "+ columnName +" = ? WHERE id=?;");
            pstmt.setString(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            //stmt.executeUpdate("UPDATE " + tableName + " SET " + columnName + "= '" + newValue + "' WHERE id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void changeValuesById(String tableName, int id, String columnName, int newValue) {
        try {
            if (connection.isClosed()) return;
            pstmt = connection.prepareStatement("UPDATE " + tableName + " SET "+ columnName +" = ? WHERE id=?;");
            pstmt.setInt(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            //stmt.executeUpdate("UPDATE " + tableName + " SET " + columnName + "= " + newValue + " WHERE id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
