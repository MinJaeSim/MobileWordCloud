package com.example.simminje.mobilewordcloud.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.simminje.mobilewordcloud.Model.Analysis;
import com.example.simminje.mobilewordcloud.Model.CrawlingData;
import com.example.simminje.mobilewordcloud.Model.OnDataCrawlingListener;
import com.example.simminje.mobilewordcloud.R;
import com.stepstone.stepper.StepperLayout;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

public class LoadWebFragment extends Fragment {

    private CrawlingData crawling;
    private Button displayButton;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_load_web, container, false);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "TitleBold.otf");

        displayButton = (Button) view.findViewById(R.id.display_result);
        displayButton.setTypeface(type);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        StepperLayout stepperLayout = (StepperLayout) view.findViewById(R.id.stepperLayout);
        stepperLayout.setAdapter(new StepAdapter(getChildFragmentManager(), this.getContext()));

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();

                TextInputEditText ed = (TextInputEditText) view.findViewById(R.id.textInput);
                String url = ed.getText().toString();

                String urlCheck = "^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$";

                if (url.length() > 0 && url.matches(urlCheck)) {
                    crawling = new CrawlingData(url, new OnDataCrawlingListener() {
                        @Override
                        public void onSuccess() {
                            if (analysisData()) {
                                Intent intent = new Intent(getContext(), DisplayActivity.class);
                                intent.putExtra("num", -1);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Snackbar.make(view, "데이터 분석에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Snackbar.make(view, "주소를 입력해 주세요", Snackbar.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            }
        });

        return view;
    }

    private boolean analysisData() {

        Elements elements = crawling.getData();

        if (elements.size() > 0) {
            Context ctx = getContext();
            AssetManager am = ctx.getAssets();
            String dirPath = ctx.getFilesDir().getAbsolutePath();
            Analysis analysis = new Analysis(elements.text());

            if (analysis.isFilterNull()) {
                try (InputStream stream = am.open("filter.txt")) {
                    analysis.loadFilter(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            analysis.saveData(dirPath);
            return true;
        }
        return false;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        displayButton.setEnabled(false);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        displayButton.setEnabled(true);
    }
}
