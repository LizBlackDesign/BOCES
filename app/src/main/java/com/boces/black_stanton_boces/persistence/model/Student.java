package com.boces.black_stanton_boces.persistence.model;

import android.graphics.Bitmap;

public class Student {

    /**
     * Id of The Student In The Database
     * May Be null If A Create Model
     */
    private Integer id;

    /**
     * Student's First Name
     */
    private String firstName;

    /**
     * Student's Last Name
     */
    private String lastName;

    /**
     * How Old A Student Is In Years
     */
    private int age;

    /**
     * What School Year The Student Is At
     */
    private int year;

    /**
     * Id of The Student's Teacher
     * May Be null If The Teacher Was Removed
     */
    private Integer teacherId;

    /**
     * Image Associated With The Student
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
