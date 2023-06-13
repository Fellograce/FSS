package model;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * A MySql database is being used to store different data of a file. For now the database consists 3 table named 'file'
 * 'employee' and 'department'.
 */
public class MySQLDatabase {
    private String url;
    private String username;
    private String password;
    private Connection connection;
    private static MySQLDatabase instance;
    private PreparedStatement employeeSelect;
    private PreparedStatement employeeInsert;
    private PreparedStatement fileInsert;
    private PreparedStatement fileDelete;
    private PreparedStatement fileSelect;
    private PreparedStatement departmentSelectGetID;
    private PreparedStatement departmentSelectGetName;

    private MySQLDatabase() {
        try (FileInputStream in = new FileInputStream("dbconnect.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            if (url == null || username == null || password == null) {
                throw new Exception("Fehler! Property File muss driver, url, username, password enthalten!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public static MySQLDatabase getInstance() {
        return instance;
    }

    public static void open() throws SQLException {
        if (instance == null) {
            instance = new MySQLDatabase();
        }

        instance.createEmployeeSelect();
        instance.createEmployeeInsert();
        instance.createFileInsert();
        instance.createFileDelete();
        instance.createFileSelect();
        instance.createDepartmentSelectGetID();
        instance.createDepartmentSelectGetName();
    }

    public static void close() {
        try {
            getInstance().connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void createFileSelect() throws SQLException {
        String sql = "SELECT name, type, path, size, departmentID, date FROM file WHERE name = ?";
        fileSelect = connection.prepareStatement(sql);
    }

    private void createFileDelete() throws SQLException {
        String sql = "DELETE FROM file WHERE name = ?";
        fileDelete = connection.prepareStatement(sql);
    }

    private void createFileInsert() throws SQLException {
        String sql = "INSERT INTO file (name, type, path, size, date, departmentID) values (?, ?, ?, ?, ?, ?)";
        fileInsert = connection.prepareStatement(sql);
    }

    private void createEmployeeInsert() throws SQLException {
        String sql = "INSERT INTO employee (username, password, authority, departmentID) values (?, ?, ?, ?)";
        employeeInsert = connection.prepareStatement(sql);
    }

    private void createEmployeeSelect() throws SQLException {
        String sql = "SELECT e.id, e.username, e.password, e.authority, d.name FROM employee e INNER JOIN department d " +
                "USING(departmentID) WHERE username = ? AND password = ?";
        employeeSelect = connection.prepareStatement(sql);
    }

    private void createDepartmentSelectGetID() throws SQLException {
        String sql = "SELECT departmentID, name FROM department where name = ?";
        departmentSelectGetID = connection.prepareStatement(sql);
    }

    private void createDepartmentSelectGetName() throws SQLException {
        String sql = "SELECT departmentID, name FROM department where departmentID = ?";
        departmentSelectGetName = connection.prepareStatement(sql);
    }

    public PreparedStatement getEmployeeSelect() {
        return employeeSelect;
    }

    public PreparedStatement getEmployeeInsert() {
        return employeeInsert;
    }

    public PreparedStatement getFileInsert() {
        return fileInsert;
    }

    public PreparedStatement getFileDelete() {
        return fileDelete;
    }

    public PreparedStatement getDepartmentSelectGetID() {
        return departmentSelectGetID;
    }

    public PreparedStatement getFileSelect() {
        return fileSelect;
    }

    public PreparedStatement getDepartmentSelectGetName() {
        return departmentSelectGetName;
    }

    /**
     * Checks if the filename already exist in the database
     *
     * @param fssFile fssFile
     * @return exist
     */
    private boolean check(FSSFile fssFile) {
        boolean exist = false;
        try {
            String sql = "SELECT name FROM file where name like '" + fssFile.getFilename() + "'";
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(sql);
            if (result.next()) {
                //If result has a next line means that the filename already exist in the database.
                exist = true;
            } else exist = false;

            st.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }
}