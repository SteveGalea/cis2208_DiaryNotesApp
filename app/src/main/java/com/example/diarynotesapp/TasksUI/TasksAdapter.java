package com.example.diarynotesapp.TasksUI;


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

import com.example.diarynotesapp.MainActivity;
import com.example.diarynotesapp.R;
import com.example.diarynotesapp.backend.DbHelperTasks;
import com.example.diarynotesapp.ui.TaskActivity;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.List;

public class TasksAdapter extends
        RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    private List<Task> items;
    private int num = -1;
    public TasksAdapter(List<Task> items) {
        this.items = items;
    }
    public void setNum(int num){
        this.num = num;
    }
/*
    public TasksAdapter(List<Task> items, int num) {
        this.items = items;
        this.num = num;
    }*/
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
        TextView taskIdTextView = holder.taskIdTextView;

        titleTextView.setText("Task: "+item.getName());
        detailsTextView.setText("Task Details: "+item.getDetails());
        dueDateTextView.setText("Due on: "+item.getDateDue());
        progressTextView.setText("Finished: "+item.getProgress()+"%");
        taskIdTextView.setText(item.getId()+"");

        double progress = Double.parseDouble(item.getProgress());
        if(progress > 75.0) {
            progressTextView.setTextColor(Color.GREEN);
        }    else{
            if(progress > 45.0) {
                progressTextView.setTextColor(Color.MAGENTA);
            }
            else{ progressTextView.setTextColor(Color.RED);}
        }
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
        public TextView dueDateTextView;
        public TextView progressTextView;
        public TextView taskIdTextView;
        public Button doneBtn;
        public Button editBtn;
        public Button deleteBtn;
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
            taskIdTextView = (TextView)
                    itemView.findViewById(R.id.taskId);
            doneBtn = itemView.findViewById(R.id.doneBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemOnClick(v);
                }

            });
            doneBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });
            editBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(taskIdTextView.getText().toString());
                    Intent intent = new Intent(v.getContext(), TaskActivity.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("Activity", "Edit");
                    v.getContext().startActivity(intent);
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    System.out.println("Clicked Delete");
                    int pos = getAdapterPosition(); // position of card in recycler view list
                    DbHelperTasks dbHelperTasks = new DbHelperTasks(v.getContext());
                    dbHelperTasks.removeTaskById(items.get(pos).getId());
                    items.remove(items.get(pos));

                    System.out.println("Removed: ");
                    notifyDataSetChanged();
                }
            });
        }
        @SuppressLint("ResourceAsColor")
        public void itemOnClick(View v) {

            /*final MaterialCardView cardView = v.findViewById(R.id.card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    cardView.setChecked(!cardView.isChecked());
                    //cardView.toggle();
                }
            });*/

        }
    }
}
