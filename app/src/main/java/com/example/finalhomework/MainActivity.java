package com.example.finalhomework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,View.OnClickListener,AdapterView.OnItemClickListener, TextWatcher {
    TextView txv;
    EditText edt;
    Button bt_insert,next_btn;
    ListView lv;

    //db
    static  final String DB_NAME ="keep";
    static  final String TB_NAME = "keeplist";
    static  final int MAX = 40;//紀錄資料上限
    static  final  String[] FROM = new String[] {"time","price","class_","note"};//資料表欄位名稱字串陣列
    public  static SQLiteDatabase db;//in order use same database in different activity
    Cursor cur; //存放查詢結果的 cursor物件
    SimpleCursorAdapter adapter;
    //日歷用
    TextView txdate,class_txv;
    private int mYear;
    private int mMonth;
    private int mDay;
    //price used
    EditText price_txv;
    EditText note_txv;
    //DecimalFormat df = new DecimalFormat("$#,##0.00");
    //建物件
    Calendar c = Calendar.getInstance();
    //获取当前日期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //title
        this.setTitle(getString(R.string.TITLE_RECORD_INPUT));
        //count
        //count = (TextView)findViewById(R.id.count);
        //count2 = (TextView)findViewById(R.id.count2);
        //date
        //
        next_btn = (Button)findViewById(R.id.next);
        bt_insert = (Button)findViewById(R.id.save);
        //
        txdate = (TextView) findViewById(R.id.editTextTime);
        txdate.setOnClickListener(this);
        //price used
        price_txv = (EditText)findViewById(R.id.price_txv);
        note_txv = (EditText)findViewById(R.id.note_txv);
        class_txv = (TextView)findViewById(R.id.class_txv);
        class_txv.setOnClickListener(this);
        //
        if("".equals(txdate.getText().toString().trim())||"".equals(price_txv.getText().toString().trim())||
                "".equals(class_txv.getText().toString().trim())||"".equals(note_txv.getText().toString().trim())){
            next_btn.setEnabled(false);
            bt_insert.setEnabled(false);
        }else{
            next_btn.setEnabled(true);
            bt_insert.setEnabled(true);
        }
        //

        //intent for moditify
        Intent it = getIntent();
        String time_edit_get = it.getStringExtra("edit_time");
        String price_edit_get = it.getStringExtra("edit_price");
        String class_edit_get = it.getStringExtra("edit_class");
        String note_edit_get = it.getStringExtra("edit_note");
        txdate.setText(time_edit_get);
        price_txv.setText(price_edit_get);
        note_txv.setText(note_edit_get);

        //open and build database
        db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null);
        //build the data table
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



        requery();
        //textxwatcher
        txdate.addTextChangedListener(this);
        price_txv.addTextChangedListener(this);
        class_txv.addTextChangedListener(this);
        note_txv.addTextChangedListener(this);

    }
    private  void addData(String time,String price,String class_,String note){
        ContentValues cv = new ContentValues(4);
        cv.put(FROM[0],time);
        cv.put(FROM[1],price);
        cv.put(FROM[2],class_);
        cv.put(FROM[3],note);
        db.insert(TB_NAME,null,cv);
    }
    private  void requery(){
        cur = db.rawQuery("SELECT * FROM "+TB_NAME,null);
        adapter.changeCursor(cur);
        if(cur.getCount() == MAX)
            bt_insert.setEnabled(false);
        else{}

    }




    public void onCancel(View v){
        finish();
        Intent it  = new Intent();
        it.setClass(this,menu_record.class);
        startActivity(it);
    }
    public void onSave(View v){
        finish();
        Intent it  = new Intent();
        it.setClass(this,menu_record.class);
        it.putExtra("time",txdate.getText().toString());
        it.putExtra("price",price_txv.getText().toString());
        it.putExtra("note",note_txv.getText().toString());

        startActivity(it);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        // 更新EditText控件日期 小於10加0
        txdate.setText(new StringBuilder()
                .append(mYear)
                .append("/")
                .append((mMonth + 1) < 10 ? "0"
                        + (mMonth + 1) : (mMonth + 1))
                .append("/")
                .append((mDay < 10) ? "0" + mDay : mDay));
        //txdate.setText("date"+year+"/"+(month+1)+"/"+dayOfMonth);
    }
    @Override
    public void onClick(View v) {
        if(v == txdate){
            new DatePickerDialog(this,this,c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH))
                    .show();
        }
        if(v == class_txv){
            GridView gridView = new GridView(MainActivity.this);
            gridView.setAdapter(new AlertDialogImageAdapter(MainActivity.this));
            gridView.setNumColumns(3);
            gridView.setGravity(Gravity.CENTER);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO: Implement
                        //Toast.makeText(view.getContext(), "Clicked position is: " + position, Toast.LENGTH_LONG).show();
                    String[] iconName = {
                            "早餐", "午餐", "晚餐",
                            "生活", "服飾", "交通",
                            "飲料", "醫療", "收入",
                            "娛樂", "進修", "其他",
                    };
                    String positionout = iconName[position];
                    class_txv.setText(positionout);
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setNeutralButton(getString(R.string.CANCEL), new DialogInterface.OnClickListener() { // define the 'Cancel' button
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton(getString(R.string.CORRECT), new DialogInterface.OnClickListener() { // define the 'Cancel' button
                public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                }
            });
            builder.setView(gridView);
            builder.setTitle(getString(R.string.TITLE_RECORD_INPUT));
            builder.show();
        }
    }
    private void showAlertDialog(Context context) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void oninsertupdate(View view){
        finish();
        Intent it  = getIntent();
        it.setClass(this,menu_record.class);
        //it.putExtra("time",txdate.getText().toString());

        String txstr = txdate.getText().toString().trim();
        String pricestr = price_txv.getText().toString().trim();
        String classstr = class_txv.getText().toString().trim();
        String notestr = note_txv.getText().toString().trim();
        if(txstr.length()==0||pricestr.length()==0||classstr.length()==0||notestr.length()==0){
            return;
        }
        if(view.getId()==R.id.save){
            int cheat=it.getIntExtra("cheat_add_or_edit",0);
            int position=it.getIntExtra("position",0);
            if(cheat == 1){//判斷修改還是新增
                cur.moveToPosition(position);
                update(txstr,pricestr,classstr,notestr,cur.getInt(0));
                Toast.makeText(this,getString(R.string.FIX),Toast.LENGTH_SHORT).show();
            }
            else {
                addData(txstr, pricestr, classstr, notestr);
                Toast.makeText(this,getString(R.string.ADD_SUCCESS),Toast.LENGTH_SHORT).show();
                //update(idstr,namestr,pricestr,cur.getInt(0));
            }
        }
        else{
            // addData(idstr,namestr,pricestr);
        }
        requery();
        startActivity(it);
    }
    private void update(String time,String price,String class_,String note,int id){
        ContentValues cv = new ContentValues(4);
        cv.put(FROM[0],time);
        cv.put(FROM[1],price);
        cv.put(FROM[2],class_);
        cv.put(FROM[3],note);
        db.update(TB_NAME,cv,"_id="+id,null);
    }
    public void next(View v){
        String txstr = txdate.getText().toString().trim();
        String pricestr = price_txv.getText().toString().trim();
        String classstr = class_txv.getText().toString().trim();
        String notestr = note_txv.getText().toString().trim();

        if(v.getId()==R.id.next){
            addData(txstr, pricestr, classstr, notestr);
            Toast.makeText(this,"新增成功!請接續輸入下一筆!",Toast.LENGTH_SHORT).show();
        }
        txdate.setText(null);
        price_txv.setText(null);
        class_txv.setText(null);
        note_txv.setText(null);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if("".equals(txdate.getText().toString().trim())||"".equals(price_txv.getText().toString().trim())||
                "".equals(class_txv.getText().toString().trim())||"".equals(note_txv.getText().toString().trim())){
            next_btn.setEnabled(false);
            bt_insert.setEnabled(false);
        }else{
            next_btn.setEnabled(true);
            bt_insert.setEnabled(true);
        }
    }
}
