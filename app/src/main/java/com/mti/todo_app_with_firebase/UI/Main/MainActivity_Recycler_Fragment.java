

package com.mti.todo_app_with_firebase.UI.Main;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mti.todo_app_with_firebase.Adapter.Main.ItemClickDataChannel;
import com.mti.todo_app_with_firebase.Adapter.Main.ListItemAdapter;
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

    //Todo: Member variables here
    private  Main_RecyclerFragment_ViewModel mMain_recyclerFragment_viewModel;


    private List<ToDo> mToDoList = new ArrayList<ToDo>();

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManagerRecyler;

    private ListItemAdapter mAdapter;

    private MainActivityRecyclerFragmentChannel mMainActivityRecyclerFragmentChannel;

    private Context mContext;

    public static MainActivity_Recycler_Fragment newInstance() {
        return new MainActivity_Recycler_Fragment();
    }



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

        //initializing viewmodel
        mMain_recyclerFragment_viewModel= ViewModelProviders.of(this).get(Main_RecyclerFragment_ViewModel.class);


        mRecyclerView = getView().findViewById(R.id.listTodo_fragment);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManagerRecyler = new LinearLayoutManager(getView().getContext());

        mRecyclerView.setLayoutManager(mLayoutManagerRecyler);
        mAdapter = new ListItemAdapter(mContext);

        mAdapter.setItemClickDataChannel(this);// initializing the SetItemClickDataChannel interface from Adapter


        loadData();

        //View model+LiveData will update the data of the adepter automatically when the data changes.
        mMain_recyclerFragment_viewModel.getListTodo().observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(@Nullable List<ToDo> toDoList) {
                mAdapter.updateAdapter(toDoList); //Update data of the adapter
            }
        });
    }


    private void loadData() {

        mAdapter.updateAdapter(mToDoList); //update data for adapter
        mRecyclerView.setAdapter(mAdapter); //set Adapter once

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
    public boolean onBackPressed() {
        // close search view on back button pressed
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            return false; //means this method will continue run with main onBackPress method
        }

        return true; //means job done should skip the implemented method
    }


    private String curItemId; // this variable needed for delete edit of the items

    //get Data from adapter using Interface of the adapter
    @Override
    public void onItemClickPassData(ArrayList<?> params, boolean longClick) {

        if(!longClick) { //for onclick action we need title and description
            Toast.makeText(mContext, "" + params.get(0).toString(), Toast.LENGTH_SHORT).show();

            mMainActivityRecyclerFragmentChannel.passData(params);
        }else{ //for long Click option we need only id
            Toast.makeText(mContext, ""+params.get(0).toString(), Toast.LENGTH_SHORT).show();
            curItemId=params.get(0).toString();
        }
    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {
        deleteItem(); //On Delete select deleteItem
        return super.onContextItemSelected(item);
    }

    private void deleteItem() {
        Toast.makeText(mContext, "Delete Item at " + curItemId, Toast.LENGTH_SHORT).show();
        //delete data through viewmodel
        mMain_recyclerFragment_viewModel.deleteOfID(curItemId);


        //Send meesage to Activity Data Loaded using interface
        mMainActivityRecyclerFragmentChannel.passResult(new ArrayList<Object>(Arrays.asList("Data Updated")));

    }



    //From Activity
    @Override
    public void passDataToFragment(ArrayList<String> strings) {
        addData(strings.get(0), strings.get(1));

    }


    private void addData(String s, String s1) {

        String id = UUID.randomUUID().toString();
        //insert data through viewmodel
        mMain_recyclerFragment_viewModel.insertTodo(new ToDo(id, s, s1));

        //Send meesage to Activity Data Loaded using interface
        mMainActivityRecyclerFragmentChannel.passResult(new ArrayList<Object>(Arrays.asList("Data Updated")));

    }
}

