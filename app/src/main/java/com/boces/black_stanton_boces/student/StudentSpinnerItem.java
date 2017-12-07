package com.boces.black_stanton_boces.student;

import com.boces.black_stanton_boces.persistence.model.Student;

public class StudentSpinnerItem {
    private Student student;

    public StudentSpinnerItem(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return student.getFirstName() + " " + student.getLastName();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
