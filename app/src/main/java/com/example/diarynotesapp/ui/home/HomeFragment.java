package com.example.diarynotesapp.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.diarynotesapp.databinding.FragmentHomeBinding;
import com.example.diarynotesapp.R;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.TasksAdapter;
import com.example.diarynotesapp.api.model.Quote;
import com.example.diarynotesapp.api.rest.QuotesRestRepository;
import com.example.diarynotesapp.ui.NoteActivity;
import com.example.diarynotesapp.ui.TaskActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment{

    //declarations
    FragmentHomeBinding binding;
    private TextView quoteDisplay;
    private Button refreshQuoteButton;
    private HomeViewModel homeViewModel;
    private TasksAdapter adapterTasks;
    private RecyclerView tasksView;
    private List<Task> tasks = new ArrayList<>();


    //gets
    public ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    System.out.println(result.getResultCode());
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if(data.getStringExtra("TaskActivity")!=null){
                            resetTasksRecyclerView();
                        }
                    }
                }
            });

    @SuppressLint("SimpleDateFormat")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasks.clear();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // initialisations
        quoteDisplay = (TextView) root.findViewById(R.id.quote);
        refreshQuoteButton = (Button) root.findViewById(R.id.refreshQuoteButton);

        //declaration and initialising of algebraic classes
        Button redirectButtonTasks = (Button) root.findViewById(R.id.redirectButtonTasks);
        Button redirectButtonNotes = (Button) root.findViewById(R.id.redirectButtonNotes);
        ExtendedFloatingActionButton extendedFabTask = (ExtendedFloatingActionButton) root.findViewById(R.id.extended_fab_task);
        ExtendedFloatingActionButton extendedFabNote = (ExtendedFloatingActionButton) root.findViewById(R.id.extended_fab_note);
        TextView dateTimeDisplay = (TextView) root.findViewById(R.id.text_display_date);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        tasksView = root.findViewById(R.id.tasks_list);
        resetTasksRecyclerView();


        //FAB
        extendedFabTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("inside new task fab");
                Toast toast = Toast.makeText(v.getContext(),
                        "Adding new Task",
                        Toast.LENGTH_SHORT);
                toast.show();
                Intent intent1 = new Intent(getActivity(), TaskActivity.class);
                intent1.putExtra("TaskActivity", "Add");
                someActivityResultLauncher.launch(intent1);
                // launches a new intent : launch task activity, and convey extra data by putting "Add" with intent. "Add" will open a new empty activity class, with no prefilled values, to the contrary of "Edit"
            }
        });
        extendedFabNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("inside new note fab");
                Toast toast = Toast.makeText(v.getContext(),
                        "Adding new Note",
                        Toast.LENGTH_SHORT);
                toast.show();
               Intent intent2 = new Intent(getActivity(), NoteActivity.class);
               intent2.putExtra("NoteActivity", "Add");
               someActivityResultLauncher.launch(intent2);
            } // similarly initiates a new intent to launch a new note activity (not edit note activity)
        });


        refreshQuoteButton.post(new Runnable(){
            @Override
            public void run() {
                refreshQuoteButton.performClick();
            }
        }); // click to fill data


        //load quote
        refreshQuoteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("getting new quote");
                QuotesRestRepository.getInstance().fetchQuote().observe(getViewLifecycleOwner(), this::populateQuoteContainer);
                //System.out.println("Quote: "+q.getText());
                Toast toast = Toast.makeText(v.getContext(),
                        "Getting a new Quote",
                        Toast.LENGTH_SHORT);
                toast.show();
            } // function on click use singleton instance and pass quote(if any) to below method

            @SuppressLint("SetTextI18n")
            public void populateQuoteContainer(Quote quote) {
                // checking quote (if Get request was successful returns quote, else returns null)
                if(quote != null){
                    if(quote.getAuthor() == null){
                        quote.setAuthor("Anonymous"); // fixing data quality issue
                    }
                    quoteDisplay.setText(quote.getText() + "\n~" + quote.getAuthor());
                }else{
                    quoteDisplay.setText("Failed to fetch a quote because you are not connected to the internet! Otherwise, the quote provider website is down.");

                }// sets the returned result in the layout
            }
        });


        redirectButtonTasks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("here");
                Toast toast = Toast.makeText(v.getContext(),
                        "Going to tasks",
                        Toast.LENGTH_SHORT);
                toast.show();
                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_tasks);

            }
        });// redirects user to task screen

        redirectButtonNotes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("here");
                Toast toast = Toast.makeText(v.getContext(),
                        "Going to notes",
                        Toast.LENGTH_SHORT);
                toast.show();
                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_notes);
            }
        }); // redirects users to home screen
        return root;
    }

    //recycler view methods
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    } // destroys view

    private void fetchTasksItems() {
        homeViewModel.getTasksMutable(getContext()).observe(getViewLifecycleOwner(),
                this::updateTasksList);
    } // gets an updated version of recycler view in view model

    private void setUpTasksRecyclerView() {
        adapterTasks = new TasksAdapter(tasks);
        adapterTasks.setNum(3); // for overview
        tasksView.setAdapter(adapterTasks);
        tasksView.setLayoutManager(new LinearLayoutManager(tasksView.getContext()));
    } // set up recycler view to top 3 tasks to not clutter home screen

    private void updateTasksList(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        adapterTasks.notifyDataSetChanged();
    } // called by fetchTasksItems... clears and re-adds all tasks to recycler view

    public void resetTasksRecyclerView(){
        tasks.clear();
        setUpTasksRecyclerView();
        fetchTasksItems();
    } // recycler view essential methods called

    public void onResume() {
        resetTasksRecyclerView();
        super.onResume();
    } // when come back to fragment, recall recyclerview
}