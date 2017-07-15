package com.example.simminje.mobilewordcloud.Model;

import android.content.Context;
import android.content.res.AssetManager;

import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Analysis {
    private Context ctx;
    private AssetManager am;
    private Elements elements;
    private HashSet<String> filter;
    private List<WordCount> words;

    public Analysis(Context ctx, Elements elements) {
        this.ctx = ctx;
        this.am = ctx.getAssets();
        this.elements = elements;
        loadFilter();
        processString();
        saveData();
    }

    private void loadFilter() {
        filter = new HashSet<>();

        try (InputStream stream = am.open("filter.txt")) {
            InputStreamReader is = new InputStreamReader(stream);
            BufferedReader bs = new BufferedReader(is);
            String str;
            while ((str = bs.readLine()) != null)
                filter.add(str);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processString() {
        Scanner scan = new Scanner(elements.text());
        Map<String, Integer> count = new HashMap<>();

        while (scan.hasNext()) {
            String word = removePunctuations(scan.next());
            if (filter.contains(word)) continue;
            if (word.equals("")) continue;
            Integer n = count.get(word);
            count.put(word, (n == null) ? 1 : n + 1);
        }

        PriorityQueue<WordCount> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            pq.add(new WordCount(entry.getKey(), entry.getValue()));
        }

        words = new ArrayList<>();

        while (!pq.isEmpty()) {
            WordCount wc = pq.poll();
            if (wc.word.length() > 1) words.add(wc);
        }

        words = words.subList(0, 60);

        for (WordCount word : words) {
            System.out.println(word.toString());
        }
    }

    private static String removePunctuations(String str) {
        String s = str.replaceAll("\\p{Punct}|\\p{Digit}", "");
        return s;
    }

    private void saveData() {
        String dirPath = ctx.getFilesDir().getAbsolutePath();
        File file = new File(dirPath);

        if (!file.exists()) {
            file.mkdir();
        }

        StringBuilder str = new StringBuilder();

        for (WordCount word : words) {
            str.append(word);
            str.append(" ");
        }

        File saveFile = new File(dirPath + generateFileName() + ".txt");

        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            fos.write(str.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateFileName() {
        return new SimpleDateFormat("/yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }

    public static class WordCount implements Comparable<WordCount>, Serializable {
        String word;
        int n;

        WordCount(String word, int n) {
            this.word = word;
            this.n = n;
        }

        @Override
        public int compareTo(WordCount another) {
            return another.n - n;
        }

        @Override
        public String toString() {
            return n + " " + word;
        }

    }
}
