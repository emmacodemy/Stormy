package com.longbridge.learning.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = "b131fa7d935e9ef2ecc4a139819143be";
        double latitude = 37.8267;
        double longitude = -122.4233;
        if (isNetworkAvailable()) {
            String forecast = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;
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
                        Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {
                            Log.v(TAG, "throw io exception");
                        } else {
                            alertUserError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception");
                    }
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        else {
            Toast.makeText(this,"pls try again network is unavailable",Toast.LENGTH_LONG);
        }
        return isAvailable;
    }

    private void alertUserError() {
        UserAlertDialog dialog = new UserAlertDialog();
        dialog.show(getFragmentManager(),"error_dialog");
    }
}
