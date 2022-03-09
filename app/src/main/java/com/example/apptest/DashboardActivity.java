package com.example.apptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import models.VolleyS;

public class DashboardActivity extends AppCompatActivity {

    private VolleyS vs;
    private RequestQueue requestQueue;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefe= getSharedPreferences("sesion", Context.MODE_PRIVATE);
        token = prefe.getString("token","0");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        perfil();
    }

    public void perfil(){

        vs = VolleyS.getInstance(this.getApplicationContext());
        requestQueue = vs.getRequestQueue();

        String url = "http://192.168.1.144:8000/api/empleado";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(DashboardActivity.this, "TOKEN SAVED: " + token, Toast.LENGTH_SHORT).show();
                            Toast.makeText(DashboardActivity.this, "ENTRASTE AL PERFIL " + response.get("empleado").toString(), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DashboardActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("DashError", error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }
}