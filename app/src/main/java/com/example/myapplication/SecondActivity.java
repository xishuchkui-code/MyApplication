package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second); // 确保这里对应你的布局文件名
        Log.d(TAG, "B (Second) ===> onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "B (Second) ===> onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "B (Second) ===> onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "B (Second) ===> onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "B (Second) ===> onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "B (Second) ===> onDestroy");
    }
}