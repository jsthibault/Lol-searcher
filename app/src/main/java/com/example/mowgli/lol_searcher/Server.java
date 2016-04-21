package com.example.mowgli.lol_searcher;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by mowgli on 21/04/2016.
 */

enum CallBack
{
    SUMMONER
}
public class Server
{
    private String              _localisation = null;
    private String              _key = null;
    private ILolInstance[]      _tab;

    Server(String localisation, String key)
    {
        this._localisation = localisation;
        this._key = key;
        _tab = new ILolInstance[3];
    }

    String          getLocalisation() { return(this._localisation); }

    private   void  updateSummuner(CallBack Type, Context context)
    {
        System.out.println("---_>LOLOLOL");
        this._tab[CallBack.SUMMONER.ordinal()].updateSummoner(context);
    }

    private void    getRank(final Context context, final Summoner summoner)
    {
        System.out.println("3");

        //System.out.println("https://" + this._sum.getServer() + ".api.pvp.net/api/lol/" + this._sum.getServer() + "/v2.5/league/by-summoner/" + this._sum.getId() + "?api_key=" + this._key);
        Future<JsonObject> jsonObjectFuture = Ion.with(context)
                .load("https://" + this._localisation + ".api.pvp.net/api/lol/" +
                        this._localisation + "/v2.5/league/by-summoner/" + summoner.getId() +
                        "?api_key=" + this._key)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        JsonArray tmpArray = null;
                        String      tmp;

                        System.out.println(result);
                        if (result == null)
                        {
                            //set error
                            return;
                        }
                        if ((tmpArray = result.getAsJsonArray(summoner.getId())) == null)
                        {
                            //set error
                            return;
                        }
                        if (tmpArray.get(0) == null)
                        {
                            //set error
                            return;
                        }
                        result = (JsonObject)tmpArray.get(0);
                        if ((tmp = (result.get("tier").toString())) == null)
                        {
                            //set error
                            return;
                        }
                        tmp = tmp.replaceAll("\"","");
                        summoner.setRank(tmp);
                        if ((tmpArray = result.getAsJsonArray("entries")) == null)
                        {
                            //set error
                            return;
                        }
                        result = (JsonObject)tmpArray.get(0);
                        if ((tmp = (result.get("division").toString())) == null)
                        {
                            //set error
                            return;
                        }
                        tmp = tmp.replaceAll("\"","");
                        summoner.setDivision(tmp);

                        System.out.println("GOOOO : " + summoner.getName() + " " + summoner.getDivision());
                        updateSummuner(CallBack.SUMMONER, context);
                    }
                });
    }

    public void     fillSumm(final Context context, final Summoner summoner)
    {
        this._tab[CallBack.SUMMONER.ordinal()] = summoner;
        System.out.println("https://" + this._localisation + ".api.pvp.net/api/lol/" + this._localisation
                + "/v1.4/summoner/by-name/" + summoner.getName() + "?api_key=" + this._key);
        Future<JsonObject> jsonObjectFuture = Ion.with(context)
                .load("https://" + this._localisation + ".api.pvp.net/api/lol/" + this._localisation
                        + "/v1.4/summoner/by-name/" + summoner.getName() + "?api_key=" + this._key)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String tmpId = null;
                        String tmpIconId = null;
                        String tmpLevel = null;

                        if (result == null)
                        {
                            //setError
                            return;
                        }
                        System.out.println(result);
                        if ((result = result.getAsJsonObject(summoner.getName())) == null)
                        {
                            System.out.println("dans sumName");
                            //setError
                            return ;
                        }
                        if (((tmpId = result.get("id").toString()) == null) ||
                                ((tmpIconId = result.get("profileIconId").toString()) == null) ||
                                ((tmpLevel = result.get("summonerLevel").toString()) == null))
                        {
                            //setError
                            return ;
                        }
                        System.out.println("4");
                        summoner.setLvl(tmpLevel);
                        summoner.setId(tmpId);
                        summoner.setIdIcon(tmpIconId);
                        getRank(context, summoner);
                    }
                });
    }

}
