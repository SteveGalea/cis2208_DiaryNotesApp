package com.example.diarynotesapp.ui.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarynotesapp.NotesUI.Note;
import com.example.diarynotesapp.NotesUI.NotesAdapter;
import com.example.diarynotesapp.TasksUI.Task;
import com.example.diarynotesapp.TasksUI.TasksAdapter;
import com.example.diarynotesapp.backend.DbHelperNotes;
import com.example.diarynotesapp.backend.DbHelperTasks;
import com.example.diarynotesapp.databinding.FragmentNotesBinding;
import com.example.diarynotesapp.databinding.FragmentTasksBinding;
import com.example.diarynotesapp.ui.home.HomeViewModel;
import com.example.diarynotesapp.ui.tasks.TasksViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<List<Note>> notesMutable;

    public NotesViewModel() {
        mText = new MutableLiveData<>();
        notesMutable = new MutableLiveData<>();
        mText.setValue("This is notes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List<Note>> geNotesMutable(Context context) {
        DbHelperNotes dbHelper = new DbHelperNotes(context);
        ArrayList<Note> notes= dbHelper.getNotes();
        notesMutable.setValue(notes);
        return notesMutable;
    }
}