/*
 * Created by Tareq Islam on 9/26/18 2:52 PM
 *
 *  Last modified 9/26/18 2:52 PM
 */

package com.mti.todo_app_with_firebase.Repository.Main.Room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mti.todo_app_with_firebase.model.ToDo;

/***
 * Created by Tareq on 26,September,2018.
 */

@Database(entities = {ToDo.class}, version = 1)
public abstract class Todo_Room_Database extends RoomDatabase {
    public abstract Todo_Dao mTodoDao();

    private static Todo_Room_Database instance;

  public   static Todo_Room_Database getDatabaseInstance(final Context context){
        if(instance==null){  //when instance null
            synchronized (Todo_Room_Database.class){ //more than one thread trying to access Syncronize them
             if(instance==null){
                 instance=Room.databaseBuilder(context.getApplicationContext(),
                         Todo_Room_Database.class,"todos")
                 // Wipes and rebuilds instead of migrating if no Migration object.
                 // Migration is not part of this codelab.
                 .fallbackToDestructiveMigration()
                         .addCallback(sRoomDatabaseCallback)
                         .build();
             }
            }
        }
        return instance;
    }
    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
        }
    };
}
