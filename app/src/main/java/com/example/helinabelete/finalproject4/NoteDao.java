package com.example.helinabelete.finalproject4;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface NoteDao {

    @Query("SELECT * FROM " + Note.TABLE_NAME )
    List<Note> getNote();

    @Insert
    void addNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("DELETE FROM " + Note.TABLE_NAME)
    public void dropTheTable();

}
