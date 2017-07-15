package com.example.simminje.mobilewordcloud.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ResultCanvas extends View {

    private Paint paint;
    private List<String> data;
    private List<Rect> rects;

    public ResultCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadAnalysisData();

        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4f);
    }

    private void loadAnalysisData() {
        String dirPath = getContext().getFilesDir().getAbsolutePath();

        File file = new File(dirPath);

        if (file.listFiles().length > 0) {
            File[] f = file.listFiles();
            File lastFile = f[f.length - 1];
            String lastFileName = lastFile.getName();

            String loadPath = dirPath + "/" + lastFileName;

            try {
                FileInputStream fis = new FileInputStream(loadPath);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

                String content = "";
                String temp = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    content += temp;
                }

                data = new ArrayList<>(Arrays.asList(content.split(" ")));

                System.out.println("총 파일 갯수 : " + f.length);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int posX, posY;
        int size;
        String word;
        Random generateRandom = new Random();
        int tryNum = 0;
        int maxNum = 20;

        if (data != null) {
            rects = new ArrayList<>();
            data = data.subList(0, 60);
            for (int i = 0; i < data.size(); i += 2) {
                word = data.get(i + 1);
                size = Integer.parseInt(data.get(i));
                Rect boundRect = new Rect();

                if (Character.getType(word.charAt(0)) == 5) {
                    paint.setTextSize(size * 8);
                } else {
                    paint.setTextSize(size * 25);
                }
                paint.getTextBounds(word, 0, word.length(), boundRect);

                posX = (int) (generateRandom.nextDouble() * (width - boundRect.width()));
                posY = (int) (generateRandom.nextDouble() * (height - boundRect.height()));

                Rect textRect = new Rect(posX, posY, posX + boundRect.width(), posY + boundRect.height());

                while (collision(textRect)) {
                    posX = (int) (generateRandom.nextDouble() * (width - boundRect.width()));
                    posY = (int) (generateRandom.nextDouble() * (height - boundRect.height()));
                    textRect = new Rect(posX, posY, posX + boundRect.width(), posY + boundRect.height());
                    if (tryNum > maxNum) break;
                    tryNum++;
                }

                rects.add(textRect);
                //canvas.drawRect(posX, posY, posX + boundRect.width(), posY + boundRect.height(), paint);
                canvas.drawText(word, posX, posY + boundRect.height(), paint);

                if (i >= 40) break;
            }
        } else {
            canvas.drawLine(0, 0, width, height, paint);
            canvas.drawLine(width, 0, 0, height, paint);
        }
    }

    private boolean collision(Rect textRect) {
        int x11 = textRect.left;
        int x12 = textRect.right;
        int y11 = textRect.top;
        int y12 = textRect.bottom;
        int x21, x22, y21, y22;

        for (Rect r : rects) {
            x21 = r.left;
            x22 = r.right;
            y21 = r.top;
            y22 = r.bottom;
            if (x11 < x22 && x21 < x12 && y11 < y22 && y21 < y12) return true;
        }

        return false;
    }
}
