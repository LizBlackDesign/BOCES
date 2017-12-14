package com.boces.black_stanton_boces.persistence.model;

/**
 * Model of AN Account Used To Access The Admin Section of The Database
 */
public class AdminAccount {

    /**
     * Id of The Account In The Database
     * null If A Create Model
     */
    private Integer id;

    /**
     * Username Used To Login To The Account
     */
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
