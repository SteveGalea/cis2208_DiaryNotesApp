package com.example.diarynotesapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.diarynotesapp.R;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;
import com.example.diarynotesapp.backend.DbHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TaskActivity extends AppCompatActivity {

    Button saveButton;
    //MaterialDatePicker simpleDatePicker;
    Slider slider;
    EditText taskName;
    EditText taskDetails;
    String sliderValue;
    MaterialToolbar topAppBar;
    String dateValue = "";
    private Button mPickDateButton;

    private TextView mShowSelectedDateText;
    private TextView sliderShowProgressText;

    TextInputLayout tilTaskName, tilTaskDetails, tilDate, tilSlider;
    //validation from https://www.youtube.com/watch?v=qcDlcITNqnE&ab_channel=CodingWithTea




    // TextInputLayout tilTaskName, tilSlider, tilDate, tilTaskDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        saveButton = (Button) findViewById(R.id.filledButton);
        taskName = (EditText) findViewById(R.id.task_name);
        taskDetails = (EditText) findViewById(R.id.task_details);
        slider = (Slider) findViewById(R.id.slider);

        tilTaskName = findViewById(R.id.textField);
        tilTaskDetails = findViewById(R.id.detailsField);
        tilDate = findViewById(R.id.dateField);
        tilSlider = findViewById(R.id.sliderArea);


        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);


        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                System.out.println("CLICKED BACK");
                finish();
            }
        });


        //https://www.geeksforgeeks.org/material-design-date-picker-in-android///

        /// now register the text view and the button with
        // their appropriate IDs
        //mPickDateButton = findViewById(R.id.pick_date_button);
        mShowSelectedDateText = findViewById(R.id.show_selected_date);
        sliderShowProgressText = findViewById(R.id.show_progress_percentage);

        if (getIntent().getStringExtra("TaskActivity").equals("Edit")) {
            int userId = getIntent().getIntExtra("ID", -2); // if not found, return -2
            if (userId != -2) {

                DbHelper db = new DbHelper(this);
                Task item = db.getTaskById(userId);
                taskName.setText(item.getName());
                taskDetails.setText(item.getDetails());
                mShowSelectedDateText.setText(item.getDateDue());
                sliderShowProgressText.setText(item.getProgress());
                float progress;
                if(item.getProgress() == null || item.getProgress().equals("")){
                    progress = 0.0f;
                }else{
                    progress = Float.parseFloat(item.getProgress());
                }
                slider.setValue(progress);
                db.close();
            }
        }
        //instantiate
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        tilDate.getEditText().setOnClickListener(
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
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        Date date = new Date(selection + offsetFromUTC);
                        mShowSelectedDateText.setText(simpleFormat.format(date));
                    }
                });

        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                //Use the value
                sliderValue = "" + value + "";
                sliderShowProgressText.setText(sliderValue);
                validateSlider();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSave();
            }
        });

        taskName.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateTaskName();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateTaskName();
            }
        });

        taskDetails.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateTaskDetails();
            }

            @Override
            public void afterTextChanged(Editable editable) {

                validateTaskDetails();
            }
        });
    }
    private boolean validateTaskName(){
        String value = tilTaskName.getEditText().getText().toString().trim();
        if(value.isEmpty()){
            tilTaskName.setError("Task Name Field can not be left empty!");
            return false;
        }
        else if(value.length()>50){
            tilTaskDetails.setError("Too many characters inputted! (Maximum 50 characters)");
            return false;
        }
        else{
            tilTaskName.setError(null);
            tilTaskName.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateTaskDetails(){
        String value = tilTaskDetails.getEditText().getText().toString().trim();
        if(value.length()>500){
            tilTaskDetails.setError("Too many characters inputted! (Maximum 500 characters)");
            return false;
        }
        else{
            tilTaskDetails.setError(null);
            tilTaskDetails.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateDate() {
        String value = tilDate.getEditText().getText().toString().trim();
        Date date2 = null;
        try {
            date2 = (new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)).parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        long difference_In_Time = date2.getTime()-today.getTime();

        if (value.isEmpty()) {
            tilDate.setError("Date Field can not be left empty!");
            return false;
        }
        else if(difference_In_Time < 0){
            tilDate.setError("Input a future date, please!");
            return false;
        }
        else{
            tilDate.setError(null);
            tilDate.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateSlider(){
        String value = tilSlider.getEditText().getText().toString().trim();
        if(value.isEmpty()){
            tilSlider.setError("Progress Field can not be left empty!");
            return false;
        }
        else{
            tilSlider.setError(null);
            tilSlider.setErrorEnabled(false);
            return true;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_top_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DbHelper db = new DbHelper(this);
        int userId = getIntent().getIntExtra("ID", -2); // if not found, return -2
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.remove:
                Intent _result = new Intent();
                Toast.makeText(this, "Removing from Database", Toast.LENGTH_SHORT)
                        .show();
                db.removeTaskById(userId);

                _result.putExtra("Deleted Task", userId);

                db.close();
                setResult(Activity.RESULT_OK, _result);
                finish();
                break;
            case R.id.save:

                Toast.makeText(this, "Attempting to save task", Toast.LENGTH_SHORT)
                        .show();
                db.close();
                this.onClickSave();

                break;
            default:
                break;
        }

        return true;
    }
    private void onClickSave() {

        //String dateValue = getDate(simpleDatePicker);
        String taskNameValue = taskName.getText().toString();
        String taskDetailsValue = taskDetails.getText().toString();
        String sliderValue = sliderShowProgressText.getText().toString();

        Intent _result = new Intent();

        if(!validateTaskName() | !validateTaskDetails() | !validateDate() | !validateSlider()){
            return;
        }

        Task task;
        if (getIntent().getStringExtra("TaskActivity").equals("Add"))
        {
            task = new Task(
                    -1,
                    taskNameValue,
                    taskDetailsValue,
                    sliderValue,
                    mShowSelectedDateText.getText().toString(),
                    "Pending");
            DbHelper dbHelper = new DbHelper(this);

            long id = dbHelper.insertTask(task);
            dbHelper.close();
            _result.putExtra("Add Task", (Serializable) task);

        }
        else{
            // editing
            int userId = getIntent().getIntExtra("ID", -2); // if not found, return -2

            if(userId != -2){
                task = new Task(
                        userId,
                        taskNameValue,
                        taskDetailsValue,
                        sliderValue,
                        mShowSelectedDateText.getText().toString(),
                        "Pending");
                DbHelper dbHelper = new DbHelper(this);
                dbHelper.updateTaskById(task);
                dbHelper.close();
                _result.putExtra("Edit Task", (Serializable) task);
            }else{
                task = new Task(
                        -1,
                        taskNameValue,
                        taskDetailsValue,
                        sliderValue,
                        mShowSelectedDateText.getText().toString(),
                        "Pending");
                DbHelper dbHelper = new DbHelper(this);
                dbHelper.close();
                long id = dbHelper.insertTask(task);
                _result.putExtra("Add Task", (Serializable) task);

            }
        }

        setResult(Activity.RESULT_OK, _result);
        finish();

    }
}