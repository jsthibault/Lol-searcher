package com.example.mowgli.lol_searcher;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by mowgli on 23/04/2016.
 */
public class Champion implements ILolInstance
{
    private String      _name = null;
    private String      _title = null;
    private String      _id = null;
    private JsonObject  _stats = null;
    private ListOfChamp _list = null;

    JsonObject getStats() { return(this._stats); }
    String getName() { return(this._name); }
    String getTitle() { return(this._title); }
    String getId()
    {
        if (this._id == null)
        {
            this._id = this._stats.get("id").toString();
        }
        return(this._id);
    }

    void setTitle(String title) { this._title = title; }
    void setName(String name) { this._name = name; }

    Champion(JsonObject stats)
    {
        this._stats = stats;
    }

    @Override
    public void update(Context context)
    {
        System.out.println("Looser3 : " + this.getName());
        this._list.getListName().add(this.getName());
        this._list.getListId().add(Integer.parseInt(this.getId()));
        this._list.fillChamp();
    }

    public void fillData(Context context, Server server, ListOfChamp list)
    {
        this._list = list;
        server.getName(context, this, this.getId());
    }

}
