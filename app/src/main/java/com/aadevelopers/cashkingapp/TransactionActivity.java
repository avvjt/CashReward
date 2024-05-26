package com.aadevelopers.cashkingapp;

import static com.aadevelopers.cashkingapp.helper.AppController.hidepDialog;
import static com.aadevelopers.cashkingapp.helper.AppController.showpDialog;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCESS_KEY;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCESS_Value;
import static com.aadevelopers.cashkingapp.helper.Constatnt.API;
import static com.aadevelopers.cashkingapp.helper.Constatnt.Base_Url;
import static com.aadevelopers.cashkingapp.helper.Constatnt.POINTS;
import static com.aadevelopers.cashkingapp.helper.Constatnt.USERNAME;
import static com.aadevelopers.cashkingapp.helper.Constatnt.USER_TRACKER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aadevelopers.cashkingapp.Adapter.Earning_Adapter;
import com.aadevelopers.cashkingapp.helper.AppController;
import com.aadevelopers.cashkingapp.helper.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {

    Earning_Adapter adapter;
    private List<Model> historyList = new ArrayList<>();
    RecyclerView list;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        prepareData();

        list = findViewById(R.id.list);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    private void prepareData() {
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {

                        try {
                            JSONObject response = new JSONObject(response1);
                            if (response.getString("error").equalsIgnoreCase("false")) {


                                JSONArray jsonArray = response.getJSONArray("data");
                                historyList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String desc = jsonObject.getString("type");
                                    String date = jsonObject.getString("date");
                                    String time = jsonObject.getString("time");
                                    String coin = jsonObject.getString(POINTS);
                                    Model model = new Model(desc, date, coin,time);
                                    historyList.add(model);
                                }
                                adapter = new Earning_Adapter(historyList);
                                  list.setAdapter(adapter);
                              hidepDialog();

                            } else {
                                if (response.getString("message").equalsIgnoreCase("No tracking history found!")) {
                                    Toast.makeText(TransactionActivity.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TransactionActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TransactionActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(USER_TRACKER, API);
                params.put(USERNAME, AppController.getInstance().getUsername());

                return params;
            }

        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}