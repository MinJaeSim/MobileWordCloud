package com.example.simminje.mobilewordcloud.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private String dirPath;

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
        dirPath = getContext().getFilesDir().getAbsolutePath();

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

                ArrayList<String> data = new ArrayList<>(Arrays.asList(content.split(" ")));

                System.out.println("총 파일 갯수 : " + f.length);
//                for (String str : data) {
//                    System.out.println(str);
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data != null) {
            System.out.println("NOT NULL");

            for (int i = 0; i < data.size(); i += 2) {
                paint.setTextSize((int) data.get(i + 1).charAt(0) * 5);
                canvas.drawText(data.get(i), i * 10, i * 10, paint);
            }
        } else {
            System.out.println("NULL");

            canvas.drawLine(0, 0, 500, 500, paint);
        }
    }
}
