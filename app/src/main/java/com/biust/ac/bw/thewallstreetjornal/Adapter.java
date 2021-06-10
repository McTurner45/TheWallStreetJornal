package com.biust.ac.bw.thewallstreetjornal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder>{
//declaring variables
    ArrayList<Journal> journals;
    Context mContext;

    //constructor
    public Adapter(ArrayList<Journal> journals) {
        this.journals=journals;
    }

    //initializing recyclerview variables and objects
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View journalView = inflater.inflate(R.layout.journalitem, parent, false);

        // Return a new holder instance
        Holder viewHolder= new Holder(journalView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        //binding enries to the recyclerview
        Journal journal= journals.get(position);

        holder.title.setText(journal.getTitle());
        holder.date.setText(journal.getDate());

        String journalId=journal.getId();

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JournalActivity.class);
                intent.putExtra("journalId", journalId);
                intent.putExtra("title", journal.getTitle());
                intent.putExtra("date", journal.getDate());
                intent.putExtra("content", journal.getContent());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return journals.size();
    }

    //connecting our view resurces to the adapter
    public class Holder extends RecyclerView.ViewHolder{

        TextView title;
        TextView date;

        View view;


        public Holder(@NonNull View itemView) {
            super(itemView);
            view=itemView.findViewById(R.id.journalItem);
            title=itemView.findViewById(R.id.tittle);
            date=itemView.findViewById(R.id.date);
        }
    }
}
