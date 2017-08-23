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
    private List<Rect> rect;
    private int dataPosition;
    private List<Integer> colors;

    public ResultCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        colors = new ArrayList<>();

        colors.add(Color.rgb(245, 209, 183));
        colors.add(Color.rgb(244, 201, 107));
        colors.add(Color.rgb(233, 129, 56));
        colors.add(Color.rgb(136, 133, 164));
        colors.add(Color.rgb(122, 154, 130));

        colors.add(Color.rgb(248, 226, 94));
        colors.add(Color.rgb(242, 99, 93));
        colors.add(Color.rgb(35, 98, 107));
        colors.add(Color.rgb(83, 173, 213));
        colors.add(Color.rgb(158, 215, 235));

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4f);
    }

    public void setDataPosition(int num) {
        this.dataPosition = num;
        loadAnalysisData();
    }

    private void loadAnalysisData() {
        String dirPath = getContext().getFilesDir().getAbsolutePath();

        File file = new File(dirPath);

        if (file.listFiles().length > 0) {
            List<File> f = Arrays.asList(file.listFiles());
            File targetFile;

            if (dataPosition == -1) {
                targetFile = f.get(f.size() - 1);
            } else {
                targetFile = f.get(dataPosition);
            }
            String targetFileName = targetFile.getName();

            String loadPath = dirPath + "/" + targetFileName;

            try {
                FileInputStream fis = new FileInputStream(loadPath);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

                StringBuilder content = new StringBuilder();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    content.append(temp);
                }

                data = new ArrayList<>(Arrays.asList(content.toString().split(" ")));

                System.out.println("총 파일 갯수 : " + f.size());

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

        if (data != null) {

            rect = new ArrayList<>();
            List<String> words = new ArrayList<>();

            int posX, posY;
            int size;
            int tryNum = 0;
            int maxNum = 20;
            Random generateRandom = new Random();

            data = data.subList(0, 60);


            for (int i = 0; i < data.size(); i += 2)
                words.add(data.get(i + 1));

            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);
                Rect boundRect = new Rect();

                int fontSize = (25 - i) * 5 > 25 ? (25 - i) * 5 : 25;

                paint.setColor(colors.get((int) (Math.random() * 10)));
                paint.setTextSize(fontSize);
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

                rect.add(textRect);
                canvas.drawText(word, posX, posY + boundRect.height(), paint);

                if (i >= 25) break;
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

        for (Rect r : rect) {
            x21 = r.left;
            x22 = r.right;
            y21 = r.top;
            y22 = r.bottom;
            if (x11 < x22 && x21 < x12 && y11 < y22 && y21 < y12) return true;
        }

        return false;
    }
}
