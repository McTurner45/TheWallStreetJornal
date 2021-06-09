package com.biust.ac.bw.thewallstreetjornal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JournalActivity extends AppCompatActivity {

    String journalId;

    TextView title;
    TextView date;
    TextView content;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        title=findViewById(R.id.title);
        date=findViewById(R.id.date);
        content=findViewById(R.id.content);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Journals");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        String journalId = getIntent().getStringExtra("journalId");

        Log.d("journalId", "onCreate: "+journalId);

        getJournalInfo(journalId);

    }

    private void getJournalInfo(String journalId) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if (journalId.equals(dataSnapshot.getKey())) {
                        //adding data to the views
                        title.setText(dataSnapshot.child("title").getValue().toString());
                        date.setText(dataSnapshot.child("date").getValue().toString());
                        content.setText(dataSnapshot.child("content").getValue().toString());
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
}