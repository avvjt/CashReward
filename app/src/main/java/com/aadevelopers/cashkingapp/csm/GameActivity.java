package com.aadevelopers.cashkingapp.csm;

import static com.aadevelopers.cashkingapp.helper.PrefManager.BANNER_AD_TYPE;
import static com.aadevelopers.cashkingapp.helper.PrefManager.CHARTBOOST_AD_TYPE;
import static com.aadevelopers.cashkingapp.helper.PrefManager.INTERSTITAL_AD_TYPE;
import static com.aadevelopers.cashkingapp.helper.PrefManager.getSavedString;
import static com.aadevelopers.cashkingapp.helper.PrefManager.setWindowFlag;
import static com.aadevelopers.cashkingapp.helper.PrefManager.user_points;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aadevelopers.cashkingapp.AdsManager;
import com.aadevelopers.cashkingapp.R;
import com.aadevelopers.cashkingapp.csm.adapter.GameAdapter;
import com.aadevelopers.cashkingapp.csm.model.GameModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private List<GameModel> gameModel = new ArrayList<>();
    RecyclerView rv_game;
    GameAdapter game_adapter;
    ImageView back;

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
        setContentView(R.layout.activity_game);
        if (AdsManager.isInterstitialLoaded()) {
            AdsManager.showInterstitalAd(this);
        }
        rv_game = findViewById(R.id.rv_game);


        TextView points = findViewById(R.id.points);
        back = findViewById(R.id.back);
        user_points(points);
        back.setOnClickListener(view -> finish());

        Intent i = getIntent();
        try {
            JSONArray array = new JSONArray(i.getStringExtra("res"));
            for (int index = 0; index < array.length(); index++) {
                JSONObject feedObj = (JSONObject) array.get(index);
                Integer id = (feedObj.getInt("id"));
                String title = (feedObj.getString("title"));
                String image = (feedObj.getString("image"));
                String game_link = (feedObj.getString("game"));
                String gamePoints = (feedObj.getString("points"));
                String gameTime = (feedObj.getString("game_time"));
                GameModel item = new GameModel(id, title, image, game_link, gamePoints, gameTime);
                gameModel.add(item);
            }
            game_adapter = new GameAdapter(gameModel, GameActivity.this, 1);
            rv_game.setLayoutManager(new LinearLayoutManager(GameActivity.this));
            rv_game.setAdapter(game_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (getSavedString(this, BANNER_AD_TYPE).equalsIgnoreCase(CHARTBOOST_AD_TYPE) &&
                getSavedString(this, INTERSTITAL_AD_TYPE).equalsIgnoreCase(CHARTBOOST_AD_TYPE)
        ) {

        } else {
            AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));
        }
    }
}