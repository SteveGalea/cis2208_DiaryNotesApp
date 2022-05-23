package com.example.diarynotesapp.ui.tasks;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;
import com.example.diarynotesapp.backend.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class TasksViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<List<Task>> tasksMutable;


    public TasksViewModel() {
        mText = new MutableLiveData<>();
        tasksMutable = new MutableLiveData<>();
        mText.setValue("Tasks");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List<Task>> getTasksMutable(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        ArrayList<Task> tasks= dbHelper.getTasks();
        tasksMutable.setValue(tasks);
        return tasksMutable;
    }

}