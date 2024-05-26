package com.aadevelopers.cashkingapp.csm;

import static android.content.ContentValues.TAG;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCESS_KEY;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCESS_Value;
import static com.aadevelopers.cashkingapp.helper.Constatnt.Base_Url;
import static com.aadevelopers.cashkingapp.helper.Helper.FRAGMENT_LOAD_WEB_VIEW;
import static com.aadevelopers.cashkingapp.helper.Helper.FRAGMENT_TYPE;
import static com.aadevelopers.cashkingapp.helper.PrefManager.Add_Coins_;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.aadevelopers.cashkingapp.AdsManager;
import com.aadevelopers.cashkingapp.R;
import com.aadevelopers.cashkingapp.helper.AppController;
import com.aadevelopers.cashkingapp.helper.CircularTimerListener;
import com.aadevelopers.cashkingapp.helper.CircularTimerView;
import com.aadevelopers.cashkingapp.helper.ContextExtensionKt;
import com.aadevelopers.cashkingapp.helper.JsonRequest;
import com.aadevelopers.cashkingapp.helper.TimeFormatEnum;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayActivity extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    CircularTimerView countDownTimer;
    Float progress = 0f, maxpro = 60f;
    Boolean isOver = false, stopAnimation = false, isover = false, limit = false, check_play = false, isFistTimeLoaded = false;
    String url;
    CardView close, close3, play3, play2;
    LinearLayout loading;
    ImageView cut;
    TextView mints, coins, title;
    int sec = 0;
    int total_sec = 360; // in seconds
    String game_points = "30";
    //reward int
    int minuts, reward, time = 60;
    RelativeLayout claim, first, second, third, main_page, play;
    CircleImageView img;
    int pro = 0;
    ProgressBar progressBar;
    RelativeLayout animateLayout;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        first = findViewById(R.id.progress);
        countDownTimer = findViewById(R.id.progress_circular);
        countDownTimer.setProgress(0);
        animateLayout = findViewById(R.id.animateLayout);
        webView = findViewById(R.id.webView);
        second = findViewById(R.id.claim_layout);
        third = findViewById(R.id.warn);
        mints = findViewById(R.id.minuts);
        coins = findViewById(R.id.coins);
        play = findViewById(R.id.play);
        close = findViewById(R.id.close);
        close3 = findViewById(R.id.close3);
        play3 = findViewById(R.id.play3);
        claim = findViewById(R.id.claim);
        play2 = findViewById(R.id.play2);
        loading = findViewById(R.id.loading);
        cut = findViewById(R.id.cut);
        main_page = findViewById(R.id.main_page);
        img = findViewById(R.id.img);
        title = findViewById(R.id.title);
        progressBar = findViewById(R.id.progressBar);

        third.setVisibility(View.GONE);
        first.setVisibility(View.GONE);
        second.setVisibility(View.GONE);
        main_page.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        progressBar.setMax(total_sec);


        circularProgressBar = findViewById(R.id.PBar);
        circularProgressBar.setProgressWithAnimation(progress, 1000L); // =1s
        circularProgressBar.setProgressMax(maxpro);
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setStartAngle(-180f);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);

        getList();
        Intent i = getIntent();
        url = i.getStringExtra("url");

        if (i.getStringExtra("time") != null) {
            total_sec = Integer.parseInt(i.getStringExtra("time"));
        }
        if (i.getStringExtra("point") != null) {
            game_points = i.getStringExtra("point");
        }

        Glide.with(this).load(i.getStringExtra("image"))
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(img);

        title.setText(i.getStringExtra("name"));

        cut.setOnClickListener(view -> {
            if (AdsManager.isInterstitialLoaded()) {
                AdsManager.showInterstitalAd(PlayActivity.this);
                finish();
            } else {
                finish();
            }
        });
        play.setOnClickListener(view -> {
            check_play = true;
            launchWebView();
            main_page.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        });
        play2.setOnClickListener(view -> {
            check_play = true;
            launchWebView();
            main_page.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);

        });
        play3.setOnClickListener(view -> launchWebView());
        close.setOnClickListener(view -> {
            if (AdsManager.isInterstitialLoaded()) {
                AdsManager.showInterstitalAd(PlayActivity.this);
                finish();
            } else {
                finish();
            }

        });
        close3.setOnClickListener(view -> {
            if (AdsManager.isInterstitialLoaded()) {
                AdsManager.showInterstitalAd(PlayActivity.this);
                finish();
            } else {
                finish();
            }
        });
        animateLayout.setOnClickListener(v -> {
            if (isOver) {
                stopAnimation = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (AdsManager.isInterstitialLoaded()) {
                            AdsManager.showInterstitalAd(PlayActivity.this);
                        } else {
                            showPointsDialog();
                        }
                    }
                }, 500);
            }
        });
        loadAd();
    }

    private void showPointsDialog() {
        Add_Coins_(
                PlayActivity.this,
                game_points,
                "Game Star",
                "false",
                () -> {
                    finish();
                }
        );
    }

    private void loadAd() {
        AdsManager.loadInterstitalAd(this);
    }

    private void count() {
        if (countDownTimer != null) {
            countDownTimer.setClockwise(true);
            countDownTimer.setCircularTimerListener(new CircularTimerListener() {
                @Override
                public String updateDataOnTick(long remainingTimeInMs) {
                    return String.valueOf((int) Math.ceil((remainingTimeInMs / 1000.f)));
                }

                @Override
                public void onTimerFinished() {
                showAnimationInLoop();
                isOver = true;
                }
            }, total_sec, TimeFormatEnum.SECONDS, 1000);
        }
    }

    private void showAnimationInLoop() {
        stopAnimation = false;
        Animation animShake = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.shake_anim);
        animShake.setRepeatCount(Animation.INFINITE);
        animShake.setRepeatMode(Animation.RESTART);
        animateLayout.startAnimation(animShake);
        animShake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (stopAnimation) {
                    animShake.setRepeatCount(0);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        safelyPauseCountdownTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOver) {
            showPointsDialog();
        } else {
            safelyResumeCountdownTimer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        safelyRemoveCountdownTimer();
    }

    private void safelyPauseCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.pauseTimer();
        }
    }

    private void safelyResumeCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.resumeTimer();
        }
    }

    private void safelyRemoveCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.stopTimer();
            countDownTimer = null;
        }
    }


    public void getList() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, response -> {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }, error -> {
            webView.loadUrl(url);

            // this will enable the javascript.
            webView.getSettings().setJavaScriptEnabled(true);

            // WebViewClient allows you to handle
            // onPageFinished and override Url loading.
            webView.setWebViewClient(new MyBrowser());
            ContextExtensionKt.showLongToast(PlayActivity.this, error.toString());
                }
                ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("check_zone", AppController.getInstance().getUsername());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!isFistTimeLoaded) {
                loading.setVisibility(View.GONE);
                isFistTimeLoaded = true;
                count();
            }
        }
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            if (response.getString("error").equals("false")) {
                limit = true;
                check_play = true;
                webView.loadUrl(url);

                // this will enable the javascript.
                webView.getSettings().setJavaScriptEnabled(true);

                // WebViewClient allows you to handle
                // onPageFinished and override Url loading.
                webView.setWebViewClient(new MyBrowser());
            } else {
                limit = false;
                ContextExtensionKt.showLongToast(this, "Today chance is over");
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ContextExtensionKt.showLongToast(PlayActivity.this, e.toString());
        }
    }

    private void launchWebView() {
    }
}
