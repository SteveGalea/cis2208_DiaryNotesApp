package com.example.diarynotesapp.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.TasksAdapter;
import com.example.diarynotesapp.databinding.FragmentTasksBinding;
import com.example.diarynotesapp.ui.TaskActivity;
import com.example.diarynotesapp.ui.home.HomeViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    private FragmentTasksBinding binding;
    private TasksViewModel tasksViewModel;
    private ExtendedFloatingActionButton extendedFabTask;
    private HomeViewModel homeViewModel;
    private TasksAdapter adapter;
    private RecyclerView tasksView;
    private MaterialCardView card;
    private List<Task> tasks = new ArrayList<>();

    public ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    System.out.println(result.getResultCode());
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        //Intent data = result.getData();
                        resetRecyclerView();
                    }
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTasks;
        tasksViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        extendedFabTask = (ExtendedFloatingActionButton) root.findViewById(R.id.extended_fab_task);


        //FAB
        extendedFabTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("here");
                Toast toast = Toast.makeText(v.getContext(),
                        "Adding new Task",
                        Toast.LENGTH_SHORT);
                toast.show();
                Intent intent1 = new Intent(getActivity(), TaskActivity.class);
                intent1.putExtra("TaskActivity", "Add");
                someActivityResultLauncher.launch(intent1);
            }
        });

        tasksView = root.findViewById(R.id.tasks_list);

        resetRecyclerView();
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //tasks.clear();
        binding = null;
    }

    private void fetchItems() {
        tasksViewModel.getTasksMutable(getContext()).observe(getViewLifecycleOwner(),
                this::updateTasksList);
    }

    private void setUpRecyclerView() {
        //adapter = new AlertsAdapter(tasks,5);
        adapter = new TasksAdapter(tasks);
        tasksView.setAdapter(adapter);
        tasksView.setLayoutManager(new LinearLayoutManager(tasksView.getContext()));
    }

    private void updateTasksList(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        adapter.notifyDataSetChanged();
    }
    public void resetRecyclerView(){
        tasks.clear();
        setUpRecyclerView();
        fetchItems();
    }

    public void onResume() {
        resetRecyclerView();
        super.onResume();
    }
}