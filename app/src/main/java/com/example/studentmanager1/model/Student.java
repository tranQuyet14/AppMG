package com.example.studentmanager1.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "student")
public class Student implements Serializable {
    public static final int TYPE_STORAGE=1;
    public static final int TYPE_DRAWABLE=2;
    public static final int TYPE_ASSETS=3;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String codeStudent;
    private String nameStudent;
    private String nameClass;
    private String pathImage;
    private int typeImage;

    public int getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(int typeImage) {
        this.typeImage = typeImage;
    }

    public Student(String codeStudent, String nameStudent, String nameClass) {
        this.codeStudent = codeStudent;
        this.nameStudent = nameStudent;
        this.nameClass = nameClass;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeStudent() {
        return codeStudent;
    }

    public void setCodeStudent(String codeStudent) {
        this.codeStudent = codeStudent;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }
}
