package com.example.diarynotesapp.ui.notes;

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

import com.example.diarynotesapp.recyclerviewUI.NotesUI.Note;
import com.example.diarynotesapp.recyclerviewUI.NotesUI.NotesAdapter;
import com.example.diarynotesapp.R;
import com.example.diarynotesapp.databinding.FragmentNotesBinding;
import com.example.diarynotesapp.ui.NoteActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {
    private FragmentNotesBinding binding;
    private ExtendedFloatingActionButton extendedFabNote;
    private NotesViewModel notesViewModel;
    private NotesAdapter adapter;
    private RecyclerView notesView;

    private List<Note> notes = new ArrayList<>();

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
        notesViewModel.geNotesMutable(getContext()).observe(getViewLifecycleOwner(),
                this::updateNotesList);
    }

    private void setUpRecyclerView() {
        //adapter = new TasksAdapter(tasks,5);
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