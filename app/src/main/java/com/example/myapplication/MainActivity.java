package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "A (Main) ---> onCreate: 界面创建");

        Button btn = findViewById(R.id.btn_click_me);
        if(btn != null){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 创建Intent：从"这里(this)"跳到"SecondActivity"
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "A (Main) ---> onStart: 可见但不可操作");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "A (Main) ---> onResume: 处于前台，可交互");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "A (Main) ---> onPause: 正在暂停");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "A (Main) ---> onStop: 已经停止（不可见）");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "A (Main) ---> onRestart: 重新启动");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "A (Main) ---> onDestroy: 销毁");
    }
}