package com.example.simminje.mobilewordcloud.View;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.simminje.mobilewordcloud.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button loadFromWebButton = (Button) findViewById(R.id.load_from_web_button);
        loadFromWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("AAAA", "Web");

            }
        });

        Button loadFromKaKaoButton = (Button) findViewById(R.id.load_from_kakao_button);
        loadFromKaKaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("AAAA", "KAKAO");

            }
        });
    }
}
