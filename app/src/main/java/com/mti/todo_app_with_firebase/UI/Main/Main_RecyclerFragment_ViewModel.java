/*
 * Created by Tareq Islam on 9/26/18 2:43 PM
 *
 *  Last modified 9/26/18 2:43 PM
 */

package com.mti.todo_app_with_firebase.UI.Main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mti.todo_app_with_firebase.Repository.Main.MainActivityRepository;
import com.mti.todo_app_with_firebase.model.ToDo;

import java.util.List;

/***
 * Created by Tareq on 26,September,2018.
 */
public class Main_RecyclerFragment_ViewModel extends AndroidViewModel {

    LiveData<List<ToDo>> mListTodo;

    MainActivityRepository mMainActivityRepository;

    public Main_RecyclerFragment_ViewModel(@NonNull Application application) {
        super(application);
        mMainActivityRepository=new MainActivityRepository(application);
        mListTodo=mMainActivityRepository.getList_Todo_Data();

    }

    public LiveData<List<ToDo>> getListTodo() {
        return mListTodo;
    }

    public void insertTodo(ToDo todo){
        mMainActivityRepository.insertTodo(todo);
    }
    public void deleteOfID(String id){
        mMainActivityRepository.deleteOfID(id);
    }

}
