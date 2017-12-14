package com.boces.black_stanton_boces.persistence.model;

import android.graphics.Bitmap;

public class Teacher {

    /**
     * Id of The Task Punch In The Database
     * May Be Null if A Create Model
     */
    private Integer id;

    /**
     * First Name of The Teacher
     */
    private String firstName;

    /**
     * Last Name of The Teacher
     */
    private String lastName;

    /**
     * Email of The Teacher
     */
    private String email;

    /**
     * Phone Number of The Teacher
     */
    private String phoneNumber;

    /**
     * Image Associated With The Teacher
     * May Be null
     */
    private Bitmap image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
