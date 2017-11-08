package com.boces.black_stanton_boces.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.util.ArrayList;

public class PersistenceInteractor extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "boces";
    private static final String TAG = "bocesDbHelper";

    private static class TEACHER {
        private static final String TABLE = "Teacher";
        private static final String ID = "TeacherId";
        private static final String FIRST_NAME = "FirstName";
        private static final String LAST_NAME = "LastName";
        private static final String EMAIL = "Email";
        private static final String PHONE_NUMBER = "PhoneNumber";
    }

    private static class TASK {
        private static final String TABLE = "Task";
        private static final String ID = "TaskId";
        private static final String NAME = "TaskName";
    }

    private static class STUDENT {
        private static final String TABLE = "Student";
        private static final String ID = "StudentId";
        private static final String FIRST_NAME = "FirstName";
        private static final String LAST_NAME = "LastName";
        private static final String AGE = "Age";
        private static final String YEAR = "Year";
        private static final String TEACHER_ID = "TeacherId";
    }

    private static final String STUDENT_DDL =
            "CREATE TABLE " + STUDENT.TABLE + "( " +
                    STUDENT.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    STUDENT.TEACHER_ID + " INTEGER, " +
                    STUDENT.FIRST_NAME + " TEXT, " +
                    STUDENT.LAST_NAME + " TEXT, " +
                    STUDENT.AGE + " INTEGER, " +
                    STUDENT.YEAR + " INTEGER, " +
                    "FOREIGN KEY(" + STUDENT.TEACHER_ID +") " +
                        "REFERENCES " + TEACHER.TABLE + "(" + TEACHER.ID + ")" +
                    ")";

    private static final String TASK_DDL =
            "CREATE TABLE " + TASK.TABLE + "( " +
                    TASK.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TASK.NAME + " TEXT" +
                    ")";

    private static final String TEACHER_DDL =
            "CREATE TABLE " + TEACHER.TABLE + "( " +
                    TEACHER.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TEACHER.FIRST_NAME + " TEXT, " +
                    TEACHER.LAST_NAME + " TEXT, " +
                    TEACHER.EMAIL + " TEXT, " +
                    TEACHER.PHONE_NUMBER + " TEXT" +
                    ")";

    public PersistenceInteractor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TEACHER_DDL);
        sqLiteDatabase.execSQL(TASK_DDL);
        sqLiteDatabase.execSQL(STUDENT_DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STUDENT.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TASK.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TEACHER.TABLE);
        this.onCreate(sqLiteDatabase);
    }

    public void empty() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(STUDENT.TABLE, null, null);
        db.delete(TASK.TABLE, null, null);
        db.delete(TEACHER.TABLE, null, null);
    }

    public void emptyTeachers() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TEACHER.TABLE, null, null);
    }

    public void emptyTasks() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TASK.TABLE, null, null);
    }

    public void emptyStudents() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(STUDENT.TABLE, null, null);
    }

    public void dropAndRecreate() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + STUDENT.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TASK.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEACHER.TABLE);

        this.onCreate(db);
    }

    private Student studentFromRow(Cursor cursor) {
        Student student = new Student();
        student.setId(cursor.getInt(0));
        student.setFirstName(cursor.getString(1));
        student.setLastName(cursor.getString(2));
        student.setAge(cursor.getInt(3));
        student.setYear(cursor.getInt(4));
        student.setTeacherId(cursor.getInt(5));
        return student;
    }

    public Student getStudent(int id) {
        Student student = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        STUDENT.ID + " , " +
                        STUDENT.FIRST_NAME + " , " +
                        STUDENT.LAST_NAME + " , " +
                        STUDENT.AGE + " , " +
                        STUDENT.YEAR + " , " +
                        STUDENT.TEACHER_ID +
                        " FROM " + STUDENT.TABLE +
                        " WHERE " + STUDENT.ID + "=" +id, null);

        if (cursor.moveToFirst()) {
            student = studentFromRow(cursor);
        }
        cursor.close();
        return student;
    }

    public ArrayList<Student> getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        STUDENT.ID + " , " +
                        STUDENT.FIRST_NAME + " , " +
                        STUDENT.LAST_NAME + " , " +
                        STUDENT.AGE + " , " +
                        STUDENT.YEAR + " , " +
                        STUDENT.TEACHER_ID +
                        " FROM " + STUDENT.TABLE, null);

        ArrayList<Student> students = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                students.add(studentFromRow(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return students;
    }

    public int addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STUDENT.FIRST_NAME, student.getFirstName());
        values.put(STUDENT.LAST_NAME, student.getLastName());
        values.put(STUDENT.AGE, student.getAge());
        values.put(STUDENT.YEAR, student.getYear());
        values.put(STUDENT.TEACHER_ID, student.getTeacherId());

        long rowId = db.insert(STUDENT.TABLE, null, values);

        // Abort on failed insert
        if (rowId == -1)
            return -1;

        // Get The New Student And Return Its ID
        Cursor cursor = db.rawQuery(
                "SELECT " + STUDENT.ID + " FROM " + STUDENT.TABLE +
                        " WHERE ROWID = " + rowId , null);

        int studentId = -1;

        if (cursor.moveToFirst()) {
            studentId = cursor.getInt(0);
        }
        cursor.close();
        return studentId;
    }

    public void update(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STUDENT.FIRST_NAME, student.getFirstName());
        values.put(STUDENT.LAST_NAME, student.getLastName());
        values.put(STUDENT.AGE, student.getAge());
        values.put(STUDENT.YEAR, student.getYear());

        int affectedRows = db.update(STUDENT.TABLE, values,
                STUDENT.ID + " = ?",
                new String[]{student.getId().toString()});

        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");
    }

    //START OF TASK
    private Task taskFromRow(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getInt(0));
        task.setName(cursor.getString(1));
        return task;
    }

    public Task getTask(int id) {
        Task task = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT" +
                        TASK.ID + " , " +
                        TASK.NAME +
                        " FROM " + TASK.TABLE +
                        " WHERE " + TASK.ID + "=" +id, null);

        if (cursor.moveToFirst()) {
            task = taskFromRow(cursor);
        }
        cursor.close();
        return task;
    }

    public ArrayList<Task> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        TASK.ID + " , " +
                        TASK.NAME +
                        " FROM " + TASK.TABLE
                        , null);

        ArrayList<Task> tasks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                tasks.add(taskFromRow(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    public int addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TASK.NAME, task.getName());

        long rowId = db.insert(TASK.TABLE, null, values);

        // Abort on failed insert
        if (rowId == -1)
            return -1;

        // Get The New Task And Return Its ID
        Cursor cursor = db.rawQuery(
                "SELECT " + TASK.ID + " FROM " + TASK.TABLE +
                        " WHERE ROWID = " + rowId , null);

        int taskId = -1;

        if (cursor.moveToFirst()) {
            taskId = cursor.getInt(0);
        }
        cursor.close();
        return taskId;
    }

    public void update(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TASK.NAME, task.getName());

        int affectedRows = db.update(TASK.TABLE, values,
                TASK.ID + " = ?",
                new String[]{task.getId().toString()});

        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");
    }
    //END OF TASK

    private Teacher teacherFromRow(Cursor cursor) {
        Teacher teacher = new Teacher();

        teacher.setId(cursor.getInt(0));
        teacher.setFirstName(cursor.getString(1));
        teacher.setLastName(cursor.getString(2));
        teacher.setEmail(cursor.getString(3));
        teacher.setPhoneNumber(cursor.getString(4));

        return teacher;
    }

    public Teacher getTeacher(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        TEACHER.ID + " , " +
                        TEACHER.FIRST_NAME + " , " +
                        TEACHER.LAST_NAME + " , " +
                        TEACHER.EMAIL + " , " +
                        TEACHER.PHONE_NUMBER +
                        " FROM " + TEACHER.TABLE +
                        " WHERE " + TEACHER.ID + "=" + id
                , null);


        Teacher teacher = null;
        if (cursor.moveToFirst()) {
            teacher = teacherFromRow(cursor);
        }

        cursor.close();
        return teacher;
    }

    public ArrayList<Teacher> getAllTeachers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        TEACHER.ID + " , " +
                        TEACHER.FIRST_NAME + " , " +
                        TEACHER.LAST_NAME + " , " +
                        TEACHER.EMAIL + " , " +
                        TEACHER.PHONE_NUMBER +
                        " FROM " + TEACHER.TABLE
                , null);

        ArrayList<Teacher> teachers = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                teachers.add(teacherFromRow(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return teachers;
    }

    public int addTask(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TEACHER.FIRST_NAME, teacher.getFirstName());
        values.put(TEACHER.LAST_NAME, teacher.getLastName());
        values.put(TEACHER.EMAIL, teacher.getEmail());
        values.put(TEACHER.PHONE_NUMBER, teacher.getPhoneNumber());

        long rowId = db.insert(TEACHER.TABLE, null, values);

        // Abort on failed insert
        if (rowId == -1)
            return -1;

        Cursor cursor = db.rawQuery(
                "SELECT " + TEACHER.ID + " FROM " + TEACHER.TABLE + " WHERE ROWID=" + rowId, null
        );

        int id = -1;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        cursor.close();
        return id;
    }

    public void update(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TEACHER.FIRST_NAME, teacher.getFirstName());
        values.put(TEACHER.LAST_NAME, teacher.getLastName());
        values.put(TEACHER.EMAIL, teacher.getEmail());
        values.put(TEACHER.PHONE_NUMBER, teacher.getPhoneNumber());

        int affectedRows = db.update(TEACHER.TABLE, values,
                TEACHER.ID + " = ?", new String[]{teacher.getId().toString()});

        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");
    }
}
