package com.example.mowgli.lol_searcher;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

/**
 * Created by mowgli on 12/04/2016.
 */
public class Summoner implements ILolInstance
{
    private String      _name = null;
    private String      _id = null;
    private String      _lvl = null;
    private String      _idIcon = null;
    private String      _rank = null;
    private String      _division = null;
    private String      _localisation = null;
    private JsonObject  _stats = null;

    private TextView    _msummonerName = null;
    private TextView    _mrank = null;
    private TextView    _mlevel = null;
    private ImageView   _micon = null;
    private ProgressBar _mprogressSum = null;

    private ListOfChamp _callback = null;

    Summoner(String name)
    {
        this._name = name;
    }

    String getName() { return(this._name); }
    String getId() { return(this._id); }
    String getLvl() { return(this._lvl); }
    String getIdIcon() { return(this._idIcon); }
    String getRank() { return(this._rank); }
    String getDivision() { return(this._division); }
    String getLocalisation() { return(this._localisation); }
    JsonObject getStats() { return(this._stats); }

    void setId(String id) { this._id = id; }
    void setLvl(String lvl) { this._lvl = lvl; }
    void setIdIcon(String idIcon) { this._idIcon = idIcon; }
    void setRank(String rank) { this._rank = rank; }
    void setDivision(String division) { this._division = division; }
    void setStats(JsonObject stats) {this._stats = stats; }


    @Override
    public void    update(Context context)
    {
        this._mprogressSum.setVisibility(ProgressBar.GONE);
        this._msummonerName.setText(this._name);
        this._mrank.setText(this._rank + " " + this._division);
        this._mlevel.setText(context.getResources().getString(R.string.level) + " " + this._lvl + " " + this._localisation);
        Ion.with(this._micon)
                .load("http://ddragon.leagueoflegends.com/cdn/6.6.1/img/profileicon/" + this._idIcon + ".png");
        this._callback.getListChamp();
    }


    void    display(Context context, Server server, TextView summonerName, TextView rank, TextView level,
                    ImageView icon, ProgressBar progressSum, ListOfChamp call)
    {
        this._micon = icon;
        this._mlevel = level;
        this._mrank = rank;
        this._msummonerName = summonerName;
        this._mprogressSum = progressSum;
        this._localisation = server.getLocalisation();
        this._callback = call;
        if (this._name == null || this._id == null || this._lvl == null || this._idIcon == null ||
                this._rank == null || this._division == null)
        {
            System.out.println("---_>LOLOLO2");
            server.fillSumm(context, this);
        }
        else
        {
            update(context);
        }
    }
}
