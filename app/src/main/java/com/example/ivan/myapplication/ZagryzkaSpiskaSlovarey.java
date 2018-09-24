package com.example.ivan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class ZagryzkaSpiskaSlovarey extends Activity {
    String jsonStr;
    public String flag;
    //String namelessons;
    int[] dbname_id_arr;
    String[] name_lesson_arr;
    String[] name_lesson_otobr_arr;
    int[] name_lesson_id_arr;
    String[] dbname_name_otobrajenie_arr;
    public static String[] args;
    URLConnectionExample fff;
    static ZagryzkaSpiskaSlovarey zagryzkaSpiskaSlovarey;
    private static String TAG = MainActivity.class.getSimpleName();
    List<String> strings = Collections.synchronizedList(new ArrayList<String>());
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiti_slovarb);
        //jsonStr = "";
        dbname_id_arr = new int[20];
        dbname_name_otobrajenie_arr = new String[20];
        args = new String[3];
        //new GetContacts().execute();
        args[0] = "POST";
        args[1] = "";
        args[2] = "";
        flag = "";
        new TreadGetContacts(countDownLatch);
        new TreadGetContactsComplit(countDownLatch);
    }
    //Запускаем Активити для отображения списка словарей и передаем ей два архива со списком имен словарей и списком имен словарей для отображения
    private void londonberlinfynk() {
        Intent intent = new Intent(this, ZagryzkaSpiskaSlovareyActivity.class);
        intent.putExtra("name_lesson_arr", name_lesson_arr);
        intent.putExtra("name_lesson_id_arr", name_lesson_id_arr);
        intent.putExtra("name_lesson_otobr_arr", name_lesson_otobr_arr);
        startActivity(intent);
        finish();
    }

    private void londonberlinfynk2() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class TreadGetContactsComplit extends Thread {
        CountDownLatch countDownLatch;
        TreadGetContactsComplit(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
            start();
        }
        @Override
        public void run() {

            try {
                Log.d("ivan","Complit do await");
                countDownLatch.await();
                Log.d("ivan","Complit posle await");

                if(flag.equals("2")){
                    Log.d("ivan","Zapysk londonberlinfynk2");
                    flag = "";
                    londonberlinfynk2();
            }else {
                    Log.d("ivan","Zapysk londonberlinfynk2");
                londonberlinfynk();
            }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class TreadGetContacts extends Thread {
        CountDownLatch countDownLatch;
        TreadGetContacts(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
            start();
        }
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Загружаем список словарей для скачивания",
                            Toast.LENGTH_SHORT).show();
                }
            });
            StringBuilder content = new StringBuilder();
            try {
                //int c;
                Log.d(TAG, "Подключ ");
                URL myUrl = new URL("http://annarybakova.net/android");
                HttpURLConnection ddd = (HttpURLConnection) myUrl.openConnection();
                ddd.setRequestMethod(args[0]);
                ddd.setReadTimeout(10000);
                ddd.setConnectTimeout(15000);
                ddd.setDoInput(true);
                ddd.setDoOutput(true);
                OutputStream os = ddd.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(args[1] + "=" + args[2]);
                writer.flush();
                writer.close();
                os.close();
                ddd.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ddd.getInputStream()));
                String line;
                while (((line = bufferedReader.readLine()) != null)) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (final Exception e) {
                Log.e(TAG, "Проверте интернет соединение: " + e.getMessage());
                //e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Перевірте інтернет з'єднання",
                                Toast.LENGTH_LONG).show();
                    }
                });
                flag = "2";
            }
            jsonStr = content.toString();
            if (jsonStr.equalsIgnoreCase("")) {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
                flag = "2";
            } else {
                try {
                    JSONArray contacts = new JSONArray(jsonStr);
                    name_lesson_arr = new String[20];
                    name_lesson_otobr_arr = new String[20];
                    name_lesson_id_arr = new int[20];
                    String name_lesson;
                    String name_lesson_otobr;
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        name_lesson_id_arr[i] = c.getInt("id");
                        name_lesson = c.getString("name_lesson");
                        name_lesson_otobr = c.getString("name_lesson_otobr");
                        dbname_id_arr[i] = i;
                        name_lesson_arr[i] = name_lesson;
                        name_lesson_otobr_arr[i] = name_lesson_otobr;
                    }
                    flag = "1";
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error1: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error2: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    flag = "2";
                }
            }
            countDownLatch.countDown();
        }
    }
}