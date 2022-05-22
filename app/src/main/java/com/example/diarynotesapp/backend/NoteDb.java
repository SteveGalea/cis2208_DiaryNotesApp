package com.example.diarynotesapp.backend;

import android.provider.BaseColumns;

public final class NoteDb {
    /* Inner class that defines the table contents */
    public static class NoteEntry implements BaseColumns {
        public NoteEntry() {}
        public static final String TABLE_NAME = "notes";
        //public static final String _ID = "id";
        public static final String COLUMN_NAME_TITLE = "_title";
        public static final String COLUMN_NAME_NOTE_TEXT = "_noteText";
        public static final String COLUMN_NAME_IMAGE_URL = "_imageURL";
        public static final String COLUMN_NAME_FAVOURITE = "_favourite";
        public static final String COLUMN_NAME_DATE = "_date";
    }
}
