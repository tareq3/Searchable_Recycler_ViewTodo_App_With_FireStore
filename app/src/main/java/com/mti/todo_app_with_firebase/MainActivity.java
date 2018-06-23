package com.mti.todo_app_with_firebase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    public String idUpdate=""; //id of item need to be updated
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

     return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("DELETE"))
            deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int order) {
        Toast.makeText(this, "Delete Item code should be here", Toast.LENGTH_SHORT).show();
        mToDoList.remove(order);
        loadData();
    }

}
