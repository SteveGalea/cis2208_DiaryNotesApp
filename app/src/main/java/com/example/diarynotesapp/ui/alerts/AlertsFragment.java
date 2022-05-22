package com.example.diarynotesapp.ui.alerts;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.databinding.FragmentAlertsBinding;
import com.example.diarynotesapp.recyclerviewUI.AlertsUI.AlertsAdapter;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;


import java.util.ArrayList;
import java.util.List;

public class AlertsFragment extends Fragment {

    FragmentAlertsBinding binding;
    private AlertsViewModel alertsViewModel;

    private AlertsAdapter adapterTasks;


    private RecyclerView alertsView;

    private List<Task> tasks = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alertsViewModel =
                new ViewModelProvider(this).get(AlertsViewModel.class);

        binding = FragmentAlertsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAlerts;
        alertsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        alertsView = root.findViewById(R.id.tasks_list);

        resetTasksRecyclerView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //tasks.clear();
        binding = null;
    }

    private void fetchTasksItems() {
        alertsViewModel.getTasksMutable(getContext()).observe(getViewLifecycleOwner(),
                this::updateTasksList);
    }

    private void setUpTasksRecyclerView() {
        //adapter = new AlertsAdapter(tasks,5);
        adapterTasks = new AlertsAdapter(tasks);
        alertsView.setAdapter(adapterTasks);
        alertsView.setLayoutManager(new LinearLayoutManager(alertsView.getContext()));
    }

    private void updateTasksList(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        adapterTasks.notifyDataSetChanged();
    }
    public void resetTasksRecyclerView(){
        tasks.clear();
        setUpTasksRecyclerView();
        fetchTasksItems();

    }

}