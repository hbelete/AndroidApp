package com.example.helinabelete.finalproject4;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;


@Database(entities = {Note.class},version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    private static final String DB_NAME = "Notes_Database.db";
    private static NotesDatabase INSTANCE;

    public abstract NoteDao noteDao();

    public static NotesDatabase getNotesDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, NotesDatabase.class, DB_NAME).build();

        }
        return INSTANCE;
    }


}
