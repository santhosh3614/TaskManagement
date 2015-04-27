package com.elixir.taskmanagement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhoshkumar on 25/4/15.
 */
public class TaskManagmentDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "taskmanager.db";
    private static final int DB_VERSION = 1;

    static interface ManagmentTableInfo {
        String TABLENAME = Empolyee.class.getSimpleName();
        String COLUMN_ID = "id";
        String COLUMN_NAME = "name";
        String COLUMN_PASSWORD = "pwd";
        String COLUMN_EMAILID = "emailid";
        String COLUMN_PHNO = "phno";
        String COLUMN_EMP_TYPE = "type";
    }

    static interface ProjectTableInfo {
        String TABLE_NAME = "projects";
        String COLUMN_PRO_ID = "id";
        String COLUMN_PRO_NAME = "name";
        String COLUMN_START_TIME = "starttime";
        String COLUMN_END_TIME = "endtime";
        String COLUMN_MANGER_ID = "mngr_id";
    }

    static interface TaskTableInfo {
        String TABLE_NAME = "Tasks";
        String COLUMN_TASK_ID = "id";
        String COLUMN_TASK_NAME = "name";
        String COLUMN_START_TIME = "starttime";
        String COLUMN_STATUS = "status";
        String COLUMN_ENDTIME = "endtime";
        String COLUMN_PRO_ID = "pro_id";
        String COLUMN_DEV_ID = "dev_id";
    }

    private static final String MANAGMENT_TABLE = "create table if not exists " + ManagmentTableInfo.TABLENAME + "(" + ManagmentTableInfo.COLUMN_ID + " integer " +
            "primary key autoincrement," + ManagmentTableInfo.COLUMN_NAME + " text not null," +
            ManagmentTableInfo.COLUMN_PASSWORD + " text not null," + ManagmentTableInfo.COLUMN_EMAILID + " text unique not null,"
            + ManagmentTableInfo.COLUMN_PHNO + " text not null," + ManagmentTableInfo.COLUMN_EMP_TYPE + " integer not null)";

    private static final String PROJECT_TABLE = "create table if not exists " + ProjectTableInfo.TABLE_NAME + "("
            + ProjectTableInfo.COLUMN_PRO_ID + " integer " +
            "primary key autoincrement," + ProjectTableInfo.COLUMN_PRO_NAME + " text not null," + ProjectTableInfo.COLUMN_START_TIME + " Integer not null," + ProjectTableInfo.COLUMN_END_TIME
            + " Integer not null," + ProjectTableInfo.COLUMN_MANGER_ID + " integer)";

    private static final String TASKSTABLE = "create table if not exists " + TaskTableInfo.TABLE_NAME + "(" + TaskTableInfo.COLUMN_TASK_ID + " integer " +
            "primary key autoincrement," + TaskTableInfo.COLUMN_TASK_NAME + " text not null," + TaskTableInfo.COLUMN_START_TIME + " Integer not null,"
            + TaskTableInfo.COLUMN_STATUS + " integer not null," +
            TaskTableInfo.COLUMN_ENDTIME
            + " Integer not null," + TaskTableInfo.COLUMN_PRO_ID + " integer not null," + TaskTableInfo.COLUMN_DEV_ID + " integer not null)";


    private TaskManagmentDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static TaskManagmentDb taskManagmentDb;

    public synchronized static TaskManagmentDb getInstance(Context context){
        if(taskManagmentDb==null){
            taskManagmentDb=new TaskManagmentDb(context);
        }
        return taskManagmentDb;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MANAGMENT_TABLE);
        db.execSQL(PROJECT_TABLE);
        db.execSQL(TASKSTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<Empolyee> getAllEmployee(Empolyee.Type type) {
        Cursor cursor = getReadableDatabase().query(ManagmentTableInfo.TABLENAME, null,
                ManagmentTableInfo.COLUMN_EMP_TYPE + "=?", new String[]{String.valueOf(type.ordinal())},
                null, null, ManagmentTableInfo.COLUMN_ID);
        List<Empolyee> projectManagers = new ArrayList<>();
        while (cursor.moveToNext()) {
            projectManagers.add((ProjectManager) readEmployee(cursor, type));
        }
        cursor.close();
        return projectManagers;
    }

    public Empolyee readEmployee(Cursor cursor, Empolyee.Type type) {
        long id = cursor.getLong(cursor.getColumnIndex(ManagmentTableInfo.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(ManagmentTableInfo.COLUMN_NAME));
        String emailId = cursor.getString(cursor.getColumnIndex(ManagmentTableInfo.COLUMN_EMAILID));
        String phNo = cursor.getString(cursor.getColumnIndex(ManagmentTableInfo.COLUMN_PHNO));
        if (type == Empolyee.Type.ADMIN) {
            return new Admin(id, name, phNo, emailId);
        } else if (type == Empolyee.Type.DEVELOPER) {
            return new Developer(id, name, phNo, emailId, null);
        }
        return new ProjectManager(id, name, phNo, emailId, null);
    }

    public List<Task> getTasks(Developer developer) {
        String selectArg = developer != null ? TaskTableInfo.COLUMN_DEV_ID + "=" + developer.getId() : null;
        Cursor cursor = getReadableDatabase().query(TaskTableInfo.TABLE_NAME, null, selectArg, null,
                null, null, TaskTableInfo.COLUMN_PRO_ID);
        List<Task> projects = new ArrayList<>();
        while (cursor.moveToNext()) {
            projects.add(readTask(cursor));
        }
        cursor.close();
        return projects;
    }

    public Empolyee createEmployee(String name, String email, String pwd, String phno, Empolyee.Type type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ManagmentTableInfo.COLUMN_NAME, name);
        contentValues.put(ManagmentTableInfo.COLUMN_EMAILID, email);
        contentValues.put(ManagmentTableInfo.COLUMN_PASSWORD, pwd);
        contentValues.put(ManagmentTableInfo.COLUMN_PHNO, phno);
        contentValues.put(ManagmentTableInfo.COLUMN_EMP_TYPE, type.ordinal());

        long id = getWritableDatabase().insert(ManagmentTableInfo.TABLENAME, null, contentValues);
        Empolyee empolyee = null;
        if (id > -1) {
            if (type == Empolyee.Type.ADMIN) {
                empolyee = new Admin(id, name, phno, email);
            } else if (type == Empolyee.Type.DEVELOPER) {
                empolyee = new Developer(id, name, phno, email, null);
            } else {
                empolyee = new ProjectManager(id, name, phno, email, null);
            }
        }
        return empolyee;
    }

    public List<Task> getTasks(Project project) {
        String selectArg = project != null ? TaskTableInfo.COLUMN_PRO_ID + "=" + project.getProId() : null;
        Cursor cursor = getReadableDatabase().query(TaskTableInfo.TABLE_NAME, null, selectArg, null,
                null, null, TaskTableInfo.COLUMN_PRO_ID);
        List<Task> projects = new ArrayList<>();
        while (cursor.moveToNext()) {
            projects.add(readTask(cursor));
        }
        cursor.close();
        return projects;
    }

    private Task readTask(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(TaskTableInfo.COLUMN_TASK_ID));
        String name = cursor.getString(cursor.getColumnIndex(TaskTableInfo.COLUMN_TASK_NAME));
        long startTime = cursor.getInt(cursor.getColumnIndex(TaskTableInfo.COLUMN_START_TIME));
        long endTime = cursor.getInt(cursor.getColumnIndex(TaskTableInfo.COLUMN_ENDTIME));
        Task.Status status = Task.Status.values()[cursor.getInt(cursor.getColumnIndex(TaskTableInfo.COLUMN_STATUS))];
        return new Task(id, name, startTime, endTime, status);
    }


    public Task createTask(Developer developer, ProjectManager projectManager, String taskName, long startTime, long endTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskTableInfo.COLUMN_TASK_NAME, taskName);
        contentValues.put(TaskTableInfo.COLUMN_START_TIME, startTime);
        contentValues.put(TaskTableInfo.COLUMN_ENDTIME, endTime);
        contentValues.put(TaskTableInfo.COLUMN_STATUS, Task.Status.PENDING.ordinal());
        contentValues.put(TaskTableInfo.COLUMN_DEV_ID, developer.getId());
        contentValues.put(TaskTableInfo.COLUMN_PRO_ID, projectManager.getId());
        long id = getWritableDatabase().insert(TaskTableInfo.TABLE_NAME, null, contentValues);
        return id > -1 ? new Task(id, taskName, startTime, endTime, Task.Status.PENDING) : null;
    }

    public Project createProject(ProjectManager projectManager, String projectName, long startTime, long endTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProjectTableInfo.COLUMN_PRO_NAME, projectName);
        contentValues.put(ProjectTableInfo.COLUMN_START_TIME, startTime);
        contentValues.put(ProjectTableInfo.COLUMN_END_TIME, endTime);
        contentValues.put(ProjectTableInfo.COLUMN_MANGER_ID, projectManager.getId());
        long id = getWritableDatabase().insert(ProjectTableInfo.TABLE_NAME, null, contentValues);
        return id > -1 ? new Project(id, projectName, startTime, endTime) : null;
    }

    private Project readProject(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(ProjectTableInfo.COLUMN_PRO_ID));
        String name = cursor.getString(cursor.getColumnIndex(ProjectTableInfo.COLUMN_PRO_NAME));
        long startTime = cursor.getInt(cursor.getColumnIndex(ProjectTableInfo.COLUMN_START_TIME));
        long endTime = cursor.getInt(cursor.getColumnIndex(ProjectTableInfo.COLUMN_END_TIME));
        return new Project(id, name, startTime, endTime, null);
    }

    /**
     * if proManger is null return all projects
     *
     * @param projectManager
     * @return
     */
    public List<Project> getAllProjects(ProjectManager projectManager) {
        String selectArg = projectManager != null ? ProjectTableInfo.COLUMN_PRO_ID + "=" + projectManager.getId() : null;
        Cursor cursor = getReadableDatabase().query(ProjectTableInfo.TABLE_NAME, null, selectArg, null,
                null, null, ProjectTableInfo.COLUMN_PRO_ID);
        List<Project> projects = new ArrayList<>();
        while (cursor.moveToNext()) {
            projects.add(readProject(cursor));
        }
        cursor.close();
        return projects;
    }

    public Empolyee getEmployee(String emailId, String password, Empolyee.Type type) {
        Empolyee emp = null;
        Cursor cursor = getReadableDatabase().query(ManagmentTableInfo.TABLENAME, null,
                ManagmentTableInfo.COLUMN_EMAILID + "=? and " + ManagmentTableInfo.COLUMN_PASSWORD
                        + "= ? and " + ManagmentTableInfo.COLUMN_EMP_TYPE + "= ?",
                new String[]{emailId, password, String.valueOf(type.ordinal())}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            emp = readEmployee(cursor, type);
        }
        cursor.close();
        return emp;
    }


}
