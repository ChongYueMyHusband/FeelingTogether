package com.example.musicPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.adapter.ViewPagerAdapter;
import com.example.feelingtogethertestone.R;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xui.XUI;

public class MusicPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initTabLayout();

    }

    private void initTabLayout(){
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(new ViewPagerAdapter(this, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        int[] icons = new int[]{
                R.drawable.play,
                R.drawable.my_playlist,
                R.drawable.search,
                R.drawable.setting
        };
        for (int i = 0; i < icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
    }
}