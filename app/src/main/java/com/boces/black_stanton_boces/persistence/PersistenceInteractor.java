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
import com.boces.black_stanton_boces.report.StudentPunches;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class PersistenceInteractor extends SQLiteOpenHelper {

    /**
     * Semantic Version of The Database
     * Increments When The Schema Changes
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Schema Name
     */
    private static final String DATABASE_NAME = "boces";

    /**
     * Debug Tag
     */
    private static final String TAG = "bocesPersistance";

    /**
     * All Fields/Table Associated With Teachers
     */
    private static class TEACHER {
        /**
         * Table Teachers Are Stored In
         */
        private static final String TABLE = "Teacher";
        private static final String ID = "TeacherId";
        private static final String FIRST_NAME = "FirstName";
        private static final String LAST_NAME = "LastName";
        private static final String EMAIL = "Email";
        private static final String PHONE_NUMBER = "PhoneNumber";
        private static final String IMAGE = "TeacherImage";
    }

    /**
     * All Fields/Table Associated With Tasks
     */
    private static class TASK {
        /**
         * Table Tasks Are Stored In
         */
        private static final String TABLE = "Task";
        private static final String ID = "TaskId";
        private static final String NAME = "TaskName";
        private static final String IMAGE = "TaskImage";
    }

    /**
     * All Fields/Table Associated With Punches
     */
    private static class TASK_PUNCH {
        /**
         * Table Punches Are Stored In
         */
        private static final String TABLE = "TaskPunch";
        private static final String ID = "TaskPunchID";
        private static final String STUDENT_ID = "StudentID";
        private static final String TASK_ID = "TaskID";
        private static final String TIME_START = "TimeStart";
        private static final String TIME_STOP = "TimeStop";
    }

    /**
     * All Fields/Table Associated With Students
     */
    private static class STUDENT {
        /**
         * Table Students Are Stored In
         */
        private static final String TABLE = "Student";
        private static final String ID = "StudentId";
        private static final String FIRST_NAME = "FirstName";
        private static final String LAST_NAME = "LastName";
        private static final String AGE = "Age";
        private static final String YEAR = "Year";
        private static final String TEACHER_ID = "TeacherId";
        private static final String IMAGE = "StudentImage";
    }

    /**
     * All Fields/Table Associated With Admin Accounts
     */
    private static class ADMIN_ACCOUNT {
        /**
         * Table Accounts Are Stored In
         */
        private static final String TABLE = "adminAccount";
        private static final String ID = "accountId";
        private static final String USERNAME = "username";
        private static final String PASSWORD = "password";
    }

    /**
     * DDL of The Student Table
     */
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
                        " ON DELETE SET NULL " +
                    ")";

    /**
     * DDL of The Task Table
     */
    private static final String TASK_DDL =
            "CREATE TABLE " + TASK.TABLE + "( " +
                    TASK.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TASK.NAME + " TEXT, " +
                    TASK.IMAGE + " BLOB DEFAULT NULL " +
                    ")";

    /**
     * DDL of The Punch Table
     */
    private static final String TASK_PUNCH_DDL =
            "CREATE TABLE " + TASK_PUNCH.TABLE + "( " +
                    TASK_PUNCH.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TASK_PUNCH.STUDENT_ID + " INTEGER NOT NULL, " +
                    TASK_PUNCH.TASK_ID + " INTEGER NOT NULL, " +
                    TASK_PUNCH.TIME_START + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    TASK_PUNCH.TIME_STOP + " DATETIME DEFAULT NULL, " +
                    "FOREIGN KEY(" + TASK_PUNCH.STUDENT_ID + ") " +
                        "REFERENCES " + STUDENT.TABLE + "(" + STUDENT.ID + ")" +
                            "ON DELETE CASCADE ," +
                    "FOREIGN KEY(" + TASK_PUNCH.TASK_ID + ") " +
                        "REFERENCES " + TASK.TABLE + "(" + TASK.ID + ") " +
                            "ON DELETE CASCADE " +
                    ")";

    /**
     * DDL of The Teacher Table
     */
    private static final String TEACHER_DDL =
            "CREATE TABLE " + TEACHER.TABLE + "( " +
                    TEACHER.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TEACHER.FIRST_NAME + " TEXT, " +
                    TEACHER.LAST_NAME + " TEXT, " +
                    TEACHER.EMAIL + " TEXT, " +
                    TEACHER.PHONE_NUMBER + " TEXT, " +
                    TEACHER.IMAGE + " BLOB DEFAULT NULL " +
                    ")";

    /**
     * DDL of The Account Table
     */
    private static final String ADMIN_ACCOUNT_DDL =
            "CREATE TABLE " + ADMIN_ACCOUNT.TABLE + "( " +
                    ADMIN_ACCOUNT.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ADMIN_ACCOUNT.USERNAME + " TEXT UNIQUE NOT NULL, " +
                    ADMIN_ACCOUNT.PASSWORD + " TEXT NOT NULL " +
                    ")";

    /**
     * Default Constructor
     * @param context
     * Application/Current Context
     */
    public PersistenceInteractor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called When A Database Is Needed, And Has Not Yet Been Created
     * Creates Tables & Does Initial Inserts
     * @param sqLiteDatabase
     * A Writable Database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TEACHER_DDL);
        sqLiteDatabase.execSQL(TASK_DDL);
        sqLiteDatabase.execSQL(STUDENT_DDL);
        sqLiteDatabase.execSQL(TASK_PUNCH_DDL);
        sqLiteDatabase.execSQL(ADMIN_ACCOUNT_DDL);
        createInitialData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        emptyAndRecreate();
    }

    /**
     * Enables Foreign Keys Along With Default Behavior
     * @param db
     * The Database
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Make Sure The Reference Is Not Readonly
        if (!db.isReadOnly())
            db.execSQL("PRAGMA foreign_keys = 1;");
    }

    /**
     * Drop All Tables & Recreate Them With Initial Data
     */
    public void emptyAndRecreate() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TASK_PUNCH.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TASK.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEACHER.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ADMIN_ACCOUNT.TABLE);

        this.onCreate(db);
    }

    /**
     * Completely Drops The Database
     * May Require An App Restart To Take Effect
     *
     * @param context
     * Application/Current Context
     */
    public void dropDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    /**
     * Inserts The Initial Setup Data
     * May Be Called After Tables Are Created
     * @param writable
     * A Writable Database Reference
     */
    private void createInitialData(SQLiteDatabase writable) {
        writable.beginTransaction();

        try {
            // Create Initial Admin Account
            {
                ContentValues values = new ContentValues();
                values.put(ADMIN_ACCOUNT.USERNAME, "admin");
                values.put(ADMIN_ACCOUNT.PASSWORD, BCrypt.hashpw("a", BCrypt.gensalt()));
                writable.insertOrThrow(ADMIN_ACCOUNT.TABLE, null, values);
            }

            // Last Thing That Should Be Run
            // Marks All Operations As Successful
            writable.setTransactionSuccessful();
        } catch (Exception e) { // Log Failures
            Log.e(TAG, "Error Creating Initial Data. Message: " + e.getMessage());
        } finally { // In Anything Fails, Rollback
            writable.endTransaction();
        }
    }

    /**
     * Base Query To Select Students
     */
    private final String STUDENT_QUERY = "SELECT " +
            STUDENT.ID + " , " +
            STUDENT.FIRST_NAME + " , " +
            STUDENT.LAST_NAME + " , " +
            STUDENT.AGE + " , " +
            STUDENT.YEAR + " , " +
            STUDENT.TEACHER_ID + ", " +
            STUDENT.IMAGE +
            " FROM " + STUDENT.TABLE;

    /**
     * Converts A Result Row To A Student Model
     *
     * @param cursor
     * A Cursor On A Student Row Having Results In Order the Base Query
     *
     * @return
     * A Model With All Previously Saved Info Populated
     */
    private Student studentFromRow(Cursor cursor) {
        Student student = new Student();
        student.setId(cursor.getInt(0));
        student.setFirstName(cursor.getString(1));
        student.setLastName(cursor.getString(2));
        student.setAge(cursor.getInt(3));
        student.setYear(cursor.getInt(4));

        // Handle Optional Fields
        if (cursor.isNull(5))
            student.setTeacherId(null);
        else
            student.setTeacherId(cursor.getInt(5));

        // Convert Image
        if (!cursor.isNull(6)) {
            ByteArrayInputStream istream = new ByteArrayInputStream(cursor.getBlob(6));
            Bitmap teacherImage = BitmapFactory.decodeStream(istream);
            student.setImage(teacherImage);
        }
        return student;
    }

    /**
     * Retrieves A Student By ID
     *
     * @param id
     * The ID of The Student To Retrieve
     *
     * @return
     * A Filled In Model Object, or
     * Null if The Student Was Not Found
     */
    public Student getStudent(int id) {
        Student student = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( STUDENT_QUERY +" WHERE " + STUDENT.ID + "=" +id, null);

        if (cursor.moveToFirst()) {
            student = studentFromRow(cursor);
        }
        cursor.close();
        return student;
    }

    /**
     * Retrieves All Saved Students
     *
     * @return
     * An ArrayList of All Students
     * An Empty List If There Are None
     */
    public ArrayList<Student> getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(STUDENT_QUERY, null);

        ArrayList<Student> students = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                students.add(studentFromRow(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return students;
    }

    /**
     * Retrieves All Students Associated With A Teacher
     *
     * @param teacherID
     * The ID of The Teacher
     *
     * @return
     * An ArrayList of That Teachers Students
     * An Empty List If The Teacher Does Not Exist or
     * No Students Were Found
     */
    public ArrayList<Student> getStudentsForTeacher(int teacherID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(STUDENT_QUERY + " WHERE " + STUDENT.TEACHER_ID + "=" + teacherID, null);

        ArrayList<Student> students = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                students.add(studentFromRow(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return students;
    }

    /**
     * Adds A New Student
     *
     * @param student
     * A Filled In Student Model
     * Image & TeacherId May Be Null
     *
     * @return
     * The ID of The New Student
     * -1 If The Insert Failed
     */
    public int addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STUDENT.FIRST_NAME, student.getFirstName());
        values.put(STUDENT.LAST_NAME, student.getLastName());
        values.put(STUDENT.AGE, student.getAge());
        values.put(STUDENT.YEAR, student.getYear());

        if (student.getTeacherId() == null)
            values.putNull(STUDENT.TEACHER_ID);
        else
            values.put(STUDENT.TEACHER_ID, student.getTeacherId());

        // Encode Image
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

    /**
     * Updates A Student's Fields
     * All Fields Except For ID May Be Updated
     *
     * @param student
     * A Student Model With Updated Fields
     * Image & TeacherId May Be Null
     */
    public void update(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STUDENT.FIRST_NAME, student.getFirstName());
        values.put(STUDENT.LAST_NAME, student.getLastName());
        values.put(STUDENT.AGE, student.getAge());
        values.put(STUDENT.YEAR, student.getYear());

        // Handle Optional ID
        if (student.getTeacherId() == null)
            values.putNull(STUDENT.TEACHER_ID);
        else
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

    /**
     * Removes A Student
     *
     * @param studentId
     * The ID of The Student
     */
    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(STUDENT.TABLE, STUDENT.ID + " = ?", new String[]{Integer.toString(studentId)});
    }

    /**
     * Base Query For Selecting A Task
     */
    private static final String TASK_QUERY = "SELECT " +
            TASK.ID + ", " +
            TASK.NAME + ", " +
            TASK.IMAGE +
            " FROM " + TASK.TABLE;

    /**
     * Converts A Result Row To A Task Model
     *
     * @param cursor
     * A Cursor On A Task Row Having Results In Order the Base Query
     *
     * @return
     * A Model With All Previously Saved Info Populated
     */
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

    /**
     * Retrieves A Task By ID
     *
     * @param id
     * The ID of The Task To Retrieve
     *
     * @return
     * A Filled In Model Object, or
     * Null if The Task Was Not Found
     */
    public Task getTask(int id) {
        Task task = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( TASK_QUERY + " WHERE " + TASK.ID + "=" +id, null);

        if (cursor.moveToFirst()) {
            task = taskFromRow(cursor);
        }
        cursor.close();
        return task;
    }

    /**
     * Retrieves All Saved Tasks
     *
     * @return
     * An ArrayList of All Tasks
     * An Empty List If There Are None
     */
    public ArrayList<Task> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(TASK_QUERY,null);

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

    /**
     * Updates A Task's Fields
     * All Fields Except For ID May Be Updated
     *
     * @param task
     * A Task Model With Updated Fields
     * Image May Be Null
     */
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

    /**
     * Removes A Task
     *
     * @param taskId
     * The ID of The Task
     */
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK.TABLE, TASK.ID + " = ?", new String[]{Integer.toString(taskId)});
    }

    /**
     * Base Query For Selecting A Punch
     */
    private static final String TASK_PUNCH_QUERY =
            "SELECT " +
            TASK_PUNCH.ID + ", " +
            TASK_PUNCH.STUDENT_ID + ", " +
            TASK_PUNCH.TASK_ID + ", " +
            TASK_PUNCH.TIME_START + ", " +
            TASK_PUNCH.TIME_STOP +
            " FROM " + TASK_PUNCH.TABLE;

    /**
     * Converts A Result Row To A TaskPunch Model
     *
     * @param cursor
     * A Cursor On A TaskPunch Row Having Results In Order the Base Query
     *
     * @return
     * A Model With All Previously Saved Info Populated
     * TimeEnd May Be null
     */
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

    /**
     * Retrieves A Punch By ID
     *
     * @param id
     * The ID of The Punch To Retrieve
     *
     * @return
     * A Filled In Model Object, or
     * Null if The Punch Was Not Found
     */
    public TaskPunch getTaskPunch(int id) {
        TaskPunch taskPunch = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(TASK_PUNCH_QUERY + " WHERE " + TASK_PUNCH.ID + "=" + id, null);

        if (cursor.moveToFirst())
            taskPunch = taskPunchFromRow(cursor);

        cursor.close();

        return taskPunch;
    }

    /**
     * Adds A New Punch
     *
     * @param taskPunch
     * A Filled In TaskPunch Model
     * timeEnd May Be Null
     *
     * @return
     * The ID of The New Punch
     * -1 If The Insert Failed
     */
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

    /**
     * Updates A Punch Fields
     * All Fields Except For ID May Be Updated
     *
     * @param taskPunch
     * A Punch Model With Updated Fields
     * timeEnd May Be Null
     */
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

    /**
     * Removes A Punch
     *
     * @param taskPunchId
     * The ID of The Punch
     */
    public void deleteTaskPunch(int taskPunchId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK_PUNCH.TABLE, TASK_PUNCH.ID + " = ?", new String[]{Integer.toString(taskPunchId)});
    }

    /**
     * Retrieves All Saved Punches
     *
     * @return
     * An ArrayList of All Punches
     * An Empty List If There Are None
     */
    public ArrayList<TaskPunch> getAllTaskPunches() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(TASK_PUNCH_QUERY, null);

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
     * Retrieves All Saved Punches Associated With A Student
     *
     * @param studentId
     * The Id Of The Student To Search Punches For
     *
     * @return
     * An ArrayList of The Student's Punches
     * An Empty List If There Are None
     */
    public ArrayList<TaskPunch> getTaskPunchesForStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                TASK_PUNCH_QUERY + " WHERE " + STUDENT.ID + "=?",
                new String[]{Integer.toString(studentId)});

        ArrayList<TaskPunch> taskPunches = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                taskPunches.add(taskPunchFromRow(cursor));
            } while (cursor.moveToNext());
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
                TASK_PUNCH_QUERY +
                        " WHERE " + TASK_PUNCH.STUDENT_ID + "=" + studentId +
                        " AND " + TASK_PUNCH.TIME_STOP + " IS NULL" , null);
        if (cursor.moveToFirst())
            taskPunch = taskPunchFromRow(cursor);
        cursor.close();
        return taskPunch;
    }

    /**
     * Retrieves All Saved Punches Within A Given Range
     *
     * @param startDate
     * Date To Start Searching From, Exclusive.
     * May Not Be null
     *
     * @param endDate
     * Date To Stop The Search On, Exclusive.
     * May Not Be Null
     *
     * @return
     * An ArrayList of All Punches
     * An Empty List If There Are None
     */
    public ArrayList<StudentPunches> getStudentPunches(Date startDate, Date endDate) {
        long startSeconds = startDate.getTime()/1000L;
        long endSeconds = endDate.getTime()/1000L;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(TASK_PUNCH_QUERY +
                " WHERE " + TASK_PUNCH.TIME_START + ">" + Long.toString(startSeconds) +
                    " AND (" + TASK_PUNCH.TIME_STOP + " IS NULL OR " + TASK_PUNCH.TIME_STOP +"<" + Long.toString(endSeconds) +")" +
                " ORDER BY " + TASK_PUNCH.STUDENT_ID + ", " + TASK_PUNCH.TIME_START, null);

        ArrayList<StudentPunches> studentPunches = new ArrayList<>();
        if (cursor.moveToFirst()) {
            StudentPunches currentStudentPunches = new StudentPunches();
            currentStudentPunches.setStudent(getStudent(cursor.getInt(1)));

            do {
                if (currentStudentPunches.getStudent().getId() != cursor.getInt(1)) {
                    // Add Previous Student Punches And Start The Next Set
                    studentPunches.add(currentStudentPunches);
                    currentStudentPunches = new StudentPunches();
                    currentStudentPunches.setStudent(getStudent(cursor.getInt(1)));
                }

                currentStudentPunches.getPunches().add(taskPunchFromRow(cursor));

            } while (cursor.moveToNext());

            // Add Last StudentPunches
            studentPunches.add(currentStudentPunches);
        }
        cursor.close();
        return studentPunches;
    }

    /**
     * Converts A Result Row To A Teacher Model
     *
     * @param cursor
     * A Cursor On A Teacher Row Having Results In Order the Base Query
     *
     * @return
     * A Model With All Previously Saved Info Populated
     */
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

    /**
     * Base Query For Selecting A Teacher
     */
    private static final String TEACHER_QUERY = "SELECT " +
            TEACHER.ID + " , " +
            TEACHER.FIRST_NAME + " , " +
            TEACHER.LAST_NAME + " , " +
            TEACHER.EMAIL + " , " +
            TEACHER.PHONE_NUMBER + " , " +
            TEACHER.IMAGE +
            " FROM " + TEACHER.TABLE;

    /**
     * Retrieves A Teacher By ID
     *
     * @param id
     * The ID of The Teacher To Retrieve
     *
     * @return
     * A Filled In Model Object, or
     * Null if The Teacher Was Not Found
     */
    public Teacher getTeacher(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(TEACHER_QUERY + " WHERE " + TEACHER.ID + "=" + id
                , null);


        Teacher teacher = null;
        if (cursor.moveToFirst()) {
            teacher = teacherFromRow(cursor);
        }

        cursor.close();
        return teacher;
    }

    /**
     * Retrieves All Saved Teachers
     *
     * @return
     * An ArrayList of All Teachers
     * An Empty List If There Are None
     */
    public ArrayList<Teacher> getAllTeachers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(TEACHER_QUERY, null);

        ArrayList<Teacher> teachers = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                teachers.add(teacherFromRow(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return teachers;
    }

    /**
     * Adds A New Teacher
     *
     * @param teacher
     * A Filled In Teacher Model
     * image May Be Null
     *
     * @return
     * The ID of The New Teacher
     * -1 If The Insert Failed
     */
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

    /**
     * Updates A Teacher's Fields
     * All Fields Except For ID May Be Updated
     *
     * @param teacher
     * A Teacher Model With Updated Fields
     * Image May Be Null
     */
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

    /**
     * Removes A Teacher
     *
     * @param teacherId
     * The ID of The Teacher To Remove
     */
    public void deleteTeacher(int teacherId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TEACHER.TABLE, TEACHER.ID + " = ?", new String[]{Integer.toString(teacherId)});
    }

    /**
     * Base Query For Selecting An Admin Account
     */
    private static final String ADMIN_ACCOUNT_QUERY = "SELECT " +
            ADMIN_ACCOUNT.ID + " , " +
            ADMIN_ACCOUNT.USERNAME + " , " +
            ADMIN_ACCOUNT.PASSWORD + " " +
            " FROM " + ADMIN_ACCOUNT.TABLE + " ";


    /**
     * Converts A Result Row To A Admin Account Model
     *
     * @param cursor
     * A Cursor On An Account Row Having Results In Order the Base Query
     *
     * @return
     * A Model With All Previously Saved Info Populated
     */
    private AdminAccount adminAccountFromRow(Cursor cursor) {
        AdminAccount adminAccount = new AdminAccount();

        adminAccount.setId(cursor.getInt(0));
        adminAccount.setUsername(cursor.getString(1));
        adminAccount.setPassword(cursor.getString(2));

        return adminAccount;
    }

    /**
     * Retrieves An Account By ID
     *
     * @param id
     * The ID of The Account To Retrieve
     *
     * @return
     * A Filled In Model Object, or
     * Null if The Account Was Not Found
     */
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

    /**
     * Retrieves An Account By Username
     *
     * @param username
     * The Username of The Account To Retrieve
     *
     * @return
     * A Filled In Model Object, or
     * Null if The Account Was Not Found
     */
    public AdminAccount getAdminAccount(String username) {
        if (username == null)
            throw new IllegalArgumentException("Username May Not Be null");
        if (username.isEmpty())
            return null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                ADMIN_ACCOUNT_QUERY + " WHERE " + ADMIN_ACCOUNT.USERNAME + "= ?", new String[]{username});

        AdminAccount adminAccount = null;
        if (cursor.moveToFirst()) {
            adminAccount = adminAccountFromRow(cursor);
        }

        cursor.close();
        return adminAccount;
    }

    /**
     * Retrieves All Saved Accounts
     *
     * @return
     * An ArrayList of All Accounts
     * An Empty List If There Are None
     */
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

    /**
     * Creates A New Account
     *
     * @param username
     * The Username of The New Account
     *
     * @param plaintextPassword
     * The Password of The New Account In PlainText
     *
     * @return
     * The ID of The New Account
     * -1 If The Insert Failed
     */
    public int createAdminAccount(String username, String plaintextPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        final String hashedPassword = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());

        values.put(ADMIN_ACCOUNT.USERNAME, username);
        values.put(ADMIN_ACCOUNT.PASSWORD, hashedPassword);

        long rowId = db.insertOrThrow(ADMIN_ACCOUNT.TABLE, null, values);

        // Abort on failed insert
        if (rowId == -1)
            return -1;

        Cursor cursor = db.rawQuery(
                "SELECT " + ADMIN_ACCOUNT.ID + " FROM " + ADMIN_ACCOUNT.TABLE + " WHERE ROWID=" + rowId, null
        );

        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();

        return id;
    }

    /**
     * Updates ONLY The Username of An Existing Account
     *
     * @param adminAccount
     * A Model With An ID And A Username
     */
    public void updateUsername(AdminAccount adminAccount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (adminAccount.getUsername() != null)
            values.put(ADMIN_ACCOUNT.USERNAME, adminAccount.getUsername());
        else
            throw new IllegalArgumentException("Username Must Not Be Null");


        int affectedRows = db.update(ADMIN_ACCOUNT.TABLE, values,
                ADMIN_ACCOUNT.ID + " = ?", new String[]{adminAccount.getId().toString()});


        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");

    }

    /**
     * Updates ONLY The Password Of An Account
     *
     * @param id
     * The ID of The Account To Update
     *
     * @param newPlaintextPassword
     * The New Password of The Account In PlainText
     */
    public void updateAdminPassword(int id, String newPlaintextPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        final String hashedPassword = BCrypt.hashpw(newPlaintextPassword, BCrypt.gensalt());

        values.put(ADMIN_ACCOUNT.PASSWORD, hashedPassword);
        int affectedRows = db.update(ADMIN_ACCOUNT.TABLE, values,
                ADMIN_ACCOUNT.ID + " = ?", new String[]{Integer.toString(id)});

        if (affectedRows < 1)
            Log.w(TAG, "Update Affected No Rows");
    }

    /**
     * Verifies If A PlainText Password Matches Its Hashed Counterpart
     *
     * @param plaintext
     * The PlainText Version of The Password
     *
     * @param hashed
     * The Hashed Password From The Database
     *
     * @return
     * True If The Password Matches,
     * False Otherwise
     */
    public boolean checkPassword(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    /**
     * Remove An Account
     *
     * @param id
     * The Id of The Account To Remove
     */
    public void deleteAdminAccount(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ADMIN_ACCOUNT.TABLE, ADMIN_ACCOUNT.ID + " = ?", new String[]{Integer.toString(id)});
    }
}
