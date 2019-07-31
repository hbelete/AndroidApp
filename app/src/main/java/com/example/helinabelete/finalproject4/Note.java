package com.example.helinabelete.finalproject4;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static com.example.helinabelete.finalproject4.Note.TABLE_NAME;


@Entity(tableName = TABLE_NAME)
public class Note {


    public static final String TABLE_NAME = "notes";


    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private String date;

    public Note() {

    }

    @Ignore
    public Note(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        //this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

}