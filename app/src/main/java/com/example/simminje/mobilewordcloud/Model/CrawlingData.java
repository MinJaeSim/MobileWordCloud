package com.example.simminje.mobilewordcloud.Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CrawlingData {
    private Elements elements;

    public CrawlingData(final String url, final OnDataCrawlingListener listener) {
        new Thread() {
            @Override
            public void run() {
                Document doc;
                try {
                    doc = Jsoup.connect(url).header("content-type", "multipart/form-data; boundary=---011000010111000001101001").header("authorization", "Basic ZmJfMTY2MTUzMDc0NzQ2MTk5NDox").header("cache-control", "no-cache").get();
                    elements = doc.select("body");
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
