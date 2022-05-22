package com.example.diarynotesapp.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diarynotesapp.NotesUI.Note;
import com.example.diarynotesapp.TasksUI.Task;
import com.example.diarynotesapp.api.model.Quote;
import com.example.diarynotesapp.api.rest.QuotesRestRepository;
import com.example.diarynotesapp.backend.DbHelperNotes;
import com.example.diarynotesapp.backend.DbHelperTasks;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<List<Task>> tasksMutable;
    private MutableLiveData<List<Note>> notesMutable;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        tasksMutable = new MutableLiveData<>();
        notesMutable = new MutableLiveData<>();
        //mText.setValue("This is home fragment");
        mText.setValue("Today");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List<Task>> getTasksMutable(Context context) {
        DbHelperTasks dbHelper = new DbHelperTasks(context);
        ArrayList<Task> tasks= dbHelper.getTasks();
        tasksMutable.setValue(tasks);
        return tasksMutable;
    }
    public LiveData<List<Note>> getNotesMutable(Context context) {
        DbHelperNotes dbHelper = new DbHelperNotes(context);
        ArrayList<Note> notes= dbHelper.getNotes();
        notesMutable.setValue(notes);
        return notesMutable;
    }


}