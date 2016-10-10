package com.example.samsung.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.samsung.weather.R;

import java.net.URLEncoder;

/**
 * Created by samsung on 2016/10/6.
 */
public class lookUp_weather extends Activity {

    private Button submit;
    private TextView input;
    private SharedPreferences sharedPreferences;

    private boolean isFromWeather;
    private final String urlName="http://op.juhe.cn/onebox/weather/query";//appkey
    private final String keys = "bc5bc07176469e357fc53e1682c4112b";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isFromWeather = getIntent().getBooleanExtra("is_from_weather", false);
        if(sharedPreferences.getBoolean("city_selected", false) && !isFromWeather){
            Intent intent = new Intent(lookUp_weather.this, weatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.lookup);
        submit = (Button) findViewById(R.id.submit);
        input = (TextView) findViewById(R.id.inputCity);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = input.getText().toString();
                Log.i("look_up", city);
                String Url = code(city, urlName, keys);
                Intent weatherActivity = new Intent(lookUp_weather.this, weatherActivity.class);
                weatherActivity.putExtra("is_from_look", true);
                weatherActivity.putExtra("city_url", Url);
                startActivity(weatherActivity);
                finish();
            }
        });
    }

    public static String code(String city, String urlname, String key){
        StringBuilder builder = new StringBuilder();
        try{
            builder.append(urlname).append("?cityname=").append(URLEncoder.encode(city, "UTF-8")).append("&").append("key=").append(key);
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.i("look_up", builder.toString());
        return builder.toString();
    }

}
