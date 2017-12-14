package com.example.a45773.Main;

import com.example.a45773.bean.Giftbean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 45773 on 2016-11-14.
 */

public class json_object {
    private ArrayList<Giftbean> giftbeans = new ArrayList<Giftbean>();

    public json_object() {

        try {
            URL url = new URL("http://open.douyucdn.cn/api/RoomApi/room/606118");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (200 == urlConnection.getResponseCode()) {

                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }
                js(baos.toString("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void js(String x) {
        try {
            JSONObject root = new JSONObject(x);
            JSONObject data = root.getJSONObject("data");
            JSONArray gift = data.getJSONArray("gift");
            for (int i = 0; i < gift.length(); i++) {
                JSONObject g = gift.getJSONObject(i);
                String id = g.getString("id");
                String name = g.getString("name");
                Giftbean giftbean=new Giftbean(id,name);
                giftbeans.add(giftbean);
            }
            MainActivity.giftbeans.addAll(giftbeans);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
