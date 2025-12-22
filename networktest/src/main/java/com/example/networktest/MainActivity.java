package com.example.networktest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvResult;
    private ImageView ivResult;

    private final String TEACHER_URL = "https://www.httpbin.org/image/png";
    private final String TEXT_URL = "https://www.baidu.com";
    private final String IMAGE_URL = "https://www.baidu.com/img/flexible/logo/pc/result.png";
    private Handler mainHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tv_result);
        ivResult = findViewById(R.id.iv_result);

        findViewById(R.id.btn_native_text).setOnClickListener(this);
        findViewById(R.id.btn_native_image).setOnClickListener(this);
        findViewById(R.id.btn_okhttp_text).setOnClickListener(this);
        findViewById(R.id.btn_glide_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        tvResult.setText("加载中...");
        ivResult.setImageBitmap(null);

        int id = v.getId();
        if (id == R.id.btn_native_text) {
            sendRequestWithHttpURLConnection();
        } else if (id == R.id.btn_native_image) {
            sendImageRequestWithHttpURLConnection();
        } else if (id == R.id.btn_okhttp_text) {
            sendRequestWithOkHttp();
        } else if (id == R.id.btn_glide_image) {
            loadImageWithGlide();
        }
    }

    private void sendRequestWithHttpURLConnection() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(TEXT_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);

                InputStream in = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                showResponse(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void sendImageRequestWithHttpURLConnection() {
        new Thread(() -> {
            try {
                URL url = new URL(IMAGE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);

                if (connection.getResponseCode() == 200) {
                    InputStream inputStream = connection.getInputStream();
                    // 将流解码为 Bitmap
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // 切回主线程显示图片
                    mainHandler.post(() -> ivResult.setImageBitmap(bitmap));
                    runOnUiThread(() -> tvResult.setText("原生图片加载成功"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendRequestWithOkHttp() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(TEACHER_URL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> tvResult.setText("请求失败: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();

                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    runOnUiThread(() -> {
                        ivResult.setImageBitmap(bitmap);
                        tvResult.setText("OkHttp图片成功！\nURL: " + TEACHER_URL);
                    });
                }
            }
        });
    }

    private void loadImageWithGlide() {
        Glide.with(this)
                .load(IMAGE_URL)
                .placeholder(R.drawable.ic_launcher_background) // 加载占位图
                .into(ivResult);

        tvResult.setText("Glide图片加载成功");
    }

    private void showResponse(final String response) {
        runOnUiThread(() -> {
            if (response.length() > 500) {
                tvResult.setText(response.substring(0, 500) + "\n\n...剩余内容省略");
            } else {
                tvResult.setText(response);
            }
        });
    }
}