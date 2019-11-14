package com.devxpart.seraamm;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView id;
    ImageView cross;
    Button ammbutton,chart;
    DatabaseHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        id=findViewById(R.id.orderno);
        cross=findViewById(R.id.crossbutton);
        ammbutton=findViewById(R.id.ammbutton);
        chart=findViewById(R.id.chart);
        dh=new DatabaseHelper(this);



        ammbutton.setOnClickListener(v -> {


            if(dh.getData("Request_1")!=null){
                Intent intent12 =new Intent(Result.this,HomePage.class);
                Cursor cursor=dh.getData("Request_1");
                StringBuilder sb=new StringBuilder();
                while (cursor.moveToNext()){
                    sb.append(cursor.getString(0));
                }
                String str=sb.toString();
                intent12.putExtra("server-response",str);
                startActivity(intent12);
            }else {
                new RequestToServer("https://seraaam.com/api/v1/android-app/home/offer",this,"home-page");
            }


        });

        chart.setOnClickListener(v -> startActivity(new Intent(Result.this,ListofOrder.class)));

        findViewById(R.id.chat).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });

        id.setText("অর্ডার নম্বরঃ "+getIntent().getStringExtra("order_id"));

        cross.setOnClickListener(v -> {

            Cursor c=dh.getData("Request_1");
            StringBuilder sb=new StringBuilder();
            while (c.moveToNext()){
                sb.append(c.getString(0));
            }


            Intent i=new Intent(Result.this,HomePage.class);
            i.putExtra("server-response",sb.toString());
            Log.e("to op1",sb.toString());
            startActivity(i);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Cursor c=dh.getData("Request_1");
        StringBuilder sb=new StringBuilder();
        while (c.moveToNext()){
            sb.append(c.getString(0));
        }


        Intent i=new Intent(Result.this,HomePage.class);
        i.putExtra("server-response",sb.toString());
        Log.e("to op1",sb.toString());
        startActivity(i);
        finish();
    }
}
