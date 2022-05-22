package com.example.diarynotesapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diarynotesapp.ImageResizer;
import com.example.diarynotesapp.recyclerviewUI.NotesUI.Note;
import com.example.diarynotesapp.R;
import com.example.diarynotesapp.backend.DbHelperTasks;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public class NoteActivity extends AppCompatActivity {

    //declarations
    MaterialToolbar topAppBar;
    // One Button
    Button selectImage;

    // One Preview Image
    ImageView previewImage;

    Button saveButton;
    EditText noteTitle;
    EditText notesText;
    Bitmap fetchedImageUri;

    TextInputLayout tilNoteTitle,tilNoteText;

    //initialisation
    int RESULT_LOAD_IMG = 1;


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
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // register the UI widgets with their appropriate IDs
        selectImage = findViewById(R.id.selectImageButton);
        previewImage = findViewById(R.id.notes_image);


        // fields initialisations
        saveButton = (Button) findViewById(R.id.saveBtn);
        noteTitle = (EditText) findViewById(R.id.noteTitle);
        notesText = (EditText) findViewById(R.id.notesText);

        tilNoteTitle = findViewById(R.id.textField);
        tilNoteText = findViewById(R.id.detailsField);
        //imageView = findViewById(R.id.no);

        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                System.out.println("CLICKED BACK");
                finish();
            }
        });

        if (getIntent().getStringExtra("NoteActivity").equals("Edit")) {
            int userId = getIntent().getIntExtra("ID", -2); // if not found, return -2
            if (userId != -2) {

                DbHelperTasks db = new DbHelperTasks(this);
                Note item = db.getNoteById(userId);
                noteTitle.setText(item.getTitle());
                notesText.setText(item.getNoteText());
                previewImage.setImageBitmap(StringToBitMap(item.getImageURL()));
                //previewImage.setImageURI(item.getImageURL());
            }
        }
        // handle the Choose Image button to trigger
        // the image chooser function
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                //imageChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSave();
            }
        });
        //addvalidations listeners
        tilNoteText.getEditText().addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //validateNoteTitle();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateNoteTitle();
            }
        });

        tilNoteText.getEditText().addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //validateNoteText();
            }

            @Override
            public void afterTextChanged(Editable editable) {

                validateNoteText();
            }
        });


    }

    // custom validation
    private boolean validateImage(){
        if(fetchedImageUri == null){
            return false;
        }
        return true;
    }

    private boolean validateNoteText() {
        String value = tilNoteText.getEditText().getText().toString().trim();
        if(value.isEmpty()){
            tilNoteText.setError("Note Title Field can not be left empty!");
            return false;
        }
        else if(value.length()>9999){
            tilNoteText.setError("Too many characters inputted! (Maximum 9999 characters)");
            return false;
        }
        else{
            tilNoteText.setError(null);
            tilNoteText.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateNoteTitle() {
        String value = tilNoteTitle.getEditText().getText().toString().trim();
        if(value.isEmpty()){
            tilNoteTitle.setError("Note Title Field can not be left empty!");
            return false;
        }
        else if(value.length()>50){
            tilNoteTitle.setError("Too many characters inputted! (Maximum 50 characters)");
            return false;
        }
        else{
            tilNoteTitle.setError(null);
            tilNoteTitle.setErrorEnabled(false);
            return true;
        }
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



            if (requestCode == RESULT_LOAD_IMG) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    fetchedImageUri = selectedImage;
                    previewImage.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_top_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DbHelperTasks db = new DbHelperTasks(this);
        int userId = getIntent().getIntExtra("ID", -2); // if not found, return -2
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.remove:
                Intent _result = new Intent();
                Toast.makeText(this, "Removing from Database", Toast.LENGTH_SHORT)
                        .show();
                db.removeNoteById(userId);

                _result.putExtra("Deleted Note", userId);


                setResult(Activity.RESULT_OK, _result);
                finish();
                break;
            case R.id.save:

                Toast.makeText(this, "Attempting to save task", Toast.LENGTH_SHORT)
                        .show();
                this.onClickSave();

                break;
            default:
                break;
        }

        return true;
    }
    private void onClickSave() {

        //String dateValue = getDate(simpleDatePicker);
        String noteTitleValue = noteTitle.getText().toString();
        String notesTextValue = notesText.getText().toString();


        Intent _result = new Intent();



        if(!validateNoteTitle() | !validateNoteText() | !validateImage()){
            return;
        }


        Bitmap fullsizedImage = fetchedImageUri;
        Bitmap resizedImage = ImageResizer.reduceBitmapSize(fullsizedImage, 240000);

        Note note;
        if (getIntent().getStringExtra("NoteActivity").equals("Add"))
        {
            note = new Note(
                    -1,
                    noteTitleValue,
                    notesTextValue,
                    BitMapToString(resizedImage),
                    "");
            DbHelperTasks dbHelperNotes = new DbHelperTasks(this);

            long id = dbHelperNotes.insertNote(note);

            _result.putExtra("Add Note", (Serializable) note);

        }
        else{
            // editing
            int userId = getIntent().getIntExtra("ID", -2); // if not found, return -2

            if(userId != -2){

                note = new Note(
                        userId,
                        noteTitleValue,
                        notesTextValue,
                        BitMapToString(resizedImage),
                        "");
                DbHelperTasks dbHelperNotes = new DbHelperTasks(this);
                dbHelperNotes.updateNoteById(note);

                _result.putExtra("Edit Note", (Serializable) note);
            }else{
                note = new Note(
                        -1,
                        noteTitleValue,
                        notesTextValue,
                        BitMapToString(resizedImage),
                        "");
                DbHelperTasks dbHelperNotes = new DbHelperTasks(this);

                long id = dbHelperNotes.insertNote(note);
                _result.putExtra("Add Note", (Serializable) note);

            }
        }

        setResult(Activity.RESULT_OK, _result);
        finish();

    }
}