package com.example.finalhomework;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.icu.lang.UProperty.NAME;
import static com.example.finalhomework.MainActivity.FROM;

public class search extends AppCompatActivity implements
        SearchView.OnQueryTextListener{
    //db
    SQLiteDatabase db = MainActivity.db;
    Cursor cur; //存放查詢結果的 cursor物件
    SimpleCursorAdapter adapter;
    //
    private SearchView sv;
    private ListView lvs;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.setTitle(getString(R.string.TITLE_RECORD_MENU_SRC));
        lvs = (ListView) findViewById(R.id.lvs);
        //lvs.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mStrings));
        //lvs.setTextFilterEnabled(true);//設定lv可以被過慮
        //
        db = openOrCreateDatabase("keep", Context.MODE_PRIVATE,null);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.search, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        sv = (SearchView) menu.findItem(R.id.src_btn).getActionView();
        sv.setOnQueryTextListener(this);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cur = db.rawQuery("SELECT * FROM "+"keeplist",null);
                ArrayList<String> list1 = new ArrayList<>();
                //show
                ArrayList<String> userlist = new ArrayList<>();
                cur.moveToFirst();
                while(!cur.isAfterLast()) {
                    list1.add(cur.getString(cur.getColumnIndex(FROM[0]))
                            +"|"+cur.getString(cur.getColumnIndex(FROM[1]))
                            +"|"+cur.getString(cur.getColumnIndex(FROM[2]))
                            +"|"+cur.getString(cur.getColumnIndex(FROM[3]))); //add the item
                    cur.moveToNext();
                }

                for(String user: list1){
                    if(user.toLowerCase().contains(newText.toLowerCase())){
                        userlist.add(user);
                    }
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(search.this,android.R.layout.simple_list_item_1,userlist);
                lvs.setAdapter(adapter1);
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.src_btn:
                Intent it1 = new Intent(this,search.class);
                startActivity(it1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    // 使用者輸入字元時激發該方法

}