package zrzn.com.damping_view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zrzn.com.damping_view.DampingView.DampingViewPager;


public class MainActivity extends AppCompatActivity {
    private List<android.support.v4.app.Fragment> fragments;
    private String[] tabs = {"第一","第二","第三"};
    private DampingViewPager dampingViewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dampingViewPager = (DampingViewPager) findViewById(R.id.damp_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("阻尼效果");
        toolbar.setSubtitle("ZRZn");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragments = new ArrayList<>();
        fragments.add(TabFragment.newInstance("第一页"));
        fragments.add(TabFragment.newInstance("第二页"));
        fragments.add(TabFragment.newInstance("第三页"));

        dampingViewPager.setpagerCount(fragments.size());
        dampingViewPager.setOffscreenPageLimit(fragments.size());
        dampingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dampingViewPager.setCurrentIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        dampingViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs[position];
            }
        });
        tabLayout.setupWithViewPager(dampingViewPager);
    }
}
