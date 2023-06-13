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

    /**
     * Private constructor
     */
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

    /**
     * Getter for Instance
     * @return instance
     */
    public static MySQLDatabase getInstance() {
        return instance;
    }

    /**
     * Opens the connection to the database and initialize the static variable instance (singleton)
     * @throws SQLException
     */
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

    /**
     * Closes the database conenction
     */
    public static void close() {
        try {
            getInstance().connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates the PreparedStatement to select a file
     * @throws SQLException
     */
    private void createFileSelect() throws SQLException {
        String sql = "SELECT name, type, path, size, departmentID, date FROM file WHERE name = ?";
        fileSelect = connection.prepareStatement(sql);
    }

    /**
     * Creates the PreparedStatement to delete a file
     * @throws SQLException
     */
    private void createFileDelete() throws SQLException {
        String sql = "DELETE FROM file WHERE name = ?";
        fileDelete = connection.prepareStatement(sql);
    }

    /**
     * Creates the PreparedStatement to insert a file
     * @throws SQLException
     */
    private void createFileInsert() throws SQLException {
        String sql = "INSERT INTO file (name, type, path, size, date, departmentID) values (?, ?, ?, ?, ?, ?)";
        fileInsert = connection.prepareStatement(sql);
    }

    /**
     * Creates the PreparedStatement to insert an employee
     * @throws SQLException
     */
    private void createEmployeeInsert() throws SQLException {
        String sql = "INSERT INTO employee (username, password, authority, departmentID) values (?, ?, ?, ?)";
        employeeInsert = connection.prepareStatement(sql);
    }

    /**
     * Creates the PreparedStatement to select an employee
     * @throws SQLException
     */
    private void createEmployeeSelect() throws SQLException {
        String sql = "SELECT e.id, e.username, e.password, e.authority, d.name FROM employee e INNER JOIN department d " +
                "USING(departmentID) WHERE username = ? AND password = ?";
        employeeSelect = connection.prepareStatement(sql);
    }

    /**
     * Creates the PreparedStatement to get the ID of a specific department
     * @throws SQLException
     */
    private void createDepartmentSelectGetID() throws SQLException {
        String sql = "SELECT departmentID, name FROM department where name = ?";
        departmentSelectGetID = connection.prepareStatement(sql);
    }

    /**
     * Creates the PreparedStatement to get the name of a specific deparment ID
     * @throws SQLException
     */
    private void createDepartmentSelectGetName() throws SQLException {
        String sql = "SELECT departmentID, name FROM department where departmentID = ?";
        departmentSelectGetName = connection.prepareStatement(sql);
    }

    /**
     * Getter for employeeSelect
     * @return employeeSelect
     */
    public PreparedStatement getEmployeeSelect() {
        return employeeSelect;
    }

    /**
     * Getter for employeeInsert
     * @return employeeInsert
     */
    public PreparedStatement getEmployeeInsert() {
        return employeeInsert;
    }

    /**
     * Getter for fileInsert
     * @return fileInsert
     */
    public PreparedStatement getFileInsert() {
        return fileInsert;
    }

    /**
     * Getter for fileDelete
     * @return fileDelete
     */
    public PreparedStatement getFileDelete() {
        return fileDelete;
    }

    /**
     * Getter for fileSelect
     * @return fileSelect
     */
    public PreparedStatement getFileSelect() {
        return fileSelect;
    }

    /**
     * Getter for departmentSelectGetID
     * @return departmentSelectGetID
     */
    public PreparedStatement getDepartmentSelectGetID() {
        return departmentSelectGetID;
    }

    /**
     * Getter for departmentSelectGetName
     * @return departmentSelectGetName
     */
    public PreparedStatement getDepartmentSelectGetName() {
        return departmentSelectGetName;
    }
}