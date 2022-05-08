package com.example.diarynotesapp.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.diarynotesapp.NotesUI.Note;

import java.util.ArrayList;

public class DbHelperNotes extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "appDb.db";
    private final String _title = NoteDb.NoteEntry.COLUMN_NAME_TITLE;
    private final String _noteText = NoteDb.NoteEntry.COLUMN_NAME_NOTE_TEXT;
    private final String _imageURL = NoteDb.NoteEntry.COLUMN_NAME_IMAGE_URL;
    private final String _favourite = NoteDb.NoteEntry.COLUMN_NAME_FAVOURITE;
    private final String _date = NoteDb.NoteEntry.COLUMN_NAME_DATE;

    public DbHelperNotes(Context context) {
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
        return "CREATE TABLE "+ NoteDb.NoteEntry.TABLE_NAME+" ("+ NoteDb.NoteEntry._ID+ " INTEGER PRIMARY KEY, " + _title + " varchar, "+_noteText + " varchar, "+_imageURL + " varchar, "+_favourite+ " varchar, "+_date+ " varchar)";
    }

    private String dropTables() {
        return "DROP TABLE IF EXISTS " + NoteDb.NoteEntry.TABLE_NAME;
    }
    public void removeAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(dropTables());
        onCreate(db);
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
        return id;
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
        String sortOrder = _date + " ASC";
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
        return notes;
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
        String[] selectionArgs = {Long.toString(id)};
        // How you want the results sorted in the resulting Cursor
        String sortOrder = _date + " ASC";
        Cursor cursor = db.query(
                NoteDb.NoteEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
 );
        Note note = null;
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String noteText = cursor.getString(cursor.getColumnIndexOrThrow(_noteText));
            String imageURL = cursor.getString(cursor.getColumnIndexOrThrow(_imageURL));
            String favourite = cursor.getString(cursor.getColumnIndexOrThrow(_favourite));
            String dateDB = cursor.getString(cursor.getColumnIndexOrThrow(_date));
            note = new Note(id, title, noteText, imageURL, favourite, dateDB);
        }
        cursor.close();
        return note;
    }

}