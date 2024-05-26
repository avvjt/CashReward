package com.aadevelopers.cashkingapp.csm;

import static com.aadevelopers.cashkingapp.Just_base.addPoint;
import static com.aadevelopers.cashkingapp.helper.Helper.VISIT_REQUEST_CODE;
import static com.aadevelopers.cashkingapp.helper.PrefManager.BANNER_AD_TYPE;
import static com.aadevelopers.cashkingapp.helper.PrefManager.CHARTBOOST_AD_TYPE;
import static com.aadevelopers.cashkingapp.helper.PrefManager.INTERSTITAL_AD_TYPE;
import static com.aadevelopers.cashkingapp.helper.PrefManager.getSavedString;
import static com.aadevelopers.cashkingapp.helper.PrefManager.user_points;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aadevelopers.cashkingapp.AdsManager;
import com.aadevelopers.cashkingapp.OnVideoAdEnded;
import com.aadevelopers.cashkingapp.R;
import com.aadevelopers.cashkingapp.UpdateListener;
import com.aadevelopers.cashkingapp.csm.adapter.WebsiteAdapter;
import com.aadevelopers.cashkingapp.csm.gateway.Constant;
import com.aadevelopers.cashkingapp.csm.model.WebsiteModel;
import com.aadevelopers.cashkingapp.helper.AppController;
import com.aadevelopers.cashkingapp.helper.Helper;
import com.aadevelopers.cashkingapp.helper.PrefManager;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.startapp.sdk.adsbase.StartAppAd;
import com.unity3d.services.banners.BannerView;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VideoVisitActivity extends AppCompatActivity implements UpdateListener {

    VideoVisitActivity activity;
    TextView user_points_text_view, noWebsiteFound;
    private MediaPlayer timerSound;
    public int poiints = 0, counter_dialog = 0, visit_index = 0;
    private Handler timerHandler, pointsHandler;
    long soundTime = 10000;
    private WebsiteAdapter websiteAdapter;
    private ArrayList<WebsiteModel> websiteModelArrayList = new ArrayList<>();
    private RecyclerView websiteRv;
    private String visit1_link, visit_time, visit_browser, visit_id;

    CountDownTimer collectClickTimer;
    int collectClickCounter = 0;
    boolean collectClickTimerTrue = false, isWebsiteVisited = false,isTimerHandleFinished=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_video);
        activity = this;
        user_points_text_view = findViewById(R.id.points);
        noWebsiteFound = findViewById(R.id.noWebsiteFound);
        ImageView back = findViewById(R.id.back);
        websiteRv = findViewById(R.id.websiteRv);
        websiteRv.setLayoutManager(new LinearLayoutManager(activity));
        String json = PrefManager.getSavedString(activity, Helper.VIDEO_LIST);
        if (!json.equalsIgnoreCase("")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<WebsiteModel>>() {
            }.getType();
            ArrayList<WebsiteModel> websiteModels = gson.fromJson(json, type);
            for (int i = 0; i < websiteModels.size(); i++) {
                if (!PrefManager.getSavedString(activity, Helper.WATCHED1_DATE + websiteModels.get(i).getId()).equalsIgnoreCase(PrefManager.getSavedString(activity, Helper.TODAY_DATE))) {
                    websiteModelArrayList.add(websiteModels.get(i));
                }
            }
            websiteAdapter = new WebsiteAdapter(websiteModelArrayList, activity, "video", activity);
            websiteRv.setAdapter(websiteAdapter);
        }
        setWebsiteData();
        collectTimer();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (getSavedString(this, BANNER_AD_TYPE).equalsIgnoreCase(CHARTBOOST_AD_TYPE) &&
                getSavedString(this, INTERSTITAL_AD_TYPE).equalsIgnoreCase(CHARTBOOST_AD_TYPE)
        ) {

        } else {
            AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));
        }
        AdsManager.loadRewardedVideoAd(this);
    }

    private void setWebsiteData() {
        if (websiteModelArrayList.isEmpty()) {
            websiteRv.setVisibility(View.GONE);
            noWebsiteFound.setVisibility(View.VISIBLE);
        } else {
            websiteRv.setVisibility(View.VISIBLE);
            noWebsiteFound.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        user_points(user_points_text_view);
        if (timerHandler != null) {
            if (!isTimerHandleFinished) {
                timerHandler = null;
            }
        }
    }

    private void showDialogPoints(final int addNoAddValueVideo, final String points) {
        SweetAlertDialog sweetAlertDialog;

        if (PrefManager.isConnected(activity)) {
            if (addNoAddValueVideo == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText("Oops!");
                    sweetAlertDialog.setContentText(getResources().getString(R.string.better_luck));
                    sweetAlertDialog.setConfirmText("Ok");
                } else {
                    Log.e("TAG", "showDialogPoints: points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.you_won));
                    sweetAlertDialog.setContentText(points);
                    sweetAlertDialog.setConfirmText("Collect");
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setTitleText("Today chance is over");
                sweetAlertDialog.setConfirmText("Ok");
            }

            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    collectClickTimer.start();
                    if (collectClickTimerTrue) {
                        collectClickCounter++;
                    }
                    int finalPoint;
                    if (points.equals("")) {
                        finalPoint = 0;
                    } else {
                        finalPoint = Integer.parseInt(points);
                    }
                    poiints = finalPoint;
                    addPoint(activity, String.valueOf(poiints), "youtube_video");
                    user_points(user_points_text_view);
                    sweetAlertDialog.dismiss();
                    if (AdsManager.isRewardedVideoAdLoaded(activity)) {
                        AdsManager.showRewardedVideo(new OnVideoAdEnded() {
                            @Override
                            public void videoWatched() {

                            }
                        }, activity);
                    }
                    websiteModelArrayList.remove(visit_index);
                    websiteAdapter.notifyItemRemoved(visit_index);
                    setWebsiteData();
                    putDate(Helper.WATCHED1_DATE + visit_id);
                }
            }).show();
        } else {
            Toast.makeText(activity, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void putDate(String watched_date) {
        if (AppController.isConnected(activity)) {
            String currentDateWatched = PrefManager.getSavedString(activity, Helper.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDateWatched);
            String last_date_watched = PrefManager.getSavedString(activity, watched_date);
            if (last_date_watched.equalsIgnoreCase("0")) {
                last_date_watched = "";
            }
            Log.e("TAG", "onClick: last_date Date" + last_date_watched);
            if (last_date_watched.equals("")) {
                PrefManager.setString(activity, watched_date, currentDateWatched);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date pastDAte = sdf.parse(last_date_watched);
                    Date currentDAte = sdf.parse(currentDateWatched);
                    long diff = currentDAte.getTime() - pastDAte.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        PrefManager.setString(activity, watched_date, currentDateWatched);
                    } else {

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(activity, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    void collectTimer() {
        collectClickTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                collectClickTimerTrue = true;
            }

            @Override
            public void onFinish() {
                collectClickTimerTrue = false;
            }
        };
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void visitLinks() {
        if (visit_browser.equalsIgnoreCase("external")) {
            openLink(visit1_link);
            timerHandler = new Handler();
            timerHandler.postDelayed(() -> {
                isTimerHandleFinished = true;
                timerSound.start();
                pointsHandler = new Handler();
                pointsHandler.postDelayed(() -> {
                    timerHandler = null;
                    pointsHandler = null;
                    showDialogPoints(1, String.valueOf(poiints));
                }, soundTime);
            }, TimeUnit.MINUTES.toMillis(Long.parseLong(visit_time)));
        } else {
            Intent intent = new Intent(activity, VisitLinksActivity.class);
            intent.putExtra("type", "video");
            intent.putExtra("websiteModal", websiteModelArrayList.get(visit_index));
            startActivityForResult(intent, VISIT_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VISIT_REQUEST_CODE && resultCode == RESULT_OK) {
            showDialogPoints(1, String.valueOf(poiints));
        }
    }

    @Override
    public void UpdateListener(
            @Nullable String coin,
            @Nullable String time,
            @Nullable String link,
            @Nullable String browser,
            @Nullable String id,
            int index,
            String description,
            String logo,
            String packages) {
        visit1_link = link;
        visit_time = time;
        visit_browser = browser;
        visit_id = id;
        visit_index = index;
        poiints = Integer.parseInt(coin);
        isTimerHandleFinished = false;
        visitLinks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerSound != null) {
            timerSound.stop();
            timerSound = null;
        }
        if (pointsHandler != null) {
            pointsHandler = null;
        }
        if (timerHandler != null) {
            timerHandler = null;
        }
    }
}