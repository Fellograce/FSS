package model;

import javafx.beans.property.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * The Employee class is used to decide if the user will get limited visibility or not.
 */
public class Employee {
    private int id;
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final BooleanProperty authority = new SimpleBooleanProperty();
    private final ObjectProperty<Department> department = new SimpleObjectProperty<>();


    /**
     * Default Contructor
     */
    public Employee() {
    }


    /**
     * Constructor
     * @param id int
     * @param username String
     * @param password String
     * @param authority boolean
     * @param department Department
     */
    public Employee(int id, String username, String password, boolean authority, Department department) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setAuthority(authority);
        setDepartment(department);
    }


    /**
     * Getter for ID
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id
     * @param id int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for username
     * @return username
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Getter for usernameProperty
     * @return usernameProperty
     */
    public StringProperty usernameProperty() {
        return username;
    }


    /**
     * Setter for username
     * @param username String
     */
    public void setUsername(String username) {
        this.username.set(username);
    }


    /**
     * Getter for password
     * @return password
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Getter for passwordProperty
     * @return passwordProperty
     */
    public StringProperty passwordProperty() {
        return password;
    }

    /**
     * Setter for password
     * @param password String
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /**
     * Getter for authority
     * @return authority
     */
    public boolean isAuthority() {
        return authority.get();
    }


    /**
     * Getter for authorityProperty
     * @return authorityProperty
     */
    public BooleanProperty authorityProperty() {
        return authority;
    }

    /**
     * Setter for authority
     * @param authority authority
     */
    public void setAuthority(boolean authority) {
        this.authority.set(authority);
    }

    /**
     * Getter for department
     * @return department
     */
    public Department getDepartment() {
        return department.get();
    }

    /**
     * Getter for departmentProperty
     * @return departmentProperty
     */
    public ObjectProperty<Department> departmentProperty() {
        return department;
    }

    /**
     * Setter for department
     * @param department department
     */
    public void setDepartment(Department department) {
        this.department.set(department);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && authority == employee.authority && username.equals(employee.username) && password.equals(employee.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, authority);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    /**
     * Saves new employee into the database
     * @throws SQLException
     */
    public void save() throws SQLException {
        try {
            PreparedStatement departmentStatement = MySQLDatabase.getInstance().getDepartmentSelectGetID();
            departmentStatement.setString(1, getDepartment().toString());
            ResultSet rsDepartment = departmentStatement.executeQuery();

            rsDepartment.next();

            PreparedStatement pstmt = MySQLDatabase.getInstance().getEmployeeInsert();
            pstmt.setString(1, getUsername());
            pstmt.setString(2, getPassword());
            pstmt.setBoolean(3, isAuthority());
            pstmt.setInt(4, rsDepartment.getInt("departmentID"));
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}