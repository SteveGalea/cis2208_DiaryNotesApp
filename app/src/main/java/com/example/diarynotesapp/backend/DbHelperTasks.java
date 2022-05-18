package com.example.diarynotesapp.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.diarynotesapp.TasksUI.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DbHelperTasks extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "appDb.db";

    private final String _id = TaskDb.TaskEntry.COLUMN_NAME_ID;
    private final String _name = TaskDb.TaskEntry.COLUMN_NAME_NAME;
    private final String _details = TaskDb.TaskEntry.COLUMN_NAME_DETAILS;
    private final String _dueDate = TaskDb.TaskEntry.COLUMN_NAME_DUEDATE;
    private final String _progress = TaskDb.TaskEntry.COLUMN_NAME_PROGRESS;

    public DbHelperTasks(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTables());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL(dropTables());
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private String createTables() {
        return "CREATE TABLE "+ TaskDb.TaskEntry.TABLE_NAME+" ("+TaskDb.TaskEntry._ID+ " INTEGER PRIMARY KEY, " + _name + " varchar, "+_details + " varchar, "+_dueDate+ " varchar, "+ _progress + " varchar)";
    }

    private String dropTables() {
        return "DROP TABLE IF EXISTS " +TaskDb.TaskEntry.TABLE_NAME;
    }
    public void removeAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(dropTables());
        onCreate(db);
    }
    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_name, task.getName());
        values.put(_details, task.getDetails());
        values.put(_dueDate, task.getDateDue());
        values.put(_progress, task.getProgress());
        long id = db.insert(TaskDb.TaskEntry.TABLE_NAME, null,
                values);
        return id;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                _name,
                _details,
                _dueDate,
                _progress
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = _dueDate + " ASC";
        Cursor cursor = db.query(
                TaskDb.TaskEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(_name));
            String details = cursor.getString(cursor.getColumnIndexOrThrow(_details));
            String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(_dueDate));
            String progress = cursor.getString(cursor.getColumnIndexOrThrow(_progress));
            Task task = new Task(id, name, details, progress, dueDate);
            tasks.add(task);
        }
        cursor.close();
        return tasks;
    }

    public Task getTaskById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                _name,
                _details,
                _dueDate,
                _progress
        };
        // Filter results WHERE "id" = condition
        String selection = BaseColumns._ID + " = ?";
        System.out.println("id: "+id);
        String[] selectionArgs = {id+""};
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID;
        Cursor cursor = db.query(
                TaskDb.TaskEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return(pass null to get all)
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
         );
        Task task = null;
        while (cursor.moveToNext()) {

            long cid = cursor.getLong(cursor.getColumnIndexOrThrow(_id));
            System.out.println("Checking: "+id);
            if(cid == id) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(_name));
                String details = cursor.getString(cursor.getColumnIndexOrThrow(_details));
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(_dueDate));
                String progress = cursor.getString(cursor.getColumnIndexOrThrow(_progress));

                task = new Task(id, name, details, progress, dueDate);
                System.out.println("Got a task matching id"+id);
            }
        }
        cursor.close();
        return task;
    }

    public void removeTaskById(long id) {

        // Filter results WHERE "id" = condition
        String selection = BaseColumns._ID + " = ?";
        System.out.println(BaseColumns._ID );
        String[] selectionArgs = new String[]{String.valueOf(id)};

        System.out.println(selectionArgs[0]);
        int deletedRows= deleteDataById(selectionArgs[0]);
        System.out.println("deleted:"+deletedRows);
    }
    public int deleteDataById(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TaskDb.TaskEntry.TABLE_NAME, "_id = ?", new String[] {Id});
        db.close();
        return deletedRows;
    }
}