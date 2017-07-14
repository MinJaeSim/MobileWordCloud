package com.example.simminje.mobilewordcloud.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.simminje.mobilewordcloud.Model.CrawlingData;
import com.example.simminje.mobilewordcloud.Model.OnDataCrawlingListener;
import com.example.simminje.mobilewordcloud.R;


public class LoadKakaoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_load_kakao, container, false);

        Button displayButton = (Button) view.findViewById(R.id.display_result);

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DisplayActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}