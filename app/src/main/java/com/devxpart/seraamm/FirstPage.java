package com.devxpart.seraamm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FirstPage extends AppCompatActivity {

    ProgressBar progressBar;
    static int inti=0;
    DatabaseHelper dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpage);
        progressBar=findViewById(R.id.progressbar);
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);
        dh=new DatabaseHelper(this);
        if(isNetworkAvailable()){
            if(doesDatabaseExist(this)){
                if(dh.getData("RequestTime")!=null){
                    Cursor c=dh.getData("RequestTime");
                    StringBuilder sb=new StringBuilder();
                    while (c.moveToNext()){
                        sb.append(c.getString(0));
                    }
                    String s=sb.toString();

                    Date currentdate = Calendar.getInstance().getTime();
                    String formattedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(currentdate);


                    Log.e("date db",s);
                    Log.e("date current",formattedDate);

                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
                    Date date1 = null;
                    Date date2 = null;
                    date1 = format.parse(formattedDate,new ParsePosition(0));
                    Log.e("date 1",date1.toString());

                    date2 = format.parse(s,new ParsePosition(0));
                    Log.e("date 2",date2.toString());

                    if (date2.compareTo(date1) <0) {
                        dh.removeALlData();
                        Toast.makeText(this,"Data Expired!", Toast.LENGTH_LONG).show();
                        new RequestToServer("https://seraaam.com/api/v1/android-app/home/offer",this,"home-page");
                    }else {

                        Cursor cursor=dh.getData("Request_1");
                        StringBuilder requestbuilder=new StringBuilder();
                        while (cursor.moveToNext()){
                            requestbuilder.append(cursor.getString(0));
                        }
                        JSONObject object;
                        try {
                            object=new JSONObject(requestbuilder.toString());
                            Intent i=new Intent(FirstPage.this,HomePage.class);
                            i.putExtra("server-response",object.toString());
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    Toast.makeText(this,"Data Date Not Found", Toast.LENGTH_LONG).show();
                    new RequestToServer("https://seraaam.com/api/v1/android-app/home/offer",this,"home-page");
                }
            }else {
                Toast.makeText(this,"Database Does Not Exist", Toast.LENGTH_LONG).show();
                new RequestToServer("https://seraaam.com/api/v1/android-app/home/offer",this,"home-page");
            }
        }else {
            Toast.makeText(this,"Currently You Are Not Connected To The Internet. Previous Data Will Be Loaded.There Will Be No Image.",Toast.LENGTH_SHORT).show();
            if(doesDatabaseExist(this)){
                Cursor cursor=dh.getData("Request_1");
                StringBuilder requestbuilder=new StringBuilder();
                while (cursor.moveToNext()){ requestbuilder.append(cursor.getString(0)); }
                JSONObject object;
                try {
                    object=new JSONObject(requestbuilder.toString());
                    Intent i=new Intent(FirstPage.this,HomePage.class);
                    i.putExtra("server-response",object.toString());
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(this,"Failed to connect to internet. Please try again later!",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        progressBar.setMax(100);
        new SetTickerText().start();
    }
    int getprogress(){
        inti++;
        return inti;
    }
    public class SetTickerText extends Thread {
        @Override
        public void run() {
            while (progressBar.getProgress() < 100) {

                if (progressBar.getProgress() <= 50) {
                    SystemClock.sleep(25);
                    progressBar.setProgress(getprogress());
                } else if (progressBar.getProgress() <=75 && progressBar.getProgress() > 50) {

                    SystemClock.sleep(50);
                    progressBar.setProgress(getprogress());
                } else if (progressBar.getProgress() <100 && progressBar.getProgress() > 75) {
                    SystemClock.sleep(75);
                    progressBar.setProgress(getprogress());

                }else if(progressBar.getProgress()==100){
                    if (FirstPage.this.getWindow().getDecorView().isShown()) {
                        SystemClock.sleep(5000);
                        FirstPage.this.runOnUiThread(() -> {
                            Toast.makeText(FirstPage.this, "Load Failed! Check Your Internet Connection and Try Again!", Toast.LENGTH_LONG).show();
                            finish();
                        });
                    }
                }
            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath("SeraAmm.db");
        return dbFile.exists();
    }
}
