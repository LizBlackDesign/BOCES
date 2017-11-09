package com.boces.black_stanton_boces.teacher;

import com.boces.black_stanton_boces.persistence.model.Teacher;

public class TeacherSpinnerItem {
    private Teacher teacher;

    public TeacherSpinnerItem(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return teacher.getFirstName() + " " + teacher.getLastName();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
