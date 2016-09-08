package com.sxu.tabpagerindicatorex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ChildFragment extends Fragment {

    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        position = getArguments().getInt("position");
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        TextView descText = (TextView) rootView.findViewById(R.id.desc_text);
        descText.setText("这是第" + (position+1) + "个页面");

        return rootView;
    }

    public static ChildFragment getInstance(int position) {
        ChildFragment fragment = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);

        return  fragment;
    }
}
