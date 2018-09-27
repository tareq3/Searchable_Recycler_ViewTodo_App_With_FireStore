/*
 * Created by Tareq Islam on 9/27/18 9:04 PM
 *
 *  Last modified 9/27/18 9:04 PM
 */

package com.mti.todo_app_with_firebase.Repository.Main.Api;

import com.mti.todo_app_with_firebase.model.ToDo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/***
 * Created by Tareq on 27,September,2018.
 */
public interface TodoApiServices {

    @GET("/test.json")
    Call<List<ToDo>> getTodoDatas();
}
