package com.example.mowgli.lol_searcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSearch(View v) {
        Intent i = new Intent(getApplicationContext(), List.class);
        EditText mEdit   = (EditText)findViewById(R.id.summoner_name);

        Log.v("EditText", mEdit.getText().toString());
        i.putExtra("name", mEdit.getText().toString());
        startActivity(i);
    }
}
