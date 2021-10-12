package com.highcom.ponshu.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.highcom.ponshu.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SakenowaDataCollector {
    private static SakenowaDataCollector mSakenowaDataCollector;
    private List<String> mBrandsList;

    private SakenowaDataCollector() {
        requestBrands();
    }

    public static SakenowaDataCollector getInstance() {
        if (mSakenowaDataCollector == null) {
            mSakenowaDataCollector = new SakenowaDataCollector();
        }
        return mSakenowaDataCollector;
    }

    public List<String> getBrandsList() {
        return mBrandsList;
    }

    private void requestBrands() {
        try{
            httpRequest("https://muro.sakenowa.com/sakenowa-data/api/brands");
        }catch(Exception e){
            Log.e("HTTP_ERR",e.getMessage());
        }
    }

    void httpRequest(String url) throws IOException {

        //OkHttpClinet生成
        OkHttpClient client = new OkHttpClient();

        //request生成
        Request request = new Request.Builder()
                .url(url)
                .build();

        //非同期リクエスト
        client.newCall(request)
                .enqueue(new Callback() {

                    //エラーのとき
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("HTTP_ERR",e.getMessage());
                    }

                    //正常のとき
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        //response取り出し
                        final String jsonStr = response.body().string();

                        //JSON処理
                        try{
                            //jsonパース
                            JSONObject json = new JSONObject(jsonStr);
                            JSONArray brandsArray = json.getJSONArray("brands");
                            mBrandsList = new ArrayList<>();
                            for (int i = 0; i < brandsArray.length(); i++) {
                                mBrandsList.add(brandsArray.getJSONObject(i).get("name").toString());
                            }
                        }catch(Exception e){
                            Log.e("HTTP_ERR",e.getMessage());
                        }

                    }
                });
    }
}
