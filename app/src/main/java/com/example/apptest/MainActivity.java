package com.example.apptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.VolleyS;

public class MainActivity extends AppCompatActivity {

    private EditText etNumero;
    private Button btnIniciar;
    private VolleyS vs;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumero = findViewById(R.id.editTextNumero);
        btnIniciar = findViewById(R.id.btnIniciar);



        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion(etNumero.getText().toString());
            }
        });
    }


    public void iniciarSesion(String numeroEmpleado) {

        vs = VolleyS.getInstance(this.getApplicationContext());
        requestQueue = vs.getRequestQueue();

        String url = "http://192.168.1.144:8000/api/login";

        JSONObject data = new JSONObject();
        try {
            data.put("numero_empleado", numeroEmpleado);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.get("token").toString();
                            Toast.makeText(MainActivity.this, "token: " + response.get("token").toString(), Toast.LENGTH_SHORT).show();
                            SharedPreferences preferencias =  getSharedPreferences("sesion", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencias.edit();
                            editor.putString("token", token);
                            editor.commit();

                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) ;

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }
}