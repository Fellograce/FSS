package model;

import java.util.Objects;

public class Employee {
    private int id;
    private String username;
    private String password;
    private boolean authority;


    public Employee() {
    }

    public Employee(int id, String username, String password, boolean authority) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setAuthority(authority);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthority() {
        return authority;
    }

    public void setAuthority(boolean authority) {
        this.authority = authority;
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

    public void save() throws FSSException {
        MySQLDatabase.getInstance().insert(this);
    }
}