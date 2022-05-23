package com.example.diarynotesapp.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Base64;

import com.example.diarynotesapp.recyclerviewUI.NotesUI.Note;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "appDb.db";

    private final String _id = TaskDb.TaskEntry.COLUMN_NAME_ID;
    private final String _name = TaskDb.TaskEntry.COLUMN_NAME_NAME;
    private final String _details = TaskDb.TaskEntry.COLUMN_NAME_DETAILS;
    private final String _dueDate = TaskDb.TaskEntry.COLUMN_NAME_DUEDATE;
    private final String _progress = TaskDb.TaskEntry.COLUMN_NAME_PROGRESS;
    private final String _flag = TaskDb.TaskEntry.COLUMN_NAME_FLAG;


    private final String _title = NoteDb.NoteEntry.COLUMN_NAME_TITLE;
    private final String _noteText = NoteDb.NoteEntry.COLUMN_NAME_NOTE_TEXT;
    private final String _imageURL = NoteDb.NoteEntry.COLUMN_NAME_IMAGE_URL;
    private final String _favourite = NoteDb.NoteEntry.COLUMN_NAME_FAVOURITE;
    private final String _date = NoteDb.NoteEntry.COLUMN_NAME_DATE;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL(dropTaskTable());
        db.execSQL(dropNoteTable());
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void createTables(SQLiteDatabase db){
        db.execSQL(createTaskTable());
        db.execSQL(createNoteTable());
    }
    private String createTaskTable() {
        return "CREATE TABLE "+ TaskDb.TaskEntry.TABLE_NAME+" ("+TaskDb.TaskEntry._ID+ " INTEGER PRIMARY KEY, " + _name + " varchar, "+_details + " varchar, "+_dueDate+ " varchar, "+ _progress + " varchar, "+ _flag + " varchar)";
    }
    private String createNoteTable() {
        return "CREATE TABLE "+ NoteDb.NoteEntry.TABLE_NAME+" ("+ NoteDb.NoteEntry._ID+ " INTEGER PRIMARY KEY, " + _title + " varchar, "+_noteText + " varchar, "+_imageURL + " varchar, "+_favourite+ " varchar, "+_date+ " varchar)";
    }


    private String dropTaskTable() {
        return "DROP TABLE IF EXISTS " +TaskDb.TaskEntry.TABLE_NAME;
    }
    private String dropNoteTable() {
        return "DROP TABLE IF EXISTS " + NoteDb.NoteEntry.TABLE_NAME;
    }


    public void removeAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(dropTaskTable());
        db.execSQL(dropNoteTable());
        onCreate(db);
        db.close();
    }
    //insert
    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_name, task.getName());
        values.put(_details, task.getDetails());
        values.put(_dueDate, task.getDateDue());
        values.put(_progress, task.getProgress());
        values.put(_flag, task.getFlag());
        long id = db.insert(TaskDb.TaskEntry.TABLE_NAME, null,
                values);
        db.close();
        return id;
    }
    public long insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_title, note.getTitle());
        values.put(_noteText, note.getNoteText());
        values.put(_imageURL, note.getImageURL());
        values.put(_favourite, note.getFavourite());
        values.put(_date, note.getDate());
        long id = db.insert(NoteDb.NoteEntry.TABLE_NAME, null,
                values);
        db.close();
        return id;
    }
    public String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }



    // update
    public void updateTaskById(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_name, task.getName());
        values.put(_details, task.getDetails());
        values.put(_dueDate, task.getDateDue());
        values.put(_progress, task.getProgress());
        values.put(_flag, task.getFlag());
        db.update(TaskDb.TaskEntry.TABLE_NAME,  values, "_id =?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public void updateNoteById(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_title, note.getTitle());
        values.put(_noteText, note.getNoteText());
        values.put(_imageURL, note.getImageURL());
        values.put(_favourite, note.getFavourite());
        values.put(_date, note.getDate());
        db.update(NoteDb.NoteEntry.TABLE_NAME,  values, "_id =?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    //get all
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                _name,
                _details,
                _dueDate,
                _progress,
                _flag
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = _progress + " ASC";
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
            String flag = cursor.getString(cursor.getColumnIndexOrThrow(_flag));
            Task task = new Task(id, name, details, progress, dueDate, flag);
            tasks.add(task);
        }
        cursor.close();
        db.close();
        return tasks;
    }

    public ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                _title,
                _noteText,
                _imageURL,
                _favourite,
                _date
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = _title + " ASC";
        Cursor cursor = db.query(
                NoteDb.NoteEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String noteText = cursor.getString(cursor.getColumnIndexOrThrow(_noteText));
            String imageURL = cursor.getString(cursor.getColumnIndexOrThrow(_imageURL));
            String favourite = cursor.getString(cursor.getColumnIndexOrThrow(_favourite));
            String dateDB = cursor.getString(cursor.getColumnIndexOrThrow(_date));

            Note note = new Note(id, title, noteText, imageURL, favourite, dateDB);
            notes.add(note);
        }
        cursor.close();
        db.close();
        return notes;
    }

    //get
    public Task getTaskById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                _name,
                _details,
                _dueDate,
                _progress,
                _flag
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
                String flag = cursor.getString(cursor.getColumnIndexOrThrow(_flag));

                task = new Task(id, name, details, progress, dueDate, flag);
                System.out.println("Got a task matching id"+id);
            }
        }
        cursor.close();
        db.close();
        return task;
    }

    public Note getNoteById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                _title,
                _noteText,
                _imageURL,
                _favourite,
                _date
        };
        // Filter results WHERE "id" = condition
        String selection = BaseColumns._ID + " = ?";
        System.out.println("id: "+id);
        String[] selectionArgs = {id+""};
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID;
        Cursor cursor = db.query(
                NoteDb.NoteEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        Note note = null;
        while (cursor.moveToNext()) {
            long cid = cursor.getLong(cursor.getColumnIndexOrThrow(_id));
            System.out.println("Checking: "+id);
            if(cid == id) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
                String noteText = cursor.getString(cursor.getColumnIndexOrThrow(_noteText));
                String imageURL = cursor.getString(cursor.getColumnIndexOrThrow(_imageURL));
                String favourite = cursor.getString(cursor.getColumnIndexOrThrow(_favourite));
                String dateDB = cursor.getString(cursor.getColumnIndexOrThrow(_date));

                note = new Note(id, title, noteText, imageURL, favourite, dateDB);
            }
        }
        cursor.close();
        db.close();
        return note;
    }

    //removal
    public void removeTaskById(long id) {

        // Filter results WHERE "id" = condition
        String selection = BaseColumns._ID + " = ?";
        System.out.println(BaseColumns._ID );
        String[] selectionArgs = new String[]{String.valueOf(id)};

        System.out.println(selectionArgs[0]);
        int deletedRows= deleteTaskDataById(selectionArgs[0]);
        System.out.println("deleted:"+deletedRows);
    }
    public int deleteTaskDataById(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TaskDb.TaskEntry.TABLE_NAME, "_id = ?", new String[] {Id});
        db.close();
        return deletedRows;
    }
    public void removeNoteById(long id) {
        // Filter results WHERE "id" = condition
        String selection = BaseColumns._ID + " = ?";
        System.out.println(BaseColumns._ID );
        String[] selectionArgs = new String[]{String.valueOf(id)};

        System.out.println(selectionArgs[0]);
        int deletedRows= deleteNoteDataById(selectionArgs[0]);
        System.out.println("deleted:"+deletedRows);
    }
    public int deleteNoteDataById(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(NoteDb.NoteEntry.TABLE_NAME, "_id = ?", new String[] {Id});
        db.close();
        return deletedRows;
    }
}