package com.mti.todo_app_with_firebase;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mti.todo_app_with_firebase.Adapter.ListItemAdapter;
import com.mti.todo_app_with_firebase.model.ToDo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    List<ToDo> mToDoList=new ArrayList<>();

    FirebaseFirestore mFirestoreDB;

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




        //Init FireStore
        mFirestoreDB=FirebaseFirestore.getInstance();

        //view
        mAlertDialog=new SpotsDialog(this); //third party
        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new
                if(!isUpdate){
                    setData(title.getText().toString(), description.getText().toString());
                }else{
                    updateData(title.getText().toString(),description.getText().toString());
                }
            }
        });

        mRecyclerView=findViewById(R.id.listTodo);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManagerRecyler=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManagerRecyler);

        loadData();
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

    private void deleteItem(int index) {
        mFirestoreDB.collection("ToDoList")
                .document(mToDoList.get(index).getId())
                    .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }

    private void updateData(String s_title, String s_description) {
        mFirestoreDB.collection("ToDoList").document(idUpdate)
                .update("title",s_title,
                        "description",s_description).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Update !", Toast.LENGTH_SHORT).show();
            }
        });
        //Realtime Update refresh data
        mFirestoreDB.collection("ToDoList").document(idUpdate).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                loadData();
            }
        });
    }

    private void setData(String s_title, String s_description) {
        //Random Id
        String id= UUID.randomUUID().toString();
        Map<String, Object> todo=new HashMap<>();

        todo.put("id", id);
        todo.put("title", s_title);
        todo.put("description", s_description);

        mFirestoreDB.collection("ToDoList").document(id).set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //refresh data
                loadData();
            }
        });
    }

    private void loadData() {
        mAlertDialog.show();
        if(mToDoList.size()>0)
            mToDoList.clear(); //remove old value

        mFirestoreDB.collection("ToDoList")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                for(DocumentSnapshot documentSnapshot : task.getResult())
                                {
                                    ToDo toDo=new ToDo(documentSnapshot.getString("id"),
                                            documentSnapshot.getString("title"),
                                            documentSnapshot.getString("description"));

                                    mToDoList.add(toDo);
                                }

                                mAdapter= new ListItemAdapter(MainActivity.this,mToDoList);
                                mRecyclerView.setAdapter(mAdapter);

                                mAlertDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
