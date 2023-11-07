package model;

public class User {
    private String username;
    private String password;
    private String displayName;
    private Boolean online;

    public User() {
        this.username = "";
        this.password = "";
        this.displayName = "";
        this.online = false;
    }

    public User(String username, String password, String displayName, Boolean online) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.online = online;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Boolean getOnline() {
        return this.online;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public boolean equals(Object user) {
        User otherUser = (User)user;
        return this.username == otherUser.username;
    }
}