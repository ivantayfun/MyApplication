package com.example.ivan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by on 14.07.2017.
 */
public class ProiznoshenieActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout slovary_activity_lnlname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_proiznoshenie);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_proiznoshenie, menu);
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
            case R.id.gramatika:
                gramatikafunk();
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
    //ЗАПУСК АКТИВИТИ ГРАМАТИКА
    private void gramatikafunk() {
        Intent intent = new Intent(this, GramatikaActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}
