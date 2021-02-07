package com.example.nimbleugahacks;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.json.*;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private final static int SEND_SMS_PERMISSION_REQ=1;
    private static final int PERMISSION_REQUEST_CODE = 200;

    public static TextView newText;
    Button newButton;

    public static RecyclerView recyclerView;
    public static RecyclerAdapter adapter;
    public static List<String> items;
//
//    private AppDatabase db = Room.databaseBuilder(this,
//            AppDatabase.class, "nimbleDB").build();
//    ItemDao itemDao = db.itemDao();

//    public ItemDao getDB() {
//        return itemDao;
//    }


    public static TextView itemText;
    Button scanButton, managerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        items = new ArrayList<>();
        items.add("CART");
        Log.i("Tag","First message");



        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(items);
        recyclerView.setAdapter(adapter);

        //
        itemText = findViewById(R.id.itemText);
        scanButton = findViewById(R.id.scanbutton);
        managerButton = findViewById(R.id.managerButton);
        managerButton.setEnabled(false);

        if (checkPermission(Manifest.permission.SEND_SMS)) {
            managerButton.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
        }

        Log.i("Tag", ((Boolean) checkPermission(Manifest.permission.SEND_SMS)).toString());
//        try {
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new BasicAuthInterceptor("1706446a-057c-4687-9050-3752e48ad6e1", "Temporarypassw0rd!"))
//                    .build();
//            Request request = new Request.Builder()
//                    .header("content-type", "application/json")
//                    .header("nep-organization", "9f7c0f9112384eadb0e1e70d957cecfc")
//                    .header("nep-correlation-id", "2021-0206")
//                    .header("nep-enterprise-unit", "e75260c4ea46481989ad82d220b8bf4b")
//                    .url("https://gateway-staging.ncrcloud.com/catalog/item-details/62")
//                    .build();
//            Response response = client.newCall(request).execute();
//            Log.i("Tag", response.toString());
//        } catch (Exception e) {
//            Log.i("Tag", "Error");
//            e.printStackTrace();
//        }




        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    startActivity(new Intent(getApplicationContext(), ScanActivity.class));
                } else {
                    requestPermission();
                }
            }
        });

        managerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = "4702094868";
                String name = "This works?!";
                if (!TextUtils.isEmpty(phoneNum)&&!TextUtils.isEmpty(name)) {

                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNum,null,name,null,null);
                        Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private boolean checkPermission(String sendSms) {

        int checkpermission= ContextCompat.checkSelfPermission(this,sendSms);
        return checkpermission== PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQ:
                if(grantResults.length>0 &&(grantResults[0]==PackageManager.PERMISSION_GRANTED))
                {
                    managerButton.setEnabled(true);
                }
                break;
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}