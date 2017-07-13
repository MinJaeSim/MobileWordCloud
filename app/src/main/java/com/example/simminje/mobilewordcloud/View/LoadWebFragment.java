package com.example.simminje.mobilewordcloud.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simminje.mobilewordcloud.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LoadWebFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_web, container, false);
        //Log.i("AAAA", "WEB");
        crawlingData();

        return view;
    }

    private void crawlingData() {
        new Thread(){
            @Override
            public void run() {
                String url = "http://news.joins.com/article/21755298?cloc=joongang|home|newslist1big";
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).header("content-type", "multipart/form-data; boundary=---011000010111000001101001").header("authorization", "Basic ZmJfMTY2MTUzMDc0NzQ2MTk5NDox").header("cache-control", "no-cache").get();
                    Elements title = doc.select("div#article_body");
                    for (Element e : title) {
                        System.out.println(e.text());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
