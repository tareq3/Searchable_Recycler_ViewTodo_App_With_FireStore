/*
 * Created by Tareq Islam on 9/27/18 9:03 PM
 *
 *  Last modified 9/27/18 9:03 PM
 */

package com.mti.todo_app_with_firebase.Repository.Main.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 * Created by Tareq on 27,September,2018.
 */
public class TodoApiClient {

    public static final String BASE_URL="http://192.168.43.246/";
    private static Retrofit retrofit=null;

    public static Retrofit getClient(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
