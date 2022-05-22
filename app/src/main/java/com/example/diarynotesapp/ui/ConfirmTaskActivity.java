package com.example.diarynotesapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;
import com.example.diarynotesapp.backend.DbHelperTasks;

public class ConfirmTaskActivity extends AppCompatActivity {

    // TEST CLASS... THIS WAS DISCONTINUED AFTER SHOWING RECYCLERVIEW
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_confirm);
        populateScreen();
        //onBackPressed();
    }
    private void populateScreen() {
        DbHelperTasks dbHelper = new DbHelperTasks(this);
        Intent intent = getIntent();
        long id = intent.getLongExtra("ID", -1); //-1 is default value
        // gets email from db using id
        Task task = dbHelper.getTaskById(id);
        TextView idConfirm = (TextView) findViewById(R.id.id_confirm);
        idConfirm.setText(task.getId()+"");
        TextView nameConfirm = (TextView) findViewById(R.id.name_confirm);
        nameConfirm.setText(task.getName());
        TextView detailsConfirm = (TextView)
                findViewById(R.id.details_confirm);
        detailsConfirm.setText(task.getDetails());
        TextView dateConfirm = (TextView)
                findViewById(R.id.date_confirm);
        dateConfirm.setText(task.getDateDue());
        TextView sliderConfirm = (TextView)
                findViewById(R.id.slider_confirm);
        sliderConfirm.setText(task.getProgress());
    }

}