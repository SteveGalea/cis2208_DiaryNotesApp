package com.example.diarynotesapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.TasksUI.Task;
import com.example.diarynotesapp.TasksUI.TasksAdapter;
import com.example.diarynotesapp.api.model.Quote;
import com.example.diarynotesapp.api.rest.QuotesRestRepository;
import com.example.diarynotesapp.databinding.FragmentHomeBinding;
import com.example.diarynotesapp.ui.NoteActivity;
import com.example.diarynotesapp.ui.TaskActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class HomeFragment extends Fragment{

    FragmentHomeBinding binding;
    private TextView dateTimeDisplay;
    private TextView quoteDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Button refreshQuoteButton;
    private Button redirectButtonTasks;
    private Button redirectButtonNotes;
    private ExtendedFloatingActionButton extendedFabTask;
    private ExtendedFloatingActionButton extendedFabNote;
    private HomeViewModel homeViewModel;
    private TasksAdapter adapter;
    private RecyclerView tasksView;
    private MaterialCardView card;
    private int currentTasks;
    private int totalTasks;
    private List<Task> tasks = new ArrayList<>();
    //private OnClickListener onClickListener = new OnClickListener() {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        extendedFabTask = (ExtendedFloatingActionButton) root.findViewById(R.id.extended_fab_task);

        extendedFabNote = (ExtendedFloatingActionButton) root.findViewById(R.id.extended_fab_note);
        //adding date
        //get view and calendar
        dateTimeDisplay = (TextView)root.findViewById(R.id.text_display_date);
        calendar = Calendar.getInstance();
        //set format
        //dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);



        TextView taskCount = root.findViewById(R.id.task_total);

        taskCount.setText("("+currentTasks+" visible/"+totalTasks+" total)");
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
                    intent1.putExtra("Activity", "Add");
                    startActivity(intent1);
                    essentialRecyclerView();
                    //((Button)root.findViewById(R.id.navigation_home)).performClick();
                }
            });
            extendedFabNote.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    System.out.println("here");
                    Toast toast = Toast.makeText(v.getContext(),
                            "Adding new Note",
                            Toast.LENGTH_SHORT);
                    toast.show();
                   Intent intent2 = new Intent(getActivity(), NoteActivity.class);
                    startActivity(intent2);
                }
            });
        //quote
        quoteDisplay = (TextView) root.findViewById(R.id.quote);


        refreshQuoteButton = (Button) root.findViewById(R.id.refreshQuoteButton);
        redirectButtonTasks = (Button) root.findViewById(R.id.redirectButtonTasks);
        redirectButtonNotes = (Button) root.findViewById(R.id.redirectButtonNotes);

        refreshQuoteButton.post(new Runnable(){
            @Override
            public void run() {
                refreshQuoteButton.performClick();
            }
        });


        refreshQuoteButton.post(new Runnable(){
            @Override
            public void run() {
                refreshQuoteButton.performClick();
            }
        });
        System.out.println("button clicked to fill data");


        //load quote
        refreshQuoteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("here");
                QuotesRestRepository.getInstance().fetchQuote().observe(getViewLifecycleOwner(), this::populateQuoteContainer);
                //System.out.println("Quote: "+q.getText());
                Toast toast = Toast.makeText(v.getContext(),
                        "Getting a new Quote",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

            public void populateQuoteContainer(Quote quote) {
                // checking quote (if Get request was successful returns quote, else returns null)
                if(quote != null){
                    if(quote.getAuthor() == null){
                        quote.setAuthor("Anonymous"); // fixing data quality issue
                    }
                    quoteDisplay.setText(quote.getText() + "\n~" + quote.getAuthor());
                }else{
                    quoteDisplay.setText("Failed to fetch a quote because you are not connected to the internet! Otherwise, the quote provider website is down.");

                }
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
                //Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_tasks);
                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_tasks);

            }
        });
        redirectButtonNotes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("here");
                Toast toast = Toast.makeText(v.getContext(),
                        "Going to notes",
                        Toast.LENGTH_SHORT);
                toast.show();

                // bottom navigation click

                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_notes);


            }
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        tasksView = root.findViewById(R.id.tasks_list);

        essentialRecyclerView();


        /*
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                taskCount.setText("("+currentTasks+" visible/"+totalTasks+" total)");

                Toast.makeText(root.getContext(), "Refreshing", Toast.LENGTH_SHORT).show();
                setUpRecyclerView();
                fetchItems();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });*/
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //tasks.clear();
        binding = null;
    }

    private void essentialRecyclerView(){
        setUpRecyclerView();
        fetchItems();
        /*ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                essentialRecyclerView();
                System.out.println("Ran now");
            }
        }, 0, 4, TimeUnit.SECONDS);*/
    }
    private void fetchItems() {
        homeViewModel.getTasksMutable(getContext()).observe(getViewLifecycleOwner(),
                this::updateTasksList);
    }
    private void setUpRecyclerView() {
        //adapter = new TasksAdapter(tasks,5);
        adapter = new TasksAdapter(tasks);
        totalTasks= adapter.getItemCount();
        adapter.setNum(3);
        currentTasks = adapter.getItemCount();
        tasksView.setAdapter(adapter);
        tasksView.setLayoutManager(new LinearLayoutManager(tasksView.getContext()));
    }
    private void updateTasksList(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        totalTasks= adapter.getItemCount();
        TasksAdapter temp = new TasksAdapter(tasks);
        currentTasks = temp.getItemCount();
        adapter.notifyDataSetChanged();
    }

}