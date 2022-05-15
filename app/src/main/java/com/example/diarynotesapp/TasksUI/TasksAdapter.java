package com.example.diarynotesapp.TasksUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarynotesapp.R;

import java.util.List;

public class TasksAdapter extends
        RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    private List<Task> items;
    public TasksAdapter(List<Task> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.layout_tasks, parent,
                false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Task item = items.get(position);

        TextView titleTextView = holder.titleTextView;
        TextView detailsTextView = holder.detailsTextView;
        TextView dueDateTextView = holder.dueDateTextView;
        TextView progressTextView = holder.progressTextView;

        titleTextView.setText(item.getName());
        detailsTextView.setText(item.getDetails());
        dueDateTextView.setText(item.getDateDue());
        progressTextView.setText(item.getProgress());
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView detailsTextView;
        public TextView dueDateTextView;
        public TextView progressTextView;
        public ViewHolder(final View itemView) {
            super(itemView);
            titleTextView = (TextView)
                    itemView.findViewById(R.id.title);
            detailsTextView = (TextView)
                    itemView.findViewById(R.id.details);
            dueDateTextView = (TextView)
                    itemView.findViewById(R.id.due_date);
            progressTextView = (TextView)
                    itemView.findViewById(R.id.progress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClick();
                }
            });
        }
        public void itemOnClick() {
            /*if(secondaryTextView.getText().equals("Unread")){
                secondaryTextView.setText("Read");

                iconImageView.setImageResource(R.drawable.ic_notifications_black_24dp);
            } else {
                secondaryTextView.setText("Unread");

                iconImageView.setImageResource(R.drawable.ic_notifications_purple_24dp);
            }*/
        }
    }
}
