package com.example.mowgli.lol_searcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent i = getIntent();
        String user = i.getStringExtra("name");

        Log.v("name", user);

        try
        {
            this.getUserInfo("lol");
        }
        catch (IOException e)
        {
        }
    }

    //
    // Key:
    // 967cbda0-c868-47ab-b4cc-7de78ed7a4cc
    // Rate Limit(s):
    //        500 requests every 10 minutes
    // 10 requests every 10 seconds
    //
    // url : https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/RiotSchmick?api_key=<key>



    protected void getUserInfo(String user) throws IOException {
        String key = "967cbda0-c868-47ab-b4cc-7de78ed7a4cc";
        boolean status = false;
        String response = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/RiotSchmick?api_key=967cbda0-c868-47ab-b4cc-7de78ed7a4cc";

        Thread t = new Thread() {

            @Override
            public void run() {
                URL url = null;
                HttpURLConnection urlConnection = null;
                try {
                    byte tab[] = new byte[500];

                    url = new URL("http://www.android.com");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    in.read(tab);

                    System.out.println("Characters printed:");

                    int i;
                    char c;

                    // reads till the end of the stream
                    while((i=in.read())!=-1)
                    {
                        // converts integer to character
                        c=(char)i;

                        // prints character
                        System.out.print(c);
                    }
                   //Log.e("Res : ", (char)tab);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("FAIL");
                }
                finally
                {
                    urlConnection.disconnect();
                }
            }
        };
        t.start();
    }

 }

