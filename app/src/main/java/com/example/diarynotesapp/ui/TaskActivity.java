package com.example.diarynotesapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.widget.DatePicker;

import com.example.diarynotesapp.R;
import com.google.android.material.slider.Slider;

public class TaskActivity extends AppCompatActivity {


    DatePicker simpleDatePicker;
    Slider slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //initiate needed data
        simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker);
        slider = (Slider) findViewById(R.id.slider);
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
}