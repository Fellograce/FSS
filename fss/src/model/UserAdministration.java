package model;

import javafx.beans.property.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserAdministration {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final BooleanProperty authority = new SimpleBooleanProperty();
    private final ObjectProperty<Deparment> department = new SimpleObjectProperty<>();

    public UserAdministration(String username, String password, boolean authority, Deparment department) {
        setUsername(username);
        setPassword(password);
        setAuthority(authority);
        setDepartment(department);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public boolean isAuthority() {
        return authority.get();
    }

    public BooleanProperty authorityProperty() {
        return authority;
    }

    public void setAuthority(boolean authority) {
        this.authority.set(authority);
    }

    public Deparment getDepartment() {
        return department.get();
    }

    public ObjectProperty<Deparment> departmentProperty() {
        return department;
    }

    public void setDepartment(Deparment department) {
        this.department.set(department);
    }

    public void save() throws SQLException {
        try{
            PreparedStatement pstmt = MySQLDatabase.getInstance().getEmployeeInsert();
            pstmt.setString(1, getUsername());
            pstmt.setString(2, getPassword());
            pstmt.setBoolean(3, isAuthority());
            pstmt.setObject(4, getDepartment());
        }
        catch (SQLException e) {
            throw e;
        }
    }

}
