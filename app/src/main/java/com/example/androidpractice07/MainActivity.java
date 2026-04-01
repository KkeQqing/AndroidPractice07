package com.example.androidpractice07;

import static android.app.ProgressDialog.show;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

    ListView listView=null;

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
        items.add("拨打电话");
        return items;
    }

    private final AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String text = (String) parent.getItemAtPosition(position);
            Toast.makeText(MainActivity.this,"点击："+text,Toast.LENGTH_SHORT).show();
            switch (position) {
                case 0: { // (1) ComponentName 跳转
                    Intent it = new Intent();
                    ComponentName cn = new ComponentName(MainActivity.this, Main2Activity.class);
                    it.setComponent(cn);
                    mLauncher.launch(it);
                    break;
                }
                case 1: {
                    Intent it = new Intent(MainActivity.this, Main3Activity.class);
                    mLauncher.launch(it);
                    break;
                }
                case 2: { // (3) 返回 Home
                    Intent it = new Intent();
                    it.setAction(Intent.ACTION_MAIN);
                    it.addCategory(Intent.CATEGORY_HOME);
                    startActivity(it);
                    break;
                }
                case 3: { // (3) 打开百度
                    Intent it = new Intent();
                    it.setAction(Intent.ACTION_VIEW);
                    it.setData(Uri.parse("https://www.baidu.com"));
                    startActivity(it);
                    break;
                }
                case 4: { // (3) 显示联系人
                    Intent it = new Intent(Intent.ACTION_GET_CONTENT);
                    it.setType("vnd.android.cursor.item/phone");
                    contactLauncher.launch(it);
                    break;
                }
                case 5: { // (3) 拨打电话 (重点：需要动态权限)
                    Intent it = new Intent(Intent.ACTION_DIAL);
                    startActivity(it);
                    break;
                }
            }
        }
    };

    // 单独：打开联系人（需要单独处理）
    private final ActivityResultLauncher<Intent> contactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri contactData = data.getData();
                            if (contactData != null) {
                                Cursor cursor = getContentResolver().query(contactData, null,  null, null,  null);
                                if (cursor.moveToFirst()) {
                                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                                    Cursor phones = getContentResolver().query(
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                             ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                            null, null);

                                    String phoneNumber = "无号码";
                                    if (phones.moveToFirst()) {
                                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    }
                                    phones.close();
                                    Toast.makeText( MainActivity.this, phoneNumber, Toast.LENGTH_SHORT).show();
                                }
                                cursor.close();
                            }
                        }
                    }
                }
            }
    );
}