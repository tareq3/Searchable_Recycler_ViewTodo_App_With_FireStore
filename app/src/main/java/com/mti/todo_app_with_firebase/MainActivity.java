package com.mti.todo_app_with_firebase;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar);
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
