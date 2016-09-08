package com.sxu.tabpagerindicatorex;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/*******************************************************************************
 * FileName: PageAdapterEx
 *
 * Description: 让TabPageIndicator扩展性更强，可自定义指示器的布局
 *
 * Author: Freeman
 *
 * Version: v1.0
 *
 * Date: 16/8/4
 *******************************************************************************/
public class TabPageIndicatorEx extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private int mIndicatorColor;
    private float mItemWidth;
    private float mIndicatorPadding;
    private float mIndicatorHeight;

    private TextView indicatorText;
    private ViewPager viewPager;
    private LinearLayout itemLayout;
    private LinearLayout tabLayout;
    private PagerAdapterEx pagerAdapter;
    private ViewPager.OnPageChangeListener listener;

    private int currentItem = -1;
    private int indicatorLeft;
    private Runnable tabSelectorRunnable;

    private Context context;
    private OnItemClickListener itemListener;

    public TabPageIndicatorEx(Context context) {
        super(context, null);
    }

    public TabPageIndicatorEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabPageIndicatorEx);
        mItemWidth = array.getDimension(R.styleable.TabPageIndicatorEx_itemWidth, 200);
        mIndicatorPadding = array.getDimension(R.styleable.TabPageIndicatorEx_indicatorPadding, 0);
        mIndicatorHeight = array.getDimension(R.styleable.TabPageIndicatorEx_indicatorHeight, 10);
        mIndicatorColor = array.getColor(R.styleable.TabPageIndicatorEx_indicatorColor, Color.parseColor("#00c8d7"));
        array.recycle();

        itemLayout = new LinearLayout(context);
        tabLayout = new LinearLayout(context);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        tabLayout.setOrientation(LinearLayout.VERTICAL);
        setHorizontalScrollBarEnabled(false);
    }

    public TabPageIndicatorEx(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    public void setViewPager(ViewPager view) {
        if (view != null) {
            pagerAdapter = (PagerAdapterEx) view.getAdapter();
            if (pagerAdapter == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.viewPager = view;
            viewPager.addOnPageChangeListener(this);
            notifyDataSetChanged(pagerAdapter);
        } else {
            throw new IllegalStateException("ViewPager is null.");
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        currentItem = initialPosition;
        setViewPager(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemListener = listener;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.listener = listener;
    }

    public void setCurrentItem(int item) {
        if (viewPager != null) {
            indicatorLeft = (int) (item * mItemWidth + mIndicatorPadding);
            viewPager.setCurrentItem(item, false);
            animateToTab(item);
            setSelected(item);
            currentItem = item;
        }
    }

    public void setIndicatorPadding(int padding) {
        this.mIndicatorPadding = padding;
    }

    public void setItemWidth(int width) {
        if (width == 0) {
            throw new NullPointerException("width can't is 0");
        } else {
            this.mItemWidth = width;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (listener != null) {
            listener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageSelected(int position) {
        indicatorLeft = (int) (position * mItemWidth + mIndicatorPadding);
        animateToTab(position);
        setSelected(position);
        currentItem = position;
        if (listener != null) {
            listener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (listener != null) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = itemLayout.getChildAt(position);
        if (tabSelectorRunnable != null) {
            removeCallbacks(tabSelectorRunnable);
        }
        tabSelectorRunnable = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getRight() - getWidth();
                smoothScrollTo(scrollPos, 0);
                tabSelectorRunnable = null;
            }
        };
        post(tabSelectorRunnable);
    }

    private void setSelected(int position) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) indicatorText.getLayoutParams();
        params.leftMargin = indicatorLeft;
        indicatorText.setLayoutParams(params);
        if (position < itemLayout.getChildCount()) {
            LinearLayout child = (LinearLayout) itemLayout.getChildAt(position);
            LinearLayout oldSelectedChild = (LinearLayout) itemLayout.getChildAt(currentItem);
            if (child != null && child.getChildCount() > 0) {
                for (int i = 0; i < child.getChildCount(); i++) {
                    child.getChildAt(i).setSelected(true);
                    if (position != currentItem && oldSelectedChild != null) {
                        oldSelectedChild.getChildAt(i).setSelected(false);
                    }
                }
            }
        }
    }

    public void notifyDataSetChanged(PagerAdapterEx pagerAdapterEx) {
        if (itemLayout.getChildCount() > 0) {
            itemLayout.removeAllViews();
            tabLayout.removeAllViews();
            removeAllViews();
        }
        if (pagerAdapterEx != null) {
            final int count = pagerAdapterEx.getCount();
            if (count > 0) {
                itemLayout.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
                for (int i = 0; i < count; i++) {
                    View itemView = pagerAdapterEx.getTabView(i);
                    final int index = i;
                    itemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewPager.setCurrentItem(index, false);
                            if (itemListener != null) {
                                itemListener.onItemClick(index);
                            }
                        }
                    });
                    itemLayout.addView(itemView, new LinearLayout.LayoutParams((int) mItemWidth, MATCH_PARENT));
                }
                tabLayout.addView(itemLayout);
                indicatorText = new TextView(context);
                indicatorText.setPadding((int) mIndicatorPadding, 0, (int) mIndicatorPadding, 0);
                indicatorText.setBackgroundColor(mIndicatorColor);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (mItemWidth - mIndicatorPadding * 2), (int) mIndicatorHeight);
                params.topMargin = (int) -mIndicatorHeight;
                params.leftMargin = (int) mIndicatorPadding;
                params.rightMargin = (int) mIndicatorPadding;
                indicatorText.setLayoutParams(params);
                tabLayout.addView(indicatorText);
                addView(tabLayout);
            }
            if (currentItem != -1) {
                setCurrentItem(currentItem);
            } else {
                setCurrentItem(0);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
