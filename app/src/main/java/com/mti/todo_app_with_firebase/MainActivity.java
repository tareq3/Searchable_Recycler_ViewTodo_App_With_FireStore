package com.mti.todo_app_with_firebase;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mti.todo_app_with_firebase.UI.Main.MainActivity_Recycler_Fragment;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity
        implements  MainActivity_Recycler_Fragment.MainActivityRecyclerFragmentChannel {

    //Todo: As MainActivityRecyclerFragmentChannel is the shadow of this activity
    /*
            we don't need to initialize this interface in this activity class

            but we have to initialize that on Fragment onAttach() method.
     */
    public MainActivityChannel mMainActivityChannel;

    FloatingActionButton fab;

    public EditText title, description; //as need to access from adapter





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);


        initRecyclerViewFragment(savedInstanceState);


        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new data By passing it to fragment this is only for isolation
                mMainActivityChannel.passDataToFragment(new ArrayList<String>(Arrays.asList(title.getText().toString(), description.getText().toString())));
            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        //Search View is being controlled by Fragment
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


        // Todo: OnBack first hide the searchView then do as suped .
        //true means method task done
        if (mMainActivityChannel.onBackPressed()) { // first execute onBackPressed of Interface codes and if the result true then execute default function of on backpress
            super.onBackPressed();
        }
    }


    private void initRecyclerViewFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.listTodo_frame, MainActivity_Recycler_Fragment.newInstance())
                    .commitNow();
        }
    }

    private void removeFragment(){
      Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.listTodo_frame);
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    //get Result from Fragment using interface
    @Override
    public void passResult(ArrayList<?> params) {

        Toast.makeText(this, "" + params.get(0).toString(), Toast.LENGTH_SHORT).show();
    }

    //get Data from Fragment
    @Override
    public void passData(ArrayList<?> params) {
        title.setText(params.get(0).toString());
        description.setText(params.get(1).toString());
    }



}

