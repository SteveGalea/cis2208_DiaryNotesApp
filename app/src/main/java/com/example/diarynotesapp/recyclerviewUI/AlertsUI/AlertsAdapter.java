package com.example.diarynotesapp.recyclerviewUI.AlertsUI;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.backend.DbHelperTasks;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;
import com.example.diarynotesapp.ui.TaskActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlertsAdapter extends
        RecyclerView.Adapter<AlertsAdapter.ViewHolder> {
    private List<com.example.diarynotesapp.recyclerviewUI.TasksUI.Task> items;
    private int num = -1;
    public AlertsAdapter(List<Task> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.layout_card_alerts, parent,
                false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        com.example.diarynotesapp.recyclerviewUI.TasksUI.Task item = items.get(position);

        TextView titleTextView = holder.titleTextView;
        TextView detailsTextView = holder.detailsTextView;


        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        String date = item.getDateDue();


        Date date2 = null;
        try {
            date2 = (new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference_In_Time = date2.getTime()-today.getTime();


        long computedDaysLeft
                = 1+(difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;

        String title = computedDaysLeft == 1? item.getName()+" due in "+computedDaysLeft+" day" : item.getName()+" due in "+computedDaysLeft+" days";

        String details = "Due Date "+item.getDateDue();

        titleTextView.setText(title);
        detailsTextView.setText(details);
    }
    @Override
    public int getItemCount() {
        if(num != -1){
            if(items.size() < num){
                return items.size();
            }else{
                return num;
            }
        }
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView detailsTextView;
        public ViewHolder(final View itemView) {
            super(itemView);
            titleTextView = (TextView)
                    itemView.findViewById(R.id.alertTitle);
            detailsTextView = (TextView)
                    itemView.findViewById(R.id.alertDetails);

        }

    }
}
