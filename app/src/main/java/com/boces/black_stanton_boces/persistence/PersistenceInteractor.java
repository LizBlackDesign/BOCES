package com.boces.black_stanton_boces.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.boces.black_stanton_boces.persistence.model.AdminAccount;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

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
        private static final String IMAGE = "TeacherImage";
    }

    private static class TASK {
        private static final String TABLE = "Task";
        private static final String ID = "TaskId";
        private static final String NAME = "TaskName";
        private static final String IMAGE = "TaskImage";
    }

    private static class TASK_PUNCH {
        private static final String TABLE = "TaskPunch";
        private static final String ID = "TaskPunchID";
        private static final String STUDENT_ID = "StudentID";
        private static final String TASK_ID = "TaskID";
        private static final String TIME_START = "TimeStart";
        private static final String TIME_STOP = "TimeStop";
    }

    private static class STUDENT {
        private static final String TABLE = "Student";
        private static final String ID = "StudentId";
        private static final String FIRST_NAME = "FirstName";
        private static final String LAST_NAME = "LastName";
        private static final String AGE = "Age";
        private static final String YEAR = "Year";
        private static final String TEACHER_ID = "TeacherId";
        private static final String IMAGE = "StudentImage";
    }

    private static class ADMIN_ACCOUNT {
        private static final String TABLE = "adminAccount";
        private static final String ID = "accountId";
        private static final String USERNAME = "username";
        private static final String PASSWORD = "password";
        private static final String SALT = "salt";
    }

    private static final String STUDENT_DDL =
            "CREATE TABLE " + STUDENT.TABLE + "( " +
                    STUDENT.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    STUDENT.TEACHER_ID + " INTEGER, " +
                    STUDENT.FIRST_NAME + " TEXT, " +
                    STUDENT.LAST_NAME + " TEXT, " +
                    STUDENT.AGE + " INTEGER, " +
                    STUDENT.YEAR + " INTEGER, " +
                    STUDENT.IMAGE + " BLOB DEFAULT NULL, " +
                    "FOREIGN KEY(" + STUDENT.TEACHER_ID +") " +
                        "REFERENCES " + TEACHER.TABLE + "(" + TEACHER.ID + ")" +
                    ")";

    private static final String TASK_DDL =
            "CREATE TABLE " + TASK.TABLE + "( " +
                    TASK.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TASK.NAME + " TEXT, " +
                    TASK.IMAGE + " BLOB DEFAULT NULL " +
                    ")";

    private static final String TASK_PUNCH_DDL =
            "CREATE TABLE " + TASK_PUNCH.TABLE + "( " +
                    TASK_PUNCH.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TASK_PUNCH.STUDENT_ID + " INTEGER NOT NULL, " +
                    TASK_PUNCH.TASK_ID + " INTEGER NOT NULL, " +
                    TASK_PUNCH.TIME_START + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    TASK_PUNCH.TIME_STOP + " DATETIME DEFAULT NULL, " +
                    "FOREIGN KEY(" + TASK_PUNCH.STUDENT_ID + ") " +
                        "REFERENCES " + STUDENT.TABLE + "(" + STUDENT.ID + "), " +
                    "FOREIGN KEY(" + TASK_PUNCH.TASK_ID + ") " +
                        "REFERENCES " + TASK.TABLE + "(" + TASK.ID + ") " +
                    ")";

    private static final String TEACHER_DDL =
            "CREATE TABLE " + TEACHER.TABLE + "( " +
                    TEACHER.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TEACHER.FIRST_NAME + " TEXT, " +
                    TEACHER.LAST_NAME + " TEXT, " +
                    TEACHER.EMAIL + " TEXT, " +
                    TEACHER.PHONE_NUMBER + " TEXT, " +
                    TEACHER.IMAGE + " BLOB DEFAULT NULL " +
                    ")";

    private static final String ADMIN_ACCOUNT_DDL =
            "CREATE TABLE " + ADMIN_ACCOUNT.TABLE + "( " +
                    ADMIN_ACCOUNT.ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ADMIN_ACCOUNT.USERNAME + " UNIQUE TEXT NOT NULL, " +
                    ADMIN_ACCOUNT.PASSWORD + " TEXT NOT NULL, " +
                    ADMIN_ACCOUNT.SALT + " TEXT NOT NULL " +
                    ")";

    public PersistenceInteractor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TEACHER_DDL);
        sqLiteDatabase.execSQL(TASK_DDL);
        sqLiteDatabase.execSQL(STUDENT_DDL);
        sqLiteDatabase.execSQL(TASK_PUNCH_DDL);
        sqLiteDatabase.execSQL(ADMIN_ACCOUNT_DDL);
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

        db.execSQL("DROP TABLE IF EXISTS " + TASK_PUNCH.TABLE);
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
        if (!cursor.isNull(6)) {
            ByteArrayInputStream istream = new ByteArrayInputStream(cursor.getBlob(6));
            Bitmap teacherImage = BitmapFactory.decodeStream(istream);
            student.setImage(teacherImage);
        }
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
                        STUDENT.TEACHER_ID + ", " +
                        STUDENT.IMAGE +
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
                        STUDENT.TEACHER_ID + ", " +
                        STUDENT.IMAGE +
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

    public ArrayList<Student> getStudentsForTeacher(int teacherID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        STUDENT.ID + " , " +
                        STUDENT.FIRST_NAME + " , " +
                        STUDENT.LAST_NAME + " , " +
                        STUDENT.AGE + " , " +
                        STUDENT.YEAR + " , " +
                        STUDENT.TEACHER_ID + ", " +
                        STUDENT.IMAGE +
                        " FROM " + STUDENT.TABLE +
                        " WHERE " + STUDENT.TEACHER_ID + "=" + teacherID, null);

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

        if (student.getImage() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            student.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            values.put(STUDENT.IMAGE, stream.toByteArray());
        }

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
        values.put(STUDENT.TEACHER_ID, student.getTeacherId());

        if (student.getImage() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            student.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            values.put(STUDENT.IMAGE, stream.toByteArray());
        }

        int affectedRows = db.update(STUDENT.TABLE, values,
                STUDENT.ID + " = ?",
                new String[]{student.getId().toString()});

        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");
    }

    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(STUDENT.TABLE, STUDENT.ID + " = ?", new String[]{Integer.toString(studentId)});
    }

    //START OF TASK
    private Task taskFromRow(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getInt(0));
        task.setName(cursor.getString(1));
        if (!cursor.isNull(2)) {
            ByteArrayInputStream istream = new ByteArrayInputStream(cursor.getBlob(2));
            Bitmap taskImage = BitmapFactory.decodeStream(istream);
            task.setImage(taskImage);
        }
        return task;
    }

    public Task getTask(int id) {
        Task task = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        TASK.ID + ", " +
                        TASK.NAME + ", " +
                        TASK.IMAGE +
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
                        TASK.ID + ", " +
                        TASK.NAME + ", " +
                        TASK.IMAGE +
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
        if (task.getImage() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            task.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            values.put(TASK.IMAGE, stream.toByteArray());
        }

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
        if (task.getImage() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            task.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            values.put(TASK.IMAGE, stream.toByteArray());
        }

        int affectedRows = db.update(TASK.TABLE, values,
                TASK.ID + " = ?",
                new String[]{task.getId().toString()});

        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK.TABLE, TASK.ID + " = ?", new String[]{Integer.toString(taskId)});
    }

    //END OF TASK

    private TaskPunch taskPunchFromRow(Cursor cursor) {
        TaskPunch taskPunch = new TaskPunch();
        taskPunch.setId(cursor.getInt(0));
        taskPunch.setStudentId(cursor.getInt(1));
        taskPunch.setTaskId(cursor.getInt(2));
        taskPunch.setTimeStart(new Date(cursor.getLong(3) * 1000L));
        if (!cursor.isNull(4))
            taskPunch.setTimeEnd(new Date(cursor.getLong(4) * 1000L));
        return taskPunch;
    }

    public TaskPunch getTaskPunch(int id) {
        TaskPunch taskPunch = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        TASK_PUNCH.ID + ", " +
                        TASK_PUNCH.STUDENT_ID + ", " +
                        TASK_PUNCH.TASK_ID + ", " +
                        TASK_PUNCH.TIME_START + ", " +
                        TASK_PUNCH.TIME_STOP + " " +
                        " FROM " + TASK_PUNCH.TABLE +
                        " WHERE " + TASK_PUNCH.ID + "=" + id, null);

        if (cursor.moveToFirst())
            taskPunch = taskPunchFromRow(cursor);

        cursor.close();

        return taskPunch;
    }

    public int addTaskPunch(TaskPunch taskPunch) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TASK_PUNCH.STUDENT_ID, taskPunch.getStudentId());
        values.put(TASK_PUNCH.TASK_ID, taskPunch.getTaskId());
        values.put(TASK_PUNCH.TIME_START, taskPunch.getTimeStart().getTime() / 1000L);
        if (taskPunch.getTimeEnd() != null)
            values.put(TASK_PUNCH.TIME_STOP, taskPunch.getTimeEnd().getTime() / 1000L);

        long rowId = db.insertOrThrow(TASK_PUNCH.TABLE, null, values);

        // Abort on failed insert
        if (rowId == -1)
            return -1;

        // Get The New Punch And Return Its ID
        Cursor cursor = db.rawQuery(
                "SELECT " + TASK_PUNCH.ID + " FROM " + TASK_PUNCH.TABLE +
                        " WHERE ROWID = " + rowId , null);

        int punchId = -1;
        if (cursor.moveToFirst())
            punchId = cursor.getInt(0);
        cursor.close();

        return punchId;
    }

    public void update(TaskPunch taskPunch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TASK_PUNCH.STUDENT_ID, taskPunch.getStudentId());
        values.put(TASK_PUNCH.TASK_ID, taskPunch.getTaskId());
        values.put(TASK_PUNCH.TIME_START, taskPunch.getTimeStart().getTime()/1000L);
        if (taskPunch.getTimeEnd() != null)
            values.put(TASK_PUNCH.TIME_STOP, taskPunch.getTimeEnd().getTime()/1000L);

        int affectedRows = db.update(TASK_PUNCH.TABLE, values,
                TASK_PUNCH.ID + "= ?", new String[]{taskPunch.getId().toString()});

        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");
    }

    public void deleteTaskPunch(int taskPunchId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK_PUNCH.TABLE, TASK_PUNCH.ID + " = ?", new String[]{Integer.toString(taskPunchId)});
    }

    public ArrayList<TaskPunch> getAllTaskPunches() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                    TASK_PUNCH.ID + ", " +
                    TASK_PUNCH.STUDENT_ID + ", " +
                    TASK_PUNCH.TASK_ID + ", " +
                    TASK_PUNCH.TIME_START + ", " +
                    TASK_PUNCH.TIME_STOP +
                    " FROM " + TASK_PUNCH.TABLE, null);

        ArrayList<TaskPunch> taskPunches = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                taskPunches.add(taskPunchFromRow(cursor));
            }while (cursor.moveToNext());
        }

        cursor.close();

        return taskPunches;
    }

    /**
     * Finds An Open Punch For A Given Student
     * @param studentId
     * Id of The Student To Search Punches For
     * @return
     * TaskPunch Model if An Open Punch Was Found, null Otherwise
     */
    public TaskPunch getOpenPunch(int studentId) {
        TaskPunch taskPunch = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        TASK_PUNCH.ID + ", " +
                        TASK_PUNCH.STUDENT_ID + ", " +
                        TASK_PUNCH.TASK_ID + ", " +
                        TASK_PUNCH.TIME_START + ", " +
                        TASK_PUNCH.TIME_STOP + " " +
                        " FROM " + TASK_PUNCH.TABLE +
                        " WHERE " + TASK_PUNCH.STUDENT_ID + "=" + studentId +
                        " AND " + TASK_PUNCH.TIME_STOP + " IS NULL" , null);
        if (cursor.moveToFirst())
            taskPunch = taskPunchFromRow(cursor);
        cursor.close();
        return taskPunch;
    }

    private Teacher teacherFromRow(Cursor cursor) {
        Teacher teacher = new Teacher();

        teacher.setId(cursor.getInt(0));
        teacher.setFirstName(cursor.getString(1));
        teacher.setLastName(cursor.getString(2));
        teacher.setEmail(cursor.getString(3));
        teacher.setPhoneNumber(cursor.getString(4));
        if (!cursor.isNull(5)) {
            ByteArrayInputStream istream = new ByteArrayInputStream(cursor.getBlob(5));
            Bitmap teacherImage = BitmapFactory.decodeStream(istream);
            teacher.setImage(teacherImage);
        }

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
                        TEACHER.PHONE_NUMBER + " , " +
                        TEACHER.IMAGE +
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
                        TEACHER.PHONE_NUMBER + " , " +
                        TEACHER.IMAGE +
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

    public int addTeacher(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TEACHER.FIRST_NAME, teacher.getFirstName());
        values.put(TEACHER.LAST_NAME, teacher.getLastName());
        values.put(TEACHER.EMAIL, teacher.getEmail());
        values.put(TEACHER.PHONE_NUMBER, teacher.getPhoneNumber());

        // Convert Image To Byte Array For Persistence
        if (teacher.getImage() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            teacher.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            values.put(TEACHER.IMAGE, stream.toByteArray());
        }

        long rowId = db.insertOrThrow(TEACHER.TABLE, null, values);

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
        if (teacher.getImage() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            teacher.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            values.put(TEACHER.IMAGE, stream.toByteArray());
        }

        int affectedRows = db.update(TEACHER.TABLE, values,
                TEACHER.ID + " = ?", new String[]{teacher.getId().toString()});

        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");
    }

    public void deleteTeacher(int teacherId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TEACHER.TABLE, TEACHER.ID + " = ?", new String[]{Integer.toString(teacherId)});
    }

    private AdminAccount adminAccountFromRow(Cursor cursor) {
        AdminAccount adminAccount = new AdminAccount();

        adminAccount.setId(cursor.getInt(0));
        adminAccount.setUsername(cursor.getString(1));
        try {
            adminAccount.setPassword(new String(cursor.getBlob(2), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            adminAccount.setPassword("");
        }
        adminAccount.setSalt(cursor.getString(3));

        return adminAccount;
    }

    private static final String ADMIN_ACCOUNT_QUERY = "SELECT " +
            ADMIN_ACCOUNT.ID + " , " +
            ADMIN_ACCOUNT.USERNAME + " , " +
            ADMIN_ACCOUNT.PASSWORD + " , " +
            ADMIN_ACCOUNT.SALT + " " +
            " FROM " + ADMIN_ACCOUNT.TABLE + " ";


    public AdminAccount getAdminAccount(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                ADMIN_ACCOUNT_QUERY + " WHERE " + ADMIN_ACCOUNT.ID + "=" + id, null);

        AdminAccount adminAccount = null;
        if (cursor.moveToFirst()) {
            adminAccount = adminAccountFromRow(cursor);
        }
        cursor.close();

        return adminAccount;
    }

    public AdminAccount getAdminAccount(String username) {
        if (username == null)
            throw new IllegalArgumentException("Username May Not Be null");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                ADMIN_ACCOUNT_QUERY + " WHERE " + ADMIN_ACCOUNT.USERNAME + "=" + username, null);

        AdminAccount adminAccount = null;
        if (cursor.moveToFirst()) {
            adminAccount = adminAccountFromRow(cursor);
        }

        cursor.close();
        return adminAccount;
    }

    public ArrayList<AdminAccount> getAllAdminAccounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(ADMIN_ACCOUNT_QUERY, null);

        ArrayList<AdminAccount> adminAccounts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                adminAccounts.add(adminAccountFromRow(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return adminAccounts;
    }

    public int createAdminAccount(AdminAccount adminAccount) {
        // TODO EPB: Implement
        return -1;
    }
}
