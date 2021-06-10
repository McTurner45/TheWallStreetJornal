package com.biust.ac.bw.thewallstreetjornal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class JournalActivity extends AppCompatActivity {
//declaring variables and objects
    String journalId;

    TextView title;
    TextView date;
    TextView content;

    String titleST;
    String dateSt;
    String contentSt;

    FirebaseDatabase database;
    DatabaseReference reference;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        content = findViewById(R.id.content);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Journals");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //getting data stored in the database
        journalId = getIntent().getStringExtra("journalId");
        titleST = getIntent().getStringExtra("title");
        dateSt = getIntent().getStringExtra("date");
        contentSt = getIntent().getStringExtra("content");

        getJournalInfo(journalId);

    }

    //retriving inf from the cloud
    private void getJournalInfo(String journalId) {

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (journalId.equals(dataSnapshot.getKey())) {
                            //adding data to the views
                            title.setText(dataSnapshot.child("title").getValue().toString());
                            date.setText(dataSnapshot.child("date").getValue().toString());
                            content.setText(dataSnapshot.child("content").getValue().toString());
                        }else{
                            title.setText(titleST);
                            date.setText(dateSt);
                            content.setText(contentSt);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.journalmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete:
                deleteJournal(journalId);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //deleting a record from the database
    public void deleteJournal(String journalId) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //check if we are deleting the right data fro the databse
                    if (journalId.equals(dataSnapshot.getKey())) {
                        //getting data thats to be deleted and holding it in the phone
                        String title =dataSnapshot.child("title").getValue().toString();
                        String date=dataSnapshot.child("date").getValue().toString();
                        String content=dataSnapshot.child("content").getValue().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
                        SharedPreferences.Editor deletedJournal = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
                        Gson gson = new Gson();
                        String json = gson.toJson(new Journal(date,title,content,journalId));
                        deletedJournal.putString("DeletedJournal", json);

// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
                        deletedJournal.commit();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("title", title);
                        map.put("content", content);
                        map.put("date", date);
                        reference.child("Deleted").child(journalId).setValue(map);
                        reference.child(journalId).setValue(null);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}