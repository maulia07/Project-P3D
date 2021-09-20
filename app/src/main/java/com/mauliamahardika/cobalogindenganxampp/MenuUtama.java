package com.mauliamahardika.cobalogindenganxampp;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.mauliamahardika.cobalogindenganxampp.kombanslide.BannerSlider;
import com.mauliamahardika.cobalogindenganxampp.kombanslide.FragmentSlider;
import com.mauliamahardika.cobalogindenganxampp.kombanslide.SliderIndicator;
import com.mauliamahardika.cobalogindenganxampp.kombanslide.SliderPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuUtama extends AppCompatActivity {
    //deklarasi
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private BannerSlider bannerSlider;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar warna
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.warna_statusbar));

        //inisialisasi
        setContentView(R.layout.activity_menuutama);
        bannerSlider = findViewById(R.id.sliderView);
        mLinearLayout = findViewById(R.id.pagesContainer);
        setupSlider();
    }
    private void setupSlider() {
        bannerSlider.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();

        //link image
        fragments.add(FragmentSlider.newInstance("https://i.ibb.co/hVwY49Z/dua.jpg"));
        fragments.add(FragmentSlider.newInstance("https://i.ibb.co/56Q96Jh/empat.jpg"));
        fragments.add(FragmentSlider.newInstance("https://i.ibb.co/VvSLDgV/satu.jpg"));
        fragments.add(FragmentSlider.newInstance("https://i.ibb.co/BrhZGgj/tiga.jpg"));

        mAdapter = new SliderPagerAdapter(getSupportFragmentManager(), fragments);
        bannerSlider.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(this, mLinearLayout, bannerSlider, R.drawable.logop3d);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }
}