package com.example.nimbleugahacks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import java.io.IOException;

class BasicAuthInterceptor implements Interceptor {

    private String credentials;

    public BasicAuthInterceptor(String user, String password) {
        this.credentials = Credentials.basic(user, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build();
        return chain.proceed(authenticatedRequest);
    }

}

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
        //db

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("1706446a-057c-4687-9050-3752e48ad6e1", "Temporarypassw0rd!"))
                .build();
        Request request = new Request.Builder()
                .header("content-type", "application/json")
                .header("nep-organization", "9f7c0f9112384eadb0e1e70d957cecfc")
                .header("nep-correlation-id", "2021-0206")
                .header("nep-enterprise-unit", "e75260c4ea46481989ad82d220b8bf4b")
                .url("https://gateway-staging.ncrcloud.com/catalog/item-details/" + result.getText())
                .build();
        client.newCall(request).enqueue(new Callback() {

            ItemDao itemDao;
            Result result;
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    //Log.i("Tag", response.body().string());
                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(response.body().string());
                    JsonArray itemPrices = (JsonArray) jsonObject.get("itemPrices");
                    JsonObject details = itemPrices.get(0).getAsJsonObject();
                    String price = details.get("price").getAsString();
                    JsonObject item = (JsonObject) jsonObject.get("item");
                    JsonArray packageIdentifiers = (JsonArray) item.get("packageIdentifiers");
                    JsonObject type = (JsonObject) packageIdentifiers.get(0).getAsJsonObject();
                    final String name = type.get("type").getAsString();
                    final int keyID = Integer.parseInt(type.get("value").getAsString());

                    String text = result.getText();
                    final Item curr = new Item(5, text, Double.parseDouble(price));
                    itemDao.nuke();
                    itemDao.insertAll(curr);

                    Log.i("SAHIL", text);
                    //updating recycle view
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.items.add(name + "  ITEM # " + keyID + "  $" + curr.price);
                            MainActivity.adapter.notifyItemInserted(MainActivity.items.size() - 1);
                        }
                    });
                }
            }

            private Callback setParams(ItemDao itemDao, Result result) {
                this.itemDao = itemDao;
                this.result = result;
                return this;
            }
        }.setParams(itemDao, result));




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