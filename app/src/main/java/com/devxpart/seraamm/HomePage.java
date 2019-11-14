package com.devxpart.seraamm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomePage extends AppCompatActivity {

    JSONObject maindata;
    JSONArray tempjsonArray;
    HashMap<String,HashMap<String,String>> mymap;

    TextView current,future,dateofripe;
    LinearLayout homepageinfoholder;
    Button chart,mangoes,chat;

    int j=0;
    DatabaseHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        current=findViewById(R.id.current);
        future=findViewById(R.id.future);
        dateofripe=findViewById(R.id.dateofripe);
        homepageinfoholder=findViewById(R.id.homepageinfoholder);
        chart=findViewById(R.id.shoppingchart);
        mangoes=findViewById(R.id.mangoes);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        chat=findViewById(R.id.chat);

        dh=new DatabaseHelper(this);

        chat.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });



        chart.setOnClickListener(v -> {
            startActivity(new Intent(HomePage.this,ListofOrder.class));
            finish();
        });

        mangoes.setOnClickListener(v -> {


            if(dh.getData("Request_1")!=null){
                Intent intent12 =new Intent(HomePage.this,HomePage.class);
                Cursor cursor=dh.getData("Request_1");
                StringBuilder sb=new StringBuilder();
                while (cursor.moveToNext()){
                    sb.append(cursor.getString(0));
                }
                String str=sb.toString();
                intent12.putExtra("server-response",str);
                startActivity(intent12);
                finish();
            }else {
                new RequestToServer("https://seraaam.com/api/v1/android-app/home/offer",this,"home-page");
                finish();
            }


        });


        Intent intent=getIntent();
        String serverresponse=intent.getStringExtra("server-response");
        Log.e("response current",serverresponse);
        try {
            fatchdata(serverresponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(j=0;j<mymap.size();j++){
            View vi=inflater.inflate(R.layout.homepageinfo,null,false);
            TextView tv=vi.findViewById(R.id.homeactivityinfo);
            tv.setText(mymap.get(Integer.toString(j)).get("title"));
            Typeface face = ResourcesCompat.getFont(this,R.font.hind_siliguri);
            tv.setTypeface(face);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,  getResources().getDimension(R.dimen.name));

            String url = mymap.get(Integer.toString(j)).get("link");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            tv.setOnClickListener(v -> startActivity(i));
            homepageinfoholder.addView(vi);
        }

        current.setOnClickListener(v -> {
            Cursor c=dh.getData("Request_2");
            StringBuilder sb=new StringBuilder();
            while (c.moveToNext()){
                sb.append(c.getString(0));
            }


            Intent i=new Intent(HomePage.this,CurrentMangoes.class);
            i.putExtra("server-response",sb.toString());
            Log.e("to op1",sb.toString());
            startActivity(i);
            finish();
        });


        future.setOnClickListener(v -> {
            Cursor c=dh.getData("Request_4");
            StringBuilder sb=new StringBuilder();
            while (c.moveToNext()){
                sb.append(c.getString(0));
            }


            Intent i=new Intent(HomePage.this,UpComingMangoes.class);
            i.putExtra("server-response",sb.toString());
            Log.e("to op1",sb.toString());
            startActivity(i);
            finish();

        });

        dateofripe.setOnClickListener(v -> {
                    startActivity(new Intent(HomePage.this,DateOfRipe.class));
                    finish();
                });


    }


    void fatchdata(String s) throws JSONException {
        maindata = new JSONObject(s);
        tempjsonArray=maindata.getJSONArray("data");
        mymap=new HashMap<>();
        for(int i=0;i<tempjsonArray.length();i++){
            HashMap<String,String> detail=new HashMap<>();
            detail.put("title",tempjsonArray.getJSONObject(i).getString("title"));
            detail.put("link",tempjsonArray.getJSONObject(i).getString("link"));
            mymap.put(Integer.toString(i),detail);
        }
        Log.e("hashmap",mymap.toString());
    }



    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
