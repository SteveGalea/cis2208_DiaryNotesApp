package com.example.diarynotesapp.NotesUI;

import com.example.diarynotesapp.backend.NoteDb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    private long id;
    private String _title;
    private String _noteText;
    private String _imageURL;
    private String _favourite;
    private String _date;

    //constructors
    public Note(long id, String _title, String _noteText, String _imageURL, String _favourite, String _date) {
        this.id = id;
        this._title = _title;
        this._noteText = _noteText;
        this._imageURL = _imageURL;
        this._favourite = _favourite;
        this._date = _date; // different than below implementation
    }
    public Note(long id, String _title, String _noteText, String _imageURL, String _favourite) {
        this.id = id;
        this._title = _title;
        this._noteText = _noteText;
        this._imageURL = _imageURL;
        this._favourite = _favourite;
        this._date = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date()); // set to now, convert to string
    }

    //required getter and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getNoteText() {
        return _noteText;
    }

    public void setNteText(String _noteText) {
        this._noteText = _noteText;
    }

    public String getImageURL() {
        return _imageURL;
    }

    public void setImageURL(String _imageURL) {
        this._imageURL = _imageURL;
    }

    public String getFavourite() {
        return _favourite;
    }

    public void setFavourite(String _favourite) {
        this._favourite = _favourite;
    }

    public String getDate() {
        return _date;
    }
}
