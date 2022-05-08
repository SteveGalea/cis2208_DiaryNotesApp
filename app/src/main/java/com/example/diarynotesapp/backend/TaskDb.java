package com.example.diarynotesapp.backend;

import android.provider.BaseColumns;

public final class TaskDb {
    /* Inner class that defines the table contents */
    public static class TaskEntry implements BaseColumns {
        public TaskEntry() {}
        public static final String TABLE_NAME = "tasks";
        //public static final String _ID = "id";
        public static final String COLUMN_NAME_NAME = "_name";
        public static final String COLUMN_NAME_DETAILS = "_details";
        public static final String COLUMN_NAME_DUEDATE = "_dueDate";
        public static final String COLUMN_NAME_PROGRESS = "_progress";
    }
}
