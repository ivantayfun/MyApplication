package com.example.ivan.myapplication;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.ivan.myapplication.ZagryzkaSpiskaSlovarey;
public class URLConnectionExample extends Application{




    public static String main(String [] args)  {





        StringBuilder content = new StringBuilder();
        try {

            int c;
            URL myUrl = new URL("http://annarybakova.net/android");
            HttpURLConnection ddd = (HttpURLConnection)myUrl.openConnection();
            ddd.setRequestMethod(args[0]);
            ddd.setReadTimeout(10000);
            ddd.setConnectTimeout(15000);
            //ddd.setRequestMethod("POST");
            ddd.setDoInput(true);
            ddd.setDoOutput(true);
            OutputStream os = ddd.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(os, "UTF-8"));
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
        } catch (IOException e){
            String TAG = AndroidActivity.class.getSimpleName();
            Log.e(TAG, "Json parsing error1: " + e.getMessage());
                        e.printStackTrace();

        }
        return content.toString();
    }

}
