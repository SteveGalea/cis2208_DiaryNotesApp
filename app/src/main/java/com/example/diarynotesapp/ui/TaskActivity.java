package com.example.diarynotesapp.ui;

import static androidx.core.widget.TextViewKt.addTextChangedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.TasksUI.Task;
import com.example.diarynotesapp.backend.DbHelperTasks;
import com.example.diarynotesapp.ui.tasks.TasksFragment;
import com.example.diarynotesapp.ui.tasks.TasksViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TaskActivity extends AppCompatActivity {

    Button saveButton;
    MaterialDatePicker simpleDatePicker;
    Slider slider;
    EditText taskName;
    EditText taskDetails;
    String sliderValue ="0.0";
    MaterialToolbar topAppBar;
    String dateValue = "";
    //EditText dateDue;
    private Button mPickDateButton;

    private TextView mShowSelectedDateText;
    private TextView sliderShowProgressText;


   // TextInputLayout tilTaskName, tilSlider, tilDate, tilTaskDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        saveButton = (Button) findViewById(R.id.filledButton);

        taskName = (EditText) findViewById(R.id.task_name);
        taskDetails = (EditText) findViewById(R.id.task_details);
        //dateDue = (EditText) findViewById(R.id.editDueDate);
/*
        tilTaskName = findViewById(R.id.textField);
        tilTaskDetails = findViewById(R.id.detailsField);
        tilDate = findViewById(R.id.dateField);
        tilSlider = findViewById(R.id.sliderArea);

            tilTaskName.onKeyDown(addTextChangedListener(new TextWatcher(){
                @Override
                public void afterTextChanged(Editable arg0) {
                if(tilTaskName.getCounterMaxLength() == 0) {
                    tilTaskName.setErrorEnabled(true);
                    tilTaskName.setError("You need to enter a name");
                }
                isValid();

            }
        });
*/
        slider = (Slider) findViewById(R.id.slider);

        topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //https://www.geeksforgeeks.org/material-design-date-picker-in-android///

        /// now register the text view and the button with
        // their appropriate IDs
        mPickDateButton = findViewById(R.id.pick_date_button);
        mShowSelectedDateText = findViewById(R.id.show_selected_date);
        sliderShowProgressText = findViewById(R.id.show_progress_percentage);

        // now create instance of the material date picker
        // builder make sure to add the "datePicker" which
        // is normal material date picker which is the first
        // type of the date picker in material design date
        // picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    public void onPositiveButtonClick(Long selection) {

                        TimeZone timeZoneUTC = TimeZone.getDefault();
                        // It will be negative, so that's the -1
                        int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                        // Create a date format, then a date object with our offset
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date date = new Date(selection + offsetFromUTC);
                        //mShowSelectedDateText.setText(materialDatePicker.getHeaderText());
                        mShowSelectedDateText.setText(simpleFormat.format(date));
                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                //Use the value
                sliderValue= ""+value+"";
                sliderShowProgressText.setText(sliderValue);
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
    /*
    private boolean isValid(){
        tilTaskName = findViewById(R.id.textField);
        tilTaskDetails = findViewById(R.id.detailsField);
        tilDate = findViewById(R.id.dateField);
        tilSlider = findViewById(R.id.sliderArea);

        if (!tilTaskName.getError().equals("")) {
            tilTaskName.setError(tilTaskName.getError());
            return false;
        }
        if (!tilTaskDetails.getError().equals("")) {
            tilTaskDetails.setError(tilTaskDetails.getError());
            return false;
        }
        if (!tilDate.getError().equals("")) {
            tilDate.setError(tilDate.getError());
            return false;
        }
        if (!tilSlider.getError().equals("")) {
            tilSlider.setError(tilSlider.getError());
            return false;
        }
        return true;
    }*/
    private void onClickSave(View view){

        //String dateValue = getDate(simpleDatePicker);
        String taskNameValue = taskName.getText().toString();
        String taskDetailsValue = taskDetails.getText().toString();

       // if(isValid()){
            Task task = new Task(-1, taskNameValue, taskDetailsValue, sliderValue, mShowSelectedDateText.getText().toString());
            DbHelperTasks dbHelperTasks = new DbHelperTasks(this);

            long id = dbHelperTasks.insertTask(task);

            //Intent intent = new Intent(this, ConfirmTaskActivity.class);
            //intent.putExtra("ID",id);
            //startActivity(intent);
            finish();
        //}else{
         //   Toast.makeText(this, "error somewhere", Toast.LENGTH_SHORT).show();
        //    finish();
       // }
        //}
    }
    /*
    private String getDate(MaterialDatePicker simpleDatePicker){
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
    }*/
}