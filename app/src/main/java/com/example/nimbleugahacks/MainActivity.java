package com.example.nimbleugahacks;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.Manifest;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class MainActivity extends AppCompatActivity{
    public static Handler mainHandler = new Handler();
    private ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);

    public static double upTime = System.nanoTime();
    private final static int SEND_SMS_PERMISSION_REQ=1;
    private static final int PERMISSION_REQUEST_CODE = 200;

    public static TextView newText;
    Button newButton;
    public static int scanCount;

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
    public static TextView totalText;
    TextView ipmText;
    Button addManagerButton, managerButton;
    FloatingActionButton scanButton;
    String phoneNum = "";
    private boolean warned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        items = new ArrayList<>();

//        items.add("CART");



        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(items);
        recyclerView.setAdapter(adapter);

        //
        itemText = findViewById(R.id.itemText);

        ipmText = findViewById(R.id.ipmText);
        addManagerButton = findViewById(R.id.addManagerButton);
        managerButton = findViewById(R.id.managerButton);
        managerButton.setEnabled(false);
        scanButton = findViewById(R.id.floatingactionbutton);
        totalText = findViewById(R.id.totalText);

        if (checkPermission(Manifest.permission.SEND_SMS)) {
            managerButton.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
        }

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    startActivity(new Intent(getApplicationContext(), ScanActivity.class));
                    ipmText = (TextView)findViewById(R.id.ipmText);
                    if (ipmText == null) {
                        Log.i("SAHILL", "is ..");
                    }
                    scanCount++;
                    Log.i("SAHIL", "" + (System.nanoTime() - MainActivity.upTime)/1_000_000_000.0);
                    double ipm = (double)Math.round(scanCount*60*100/((System.nanoTime() - MainActivity.upTime)/1_000_000_000.0))/100;
                    String txt = "" + ipm;
                    Log.i("SCAN UPDATE MAANAS",txt);
                    ipmText.setText(txt);
                    String total = "" + ScanActivity.total;
                    totalText.setText(total);
                } else {
                    requestPermission();
                }
            }
        });

        managerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNum == null || phoneNum.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Add a Manager", Toast.LENGTH_SHORT).show();
                }
                String name = "Your Employee Requested Assistance!";
                if (!TextUtils.isEmpty(phoneNum)&&!TextUtils.isEmpty(name)) {

                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNum,null,name,null,null);
                        Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please Add a Manager", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Manager Number");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phoneNum = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        IPM thread = new IPM();
        thread.start();

//        scheduler.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                double ipm = (double)Math.round(scanCount*60*100/((System.nanoTime() - MainActivity.upTime)/1_000_000_000.0))/100;
//                String txt = "" + ipm;
//                Log.i("MAANAS FIXED",txt);
//                ipmText.setText(txt);
//            }
//        }, 50, 100 , MILLISECONDS);
//        Timer t = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                double ipm = (double)Math.round(scanCount*60*100/((System.nanoTime() - MainActivity.upTime)/1_000_000_000.0))/100;
//                String txt = "" + ipm;
//                Log.i("MAANAS FIXED",txt);
//                ipmText.setText(txt);
//                Log.i("MAANAS FIXED",txt);
//            };
//        };
//        t.scheduleAtFixedRate(tt,2000,1000);


    }

    class IPM extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        double ipm = (double) Math.round(scanCount * 60 * 100 / ((System.nanoTime() - MainActivity.upTime) / 1_000_000_000.0)) / 100;
                        String txt = "" + ipm;
                        Log.i("MAANAS FIXED", txt);
                        ipmText.setText(txt);
                        if (ipm > 0 && ipm < 2 && !warned) {
                            if (checkPermission(Manifest.permission.SEND_SMS)) {
                                SmsManager smsManager = SmsManager.getDefault();
                                try {
                                    if (phoneNum.isEmpty()) {
                                        smsManager.sendTextMessage("+16788346941", null, "Your Employee Needs Help!", null, null);
                                    } else {
                                        smsManager.sendTextMessage(phoneNum, null, "Your Employee Needs Help!", null, null);
                                    }
                                } catch(Error error) {

                                }
                            }
                            Toast.makeText(MainActivity.this, "Alerting Manager", Toast.LENGTH_SHORT).show();
                            warned = true;
                        }
                    }
                });
            }
        }
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