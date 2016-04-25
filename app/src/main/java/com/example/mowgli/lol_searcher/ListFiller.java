package com.example.mowgli.lol_searcher;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mowgli on 22/04/2016.
 */

public class ListFiller extends ArrayAdapter<String>
{
    private final Activity context;
    private final List<String> itemname;
    private final Integer[] imgid;

    public ListFiller(Activity context, List<String> list, Integer[] imgid)
    {
        //
        super(context, R.layout.mylist, list);

        this.context = context;
        this.itemname = list;
        this.imgid = imgid;
    }


    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        System.out.println("getView");

        txtTitle.setText(itemname.get(position));
        //imageView.setImageResource(imgid[position]);
        extratxt.setText("Description "+itemname.get(position));
        return rowView;
    };
}
