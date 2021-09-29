package com.highcom.ponshu;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.highcom.ponshu.databinding.ActivityMainBinding;

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

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SearchView mSearchView;
    private List<String> mBrandsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_timeline, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //httpリクエスト
        try{
            //okhttpを利用するカスタム関数（下記）
            httpRequest("https://muro.sakenowa.com/sakenowa-data/api/brands");
        }catch(Exception e){
            Log.e("Hoge",e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // 検索バーの追加
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // 文字色、サイズの変更とプレースホルダーセット(任意)
//        SearchView.SearchAutoComplete searchAutoComplete = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        searchAutoComplete.setHintTextColor(Color.rgb(0xff, 0xff, 0xff));
//        searchAutoComplete.setTextSize(25);
//        searchAutoComplete.setHint("検索キーワード");

        return true;
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
                        Log.e("Hoge",e.getMessage());
                    }

                    //正常のとき
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        //response取り出し
                        final String jsonStr = response.body().string();
                        Log.d("Hoge","jsonStr=" + jsonStr);

                        //JSON処理
                        try{
                            //jsonパース
                            JSONObject json = new JSONObject(jsonStr);
                            JSONArray brandsArray = json.getJSONArray("brands");
                            mBrandsList = new ArrayList<>();
                            for (int i = 0; i < brandsArray.length(); i++) {
                                mBrandsList.add(brandsArray.getJSONObject(i).get("name").toString());
                            }

                            //親スレッドUI更新
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO:ここにListViewに設定する処理を追加
//                                    textView.setText(finalBrandsList);
                                    Toast.makeText(MainActivity.this, "データ取得完了", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }catch(Exception e){
                            Log.e("Hoge",e.getMessage());
                        }

                    }
                });
    }
}