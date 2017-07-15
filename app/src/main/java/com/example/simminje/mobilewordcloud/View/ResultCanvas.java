package com.example.simminje.mobilewordcloud.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.simminje.mobilewordcloud.Model.Analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultCanvas extends View {

    private Paint paint;
    private ArrayList<String> data;
    private ArrayList<Rect> rects;

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

        if (data != null) {
            rects = new ArrayList<>();
            System.out.println(width + "W  canvas   H" + height);

            for (int i = 0; i < data.size(); i += 2) {
                word = data.get(i + 1);
                size = Integer.parseInt(data.get(i));
                Rect boundRect = new Rect();

                paint.setTextSize(size * 40);
                paint.getTextBounds(word, 0, word.length(), boundRect);

                posX = (int) (Math.random() * width / 2);
                posY = (int) (Math.random() * height / 2);

                Rect textRect = new Rect(posX, posY, posX + boundRect.width(), posY + boundRect.height());

                while (collision(textRect)) {
                    posX = (int) (Math.random() * width / 2);
                    posY = (int) (Math.random() * height / 2);
                    textRect = new Rect(posX, posY, posX + boundRect.width(), posY + boundRect.height());
                }

                rects.add(textRect);
                //canvas.drawRect(posX, posY, posX + boundRect.width(), posY + boundRect.height(), paint);

                canvas.drawText(word, posX, posY + boundRect.height(), paint);

                if (i > 20) break;
            }
        } else {
            canvas.drawLine(0, 0, width, height, paint);
            canvas.drawLine(width, 0, 0, height, paint);
        }
    }

    private boolean collision(Rect textRect) {
        for (Rect r : rects) {
            if (r.intersect(textRect)) return true;
        }
        return false;
    }
}
