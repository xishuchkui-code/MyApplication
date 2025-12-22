package com.example.musicservicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MusicService.MusicBinder musicBinder;
    private ServiceConnection connection;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start_service).setOnClickListener(this);
        findViewById(R.id.btn_stop_service).setOnClickListener(this);
        findViewById(R.id.btn_bind_service).setOnClickListener(this);
        findViewById(R.id.btn_unbind_service).setOnClickListener(this);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicBinder = (MusicService.MusicBinder) service;
                MusicService musicService = musicBinder.getService();
                musicService.play();
                isBound = true;
                Log.d("MusicServiceLog", "Activity: 已绑定，调用 playMusic");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(this, MusicService.class);
        if (id == R.id.btn_start_service) {
            startService(intent);
        } else if (id == R.id.btn_stop_service) {
            stopService(intent);
        } else if (id == R.id.btn_bind_service) {
            bindService(intent, connection, BIND_AUTO_CREATE);
        } else if (id == R.id.btn_unbind_service) {
            if (isBound) {
                unbindService(connection);
                isBound = false;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            try {
                unbindService(connection);
                isBound = false;
                Log.d("MusicServiceLog", "Activity: onStop 已解绑");
            } catch (IllegalArgumentException e) {
                Log.w("MusicServiceLog", "unbindService 抛出异常: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            Log.d("MusicServiceLog", "Activity: onDestroy 触发，执行解绑");
            unbindService(connection);
            isBound = false;
        }
    }
}