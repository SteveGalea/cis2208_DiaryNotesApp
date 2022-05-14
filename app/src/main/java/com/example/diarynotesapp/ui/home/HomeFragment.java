package com.example.diarynotesapp.ui.home;

import static com.example.diarynotesapp.R.id.quote;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.api.model.Quote;
import com.example.diarynotesapp.api.rest.QuotesRestRepository;
import com.example.diarynotesapp.databinding.FragmentHomeBinding;
import com.example.diarynotesapp.ui.TaskActivity;
import com.example.diarynotesapp.ui.tasks.TasksFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class HomeFragment extends Fragment{

    FragmentHomeBinding binding;
    private TextView dateTimeDisplay;
    private TextView quoteDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Button refreshQuoteButton;
    private Button redirectButtonTasks;
    private ExtendedFloatingActionButton extendedFabTask;
    //private OnClickListener onClickListener = new OnClickListener() {



        public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        extendedFabTask = (ExtendedFloatingActionButton) root.findViewById(R.id.extended_fab_task);

        //adding date
        //get view and calendar
        dateTimeDisplay = (TextView)root.findViewById(R.id.text_display_date);
        calendar = Calendar.getInstance();
        //set format
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        //final TextView textView = binding.textHome;
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        //FAB
            extendedFabTask.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    System.out.println("here");
                    Toast toast = Toast.makeText(v.getContext(),
                            "Adding new Task",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    startActivity(intent);
                /*TasksFragment fragment2 = new TasksFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_fragment, fragment2);
                fragmentTransaction.commit();*/

                }
            });
        //quote
        quoteDisplay = (TextView) root.findViewById(R.id.quote);


        refreshQuoteButton = (Button) root.findViewById(R.id.refreshQuoteButton);
        redirectButtonTasks = (Button) root.findViewById(R.id.redirectButtonTasks);

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
                /*TasksFragment fragment2 = new TasksFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_fragment, fragment2);
                fragmentTransaction.commit();*/

            }
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}