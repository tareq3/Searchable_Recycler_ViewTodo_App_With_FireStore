/*
 * Created by Tareq Islam on 9/26/18 2:49 PM
 *
 *  Last modified 9/26/18 2:49 PM
 */

package com.mti.todo_app_with_firebase.Repository.Main.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mti.todo_app_with_firebase.model.ToDo;

import java.util.List;

/***
 * Created by Tareq on 26,September,2018.
 */
@Dao
public interface Todo_Dao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from todo_table")
    LiveData<List<ToDo>> getAllTodos();

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert
    void insert(ToDo questions);

    @Query("DELETE FROM todo_table WHERE id =:id")
    void deleteOfID(String id);

    @Query("DELETE FROM todo_table")
    void deleteAll();
}
