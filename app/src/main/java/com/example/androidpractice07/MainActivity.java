package com.example.androidpractice07;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 100;
    ListView listView;

    // 1. 初始化 ActivityResultLauncher 用于接收返回数据
    private final ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String strReturn = data.getStringExtra("strReturn");
                            Toast.makeText(MainActivity.this, strReturn, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        List<String> items = GetItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, items);
        listView.setAdapter(adapter);

        // 2. 设置点击监听
        listView.setOnItemClickListener(myListener);
    }

    private List<String> GetItems() {
        List<String> items = new ArrayList<>();
        items.add("ComponentName跳转");
        items.add("Inter-filter配置");
        items.add("返回Home");
        items.add("打开百度页面");
        items.add("显示联系人");
        items.add("拨打电话"); // 必须有权限
        return items;
    }

    private final AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it = new Intent();
            switch (position) {
                case 0: // (1) ComponentName 跳转
                    ComponentName cn = new ComponentName(MainActivity.this, Main2Activity.class);
                    it.setComponent(cn);
                    mLauncher.launch(it);
                    break;

                case 1: // (2) Inter-filter 配置跳转
                    it.setAction("android.intent.action.MAIN3");
                    mLauncher.launch(it);
                    break;

                case 2: // (3) 返回 Home
                    it.setAction(Intent.ACTION_MAIN);
                    it.addCategory(Intent.CATEGORY_HOME);
                    mLauncher.launch(it);
                    break;

                case 3: // (3) 打开百度
                    it.setAction(Intent.ACTION_VIEW);
                    it.setData(Uri.parse("https://www.baidu.com"));
                    mLauncher.launch(it);
                    break;

                case 4: // (3) 显示联系人
                    it.setAction(Intent.ACTION_VIEW);
                    it.setType("vnd.android.cursor.dir/phone_v2");
                    mLauncher.launch(it);
                    break;

                case 5: // (3) 拨打电话 (重点：需要动态权限)
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // 权限已授予，直接拨号
                        it.setAction(Intent.ACTION_CALL);
                        it.setData(Uri.parse("tel:10086"));
                        mLauncher.launch(it);
                    } else {
                        // 权限未授予，申请权限
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                REQUEST_CALL_PERMISSION);
                    }
                    break;
            }
        }
    };
}