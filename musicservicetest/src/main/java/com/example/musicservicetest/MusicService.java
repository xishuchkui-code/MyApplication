package com.example.musicservicetest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    private static final String TAG = "MusicServiceLog";
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new MusicBinder();

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 服务创建，初始化播放器");
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
        } else {
            Log.e(TAG, "MediaPlayer 创建失败！");
        }
    }

    // 【关键点1】实现普通启动模式
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 普通启动");
        play();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: 绑定服务");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: 解绑服务");
        // 绑定断开时，只暂停，不销毁，除非服务也被销毁
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 服务销毁，释放资源");
        stopAndReleasePlayer();
    }

    private void stopAndReleasePlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void play() {
        if (mediaPlayer == null) {
            initMediaPlayer();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d(TAG, "play: 音乐开始播放");
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d(TAG, "stopMusic: 音乐暂停");
        }
    }
}