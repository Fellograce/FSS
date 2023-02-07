package model;

import java.sql.*;

/**
 * A MySql database is being used to store different data of a file.
 * For now the database consist 1 table named 'file' with 4 columns:
 *      name varchar(255) primary key,
 *      type varchar(10),
 *      path varchar(255),
 *      size varchar(30)
 *
 */
public class MySQLDatabase {
    private static final String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11591230";
    private static final String user = "sql11591230";
    private static final String password = "m41fl9v1HN";


    /**
     * Insert data into the MySql Database
     *
     * @param fssFile fssFile
     */
    public static void insert(FSSFile fssFile) throws FSSException {
        //Checks if the same filename already exist in the database.
        //  if true --> an Exception gets thrown
        if (check(fssFile)) {
            throw new FSSException("File " + fssFile.getFilename() + " already exist, please change the filename");
        }

        try (Connection c = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO file (name, type, path, size) VALUES (?,?,?,?)";

            //A PreparedStatement is being used explicit for the filepath and to solve SQL Injection.
            //  Filepath contains multiple backslashes and the backslahes get ignored
            //  with using the 'normal' Statement.
            //  With the help of PreparedStatement the backslashes will not get
            //  ignored during the insertion.
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
     * Checks if the filename already exist in the database
     * @param fssFile fssFile
     * @return exist
     */
    private static boolean check(FSSFile fssFile) {
        boolean exist = false;
        try (Connection c = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT name FROM file where name like " + "'" + fssFile.getFilename() + "'";
            Statement st = c.createStatement();
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
