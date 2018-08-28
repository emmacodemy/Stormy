package com.longbridge.learning.stormy;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.longbridge.learning.stormy.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;
    private ImageView iconImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        iconImageView = (ImageView) findViewById(R.id.iconImageView);
        TextView darksky = findViewById(R.id.darkSkyAttribution);
        darksky.setMovementMethod(LinkMovementMethod.getInstance());

        String apiKey = "/b131fa7d935e9ef2ecc4a139819143be/";
        double latitude = 6.6080;
        double longitude = 3.6218;
        if (isNetworkAvailable()) {
            String forecast = "https://api.darksky.net/forecast" + apiKey + latitude + "," + longitude;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecast).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.v(TAG,"IO Exception occured",e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            currentWeather = getCurrentWeather(jsonData);
                            final CurrentWeather displayWeather = new CurrentWeather(
                                    currentWeather.getLocationLabel(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTime(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getPrecipChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimeZone()
                            );
                            binding.setWeather(displayWeather);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = getResources().getDrawable(displayWeather.getIconId());
                                    iconImageView.setImageDrawable(drawable);
                                }
                            });
                        } else {
                            alertUserError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception");
                    } catch (JSONException e) {
                        Log.e(TAG,"Json Exception occured");
                    }
                }
            });
        }
    }

    private CurrentWeather getCurrentWeather(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject currently = forecast.getJSONObject("currently");
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setLocationLabel("Lagos, Nigeria.");
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setTemperature((currently.getDouble("temperature") - 32) * (5/9.0));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTimeZone(forecast.getString("timezone"));
        currentWeather.setSummary(currently.getString("summary"));
        Log.v(TAG,currentWeather.toString());
        return  currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        else {
            networkNotAvail();
        }
        return isAvailable;
    }

    private void networkNotAvail() {
        NetworkDIalog dialog = new NetworkDIalog();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    private void alertUserError() {
        UserAlertDialog dialog = new UserAlertDialog();
        dialog.show(getFragmentManager(),"error_dialog");
    }
}
