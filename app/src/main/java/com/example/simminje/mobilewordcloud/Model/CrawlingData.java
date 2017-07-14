package com.example.simminje.mobilewordcloud.Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CrawlingData {
    private String url;
    private Elements elements;
    private Analysis analysis;

    public CrawlingData(String url, final OnDataCrawlingListener listener) {
        this.url = url;

        new Thread() {
            @Override
            public void run() {

                String url = "http://english.chosun.com/site/data/html_dir/2017/07/14/2017071400803.html";

                Document doc = null;
                try {
                    doc = Jsoup.connect(url).header("content-type", "multipart/form-data; boundary=---011000010111000001101001").header("authorization", "Basic ZmJfMTY2MTUzMDc0NzQ2MTk5NDox").header("cache-control", "no-cache").get();
                    elements = doc.select("div.par");
                    listener.onSuccess();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public Elements getData() {
        return elements;
    }
}
