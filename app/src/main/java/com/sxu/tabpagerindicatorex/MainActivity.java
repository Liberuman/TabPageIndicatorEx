package com.sxu.tabpagerindicatorex;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String[] menu_texts = {"活动", "发现", "消息", "我的"};
    private int[] menu_icons = {R.drawable.menu_activity, R.drawable.menu_find,
            R.drawable.menu_msg, R.drawable.menu_my};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabPageIndicatorEx tabIndicator = (TabPageIndicatorEx) findViewById(R.id.tabIndicator);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        int screenWidth = getDeviceScreenWidth(this);
        tabIndicator.setItemWidth(screenWidth / 4);
        tabIndicator.setViewPager(viewPager);
    }

    public static int getDeviceScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        return w > h ? h : w;
    }

    public class ViewPagerAdapter extends PagerAdapterEx {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return menu_icons.length;
        }

        @Override
        public Fragment getItem(int position) {
            return ChildFragment.getInstance(position);
        }

        @Override
        public View getTabView(int position) {
            View itemView = getLayoutInflater().inflate(R.layout.item_layout, null);
            ((ImageView)itemView.findViewById(R.id.menu_icon)).setImageResource(menu_icons[position]);
            ((TextView)itemView.findViewById(R.id.menu_text)).setText(menu_texts[position]);

            return itemView;
        }
    }
}
