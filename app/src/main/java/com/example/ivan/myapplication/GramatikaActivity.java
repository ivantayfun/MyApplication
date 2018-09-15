package com.example.ivan.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class GramatikaActivity extends AppCompatActivity implements OnClickListener {

    String[] english_times = {"Present Simple","Past Simple","Future Simple","Present Continuous",
            "Past Continuous","Future Continuous","Present Perfect","Past Perfect","Future Perfect",
            "Present Perfect Continuous","Past Perfect Continuous","Future Perfect Continuous"};
    int[] colors = new int[4];
    LinearLayout gramatika_activity_lnlname;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gramatika);
        colors[0] = Color.parseColor("#ff9d9aff");
        colors[1] = Color.parseColor("#ff86ff9e");
        colors[2] = Color.parseColor("#ffff8e8d");
        colors[3] = Color.parseColor("#fffdff95");
        gramatika_activity_lnlname =  findViewById(R.id.gramatika_activity_lnlname);
            LayoutInflater ltInflayter_bdname = getLayoutInflater();
                gramatika_activity_lnlname.removeAllViews();
        for (int i = 0; i < english_times.length; i++) {
            View item = ltInflayter_bdname.inflate(R.layout.gramatika_item, gramatika_activity_lnlname, false);
            TextView text_slovary_item =  item.findViewById(R.id.text_gramatika_item);
            text_slovary_item.setText(english_times[i]);
            item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            text_slovary_item.setId(i);
            if (i==0||i==1||i==2) {
                item.setBackgroundColor(colors[0]);
            } else if (i==3||i==4||i==5){
                item.setBackgroundColor(colors[1]);
            }else if (i==6||i==7||i==8){
                item.setBackgroundColor(colors[2]);
            }else if (i==9||i==10||i==11){
                item.setBackgroundColor(colors[3]);
            }
            text_slovary_item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    zapyck(v.getId());

                                /*Intent intent = new Intent();
                                intent.putExtra("slovarb_name", dbname_name_arr[v.getId()-16]);
                                setResult(RESULT_OK, intent);
                                finish();*/
                }
            });
            gramatika_activity_lnlname.addView(item);
        }
    }
    public void zapyck(Integer id) {
        saveText("GRAMATIKA_ENGLISH_TIME",english_times[id]);
        saveText("mark_english_time", "0");
        Intent intent = new Intent(this,GramatikaMainActivity.class);
        startActivity(intent);
    }
    public void saveText(String nidname, String nidname_text) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(nidname, nidname_text);
        ed.apply();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gramatika, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nastroiki:
                nastroikifynk();
                break;
            case R.id.slovari:
                slovarifynk();
                break;
            case R.id.proiznoshenie:
                proiznosheniefunk();
                break;
            case R.id.londonberlin:
                zagryzkaspiskaslovarey();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //ЗАПУСК АКТИВИТИ НАСТРОЙКИ
    private void nastroikifynk() {
        Intent intent = new Intent(this, PrefActivity.class);
        startActivity(intent);
    }
    //ЗАПУСК АКТИВИТИ СЛОВАРИ
    private void slovarifynk() {
        Intent intent = new Intent(this, ActivitiSlovarb.class);
        startActivity(intent);
        finish();
    }
    //ФУНКЦИИ ЗАПУСТКА АКТИВИТИ ПРОИЗНОШЕНИЕ
    private void proiznosheniefunk() {
        Intent intent = new Intent(this, ProiznoshenieActivity.class);
        startActivity(intent);
        finish();
    }
    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void zagryzkaspiskaslovarey() {
        Intent intent = new Intent(this, ZagryzkaSpiskaSlovarey.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v) {
    }
}
