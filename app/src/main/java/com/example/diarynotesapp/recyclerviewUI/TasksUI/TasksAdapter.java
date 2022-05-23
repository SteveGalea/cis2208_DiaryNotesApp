package com.example.diarynotesapp.recyclerviewUI.TasksUI;




import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

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
import com.example.diarynotesapp.ui.TaskActivity;

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

        String name = "Task: "+item.getName();
        String detailsTask = item.getDetails();
        if(item.getDetails() == null||item.getDetails().equals("")){
            detailsTask = "N/A";
        }
        String details ="\tTask Details: "+detailsTask;
        String date = "\tDue on: "+item.getDateDue();
        String progressSlider= "\tFinished: "+item.getProgress()+"%";
        String taskId = item.getId()+"";

        if(item.getFlag().equals("Complete")){
            titleTextView.setText("Archived "+name);
            holder.actionBtn.setText("Undo");
        }else{
            titleTextView.setText(name);
            holder.actionBtn.setText("Archive");
        }

        detailsTextView.setText(details);
        dueDateTextView.setText(date);
        progressTextView.setText(progressSlider);
        taskIdTextView.setText(taskId);
        double progress;
        if(item.getProgress()==null||item.getProgress().equals("")){
            progress = 0.0;
        }else{
            progress = Double.parseDouble(item.getProgress());
        }
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
        public Button actionBtn;
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
            actionBtn = itemView.findViewById(R.id.doneBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClick(v);
                }

            });
            actionBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    int id = Integer.parseInt(taskIdTextView.getText().toString());
                    DbHelperTasks dbHelperTasks = new DbHelperTasks(v.getContext());
                    Task task = dbHelperTasks.getTaskById(id);

                    if(task.getFlag().equals("Pending")){
                        task.setFlag("Complete");
                        titleTextView.setText("Archived "+titleTextView.getText().toString());
                        actionBtn.setText("Undo");
                    }else {
                        task.setFlag("Pending");
                        titleTextView.setText("Task: "+task.getName());
                        actionBtn.setText("Archive");
                    }
                    dbHelperTasks.updateTaskById(task);
                    dbHelperTasks.close();
                }
            });
            editBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(taskIdTextView.getText().toString());
                    Intent intent = new Intent(v.getContext(), TaskActivity.class);
                    intent.putExtra("ID", id);
                    intent.putExtra("TaskActivity", "Edit");
                    v.getContext().startActivity(intent);

                    notifyDataSetChanged();

                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    System.out.println("Clicked Delete");
                    int pos = getAdapterPosition(); // position of card in recycler view list
                    DbHelperTasks dbHelperTasks = new DbHelperTasks(v.getContext());
                    long removeId = items.get(pos).getId();
                    dbHelperTasks.removeTaskById(removeId);
                    items.remove(items.get(pos));
                    notifyDataSetChanged();
                    dbHelperTasks.close();
                }
            });
        }

        @SuppressLint("ResourceAsColor")
        public void itemOnClick(View v) {
            int id = Integer.parseInt(taskIdTextView.getText().toString());
            Intent intent = new Intent(v.getContext(), TaskActivity.class);
            intent.putExtra("ID", id);
            intent.putExtra("TaskActivity", "Edit");
            v.getContext().startActivity(intent);
            //someActivityResultLauncher.launch(intent);
            //set edit details

            notifyDataSetChanged();
        }
    }
}
