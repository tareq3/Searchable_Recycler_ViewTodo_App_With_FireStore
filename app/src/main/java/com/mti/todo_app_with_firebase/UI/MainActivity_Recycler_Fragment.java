/*
 * Created by Tareq Islam on 9/26/18 12:53 AM
 *
 *  Last modified 9/26/18 12:45 AM
 */

/*
 * Created by Tareq Islam on 9/24/18 1:01 PM
 *
 *  Last modified 9/24/18 12:57 PM
 */

/*
 * Created by Tareq Islam on 9/24/18 12:48 PM
 *
 *  Last modified 9/24/18 12:48 PM
 */

package com.mti.todo_app_with_firebase.UI;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mti.todo_app_with_firebase.Adapter.ItemClickDataChannel;
import com.mti.todo_app_with_firebase.Adapter.ListItemAdapter;
import com.mti.todo_app_with_firebase.MainActivity;
import com.mti.todo_app_with_firebase.MainActivityChannel;
import com.mti.todo_app_with_firebase.R;
import com.mti.todo_app_with_firebase.model.ToDo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/***
 * Created by Tareq on 24,September,2018.
 */


public class MainActivity_Recycler_Fragment extends Fragment implements MainActivityChannel,ItemClickDataChannel {

   public interface MainActivityRecyclerFragmentChannel{
        void passResult(ArrayList<?> params); //passes the Task Success result

        void passData(ArrayList<?> params); //passes the data from frag to activity
    }

    //Primary List of Data
    List<ToDo> mToDoList = new ArrayList<ToDo>(
            Arrays.asList(
                    new ToDo("1", "Eat", "eating should be controlled"),
                    new ToDo("2", "Walk", "walking is good for health"),
                    new ToDo("3", "Salat", "Is the best meditation in this world")
            )
    );


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManagerRecyler;

    ListItemAdapter mAdapter;

    MainActivityRecyclerFragmentChannel mMainActivityRecyclerFragmentChannel;

    public static MainActivity_Recycler_Fragment newInstance() {
        return new MainActivity_Recycler_Fragment();
    }

    Context mContext;

    //Need to change the Cast Activity name on attach with any activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        //Todo: Initializing the interfce which is implemented
        ((MainActivity) getActivity()).mMainActivityChannel = this; //initializing interface in this class


        //As mainActivity using Fragment Interface || This is something odd
        mMainActivityRecyclerFragmentChannel = (MainActivityRecyclerFragmentChannel) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);//needed for having menu  function like onCreateOptionMenu to work
        return inflater.inflate(R.layout.recyclerview_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mRecyclerView = getView().findViewById(R.id.listTodo_fragment);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManagerRecyler = new LinearLayoutManager(getView().getContext());

        mRecyclerView.setLayoutManager(mLayoutManagerRecyler);
        mAdapter = new ListItemAdapter(mContext);

        mAdapter.setItemClickDataChannel(this);// initializing the SetItemClickDataChannel interface from Adapter


        loadData();
    }


    private void loadData() {

        mAdapter.updateAdapter(mToDoList); //update data for adapter
        mRecyclerView.setAdapter(mAdapter); //set Adapter once

    }

    public void addData(String s, String s1) {

        String id = UUID.randomUUID().toString();
        mToDoList.add(new ToDo(id, s, s1));

        mAdapter.updateAdapter(mToDoList);
        //Send meesage to Activity Data Loaded using interface
        mMainActivityRecyclerFragmentChannel.passResult(new ArrayList<Object>(Arrays.asList("Data Updated")));

    }

    private void deleteItem(int order) {
        Toast.makeText(mContext, "Delete Item at " + order, Toast.LENGTH_SHORT).show();
        mToDoList.remove(order);

        mAdapter.updateAdapter(mToDoList);
        //Send meesage to Activity Data Loaded using interface
        mMainActivityRecyclerFragmentChannel.passResult(new ArrayList<Object>(Arrays.asList("Data Updated")));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }


    public SearchView mSearchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //

        //Associate searchable configuration with the Searchview
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        //Listening to serach queary changes
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //filter recycler view when querry submitted
                Toast.makeText(mContext, "Submitted", Toast.LENGTH_SHORT).show();

                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //filter recycler view when text is changed
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void passDataToFragment(ArrayList<String> strings) {
        addData(strings.get(0), strings.get(1));

    }

    @Override
    public boolean onBackPressed() {
        // close search view on back button pressed
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            return false; //means this method will continue run with main onBackPress method
        }

        return true; //means job done should skip the implemented method
    }



    @Override
    public void onItemClickPassData(ArrayList<?> params) {
        Toast.makeText(mContext, "" + params.get(0).toString(), Toast.LENGTH_SHORT).show();

        mMainActivityRecyclerFragmentChannel.passData(params);
    }




}

