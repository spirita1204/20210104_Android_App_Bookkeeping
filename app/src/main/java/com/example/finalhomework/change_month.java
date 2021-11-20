package com.example.finalhomework;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.finalhomework.MainActivity.FROM;
import static com.example.finalhomework.MainActivity.TB_NAME;

public class change_month extends AppCompatActivity {
    SQLiteDatabase db = MainActivity.db;
    Cursor cur; //存放查詢結果的 cursor物件
    SimpleCursorAdapter adapter;
    //
    ListView lv_monthchange;
    //
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    //TextView txv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_month);
        //db力常開
        db = openOrCreateDatabase("keep", Context.MODE_PRIVATE,null);
        String createTable = "CREATE TABLE IF NOT EXISTS "+ TB_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "time VARCHAR(32),"+
                "price VARCHAR(32),"+
                "class_ VARCHAR (32),"+
                "note VARCHAR(32))";
        db.execSQL(createTable);
        cur = db.rawQuery("SELECT * FROM "+TB_NAME,null);

        //
        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        lv_monthchange = (ListView)findViewById(R.id.lv_monthchange);
        //txv6 = (TextView)findViewById(R.id.txv6);
        //for全部家盪日歷
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if(cur.getCount()>0){//若有資料
            cur.moveToFirst();
            do{
                //txv.setText("1111");
                String myDate = cur.getString(cur.getColumnIndex(FROM[0]));
                //CHANGE DATE FOTMAT TO MILLIS
                //creates a formatter that parses the date in the given format
                Date date = null;
                try {
                    date = sdf.parse(myDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long timeInMillis = date.getTime();
                //millis format to event
                compactCalendarView.addEvent(new Event(Color.RED,timeInMillis,"aaa"));
            }while(cur.moveToNext());
        }
        //actionbar展示月
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        //Event ev1 = new Event(Color.RED,1609430400000l,"teacher");
        //compactCalendarView.addEvent(new Event(Color.RED,1609430400000l,"teacher"));

        actionBar.setTitle(dateFormatMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //martch
                compactCalendarView.getEvents(dateClicked);
                //listview used
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                String reverse2cal = df.format(dateClicked);
                //txv6.setText(reverse2cal);//debug use
                //存全部
                ArrayList<String> list_month_change = new ArrayList<>();
                //展示特定
                ArrayList<String> list_month_change_output = new ArrayList<>();
                cur.moveToFirst();
                while(!cur.isAfterLast()) {
                    list_month_change.add(cur.getString(cur.getColumnIndex(FROM[0]))
                            +"|"+cur.getString(cur.getColumnIndex(FROM[1]))
                            +"|"+cur.getString(cur.getColumnIndex(FROM[2]))
                            +"|"+cur.getString(cur.getColumnIndex(FROM[3]))); //add the item
                    cur.moveToNext();
                }
                for(String user: list_month_change){
                    if(reverse2cal.equals(user.substring(0,10))){
                        list_month_change_output.add(user);
                    }
                }
                if(list_month_change_output.isEmpty()){
                    Toast.makeText(change_month.this,getString(R.string.NO_DATA),Toast.LENGTH_SHORT).show();
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(change_month.this,android.R.layout.simple_list_item_1,list_month_change_output);
                lv_monthchange.setAdapter(adapter2);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
    }

}