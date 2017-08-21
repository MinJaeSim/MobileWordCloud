package com.example.simminje.mobilewordcloud.View;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simminje.mobilewordcloud.R;

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        TextView appTitle = (TextView) v.findViewById(R.id.main_title);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "TitleBold.otf");
        appTitle.setTypeface(type);

        return v;
    }
}
