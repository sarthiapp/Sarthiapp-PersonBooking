package com.example.sarthiperson;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PersonMainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter helperAdapter;
    Query query;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personmain);
        recyclerView=findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        query=FirebaseDatabase.getInstance().getReference().child("persons").orderByChild("rating");;


        FirebaseRecyclerOptions<FetchData> options =
                new FirebaseRecyclerOptions.Builder<FetchData>()
                        .setQuery(query, FetchData.class)
                        .build();
        helperAdapter=new MyAdapter(options,this);
        recyclerView.setAdapter(helperAdapter);


    }




    @Override
    protected void onStart() {
        super.onStart();
        helperAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        helperAdapter.stopListening();
    }



}