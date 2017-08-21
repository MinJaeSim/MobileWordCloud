package com.example.simminje.mobilewordcloud.View;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;

import com.example.simminje.mobilewordcloud.Model.CustomTypeFace;
import com.example.simminje.mobilewordcloud.R;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new MainFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        setMenuItemFontType();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_kakao:
                        Fragment LoadFromKakaoFragment = new LoadKakaoFragment();
                        fm.beginTransaction().replace(R.id.fragment_container, LoadFromKakaoFragment).commit();
                        return true;
                    case R.id.action_web:
                        Fragment LoadFromWebFragment = new LoadWebFragment();
                        fm.beginTransaction().replace(R.id.fragment_container, LoadFromWebFragment).commit();
                        return true;
                    case R.id.action_review:
                        Fragment PastResultFragment = new PastResultFragment();
                        fm.beginTransaction().replace(R.id.fragment_container, PastResultFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();
    }

    private void setMenuItemFontType() {
        Menu menu = bottomNavigation.getMenu();
        String[] menuTitle = getResources().getStringArray(R.array.bottom_nav_item_title);
        Typeface type = Typeface.createFromAsset(getAssets(), "TitleBold.otf");

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spannableString = new SpannableString(menuTitle[i]);
            spannableString.setSpan(new CustomTypeFace("", type), 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
            item.setTitle(spannableString);
        }
    }
}
