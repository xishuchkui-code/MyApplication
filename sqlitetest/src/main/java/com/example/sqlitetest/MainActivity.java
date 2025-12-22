package com.example.sqlitetest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDatabaseHelper dbHelper;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this, MyDatabaseHelper.DB_NAME, null, 1);
        tvResult = findViewById(R.id.tv_result);

        findViewById(R.id.create_database).setOnClickListener(this);
        findViewById(R.id.add_data).setOnClickListener(this);
        findViewById(R.id.update_data).setOnClickListener(this);
        findViewById(R.id.delete_data).setOnClickListener(this);
        findViewById(R.id.query_data).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int id = v.getId();
        if (id == R.id.create_database) {
            Toast.makeText(this, "尝试创建数据库...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.add_data) {
            // Create
            ContentValues values = new ContentValues();
            values.put("name", "活着");
            values.put("author", "余华");
            values.put("pages", 555);
            values.put("price", 33);
            db.insert(MyDatabaseHelper.TABLE_NAME, null, values);

            values.clear();
            values.put("name", "朝花夕拾");
            values.put("author", "走熟人");
            values.put("pages", 588);
            values.put("price", 41);
            db.insert(MyDatabaseHelper.TABLE_NAME, null, values);

            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
            refreshQuery(db);

        } else if (id == R.id.update_data) {
            // Update
            ContentValues values = new ContentValues();
            values.put("price", 45); // 价格改成 45
            db.update(MyDatabaseHelper.TABLE_NAME, values, "name = ?", new String[] {"活着"});
            values.clear();
            values.put("pages", 488);// 页数修改成 488
            db.update(MyDatabaseHelper.TABLE_NAME, values, "name = ?", new String[]{"朝花夕拾"});
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            refreshQuery(db);

        } else if (id == R.id.delete_data) {
            // Delete
            db.delete(MyDatabaseHelper.TABLE_NAME, "pages > ?", new String[] {"500"});

            Toast.makeText(this, "删除成功 (页数>500的书)", Toast.LENGTH_SHORT).show();
            refreshQuery(db);

        } else if (id == R.id.query_data) {
            // Retrieve
            refreshQuery(db);
        }
    }

    @SuppressLint("SetTextI18n")
    private void refreshQuery(SQLiteDatabase db) {
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        StringBuilder sb = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                // 遍历 Cursor 对象
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));
                @SuppressLint("Range") int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));

                sb.append("书名: ").append(name).append("\n");
                sb.append("作者: ").append(author).append("\n");
                sb.append("页数: ").append(pages).append("\n");
                sb.append("价格: ").append(price).append("\n");
                sb.append("----------------\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        tvResult.setText(sb.toString());
    }
}