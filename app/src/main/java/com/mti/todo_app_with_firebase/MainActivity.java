package com.mti.todo_app_with_firebase;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mti.todo_app_with_firebase.Adapter.ListItemAdapter;
import com.mti.todo_app_with_firebase.model.ToDo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    //Primary List of Data
    List<ToDo> mToDoList=new ArrayList<ToDo>(
            Arrays.asList(
                    new ToDo("1","Eat","eating should be controlled"),
                    new ToDo("2","Walk","walking is good for health"),
                    new ToDo("3", "Salat", "Is the best meditation in this world")
            )
    );



    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManagerRecyler;

    FloatingActionButton fab;

    public EditText title,description; //as need to access from adapter

    public boolean isUpdate=false; //flag to check is update or is add new
    
    public String idSelectedItem=""; //id of item That is selected
    
    ListItemAdapter mAdapter;

    SpotsDialog mAlertDialog;  //dmax dialog thirdparty

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);





        //view
        mAlertDialog=new SpotsDialog(this); //third party

        title=findViewById(R.id.title);
        description=findViewById(R.id.description);

        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new
                addData(title.getText().toString(),description.getText().toString() );

            }
        });

        mRecyclerView=findViewById(R.id.listTodo);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManagerRecyler=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManagerRecyler);

        loadData();
    }

    private void addData(String s, String s1) {

        String id= UUID.randomUUID().toString();
        mToDoList.add(new ToDo(id,s,s1));

        loadData();

    }

    private void loadData() {

        mAlertDialog.show();

        mAdapter= new ListItemAdapter(MainActivity.this,mToDoList);
        mRecyclerView.setAdapter(mAdapter);

        mAlertDialog.dismiss();
    }


    private SearchView mSearchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        //Associate searchable configuration with the Searchview
        SearchManager searchManager= (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        mSearchView= (SearchView) menu.findItem(R.id.action_search).getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        //Listening to serach queary changes
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //filter recycler view when querry submitted
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


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("DELETE"))
            deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int order) {
        Toast.makeText(this, "Delete Item at "+ order + " and id: "+idSelectedItem, Toast.LENGTH_SHORT).show();
        mToDoList.remove(order);
        loadData();
    }

}
