package com.example.jamz;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "note")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String content;
    private long creationDate;

    // Constructor without the id parameter
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.creationDate = System.currentTimeMillis();
    }

    // Constructor with all parameters (including id)
    // Annotate with @Ignore to let Room know it shouldn't use this constructor
    @Ignore
    public Note(long id, String title, String content, long creationDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
}