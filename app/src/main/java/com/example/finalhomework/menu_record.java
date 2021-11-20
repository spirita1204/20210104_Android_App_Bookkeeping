package com.example.finalhomework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.core.view.MenuItemCompat;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.finalhomework.MainActivity.FROM;
import static com.example.finalhomework.MainActivity.TB_NAME;

public class menu_record extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener , DialogInterface.OnClickListener{
    SQLiteDatabase db = MainActivity.db;
    Cursor cur; //存放查詢結果的 cursor物件
    SimpleCursorAdapter adapter;
    //
    TextView date,price,note;
    MoneyTextView in,out,total;
    ListView lv;
    static  int alert_position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_record);
        //

        //Intent it = getIntent(); //1.切換method1
        //String get_date = it.getStringExtra("time");
        //String get_price = it.getStringExtra("price");
        //String get_note = it.getStringExtra("note");

        this.setTitle(getString(R.string.TITLE_RECORD));

        //date = (TextView) findViewById(R.id.date1);
        //price = (TextView) findViewById(R.id.price1);
        //note = (TextView) findViewById(R.id.note1);
        //date.setText(get_date);
        //price.setText(get_price);
        //note.setText(get_note);
        //
        //
        in = (MoneyTextView)findViewById(R.id.in);
        out = (MoneyTextView)findViewById(R.id.out);
        total = (MoneyTextView)findViewById(R.id.total);

        db = openOrCreateDatabase("keep", Context.MODE_PRIVATE,null);
        String createTable = "CREATE TABLE IF NOT EXISTS "+ TB_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "time VARCHAR(32),"+
                "price VARCHAR(32),"+
                "class_ VARCHAR (32),"+
                "note VARCHAR(32))";
        db.execSQL(createTable);
        cur = db.rawQuery("SELECT * FROM "+TB_NAME,null);
        if(cur.getCount() == 0){
        addData("2020/12/04","200","生活","預設資料");
        }
        //build adapter instance
        adapter = new SimpleCursorAdapter(this,
                R.layout.item,
                cur,
                FROM,
                new int[] { R.id.time,R.id.price,R.id.class_,R.id.note},
                0 );

        lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        //lv = (ListView)findViewById(R.id.lv);
        //lv.setAdapter(adapter);
        //
        if(cur.getCount()>0){//若有資料
            float input_total = 0;
            float output_total = 0;
            //int a = cur.getCount();
            //count.setText(Integer.toString(a));
            cur.moveToFirst();
            do{
                if(cur.getString(cur.getColumnIndex(FROM[2])).equals("收入")){
                    input_total+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                else{
                    output_total+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                }
                //cur.getString(cur.getColumnIndex(FROM[2]))
            }while(cur.moveToNext());
            //String aa = cur.getString(cur.getColumnIndex(FROM[2]));
            in.setAmount(input_total);
            out.setAmount(output_total);
            total.setAmount(input_total-output_total);
        }
    }
    public  void on_builddata(View v){
        finish();
        Intent it  = new Intent();
        it.setClass(this,MainActivity.class);
        startActivity(it);
    }
    private  void addData(String time,String price,String class_,String note){
        ContentValues cv = new ContentValues(4);
        cv.put(FROM[0],time);
        cv.put(FROM[1],price);
        cv.put(FROM[2],class_);
        cv.put(FROM[3],note);
        db.insert(TB_NAME,null,cv);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //多餘cur.moveToPosition(position);
        //
        String edit_time = cur.getString(cur.getColumnIndex(FROM[0]));
        String edit_price = cur.getString(cur.getColumnIndex(FROM[1]));
        String edit_class = cur.getString(cur.getColumnIndex(FROM[2]));
        String edit_note = cur.getString(cur.getColumnIndex(FROM[3]));
        Intent it = new Intent(this,MainActivity.class);
        it.putExtra("edit_time",edit_time);
        it.putExtra("edit_price",edit_price);
        it.putExtra("edit_class",edit_class);
        it.putExtra("edit_note",edit_note);
        it.putExtra("cheat_add_or_edit",1);
        it.putExtra("position",position);
        startActivityForResult(it,position);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.example_menu_main_use, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.monthcheck:
                Intent it2 = new Intent(this,change_month.class);
                startActivity(it2);
                return true;
            case R.id.search:
                Intent it1 = new Intent(this,search.class);
                startActivity(it1);
                return true;
            case R.id.chart:
                Intent it = new Intent(this,chart.class);
                startActivity(it);
                return true;
            case R.id.refresh:
                cur.moveToFirst();
                cur = db.rawQuery("SELECT * FROM "+TB_NAME,null);
                adapter.changeCursor(cur);
                if(cur.getCount()>0){//若有資料
                    float input_total = 0;
                    float output_total = 0;
                    //int a = cur.getCount();
                    //count.setText(Integer.toString(a));
                    cur.moveToFirst();
                    do{
                        if(cur.getString(cur.getColumnIndex(FROM[2])).equals("收入")){
                            input_total+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                        }
                        else{
                            output_total+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                        }
                        //cur.getString(cur.getColumnIndex(FROM[2]))
                    }while(cur.moveToNext());
                    //String aa = cur.getString(cur.getColumnIndex(FROM[2]));
                    in.setAmount(input_total);
                    out.setAmount(output_total);
                    total.setAmount(input_total-output_total);
                }
                Toast.makeText(this,getString(R.string.SYNC),Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        alert_position = position;
        new AlertDialog.Builder(this)
                .setMessage("確定要刪除?")
                .setCancelable(false)
                .setIcon(R.drawable.del)
                .setTitle("刪除")
                .setPositiveButton("確定",this)
                .setNeutralButton("取消",this)
                .show();
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            cur.moveToPosition(alert_position);
            db.delete("keeplist", "_id=" + cur.getInt(0), null);
            cur = db.rawQuery("SELECT * FROM " + "keeplist", null);
            //adapter.changeCursor(cur);
            //
            if(cur.getCount()>0){//若有資料
                float input_total = 0;
                float output_total = 0;
                //int a = cur.getCount();
                //count.setText(Integer.toString(a));
                cur.moveToFirst();
                do{
                    if(cur.getString(cur.getColumnIndex(FROM[2])).equals("收入")){
                        input_total+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                    }
                    else{
                        output_total+=Float.parseFloat(cur.getString(cur.getColumnIndex(FROM[1])));
                    }
                    //cur.getString(cur.getColumnIndex(FROM[2]))
                }while(cur.moveToNext());
                //String aa = cur.getString(cur.getColumnIndex(FROM[2]));
                in.setAmount(input_total);
                out.setAmount(output_total);
                total.setAmount(input_total-output_total);
            }
            cur = db.rawQuery("SELECT * FROM " + "keeplist", null);
            adapter.changeCursor(cur);
        }
        else{
        }

    }


}