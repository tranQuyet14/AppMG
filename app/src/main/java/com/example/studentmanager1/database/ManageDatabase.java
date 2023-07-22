package com.example.studentmanager1.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.studentmanager1.model.Class;
import com.example.studentmanager1.model.Student;

@Database(entities = {Class.class,Student.class},version = 1)
public abstract class ManageDatabase extends RoomDatabase  {
    private static final String DATABASE_NAME="data.db";
    private static ManageDatabase instance;

    public static synchronized ManageDatabase getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(), ManageDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract DbDAO dbDAO();
}
