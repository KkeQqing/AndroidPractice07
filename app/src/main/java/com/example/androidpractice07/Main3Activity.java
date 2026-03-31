package com.example.androidpractice07;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注意：这里使用的是你上传的布局文件
        setContentView(R.layout.activity_main3);

        Button btReturn2 = findViewById(R.id.bt_return2);

        btReturn2.setOnClickListener(v -> {
            // 1. 创建 Intent 携带数据
            Intent resultIntent = new Intent();
            resultIntent.putExtra("strReturn", "Main3Activity：Inter-filter配置跳转成功！");

            // 2. 设置结果码并关闭
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}