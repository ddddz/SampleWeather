package com.example.samsung.weather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.weather.R;
import com.example.samsung.weather.util.HttpCallBackListener;
import com.example.samsung.weather.util.HttpUtil;
import com.example.samsung.weather.util.Utility;

/**
 * Created by samsung on 2016/10/6.
 */
public class weatherActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView titleWeatherCity;
    private TextView refreshTime;
    private TextView weatherInfo;
    private TextView temperature;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        if(sharedPreferences.getBoolean("has_loaded", false)){
//            showWeather();
//        }
        Intent intent = getIntent();
        String Url = intent.getStringExtra("city_url");
        titleWeatherCity = (TextView) findViewById(R.id.titleWeatherCity);
        refreshTime = (TextView) findViewById(R.id.refreshTime);
        weatherInfo = (TextView) findViewById(R.id.weatherInfo);
        temperature = (TextView) findViewById(R.id.temperature);
        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(this);

        getWeather(Url);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.search:
                Intent ser = new Intent(weatherActivity.this, lookUp_weather.class);
                ser.putExtra("is_from_weather", true);
                startActivity(ser);
                break;
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
        refreshTime.setText(sharedPreferences.getString("publish_time", ""));
        weatherInfo.setText(sharedPreferences.getString("weather_info", ""));
        temperature.setText(sharedPreferences.getString("temp", ""));
    }

}
