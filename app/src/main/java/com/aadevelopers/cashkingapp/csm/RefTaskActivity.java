package com.aadevelopers.cashkingapp.csm;

import static android.content.ContentValues.TAG;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCESS_KEY;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCESS_Value;
import static com.aadevelopers.cashkingapp.helper.Constatnt.Base_Url;
import static com.aadevelopers.cashkingapp.helper.Constatnt.TASK;
import static com.aadevelopers.cashkingapp.helper.PrefManager.setWindowFlag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.aadevelopers.cashkingapp.AdsManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.aadevelopers.cashkingapp.R;
import com.aadevelopers.cashkingapp.helper.AppController;
import com.aadevelopers.cashkingapp.helper.JsonRequest;
import com.aadevelopers.cashkingapp.csm.adapter.Task_adapter;
import com.aadevelopers.cashkingapp.csm.model.Task_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefTaskActivity extends AppCompatActivity {
    ImageView back;
    private Task_adapter task_adapter;
    RecyclerView task_list;
    private List<Task_model> task_item = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_ref_task);

        back = findViewById(R.id.back);
        task_list = findViewById(R.id.task_list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getquizlist();
        AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));
    }

    public void getquizlist() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RefTaskActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(TASK, TASK);
                params.put("id", AppController.getInstance().getId());
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("data");
            task_item.clear();
            int count = 0;
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                count++;

                String id = (feedObj.getString("id"));
                Integer invites=(feedObj.getInt("invites"));
                String points = (feedObj.getString("points"));
                String check = (feedObj.getString("check"));
                String title = ""+count;
                Integer reff = response.getInt("ref");


                Task_model item = new Task_model(id, invites, points,check,title,reff);
                task_item.add(item);

            }

            task_adapter = new Task_adapter(task_item, RefTaskActivity.this,"1");

            task_list.setHasFixedSize(true);
            task_list.setLayoutManager(new LinearLayoutManager(RefTaskActivity.this));
            task_list.setAdapter(task_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(RefTaskActivity.this,"error",Toast.LENGTH_LONG).show();

        }
    }
}