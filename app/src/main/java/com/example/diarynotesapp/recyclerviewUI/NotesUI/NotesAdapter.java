package com.example.diarynotesapp.recyclerviewUI.NotesUI;


import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diarynotesapp.R;
import com.example.diarynotesapp.recyclerviewUI.TasksUI.Task;
import com.example.diarynotesapp.backend.DbHelperTasks;
import com.example.diarynotesapp.ui.NoteActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends
        RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<Note> items;
    private int num = -1;
    public NotesAdapter(List<Note> items) {
        this.items = items;
    }
    public void setNum(int num){
        this.num = num;
    }

    //essential methods
    public String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.layout_notes, parent,
                false);
        return new ViewHolder(itemView);
    }
    ImageView imageView;
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Note item = items.get(position);

        TextView nameTextView = holder.nameTextView;
        TextView dateTextView = holder.dateTextView;
        TextView notesTextView = holder.notesTextView;
        TextView noteIdTextView = holder.noteIdTextView;
        ImageView imageViewCard = holder.imageView;

        String name = "Note: "+item.getTitle();
        String date = "\tLast Update: "+item.getDate();
        String notes = "\tNotes:\n\t"+item.getNoteText();
        //String?
        String imageUrl = item.getImageURL();
        String noteId = item.getId()+"";

        Button favBtn = holder.favBtn;
        if(item.getFavourite().equals("Favourites")){
            favBtn.setText("Undo");
        }else{
            favBtn.setText("Favourites");
        }
        if(!imageUrl.equals("")) {
            imageViewCard.setImageBitmap(StringToBitMap(item.getImageURL()));
        }
        nameTextView.setText(name);
        dateTextView.setText(date);
        notesTextView.setText(notes);
        noteIdTextView.setText(noteId);
        //favBtn.performClick();

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


    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Note> filterlist) {
        items = filterlist;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView notesTextView;
        public TextView dateTextView;
        public ImageView imageView;
        public TextView noteIdTextView;

        public Button favBtn;
        public Button editBtn;
        public Button deleteBtn;
        public ViewHolder(final View itemView) {
            super(itemView);
            nameTextView = (TextView)
                    itemView.findViewById(R.id.noteTitle);
            dateTextView = (TextView)
                    itemView.findViewById(R.id.date_update);
            notesTextView = (TextView)
                    itemView.findViewById(R.id.notes);
            imageView = (ImageView) itemView.findViewById(R.id.note_img);
            noteIdTextView = (TextView)
                    itemView.findViewById(R.id.noteId);
            favBtn = itemView.findViewById(R.id.favouriteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemOnClick(v);
                }

            });
            favBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    int id = Integer.parseInt(noteIdTextView.getText().toString());
                    DbHelperTasks dbHelperNotes = new DbHelperTasks(v.getContext());
                    Note note = dbHelperNotes.getNoteById(id);




                    if(note.getFavourite().equals("Favourites")){
                        favBtn.setText("Undo");
                        note.setFavourite("");
                        Toast.makeText(v.getContext(), note.getTitle()+" Added to Favourites", Toast.LENGTH_SHORT)
                                .show();
                    }else if(note.getFavourite().equals("")){
                        favBtn.setText("Favourite");
                        note.setFavourite("Favourites");
                        Toast.makeText(v.getContext(), note.getTitle()+" Removed from Favourites", Toast.LENGTH_SHORT)
                                .show();
                    }
                    dbHelperNotes.updateNoteById(note);
                    dbHelperNotes.close();

                }
            });
            editBtn.setOnClickListener(new View.OnClickListener(){
                // GetContent creates an ActivityResultLauncher<String> to allow you to pass
// in the mime type you'd like to allow the user to select
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(noteIdTextView.getText().toString());
                    Intent intent = new Intent(v.getContext(), NoteActivity.class);
                    Toast.makeText(v.getContext(), "Editing "+id, Toast.LENGTH_LONG).show();

                    intent.putExtra("ID", id);
                    intent.putExtra("NoteActivity", "Edit");
                    v.getContext().startActivity(intent);
                    //someActivityResultLauncher.launch(intent);
                    //set edit details

                    notifyDataSetChanged();

                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    System.out.println("Clicked Delete");
                    int pos = getAdapterPosition(); // position of card in recycler view list
                    DbHelperTasks dbHelperNotes = new DbHelperTasks(v.getContext());
                    long removeId = items.get(pos).getId();

                    dbHelperNotes.removeNoteById(removeId);
                    items.remove(items.get(pos));
                    notifyDataSetChanged();
                    dbHelperNotes.close();
                }
            });
        }

        @SuppressLint("ResourceAsColor")
        public void itemOnClick(View v) {

            int id = Integer.parseInt(noteIdTextView.getText().toString());
            Intent intent = new Intent(v.getContext(), NoteActivity.class);
            intent.putExtra("ID", id);
            intent.putExtra("NoteActivity", "Edit");
            v.getContext().startActivity(intent);
            //someActivityResultLauncher.launch(intent);
            //set edit details

            notifyDataSetChanged();
        }
    }
}
