package com.example.nimbleugahacks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
//    ItemDao itemDao = new MainActivity();
    private ItemDao itemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "nimbleDB").allowMainThreadQueries().build();
        itemDao = db.itemDao();
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result result) {
        MainActivity.itemText.setText(result.getText());
//        itemDao.insertAll(new Item(1, result.getText(), 2.99));
        Log.i("SAHIL",itemDao.getAll().toString());

        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}