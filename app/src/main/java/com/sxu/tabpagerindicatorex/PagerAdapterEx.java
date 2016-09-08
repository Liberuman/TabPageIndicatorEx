package com.sxu.tabpagerindicatorex;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/*******************************************************************************
 * FileName: PageAdapterEx
 *
 * Description: 增加getTabView，使我们可以自定义TabIndicator布局
 *
 * Author: Freeman
 *
 * Version: v1.0
 *
 * Date: 16/8/4
 *******************************************************************************/
public abstract class PagerAdapterEx extends FragmentPagerAdapter {

    public PagerAdapterEx(FragmentManager fm) {
        super(fm);
    }

    public abstract View getTabView(int position);
}
