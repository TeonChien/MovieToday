package com.example.teon.myapplication.feature;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void general(View view) {
        Intent intent = new Intent(this, GeneralActivity.class);
        startActivity(intent);
    }

    public void advanced(View view) {
        Intent intent = new Intent(this, AdvancedActivity.class);
        startActivity(intent);
    }

    public void recommend(View view) {
        Intent intent = new Intent(this, RecommendActivity.class);
        startActivity(intent);
    }

    public void gps(View view) {
        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
    }
}
