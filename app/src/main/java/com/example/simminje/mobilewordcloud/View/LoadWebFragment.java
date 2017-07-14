package com.example.simminje.mobilewordcloud.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.simminje.mobilewordcloud.Model.Analysis;
import com.example.simminje.mobilewordcloud.Model.CrawlingData;
import com.example.simminje.mobilewordcloud.Model.OnDataCrawlingListener;
import com.example.simminje.mobilewordcloud.R;

import org.jsoup.select.Elements;

public class LoadWebFragment extends Fragment {

    private CrawlingData crawling;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_load_web, container, false);

        Button displayButton = (Button) view.findViewById(R.id.display_result);

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText) view.findViewById(R.id.url_input);
                String url = ed.getText().toString();

                crawling = new CrawlingData(url, new OnDataCrawlingListener() {
                    @Override
                    public void onSuccess() {
                        analysisData();

                        Intent intent = new Intent(getContext(), DisplayActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });

        return view;
    }

    private void analysisData() {

        Elements elements = crawling.getData();
        Context ctx = getContext();

        Analysis analysis = new Analysis(ctx, elements);
    }
}
