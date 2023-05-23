package model;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * A MySql database is being used to store different data of a file. For now the database consist 1 table named 'file'
 * with 4 columns: name varchar(255) primary key, type varchar(10), path varchar(255), size varchar(30)
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
    private PreparedStatement departmentSelect;

    public MySQLDatabase() {
        try (FileInputStream in = new FileInputStream("dbconnect.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            // Alles da?
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
        instance = new MySQLDatabase();

        instance.createEmployeeSelect();
        instance.createEmployeeInsert();
        instance.createFileInsert();
        instance.createFileDelete();
        instance.createDepartmentSelect();
    }


    public static void close() {
        try {
            getInstance().connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
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
        String sql = "SELECT e.username, e.password, e.authority, d.name FROM employee e where username = ? AND password = ? " +
                "INNER JOIN department d USING(departmentID)";
        employeeSelect = connection.prepareStatement(sql);
    }

    private void createDepartmentSelect() throws SQLException {
        String sql = "SELECT departmentID, name FROM department where name = ?";
        departmentSelect = connection.prepareStatement(sql);
    }

    /**
     * Insert data into the MySql Database
     *
     * @param fssFile fssFile
     */
    public void insert(FSSFile fssFile) throws FSSException {
        //Checks if the same filename already exist in the database.
        //  if true --> an Exception gets thrown
        if (check(fssFile)) {
            throw new FSSException("File " + fssFile.getFilename() + " already exist, please change the filename");
        }

        try {
            String sql = "INSERT INTO file (name, type, path, size) VALUES (?,?,?,?)";

            //A PreparedStatement is being used explicit for the filepath and to solve SQL Injection.
            //  Filepath contains multiple backslashes and the backslahes get ignored
            //  with using the 'normal' Statement.
            //  With the help of PreparedStatement the backslashes will not get
            //  ignored during the insertion.
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, fssFile.getFilename());
            pstmt.setString(2, fssFile.getFiletype());
            pstmt.setString(3, fssFile.getFilepath());
            //pstmt.setString(4, fssFile.getFilesize());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Inserted datas to the database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public PreparedStatement getDepartmentSelect() {
        return departmentSelect;
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