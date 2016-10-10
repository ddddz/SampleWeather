package com.example.samsung.weather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.weather.R;
import com.example.samsung.weather.util.HttpCallBackListener;
import com.example.samsung.weather.util.HttpUtil;
import com.example.samsung.weather.util.Utility;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by samsung on 2016/10/6.
 */
public class weatherActivity extends AppCompatActivity implements View.OnClickListener{

    private Calendar calendar;
    private String Url;
    private String hour, month, minute;
    private TextView titleWeatherCity;
    private TextView refreshTime;
    private TextView weatherInfo;
    private TextView temperature;
    private Button search;
    private Button refresh;
    private final String urlName="http://op.juhe.cn/onebox/weather/query";//appkey
    private final String keys = "bc5bc07176469e357fc53e1682c4112b";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        if(sharedPreferences.getBoolean("has_loaded", false) && !intent.getBooleanExtra("is_from_look", false)){
            Url = lookUp_weather.code(sharedPreferences.getString("city_name", ""), urlName, keys);

        } else {
            Url = intent.getStringExtra("city_url");
        }
        getWeather(Url);
        titleWeatherCity = (TextView) findViewById(R.id.titleWeatherCity);
        refreshTime = (TextView) findViewById(R.id.refreshTime);
        weatherInfo = (TextView) findViewById(R.id.weatherInfo);
        temperature = (TextView) findViewById(R.id.temperature);
        search = (Button) findViewById(R.id.search);
        refresh = (Button) findViewById(R.id.refresh);
        search.setOnClickListener(this);
        refresh.setOnClickListener(this);

//        getWeather(Url);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.search:
                Intent intent = new Intent(weatherActivity.this, lookUp_weather.class);
                intent.putExtra("is_from_weather", true);
                startActivity(intent);
                break;
            case R.id.refresh:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(weatherActivity.this);
                getWeather(lookUp_weather.code(sharedPreferences.getString("city_name", ""), urlName, keys));
                Log.i("refresh_weather", sharedPreferences.getString("weather_info", ""));
            default:
                break;
        }
    }

    private void getWeather(String Url){
        HttpUtil.sendHttpRequest(Url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(weatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(weatherActivity.this, "Failed to look up weather", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showWeather(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(weatherActivity.this);
        titleWeatherCity.setText(sharedPreferences.getString("city_name", ""));
        refreshTime.setText(currentTime());
        weatherInfo.setText(sharedPreferences.getString("weather_info", ""));
        temperature.setText(sharedPreferences.getString("temp", ""));
    }

    private String currentTime(){
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if(calendar.get(Calendar.AM_PM) == 0){
            hour = String.valueOf(calendar.get(Calendar.HOUR));
        } else {
            hour = String.valueOf(calendar.get(Calendar.HOUR) + 12);
        }
        month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if(calendar.get(Calendar.MINUTE) < 10){
            minute = 0 + String.valueOf(calendar.get(Calendar.MINUTE));
        } else {
            minute = String.valueOf(calendar.get(Calendar.MINUTE));
        }

        return calendar.get(Calendar.YEAR) + "-" + month + "-" + calendar.get(Calendar.DATE) + "  "
                + hour + ":" + minute;
    }

}
