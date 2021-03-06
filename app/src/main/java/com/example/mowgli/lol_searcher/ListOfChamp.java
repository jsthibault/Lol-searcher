package com.example.mowgli.lol_searcher;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mowgli on 25/04/2016.
 * jean.stephane.thib@gmail.com
 */

public class ListOfChamp extends AppCompatActivity implements ILolInstance {

    protected Summoner              _sum = null;
    protected Server                _server = null;
    private String                  _key = "967cbda0-c868-47ab-b4cc-7de78ed7a4cc";
    private ProgressBar             _progressSum = null;
    private ProgressBar             _progressChamp = null;
    private List<Champion>          _listChampion = null;
    private List<String>            _listName = new ArrayList<>();
    private List<Integer>           _listId = new ArrayList<>();
    public static int               _index = 0;

    public void setListChamp(List<Champion> listChamp) { this._listChampion = listChamp; }
    public void setListName(List<String> listName) { this._listName = listName; }
    public void setListId(List<Integer> listId) { this._listId = listId; }

    public List<Champion> getListChampion() { return (this._listChampion); }
    public List<String> getListName() { return (this._listName); }
    public List<Integer> getListId() { return (this._listId); }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        String tmpName = null;
        String tmpServer = null;
        Intent i = getIntent();

        if (this._listChampion != null)
        {
            this._listChampion.clear();
        }
        this._listName.clear();
        this._listId.clear();
        _index = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_champ);
        this._progressSum = (ProgressBar) findViewById(R.id.progressBarSum);
        this._progressChamp = (ProgressBar) findViewById(R.id.progressBarChamp);
        tmpName = i.getStringExtra("name").toLowerCase();
        tmpServer = i.getStringExtra("server").toLowerCase();
        //System.out.print(tmpName);
        //System.out.print(tmpName);
        if (tmpName == null || tmpServer == null)
        {
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
        this._progressChamp.setVisibility(ProgressBar.GONE);
        ListFiller adapter = new ListFiller(this, this.getListName(), this._listChampion);
        ListView list = (ListView) findViewById(R.id.champList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //String Slecteditem= itemname[+position];
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                Intent      i = new Intent(getApplicationContext(), Champion_stats.class);
                Champion    tmp = null;

                if ((tmp = _listChampion.get(position)) != null)
                {
                    System.out.println(tmp.toString());
                    i.putExtra("name", tmp.getName());
                    i.putExtra("key", tmp.getKey());
                    i.putExtra("id", tmp.getId());
                    i.putExtra("title", tmp.getTitle());
                    i.putExtra("stats", tmp.getStats().toString());
                    startActivity(i);
                }
                else
                {
                    //set Error
                }

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
        //System.out.println(this._listChampion);
        fillChamp();
    }
}
