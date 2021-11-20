package com.example.finalhomework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import static com.example.finalhomework.MainActivity.FROM;

public class chart extends AppCompatActivity {
    SQLiteDatabase db = MainActivity.db;
    Cursor cur; //存放查詢結果的 cursor物件
    SimpleCursorAdapter adapter;
    private PieChart pieChart,pie_chat2;
    //show
    static float income = 0;
    static float bre_out,lan_out,din_out,lif_out,clo_out,tra_out,dri_out,med_out,ent_out,stu_out,oth_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart2);
        this.setTitle(getString(R.string.TITLE_RECORD_CHART));
        db = openOrCreateDatabase("keep", Context.MODE_PRIVATE,null);
        cur = db.rawQuery("SELECT * FROM "+"keeplist",null);
        if(cur.getCount()>0){//若有資料
            float input_total = 0;
            float bre=0,lan=0,din=0,lif=0,clo=0,tra=0,dri=0,med=0,ent=0,stu=0,oth=0;

            //int a = cur.getCount();
            //count.setText(Integer.toString(a));
            cur.moveToFirst();
            do{
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("收入")){
                    input_total+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("早餐")){
                    bre+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("午餐")){
                    lan+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("晚餐")){
                    din+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("生活")){
                    lif+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("服飾")){
                    clo+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("交通")){
                    tra+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("飲料")){
                    dri+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("醫療")){
                    med+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("娛樂")){
                    ent+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("進修")){
                    stu+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("其他")){
                    oth+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }

                //cur.getString(cur.getColumnIndex(FROM[2]))
            }while(cur.moveToNext());
            //String aa = cur.getString(cur.getColumnIndex(FROM[2]));
        income = input_total;
        bre_out = bre;
        lan_out = lan;
        din_out = din;
        lif_out = lif;
        clo_out = clo;
        tra_out = tra;
        dri_out = dri;
        med_out = med;
        ent_out = ent;
        stu_out = stu;
        oth_out = oth;
        }
        initView();
    }

    private void initView() {
        pieChart = (PieChart) findViewById(R.id.pie_chat1);
        pie_chat2= (PieChart) findViewById(R.id.pie_chat2);
        showhodlePieChart();

        showRingPieChart();
    }

    private void showRingPieChart() {
//设置每份所占数量
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(2.0f, getString(R.string.CHART_INC)));

// 设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#6785f2"));

        PieChartManagger pieChartManagger=new PieChartManagger(pie_chat2,1,income);
        pieChartManagger.showRingPieChart(yvals,colors);
    }

    private void showhodlePieChart() {
        // 设置每份所占数量
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(bre_out, getString(R.string.CHART_BRE)));
        yvals.add(new PieEntry(lan_out, getString(R.string.CHART_LAN)));
        yvals.add(new PieEntry(din_out, getString(R.string.CHART_DIN)));
        yvals.add(new PieEntry(lif_out, getString(R.string.CHART_LIF)));
        yvals.add(new PieEntry(clo_out, getString(R.string.CHART_CLO)));
        yvals.add(new PieEntry(tra_out, getString(R.string.CHART_TRA)));
        yvals.add(new PieEntry(dri_out, getString(R.string.CHART_DRI)));
        yvals.add(new PieEntry(med_out, getString(R.string.CHART_MED)));
        yvals.add(new PieEntry(ent_out, getString(R.string.CHART_ENT)));
        yvals.add(new PieEntry(stu_out, getString(R.string.CHART_STU)));
        yvals.add(new PieEntry(oth_out, getString(R.string.CHART_OTH)));

        //设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FF2D2D"));
        colors.add(Color.parseColor("#FFBB77"));
        colors.add(Color.parseColor("#FFFF6F"));
        colors.add(Color.parseColor("#79FF79"));
        colors.add(Color.parseColor("#66B3FF"));
        colors.add(Color.parseColor("#BE77FF"));
        colors.add(Color.parseColor("#FF77FF"));
        colors.add(Color.parseColor("#B87070"));
        colors.add(Color.parseColor("#6FB7B7"));
        colors.add(Color.parseColor("#9999CC"));
        colors.add(Color.parseColor("#B766AD"));

        PieChartManagger pieChartManagger=new PieChartManagger(pieChart,0,0);
        pieChartManagger.showSolidPieChart(yvals,colors);
    }
}