package com.boces.black_stanton_boces.report;

import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;

import java.util.ArrayList;


public class StudentPunches {
    private Student student;
    private ArrayList<TaskPunch> punches = new ArrayList<>();

    public StudentPunches() {
    }

    public StudentPunches(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ArrayList<TaskPunch> getPunches() {
        return punches;
    }

    public void setPunches(ArrayList<TaskPunch> punches) {
        this.punches = punches;
    }
}
