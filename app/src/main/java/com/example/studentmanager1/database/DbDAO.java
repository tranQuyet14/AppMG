package com.example.studentmanager1.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentmanager1.model.Class;
import com.example.studentmanager1.model.Student;
import java.util.List;

@Dao
public interface DbDAO{
    @Insert
    void insertClass(Class mClass);

    @Insert
    void insertStudent(Student student);

    @Query("SELECT * FROM class")
    List<Class> getListClass();

    @Query("SELECT * FROM student")
    List<Student> getListStudent();

    @Update
    void updateClass(Class mclass);

    @Update
    void updateStudent(Student student);

    @Delete
    void deleteClass(Class mClass);

    @Delete
    void deleteStudent(Student student);

    @Query("SELECT * FROM class WHERE nameclass=:nameClass")
    List<Class> checkExistsNameClass(String nameClass);

    @Query("SELECT * FROM class WHERE codeClass=:codeClass")
    List<Class> checkExistsCodeClass(String codeClass);

    @Query("SELECT * FROM student WHERE codeStudent=:codeStudent")
    List<Student> checkExistCodeStudent(String codeStudent);
    @Query("SELECT * FROM student WHERE nameClass = :nameClass")
    List<Student> getListStudent1(String nameClass);

    @Query("SELECT * FROM class WHERE nameClass LIKE '%' || :name ||'%'")
    List<Class> searchClass(String name);

}
