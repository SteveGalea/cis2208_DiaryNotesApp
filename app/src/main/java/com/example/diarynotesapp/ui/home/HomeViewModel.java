package com.example.diarynotesapp.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;
import com.example.diarynotesapp.backend.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    //declarations
    private final MutableLiveData<String> mText;
    private MutableLiveData<List<Task>> tasksMutable;

    public HomeViewModel() {
        // initialise and set live data
        mText = new MutableLiveData<>();
        tasksMutable = new MutableLiveData<>();
        mText.setValue("Today");
    }

    public LiveData<String> getText() {
        return mText;
    }  // gets live data text

    public LiveData<List<Task>> getTasksMutable(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        ArrayList<Task> tasks= dbHelper.getTasks();
        tasksMutable.setValue(tasks);
        return tasksMutable;
    } // gets mutable tasks
}