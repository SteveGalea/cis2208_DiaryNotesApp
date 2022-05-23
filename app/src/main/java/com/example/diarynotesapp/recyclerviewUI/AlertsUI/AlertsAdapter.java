package com.example.diarynotesapp.recyclerviewUI.AlertsUI;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlertsAdapter extends
        RecyclerView.Adapter<AlertsAdapter.ViewHolder> {
    // declarations for alerts adapter
    private List<com.example.diarynotesapp.recyclerviewUI.TasksUI.Task> items;

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
    }  // card item obtained by Layout inflater

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        com.example.diarynotesapp.recyclerviewUI.TasksUI.Task item = items.get(position);

        TextView titleTextView = holder.titleTextView;
        TextView detailsTextView = holder.detailsTextView;

        // date subtraction
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        String date = item.getDateDue();
        //catch in case of errors
        Date date2 = null;
        try {
            date2 = (new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // compute
        assert date2 != null;
        long difference_In_Time = date2.getTime()-today.getTime();
        // convert to days
        long computedDaysLeft
                = 1+(difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;
        // initialised data
        String title = computedDaysLeft == 1? item.getName()+" due in "+computedDaysLeft+" day" : item.getName()+" due in "+computedDaysLeft+" days";
        String details = "Due Date "+item.getDateDue();
        // set in card
        titleTextView.setText(title);
        detailsTextView.setText(details);
    }// binds data to card.

    @Override
    public int getItemCount() {
        return items.size();
    } // get item count

    public class ViewHolder extends RecyclerView.ViewHolder {
        // declare and initialise ViewHolder of card
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
