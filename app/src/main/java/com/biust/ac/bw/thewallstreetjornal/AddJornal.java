package com.biust.ac.bw.thewallstreetjornal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddJornal extends AppCompatActivity {

    EditText titleEd;
    EditText contentEd;
    Button mSave;

    FirebaseDatabase database;

    DatabaseReference reference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jornal);

        //intialising
        mSave = findViewById(R.id.save);
        titleEd = findViewById(R.id.title);
        contentEd = findViewById(R.id.content);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        database = FirebaseDatabase.getInstance();

        progressDialog= new ProgressDialog(this);

        reference = database.getReference("Journals");

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Saving...");
                progressDialog.show();
                String title = titleEd.getText().toString();
                String content = contentEd.getText().toString();

                //getting the data
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                //preparing data
                HashMap<String, Object> map = new HashMap<>();

                map.put("title", title);
                map.put("content", content);
                map.put("date", formattedDate);

                //sending data to db and catch any errors
                reference.push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        //going back to the main page
                        Toast.makeText(AddJornal.this, "Journal Added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddJornal.this,MainActivity.class));
                        finish();
                    }
                });
            }
        });
    }
}