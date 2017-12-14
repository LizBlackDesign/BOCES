package com.boces.black_stanton_boces.persistence.model;

import android.graphics.Bitmap;

public class Task {
    /**
     * Id of The Task In The Database
     * May Be null If A Create Model
     */
    private Integer id;

    /**
     * Name of The Task
     */
    private String name;

    /**
     * Image Associated With The Task
     * May Be null
     */
    private Bitmap image;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
