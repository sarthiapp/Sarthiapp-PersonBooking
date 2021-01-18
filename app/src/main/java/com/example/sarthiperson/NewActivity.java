package com.example.sarthiperson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewActivity extends AppCompatActivity {
    public void Loginuser(View view){
        Intent i=new Intent(getApplicationContext(), PersonMainActivity.class);
        startActivity(i);

    }
    public void Loginperson(View view){
        Intent i=new Intent(getApplicationContext(),PersonDatabaseStore.class);
        startActivity(i);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);


    }

}