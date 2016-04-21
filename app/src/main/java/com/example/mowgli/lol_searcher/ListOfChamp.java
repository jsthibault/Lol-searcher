package com.example.mowgli.lol_searcher;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ListOfChamp extends AppCompatActivity
{

    protected       Summoner    _sum = null;
    protected       Server      _server = null;
    private         String      _key = "967cbda0-c868-47ab-b4cc-7de78ed7a4cc";
    private         ProgressBar _progressSum;
    private         ProgressBar _progressChamp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        String      tmpName = null;
        String      tmpServer = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_champ);
        Intent i = getIntent();
        this._progressSum = (ProgressBar) findViewById(R.id.progressBarSum);
        this._progressChamp = (ProgressBar) findViewById(R.id.progressBarChamp);

        tmpName = i.getStringExtra("name").toLowerCase();
        tmpServer = i.getStringExtra("server").toLowerCase();
        if (tmpName == null || tmpServer == null)
        {
            System.out.println("Failed extra");
            return;
        }
        this._server = new Server(tmpServer, this._key);
        this._sum = new Summoner(tmpName);
        System.out.println("---_>LOLOLOL1");
        this._sum.display(this.getApplicationContext(), this._server, (TextView) findViewById(R.id.summoner_name),
                (TextView) findViewById(R.id.rank), (TextView) findViewById(R.id.level),
                (ImageView) findViewById(R.id.summoner_icon), this._progressSum);
    }
}
