package com.example.diarynotesapp.ui.home;

import static com.example.diarynotesapp.R.id.quote_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.api.model.Quote;
import com.example.diarynotesapp.api.rest.QuotesRestRepository;
import com.example.diarynotesapp.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        System.out.println("in here1");
        //load quote
        //final TextView quoteContainer = root.findViewById(quote_container);
        //loadQuote();
        return root;
    }
    public void loadQuote() {
        System.out.println("in here2");
        //QuotesRestRepository.getInstance().fetchRandomQuote();//.observe(getViewLifecycleOwner(), this::populateQuoteContainer);

    }


    private void populateQuoteContainer(Quote quote) {

        TextView text2 = binding.getRoot().findViewById(quote_container);
        if(quote != null){
           // TextView text;
            //text = new TextView(this.getContext());
            text2.setText(quote.getText() + " : " + quote.getAuthor());
        }else{
            //TextView
             //       text = binding.getRoot().findViewById(quote_container);
            //text.setText("Failed to fetch users");
            text2.setText("Failed to fetch users");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}