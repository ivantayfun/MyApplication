


package com.example.ivan.myapplication;

//import org.json.simple.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MainActivity2Activity {

    public static String enc;

    public static void main(String[] args) throws Exception {

        URL myUrl = new URL("http://adminka/android");
        HttpURLConnection myUrlCon = (HttpURLConnection) myUrl.openConnection();

        // ������� ����� �������

        System.out.println("mlkmlm" +
                myUrlCon.getRequestMethod());
        // ������� ��� ������

        System.out.println("�������� ���������: " +
                myUrlCon.getResponseMessage());

        // �������� ������ ����� � ��������� ������ �� ���������


        int i = 0;
        char[] chars = new char[10000];
        try {
            InputStream in = new BufferedInputStream(myUrlCon.getInputStream());
            int c;
            while ((c = in.read()) != -1) {

                chars[i] = ((char) c);
                //String str = new String(chars);
                //System.out.print(str);
                i++;


                //String str = new String(chars);
                //System.out.println(str);
                //String enc = new String(str.getBytes("ISO-8859-1"), "cp1251");
                //System.out.println(enc);
            }
            /*JSONObject resultJson = new JSONObject();

            resultJson.put("name","foo");
            resultJson.put("num",new Integer(100));
            resultJson.put("is_vip",new Boolean(true));
            resultJson.put("nickname",null);
            System.out.print(resultJson.toString());*/


            String str = new String(chars);
            enc = new String(str.getBytes("ISO-8859-1"), "utf-8");
            System.out.print(enc);

            //JSONObject ddv = optJSONObject (String name)
            URI uri = new URI("http://adminka/android");
            //JSONTokener tokener = new JSONTokener(uri.toURL().openStream());
            //JSONObject root = new JSONObject(tokener);
            //JSONObject vvv = new JSONObject();
            //vvv.get(enc);
            //JSONArray dataJsonObj = new JSONArray(enc);
            //JSONObject secondFriend = dataJsonObj.getJSONObject(2);
            //String secondName = secondFriend.getString("name_meny");
            //System.out.print(dataJsonObj);
        } finally {
            myUrlCon.disconnect();

        }


    }


}


