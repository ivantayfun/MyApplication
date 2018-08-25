package com.example.ivan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

//import com.example.myapplication.R;

//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
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

public class ZagryzkaSpiskaSlovarey extends Activity {
    String jsonStr;
    public String flag;
    String namelessons;
    int[] dbname_id_arr;
    String[] name_lesson_arr;
    String[] name_lesson_otobr_arr;
    int[] name_lesson_id_arr;
    String[] dbname_name_otobrajenie_arr;
    public static String[] args;
    URLConnectionExample fff;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiti_slovarb);
        //jsonStr = "";
        dbname_id_arr = new int[20];


        dbname_name_otobrajenie_arr = new String[20];

        args = new String[3];
        new GetContacts().execute();
        args[0] = "POST";
        args[1] = "";
        args[2] = "";
        flag = "";

        //jsonStr = fff.main(args);
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

    private class GetContacts extends AsyncTask<Void, Void, Void> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Выводим сообщение о начале загрузки " );
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.zagryzka_spiska_slovarey), Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            StringBuilder content = new StringBuilder();
            try {

                int c;
                Log.d(TAG, "Подключ " );
                URL myUrl = new URL("http://annarybakova.net/android");
                HttpURLConnection ddd = (HttpURLConnection)myUrl.openConnection();
                ddd.setRequestMethod(args[0]);
                ddd.setReadTimeout(10000);
                ddd.setConnectTimeout(15000);

                //ddd.setRequestMethod("GET");
                ddd.setDoInput(true);
                ddd.setDoOutput(true);
                OutputStream os = ddd.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(args[1]+"="+args[2]);
                writer.flush();
                writer.close();
                os.close();
                ddd.connect();
                //ddd.setRequestProperty(args[1],args[2]);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ddd.getInputStream()));
                String line;
                while (((line = bufferedReader.readLine()) != null)) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (final Exception e){
                Log.e(TAG, "Проверте интернет соединение: " + e.getMessage());
                //e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Перевірте інтернет з'єднання" ,
                                Toast.LENGTH_LONG).show();

                    }
                });
                flag = "2";
                return null;


            }
            jsonStr = content.toString();
            //jsonStr = URLConnectionExample.main(args);
            //Log.e(TAG, "Response from url: " + jsonStr);
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
return null;

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
                    return null;
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

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if(flag.equalsIgnoreCase("2")){
                londonberlinfynk2();
            }else {
                londonberlinfynk();
            }
            //londonberlinfynk2();
        }


    }
}