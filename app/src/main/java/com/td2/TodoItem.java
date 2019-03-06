package com.td2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "TodoItem")
public class TodoItem {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String label;
    private boolean tache;
    private int tag;

    public TodoItem(long id, int tag, String label, boolean tache){
        this.id = id;
        this.label = label;
        this.tag = tag;
        this.tache = tache;
    }

    public boolean getTache() {
        return tache;
    }

    public void setTache(boolean tache) {
        this.tache = tache;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
