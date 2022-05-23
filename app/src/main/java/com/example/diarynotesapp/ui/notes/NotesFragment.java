package com.example.diarynotesapp.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.diarynotesapp.recyclerviewUI.NotesUI.Note;
import com.example.diarynotesapp.recyclerviewUI.NotesUI.NotesAdapter;
import com.example.diarynotesapp.R;
import com.example.diarynotesapp.databinding.FragmentNotesBinding;
import com.example.diarynotesapp.ui.NoteActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {
    private FragmentNotesBinding binding;
    private ExtendedFloatingActionButton extendedFabNote;
    private NotesViewModel notesViewModel;
    private NotesAdapter adapter;
    private RecyclerView notesView;
    private TextInputEditText searchTextInput;
    private Chip favourites, refresh;
    private boolean favFilter = false;
    private ArrayList<Note> notes = new ArrayList<>();

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
        notesViewModel =
                new ViewModelProvider(this).get(NotesViewModel.class);

        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotes;
        notesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        extendedFabNote = (ExtendedFloatingActionButton) root.findViewById(R.id.extended_fab);

        //FAB
        extendedFabNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("here");
                Toast toast = Toast.makeText(v.getContext(),
                        "Adding new Note",
                        Toast.LENGTH_SHORT);
                toast.show();
                Intent intent1 = new Intent(getActivity(), NoteActivity.class);
                intent1.putExtra("NoteActivity", "Add");
                someActivityResultLauncher.launch(intent1);
            }
        });

        notesView = root.findViewById(R.id.notes_list);
        favourites = root.findViewById(R.id.favouriteChip);
        favourites.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                resetRecyclerView();
                if(favourites.isChecked())
                {
                    handleFavouritesChip(favFilter);
                    favFilter = false;
                }else{
                    handleFavouritesChip(favFilter);
                    favFilter = true;
                }


            }

        });
        refresh = root.findViewById(R.id.refreshChip);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetRecyclerView();
            }

        });

        resetRecyclerView();

        searchTextInput = root.findViewById(R.id.searchNotes);
        searchTextInput.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        return root;
    }// on create view run this


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Note> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Note item : notes) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
                Toast.makeText(this.getContext(), "See the matching items!", Toast.LENGTH_SHORT).show();
            }
        }
        if (filteredlist.isEmpty()) {
        } else {
            adapter.filterList(filteredlist);
        }
    } // search

    private void handleFavouritesChip(boolean value) {
        // creating a new array list to filter our data.
        ArrayList<Note> filteredlist = new ArrayList<>();

        String compareElem = "";
        compareElem = "Favourites";
        for (Note item : notes) {
            // checking if the entered string matched with any item of our recycler view.
            String compareFav = item.getFavourite();
            if(compareFav == null){
                compareFav="";
            }
            if (compareFav.toLowerCase().contains(compareElem.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                System.out.println(item.getTitle()+": "+item.getFavourite()+"");
                filteredlist.add(item);
                Toast.makeText(this.getContext(), "Filtered by filter click!", Toast.LENGTH_SHORT).show();
            }
        }
        // running a for loop to compare elements
        adapter.filterList(filteredlist);
    } //

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //tasks.clear();
        binding = null;
    }

    private void fetchItems() {
        notesViewModel.getNotesMutable(getContext()).observe(getViewLifecycleOwner(),
                this::updateNotesList);
    }

    private void setUpRecyclerView() {
        //adapter = new AlertsAdapter(tasks,5);
        adapter = new NotesAdapter(notes);
        notesView.setAdapter(adapter);
        notesView.setLayoutManager(new LinearLayoutManager(notesView.getContext()));
    }

    private void updateNotesList(List<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        adapter.notifyDataSetChanged();
    }
    public void resetRecyclerView(){
        notes.clear();
        setUpRecyclerView();
        fetchItems();
    }

    public void onResume() {
        resetRecyclerView();
        super.onResume();
    }
}