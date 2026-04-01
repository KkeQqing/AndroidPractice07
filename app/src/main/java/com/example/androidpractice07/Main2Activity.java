package com.example.androidpractice07;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Main2Activity extends AppCompatActivity {

    private TextView tvInfo = null;
    private Button btReturn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvInfo = (TextView) findViewById(R.id.tv_info);
        btReturn = (Button) findViewById(R.id.bt_return);

        ComponentName cn = getIntent().getComponent();
        tvInfo.setText("组件包名：" + cn.getPackageName() + "\n组件类名：" + cn.getClassName());

        btReturn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent(); // 不写目标页面！
                        it.putExtra("strReturn", "ComponentName跳转---我回来啦！！！");
                        setResult(RESULT_OK, it); // 必须加这一行！
                        finish();
                    }
                }
        );
    }
}