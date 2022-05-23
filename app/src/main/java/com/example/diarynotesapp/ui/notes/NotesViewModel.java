package com.example.diarynotesapp.ui.notes;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diarynotesapp.recyclerviewUI.NotesUI.Note;
import com.example.diarynotesapp.backend.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class NotesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<List<Note>> notesMutable;

    public NotesViewModel() {
        mText = new MutableLiveData<>();
        notesMutable = new MutableLiveData<>();
        mText.setValue("Notes");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List<Note>> getNotesMutable(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        ArrayList<Note> notes= dbHelper.getNotes();
        notesMutable.setValue(notes);
        return notesMutable;
    }
}