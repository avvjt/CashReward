package com.aadevelopers.cashkingapp.csm;

import static com.aadevelopers.cashkingapp.helper.PrefManager.setWindowFlag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aadevelopers.cashkingapp.AdsManager;
import com.google.android.material.tabs.TabLayout;
import com.aadevelopers.cashkingapp.R;
import com.aadevelopers.cashkingapp.csm.adapter.Trans_Adapter;

public class TransActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    Trans_Adapter trans_adapter;
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
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.head_color));
        setContentView(R.layout.activity_trans);

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab);
        back = findViewById(R.id.back);

        trans_adapter = new Trans_Adapter(this);
        viewPager.setAdapter(trans_adapter);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));
    }
}