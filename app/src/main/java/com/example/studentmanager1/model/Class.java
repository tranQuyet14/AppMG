package com.example.studentmanager1.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "class")
public class Class implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String codeClass;
    private String nameClass;

    public Class() {
    }

    public Class(String codeClass, String nameclass) {
        this.codeClass = codeClass;
        this.nameClass = nameclass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeClass() {
        return codeClass;
    }

    public void setCodeClass(String codeClass) {
        this.codeClass = codeClass;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }
}
