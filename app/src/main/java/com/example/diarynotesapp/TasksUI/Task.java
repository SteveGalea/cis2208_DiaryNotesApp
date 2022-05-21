package com.example.diarynotesapp.TasksUI;

import java.io.Serializable;

public class Task implements Serializable {
    private long id;
    private String _name;
    private String _details;
    private String _progress;
    private String _dateDue;
    private String _flag;

    public Task(long id, String _name, String _details, String _progress, String _dateDue, String _flag) {
        this.id = id;
        this._name = _name;
        this._details = _details;
        this._progress = _progress;
        this._dateDue = _dateDue;
        this._flag = _flag;
    }

    //getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getDetails() {
        return _details;
    }

    public void setDetails(String _details) {
        this._details = _details;
    }

    public String getProgress() {
        return _progress;
    }

    public void setProgress(String _progress) {
        this._progress = _progress;
    }

    public String getDateDue() {
        return _dateDue;
    }

    public void setDateDue(String _dateDue) {
        this._dateDue = _dateDue;
    }

    public String getFlag() {
        return _flag;
    }

    public void setFlag(String _flag) {
        this._flag = _flag;
    }

}
