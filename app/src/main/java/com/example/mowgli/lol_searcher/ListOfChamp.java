package com.example.mowgli.lol_searcher;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class ListOfChamp extends AppCompatActivity implements ILolInstance {

    protected Summoner      _sum = null;
    protected Server        _server = null;
    private String          _key = "967cbda0-c868-47ab-b4cc-7de78ed7a4cc";
    private ProgressBar     _progressSum = null;
    private ProgressBar     _progressChamp = null;
    private List<Champion>  _listChampion = null;
    private List<String>    _listName = new ArrayList<>();
    private List<Integer>   _listId = new ArrayList<>();
    public static int       _index = 0;

    public void setListChamp(List<Champion> listChamp) { this._listChampion = listChamp; }
    public void setListName(List<String> listName) { this._listName = listName; }
    public void setListId(List<Integer> listId) { this._listId = listId; }

    public List<Champion> getListChampion() { return (this._listChampion); }
    public List<String> getListName() { return (this._listName); }
    public List<Integer> getListId() { return (this._listId); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String tmpName = null;
        String tmpServer = null;

        this._listChampion.clear();
        this._listName.clear();
        this._listId.clear();
        _index = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_champ);
        Intent i = getIntent();
        this._progressSum = (ProgressBar) findViewById(R.id.progressBarSum);
        this._progressChamp = (ProgressBar) findViewById(R.id.progressBarChamp);
        tmpName = i.getStringExtra("name").toLowerCase();
        tmpServer = i.getStringExtra("server").toLowerCase();
        if (tmpName == null || tmpServer == null) {
            System.out.println("Failed extraa");
            return;
        }
        this._server = new Server(tmpServer, this._key, i.getStringExtra("season"));

        this._sum = new Summoner(tmpName);
        this._sum.display(this.getApplicationContext(), this._server, (TextView) findViewById(R.id.summoner_name),
                (TextView) findViewById(R.id.rank), (TextView) findViewById(R.id.level),
                (ImageView) findViewById(R.id.summoner_icon), this._progressSum, this);


    }

    public void getListChamp()
    {
        this._server.getChampions(this.getApplicationContext(), this._sum, this);
    }

    public void displayList()
    {
        System.out.println("GOAL : " + this._listName.get(0));

        this._progressChamp.setVisibility(ProgressBar.GONE);

        Integer[] imgid = {
                0,
               /* R.drawable.pic1,
                R.drawable.pic2,
                R.drawable.pic3,
                R.drawable.pic4,
                R.drawable.pic5,
                R.drawable.pic6,
                R.drawable.pic7,
                R.drawable.pic8,*/
        };

        ListFiller adapter = new ListFiller(this, this.getListName(), imgid);
        ListView list = (ListView) findViewById(R.id.champList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //String Slecteditem= itemname[+position];
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void fillChamp()
    {
        if (this._listChampion.size() > _index)
        {
            this._listChampion.get(_index).fillData(getApplicationContext(), this._server, this);
            _index += 1;
        }
        else
        {
            this.displayList();
        }
    }

    @Override
    public void         update(Context context)
    {
        System.out.println(this._listChampion);
        fillChamp();
    }
}
