/*
 * Created by Tareq Islam on 9/26/18 2:41 PM
 *
 *  Last modified 9/26/18 2:41 PM
 */

package com.mti.todo_app_with_firebase.Repository.Main;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.mti.todo_app_with_firebase.Repository.Main.Room.Todo_Dao;
import com.mti.todo_app_with_firebase.Repository.Main.Room.Todo_Room_Database;
import com.mti.todo_app_with_firebase.model.ToDo;

import java.util.List;

/***
 * Created by Tareq on 26,September,2018.
 */
public class MainActivityRepository {

    LiveData<List<ToDo>> mList_Todo_Data;

    Todo_Dao m_TodoDao;

    public MainActivityRepository(Application application) {

        Todo_Room_Database todo_room_database=Todo_Room_Database.getDatabaseInstance(application);
        m_TodoDao=todo_room_database.mTodoDao();
        mList_Todo_Data=m_TodoDao.getAllTodos();

    }

    public LiveData<List<ToDo>> getList_Todo_Data() {
        return mList_Todo_Data;
    }

    public void insertTodo(final ToDo toDo){
        new Thread(new Runnable() {
            @Override
            public void run() {
                m_TodoDao.insert(toDo);
            }
        }).start();
    }

    public void deleteOfID(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                m_TodoDao.deleteOfID(id);
            }
        }).start();
    }
}
