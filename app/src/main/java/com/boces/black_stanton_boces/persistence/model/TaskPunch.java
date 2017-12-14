package com.boces.black_stanton_boces.persistence.model;

import java.util.Date;

public class TaskPunch {

    /**
     * Id of The Punch In The Database
     * May Be null If A Create Model
     */
    private Integer id;

    /**
     * Id of The Student That The Punch Is For
     */
    private int studentId;

    /**
     * The Id od The Task The Student Was Doing
     */
    private int taskId;

    /**
     * Time The Activity Starts
     * May Not Be null
     */
    private Date timeStart;

    /**
     * Time The Activity Was Concluded
     * May Be null To Signify An Ongoing Task
     */
    private Date timeEnd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
}
