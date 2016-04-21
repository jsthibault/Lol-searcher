package com.example.mowgli.lol_searcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    private EditText        _mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _mEdit = (EditText)findViewById(R.id.summoner_name);
    }

    public void onSearch(View v)
    {
        Intent              i = new Intent(getApplicationContext(), ListOfChamp.class);
        Spinner             mySpinner = (Spinner) findViewById(R.id.spinner_server);
        String              summonerName = _mEdit.getText().toString();

        i.putExtra("server", mySpinner.getSelectedItem().toString());
        if (TextUtils.isEmpty(summonerName))
        {
            _mEdit.setError("There is no summoner name to search");
        }
        else
        {
            i.putExtra("name", summonerName);
            startActivity(i);
        }
    }

    protected Boolean verifySum(String name)
    {
        return (true);
    }
}
