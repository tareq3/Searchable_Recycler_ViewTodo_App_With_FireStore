/*
 * Created by Tareq Islam on 6/22/18 10:42 PM
 *
 *  Last modified 6/22/18 10:42 PM
 */

package com.mti.todo_app_with_firebase.Adapter;

import android.view.View;

/***
 * Created by Tareq on 22,June,2018.
 */
public interface ItemClickListener {

    void onClick(View view, int position,boolean isLongClick);

}
