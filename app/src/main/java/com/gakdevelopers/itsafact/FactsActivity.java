package com.gakdevelopers.itsafact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class FactsActivity extends AppCompatActivity {

    String categoryName = null;
    String categoryImg = null;

    TextView txtCategoryName, txtFact;

    ImageView imgCategoryImg;

    CardView cardNext;

    int index = 0, counter = 0;

    ArrayList<String> list, visitedFactsList;

    private static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        categoryName = getIntent().getStringExtra("categoryName");
        categoryImg = getIntent().getStringExtra("categoryImg");

        txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);
        txtFact = (TextView) findViewById(R.id.txtFact);

        imgCategoryImg = (ImageView) findViewById(R.id.imgCategoryImg);

        cardNext = (CardView) findViewById(R.id.cardNext);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        txtCategoryName.setText(categoryName);

        switch (categoryName) {
            case "Random":
                imgCategoryImg.setImageResource(R.drawable.random);
                break;
            case "Healthy":
                imgCategoryImg.setImageResource(R.drawable.healthy);
                break;
            case "Science":
                imgCategoryImg.setImageResource(R.drawable.science);
                break;
            case "Historical":
                imgCategoryImg.setImageResource(R.drawable.historical);
                break;
            case "Creepy":
                imgCategoryImg.setImageResource(R.drawable.creepy);
                break;
            case "Animals":
                imgCategoryImg.setImageResource(R.drawable.animals);
                break;
        }

        list = new ArrayList<>();

        visitedFactsList = SaveToShared.readListFromPref(this);

        if (visitedFactsList == null)
            visitedFactsList = new ArrayList<>();

        if (!isNetworkConnected()) {
            new androidx.appcompat.app.AlertDialog.Builder(FactsActivity.this)
                    .setIcon(R.drawable.ic_error_24)
                    .setTitle("Connection Error")
                    .setMessage("Make sure you are connected to internet.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(FactsActivity.this, MainActivity.class));
                        }
                    })
                    .show();
            return;
        }

        getFacts();

        cardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTheFact();
            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void getFacts() {
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.api) + "?action=getFacts&CategoryName=" + categoryName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FactsActivity.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
        );

        int socketTimeOut = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void parseItems(String jsonResponse) {

        try {
            JSONObject jObj = new JSONObject(jsonResponse);
            JSONArray jArray = jObj.getJSONArray("items");

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jo = jArray.getJSONObject(i);
                String fact = jo.getString("Fact");
                list.add(fact);
            }

            Log.d("MY_FACTS", String.valueOf(list));

            getTheFact();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.dismiss();

    }

    private void getTheFact() {

        /*for (int i = 0; i <= list.size(); i++) {
            if (!list.isEmpty()) {
                Log.d("LIST_SIZE", String.valueOf(list.size()));
                if (!visitedFactsList.contains(list.get(index))) {
                    txtFact.setText(list.get(index));
                    visitedFactsList.add(list.get(index));

                    SaveToShared.writeListToPref(getApplicationContext(), visitedFactsList);

                    Log.d("MY_VISITED_FACTS", String.valueOf(visitedFactsList));

                    break;
                } else {
                    txtFact.setText("More interesting facts are coming soon!");
                }
            } else {
                txtFact.setText("More interesting facts are coming soon!");
            }
        }*/

        if (!list.isEmpty()) {
            Log.d("LIST_SIZE", String.valueOf(list.size()));
            index = new Random().nextInt(list.size());

            if (!visitedFactsList.contains(list.get(index))) {
                txtFact.setText(list.get(index));
                visitedFactsList.add(list.get(index));

                SaveToShared.writeListToPref(getApplicationContext(), visitedFactsList);

                Log.d("MY_VISITED_FACTS", String.valueOf(visitedFactsList));
            } else {
                getTheFact();
            }
        } else {
            txtFact.setText("More interesting facts are coming soon!");
        }
    }
}