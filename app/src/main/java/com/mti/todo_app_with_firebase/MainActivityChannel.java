/*
 * Created by Tareq Islam on 9/26/18 12:51 AM
 *
 *  Last modified 9/26/18 12:51 AM
 */

package com.mti.todo_app_with_firebase;

import java.util.ArrayList;

public interface MainActivityChannel{

        void passDataToFragment(ArrayList<String> strings);

        boolean onBackPressed();
    }
