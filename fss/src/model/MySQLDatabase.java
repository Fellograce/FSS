package model;

import java.sql.*;

public class MySQLDatabase {
    private static final String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11591230";
    private static final String user = "sql11591230";
    private static final String password = "m41fl9v1HN";


    /**
     * Insert data into the MySql Database
     *
     * @param fssFile File
     */
    public static void insert(FSSFile fssFile) throws FSSException {
        if (check(fssFile)) {
            throw new FSSException("File " + fssFile.getFilename() + " already exist, please change the filename");
        }
        try (Connection c = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO file (name, type, path, size) VALUES (?,?,?,?)";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, fssFile.getFilename());
            pstmt.setString(2, fssFile.getFiletype());
            pstmt.setString(3, fssFile.getFilepath());
            pstmt.setString(4, fssFile.getFilesize());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Inserted datas to the database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if file already exist in the database
     * @param fssFile
     * @return
     */
    private static boolean check(FSSFile fssFile) {
        boolean exist = false;
        try (Connection c = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT name FROM file where name like " + "'" + fssFile.getFilename() + "'";
            Statement st = c.createStatement();
            ResultSet result = st.executeQuery(sql);
            if (result.next()) {
                exist = true;
            } else exist = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }
}
