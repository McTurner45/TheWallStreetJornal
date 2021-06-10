package com.biust.ac.bw.thewallstreetjornal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //declaring variables and objects
    FloatingActionButton addFab;
    RecyclerView recyclerView;
    ArrayList<Journal> journals;
    Adapter adapter;

    //declaring database  variable
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //on create method thts initializes everything
        addFab = findViewById(R.id.fab);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.recyclerView);

        journals = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //intialising the db
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Journals");


        //navigating to the add a journal activity
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {//giving our button purpose(navigating to the next page)
                startActivity(new Intent(MainActivity.this, AddJornal.class));
            }
        });

        //get the data
        getJournalEntries();
    }

    //binding menu list to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //setting what happens when selecting a menu option
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        BubbleSort bubbleSort = new BubbleSort();
        ArrayList<Journal> sortedJournals = new ArrayList<>();
        Adapter adapter;
        switch (item.getItemId()) {
            case R.id.bubbleDsc:
            case R.id.insertionDsc:

                if (journals.size() > 0) {
                    Collections.sort(journals, new Comparator<Journal>() {
                       //sorting in descending order
                        @Override
                        public int compare(final Journal object1, final Journal object2) {
                            return object1.getTitle().compareTo(object2.getTitle());
                        }
                    });
                }
                adapter = new Adapter(journals);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.bubbleAsc:
            case R.id.insertionAsc:
                if (journals.size() > 0) {
                    Collections.sort(journals, new Comparator<Journal>() {
                        //sorting in ascending order
                        @Override
                        public int compare(final Journal object1, final Journal object2) {
                            return object2.getTitle().compareTo(object1.getTitle());
                        }
                    });
                }
                adapter = new Adapter(journals);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.undo:
                getDeleted();
                break;

            case R.id.redo:
                redoDelete();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void redoDelete() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        //geting from db
        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        Gson gson = new Gson();
        String json = sharedPreferences.getString("DeletedJournal", "");
        Journal obj = gson.fromJson(json, Journal.class);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        reference.child(obj.getId()).setValue(null);


    }

    private void getDeleted() {

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        //geting from db
        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        Gson gson = new Gson();
        String json = sharedPreferences.getString("DeletedJournal", "");
        Journal obj = gson.fromJson(json, Journal.class);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", obj.getTitle());
        map.put("content", obj.getContent());
        map.put("date", obj.getDate());
        map.put("journalId", obj.getId());
        reference.child(obj.getId()).setValue(map);
        //reference.child("Deleted").child(obj.getId()).setValue(null);

    }

    //method used to get the list from the database
    private void getJournalEntries() {
        //geting from db
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                journals.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals("Deleted")) {
                        Journal journal = dataSnapshot.getValue(Journal.class);
                        journal.setId(dataSnapshot.getKey());
                        journals.add(journal);
                    }
                }
                adapter = new Adapter(journals);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}