package com.example.diarynotesapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.TasksUI.Task;
import com.example.diarynotesapp.backend.DbHelperTasks;
import com.example.diarynotesapp.ui.tasks.TasksFragment;
import com.example.diarynotesapp.ui.tasks.TasksViewModel;
import com.google.android.material.slider.Slider;

public class TaskActivity extends AppCompatActivity {

    Button saveButton;
    DatePicker simpleDatePicker;
    Slider slider;
    EditText taskName;
    EditText taskDetails;
    String sliderValue ="0.0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        saveButton = (Button) findViewById(R.id.filledButton);

        taskName = (EditText) findViewById(R.id.task_name);
        taskDetails = (EditText) findViewById(R.id.task_details);

        simpleDatePicker = (DatePicker) findViewById(R.id.date_picker_spinner);
        slider = (Slider) findViewById(R.id.slider);


        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                //Use the value
                sliderValue= ""+value+"";

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSave(v);
            }
        });
        //initiate needed data
        /*slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })

        slider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
        }*/
    }
    private void onClickSave(View view){

        DbHelperTasks dbHelperTasks = new DbHelperTasks(this);
        String dateValue = getDate(simpleDatePicker);
        String taskNameValue = taskName.getText().toString();
        String taskDetailsValue = taskDetails.getText().toString();

        Task task = new Task(-1, taskNameValue, taskDetailsValue, sliderValue, dateValue);

        long id = dbHelperTasks.insertTask(task);

        Intent intent = new Intent(this, ConfirmTaskActivity.class);
        intent.putExtra("ID",id);
        startActivity(intent);
    }
    private String getDate(DatePicker simpleDatePicker){
        String delimeter = "/";
        int day = simpleDatePicker.getDayOfMonth();
        String dayDate = day+delimeter;
        int month = simpleDatePicker.getMonth();
        String monthDate = month+delimeter;
        int year = simpleDatePicker.getYear();
        if(day < 10){
            dayDate = "0"+dayDate;
        }
        if(month < 10){
            monthDate = "0"+monthDate;
        }
        return dayDate+monthDate+year;
    }
}