package com.example.androidpractice07;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    private TextView tvInfo;
    private Button btReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // 注意：你需要创建下面对应的布局文件

        tvInfo = findViewById(R.id.tv_info);
        btReturn = findViewById(R.id.bt_return);

        // 显示当前组件信息
        ComponentName cn = getIntent().getComponent();
        if (cn != null) {
            tvInfo.setText("组件包名: " + cn.getPackageName() + "\n组件类名: " + cn.getClassName());
        }

        btReturn.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("strReturn", "Main2Activity：ComponentName跳转成功！");
            setResult(RESULT_OK, resultIntent);
            finish(); // 关闭页面
        });
    }
}