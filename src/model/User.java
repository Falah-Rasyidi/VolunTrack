package model;

public class User {
    private String fullname;
    private String username;
    private String email;
    private String passwordHash;
    private String userType;
    
    public User(String fullname, String username, String email, String passwordHash, String userType) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
    }

    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}