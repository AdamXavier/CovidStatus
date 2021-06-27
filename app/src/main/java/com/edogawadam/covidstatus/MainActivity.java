package com.edogawadam.covidstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    TextView tvCases, tvRecovered,tvCritical, tvActive, tvTodayCases,tvTodayDeath, tvTotalDeath, tvAffectedCountries;
    private Button button;
    ScrollView scrollView;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecover);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTodayDeath = findViewById(R.id.tvTodayDeath);
        tvTotalDeath = findViewById(R.id.tvTotalDeath);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);

        button = findViewById(R.id.btnTrack);
        scrollView = findViewById(R.id.scView);
        pieChart = findViewById(R.id.piechart);

        fetchData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaTrack();
            }
        });

    }

    private void bukaTrack() {
        Intent intent = new Intent(this, AffectedCountries.class);
        startActivity(intent);
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            tvCases.setText(jsonObject.getString("cases"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvCritical.setText(jsonObject.getString("critical"));
                            tvActive.setText(jsonObject.getString("active"));
                            tvTodayCases.setText(jsonObject.getString("todayCases"));
                            tvTodayDeath.setText(jsonObject.getString("todayDeaths"));
                            tvTotalDeath.setText(jsonObject.getString("deaths"));
                            tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));

                            //Kasus
                            pieChart.addPieSlice(new PieModel("Cases",
                                    Integer.parseInt(tvCases.getText().toString()),
                                    Color.parseColor("#FBA526")));

                            //Sembuh
                            pieChart.addPieSlice(new PieModel("Recovered",
                                    Integer.parseInt(tvRecovered.getText().toString()),
                                    Color.parseColor("#63B867")));

                            //Kematian
                            pieChart.addPieSlice(new PieModel("Deaths",
                                    Integer.parseInt(tvTotalDeath.getText().toString()),
                                    Color.parseColor("#F15451")));
                            //kasus aktif
                            pieChart.addPieSlice(new PieModel("Active",
                                    Integer.parseInt(tvActive.getText().toString()),
                                    Color.parseColor("#29B8FA")));

                            pieChart.startAnimation();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Ada Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}