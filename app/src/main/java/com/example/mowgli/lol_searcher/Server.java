package com.example.mowgli.lol_searcher;

import android.content.Context;
import android.net.Uri;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.Headers;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.loader.AsyncHttpRequestFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by mowgli on 21/04/2016.
 */

enum CallBack
{
    SUMMONER,
    LISTCHAMP,
    CHAMPION,
}
public class Server
{
    private String              _localisation = null;
    private String              _key = null;
    private String              _season = null;
    private ILolInstance[]      _tab;

    Server(String localisation, String key, String season)
    {
        this._localisation = localisation;
        this._key = key;
        this._season = season;
        _tab = new ILolInstance[4];
        System.out.println(this._season);
    }

    String          getLocalisation() { return(this._localisation); }

    private   void  updateCallBack(CallBack Type, Context context)
    {
        this._tab[Type.ordinal()].update(context);
    }

    private void    getRank(final Context context, final Summoner summoner)
    {
        Future<JsonObject> jsonObjectFuture = Ion.with(context)
                .load("https://" + this._localisation + ".api.pvp.net/api/lol/" +
                        this._localisation + "/v2.5/league/by-summoner/" + summoner.getId() +
                        "?api_key=" + this._key)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        JsonObject  tmpJson = null;
                        JsonArray   tmpArray = null;
                        String      tmp =  null;

                        System.out.println(result);
                        if (result == null)
                        {
                            //set error
                            return;
                        }
                        System.out.println(result);
                        if ((tmpJson = result.getAsJsonObject("status")) != null)
                        {
                            if ((tmp = tmpJson.get("status_code").toString()) != null)
                            {
                                summoner.setRank("Not rank");
                                summoner.setDivision("");
                                updateCallBack(CallBack.SUMMONER, context);
                                return ;
                            }
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
                        updateCallBack(CallBack.SUMMONER, context);
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

    public void     getChampions(final Context context, final Summoner summoner, final ListOfChamp list)
    {
        this._tab[CallBack.LISTCHAMP.ordinal()] = list;
        Future<JsonObject> jsonObjectFuture = Ion.with(context)
                .load("https://" +  this._localisation + ".api.pvp.net/api/lol/" +
                        this._localisation + "/v1.3/stats/by-summoner/" + summoner.getId() + "/ranked?season=" +
                        this._season + "&api_key=" + this._key)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        JsonObject      tmpJson = null;
                        JsonArray       tmpArray = null;
                        String          tmp =  null;
                        Champion        tmpChamp = null;
                        List<Champion>  listChamp = new ArrayList<>();


                        System.out.println(result);
                        if ((tmpJson = result.getAsJsonObject("status")) != null)
                        {
                            if ((tmp = tmpJson.get("status_code").toString()) != null)
                            {
                                // TODO faire les bails
                                return;
                            }
                        }
                        if ((tmpArray = result.getAsJsonArray("champions")) == null)
                        {
                            //set error
                            return;
                        }
                        if (tmpArray.get(0) == null)
                        {
                            //set error
                            return;
                        }
                        for (int i = 0; i < tmpArray.size(); i++)
                        {
                            tmpChamp = new Champion(tmpArray.get(i).getAsJsonObject());
                            if (tmpChamp.getId().equals("0"))
                            {
                                summoner.setStats(tmpArray.get(i).getAsJsonObject());
                            }
                            else
                            {
                                listChamp.add(tmpChamp);
                            }
                            System.out.println(i + " " + tmpArray.get(i).getAsJsonObject());
                        }
                        System.out.println(listChamp);
                        list.setListChamp(listChamp);
                        updateCallBack(CallBack.LISTCHAMP, context);
                    }
                });
    }

    public void     getName(final Context context, final Champion champ, final String id)
    {
        this._tab[CallBack.CHAMPION.ordinal()] = champ;
        Future<JsonObject> jsonObjectFuture = Ion.with(context)
                .load("https://global.api.pvp.net/api/lol/static-data/" + this._localisation + "/v1.2/champion/"
                + id + "?api_key=" + this._key)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        System.out.println(result);

                        JsonObject      tmpJson = null;
                        JsonArray       tmpArray = null;
                        String          tmp =  null;

                        System.out.println(" id " + id);
                        if (result == null)
                        {
                            System.out.println("KKK : " + "https://global.api.pvp.net/api/lol/static-data/" + _localisation + "/v1.2/champion/"
                                    + id + "?api_key=" + _key);
                            return;
                        }
                        System.out.println(result + " id " + id);
                        if ((tmpJson = result.getAsJsonObject("status")) != null)
                        {
                            if ((tmp = tmpJson.get("status_code").toString()) != null)
                            {
                                // TODO faire les bails
                                return;
                            }
                        }
                        champ.setName(result.get("name").toString().replaceAll("\"",""));
                        champ.setTitle(result.get("title").toString().replaceAll("\"",""));
                        updateCallBack(CallBack.CHAMPION, context);
                    }
                });

    }

}
