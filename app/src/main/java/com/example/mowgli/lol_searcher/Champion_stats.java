package com.example.mowgli.lol_searcher;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Champion_stats extends AppCompatActivity
{
    private Champion            _champ = null;
    private List<String>        _listValue = new ArrayList<>();
    private List<Float>         _listX = new ArrayList<>();
    private List<Float>         _listY = new ArrayList<>();

    private void            fillPie(final List<Float> value, final List<String>  title, PieChart pie)
    {
        ArrayList<Entry>    yVals1 = new ArrayList<Entry>();
        PieDataSet          dataSet = null;
        ArrayList<Integer>  colors = null;

        pie.setUsePercentValues(true);
        pie.setDescription("Ratio Win/Lose");
        pie.setDrawHoleEnabled(true);
        pie.setHoleRadius(7);
        pie.setTransparentCircleRadius(10);
        pie.setRotationAngle(0);
        pie.setRotationEnabled(true);
        pie.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h)
            {
                // display msg when value selected
                if (e == null)
                    return;

               Toast.makeText(Champion_stats.this,
                       title.get(e.getXIndex()) + " = " + value.get(e.getXIndex()) + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected()
            {

            }
        });
        for (int i = 0; i < this._listX.size(); i++)
        {
            yVals1.add(new Entry(this._listX.get(i), i));
        }
        dataSet = new PieDataSet(yVals1, "Ratio Win/Late");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);
        colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

       /* for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);*/

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(this._listValue, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);
        pie.setData(data);
        pie.highlightValues(null);
        pie.invalidate();
        Legend l = pie.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    private void    getWinLose()
    {
        String      tmp = null;
        JsonObject  tmpJson = null;

        this._listValue.add("Win");
        this._listValue.add("Lose");
        if ((tmpJson = this._champ.getStats().getAsJsonObject("stats")) == null)
        {
            //set error
            return ;
        }
        if ((tmp = tmpJson.get("totalSessionsWon").toString()) == null)
        {
            //set error
            return ;
        }
        this._listX.add(Float.parseFloat(tmp));
        if ((tmp = tmpJson.get("totalSessionsLost").toString()) == null)
        {
            //set error
            return ;
        }
        this._listX.add(Float.parseFloat(tmp));
        System.out.println(this._champ.getStats());
    }

    private void    displayChamp(Champion champion)
    {
        ImageView   icon = (ImageView) findViewById(R.id.champion_icon);
        TextView    name = (TextView) findViewById(R.id.champion_name);

        champion.getImage(icon);
        //System.out.println("LE NOM : " + champion.getName());
        name.setText(champion.getName() + " - " + champion.getTitle());
    }

    @Override
    protected void  onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamption_stats);
        Intent      i = getIntent();
        String      tmp = i.getStringExtra("stats");
        String      name = i.getStringExtra("name");
        String      key = i.getStringExtra("key");
        String      title = i.getStringExtra("title");
        String      id = i.getStringExtra("id");
        PieChart    pie = null;

        if (!this._listX.isEmpty())
        {
            this._listX.clear();
        }
        if (!this._listY.isEmpty())
        {
            this._listY.clear();
        }
        if (!this._listValue.isEmpty())
        {
            this._listValue.clear();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject stat = (JsonObject)jsonParser.parse(tmp);
        this._champ = new Champion(stat);
        this._champ.setName(name);
        this._champ.setKey(key);
        this._champ.setId(id);
        this._champ.setTitle(title);
        pie = (PieChart) findViewById(R.id.chartpie);
        displayChamp(this._champ);
        getWinLose();
        fillPie(this._listX, this._listValue, pie);



        // Test code
        LineChart   chart = (LineChart) findViewById(R.id.chart);
        Chart<LineData> mLineChart = (LineChart) findViewById(R.id.chart);
        Chart<LineData> mLineChart2 = (LineChart) findViewById(R.id.char2t);
        Chart<LineData> mLineChart3 = (LineChart) findViewById(R.id.char3t);
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();
        ArrayList<Entry> valsComp3 = new ArrayList<Entry>();


        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        valsComp1.add(c1e1);
        Entry c1e2 = new Entry(50.000f, 1); // 1 == quarter 2 ...
        valsComp1.add(c1e2);
        // and so on ...

        Entry c2e1 = new Entry(120.000f, 0); // 0 == quarter 1
        valsComp2.add(c2e1);
        Entry c2e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        valsComp2.add(c2e2);

        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.Q"); xVals.add("2.Q"); xVals.add("3.Q"); xVals.add("4.Q");

        LineData data = new LineData(xVals, dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate();
        mLineChart2.setData(data);
        mLineChart2.invalidate();
        mLineChart3.setData(data);
        mLineChart3.invalidate();
    }
}
