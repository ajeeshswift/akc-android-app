package com.swift.akc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.swift.akc.BaseAppCompactActivity;
import com.swift.akc.R;
import com.swift.akc.network.ApiEndpoint;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends BaseAppCompactActivity {

    EditText username;

    EditText password;

    Button login;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginApiCall();
            }
        });
    }

//    public void loginApiCall(){
//        showLoading();
//        AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
//                .addPathParameter("pageNumber", "0")
//                .addQueryParameter("limit", "3")
//                .addHeaders("token", "1234")
//                .setTag("test")
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        hideLoading();
//                        // do anything with response
//                        Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
//                    }
//
//
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                        Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    public void loginApiCall() {
        showLoading();
        JSONObject body = new JSONObject();
        try {
            body.put("userName", "Admin");
            body.put("password", "1955");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        URL url = new URL(ApiEndpoint.LOGIN_API);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        AndroidNetworking.post(ApiEndpoint.LOGIN_API)
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}