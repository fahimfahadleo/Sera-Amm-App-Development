package com.devxpart.seraamm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class OrderPageOne extends AppCompatActivity {


    ImageView ammimage,rating1,rating2,rating3,rating4,rating5;
    ImageView ordernow,backbutton;
    TextView name,price,details,readmore;
    JSONObject maindata,mainjsondata;

    Intent intent;
    HashMap<String,String> nameandid;


    int i=0;
    HashMap<String,HashMap<String,String>> mymap;

    Button ammbutton,chart;

    DatabaseHelper dh;

    @Override
    public void onBackPressed() {
        if(intent.hasExtra("order-type")){

            Cursor c=dh.getData("Request_4");
            StringBuilder sb=new StringBuilder();
            while (c.moveToNext()){
                sb.append(c.getString(0));
            }


            Intent i=new Intent(OrderPageOne.this,UpComingMangoes.class);
            i.putExtra("server-response",sb.toString());
            Log.e("to op1",sb.toString());
            startActivity(i);
            finish();
        }else {
            Cursor c=dh.getData("Request_2");
            StringBuilder sb=new StringBuilder();
            while (c.moveToNext()){
                sb.append(c.getString(0));
            }


            Intent i=new Intent(OrderPageOne.this,CurrentMangoes.class);
            i.putExtra("server-response",sb.toString());
            Log.e("to op1",sb.toString());
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderpageone);
        ordernow=findViewById(R.id.orderpageoneordernow);
        ammimage=findViewById(R.id.orderpageoneimage);
        name=findViewById(R.id.orderpageonename);
        backbutton=findViewById(R.id.orderpageonebackbutton);
        price=findViewById(R.id.orderpageoneprice);
        details=findViewById(R.id.orderpageonedetails);
        readmore=findViewById(R.id.orderpageonereadmore);
        rating1=findViewById(R.id.orderpageonerating1);
        rating2=findViewById(R.id.orderpageonerating2);
        rating3=findViewById(R.id.orderpageonerating3);
        rating4=findViewById(R.id.orderpageonerating4);
        rating5=findViewById(R.id.orderpageonerating5);
        ammbutton=findViewById(R.id.ammbutton);
        chart=findViewById(R.id.chart);

        dh=new DatabaseHelper(this);

        intent=getIntent();
        String serverresponse=intent.getStringExtra("server-response");
        Log.e("response op1",serverresponse);


        ammbutton.setOnClickListener(v -> {


            if(dh.getData("Request_1")!=null){
                Intent intent12 =new Intent(OrderPageOne.this,HomePage.class);
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

        chart.setOnClickListener(v ->{
            startActivity(new Intent(OrderPageOne.this,ListofOrder.class));
            finish();
        } );

        findViewById(R.id.chat).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });



        if(intent.hasExtra("order-type")){
            try {
                prefatchdata(serverresponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                fatchdata(serverresponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        HashMap<String,String> data=mymap.get(nameandid.get("0"));

        backbutton.setOnClickListener(v -> onBackPressed());




        name.setText(nameandid.get("1"));
        new DownloadImageTask(ammimage).execute(data.get("image"));
        price.setText(data.get("price")+" টাকা/কেজি");
        details.setText(data.get("overview"));

        if(data.containsKey("rating")){



            int ratingvalue=Integer.parseInt(data.get("rating"));

            if(ratingvalue==4){
                rating5.setVisibility(View.INVISIBLE);
            }else if(ratingvalue==3){
                rating5.setVisibility(View.INVISIBLE);
                rating4.setVisibility(View.INVISIBLE);
            }else if(ratingvalue==2){
                rating5.setVisibility(View.INVISIBLE);
                rating4.setVisibility(View.INVISIBLE);
                rating3.setVisibility(View.INVISIBLE);
            }else if(ratingvalue==1){
                rating5.setVisibility(View.INVISIBLE);
                rating4.setVisibility(View.INVISIBLE);
                rating3.setVisibility(View.INVISIBLE);
                rating2.setVisibility(View.INVISIBLE);
            }

        }else {
            TextView rating=findViewById(R.id.rating);
            rating.setVisibility(View.INVISIBLE);
            rating1.setVisibility(View.INVISIBLE);
            rating2.setVisibility(View.INVISIBLE);
            rating3.setVisibility(View.INVISIBLE);
            rating4.setVisibility(View.INVISIBLE);
            rating5.setVisibility(View.INVISIBLE);

        }

        if(!data.containsKey("overview")){
            readmore.setVisibility(View.INVISIBLE);
            details.setVisibility(View.INVISIBLE);
        }else {
            readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.facebook.com/SeraAam/"));
                    String title = "Choose From Below!";
                    Intent chooser = Intent.createChooser(intent, title);
                    startActivity(chooser);
                }
            });
        }


        findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
                String title = "Choose From Below!";
                Intent chooser = Intent.createChooser(intent, title);
                startActivity(chooser);
            }
        });




        ordernow.setOnClickListener(v -> {


            Intent i=new Intent(OrderPageOne.this,OrderPageTwo.class);
            i.putExtra("name",data.get("name"));
            i.putExtra("price",data.get("price"));
            i.putExtra("image",data.get("image"));
            if(data.containsKey("overview")){
                i.putExtra("overview",data.get("overview"));
            }
            i.putExtra("id",nameandid.get("0"));

            Log.e("nameid",nameandid.get("0"));

            if(intent.hasExtra("order-type")){
                i.putExtra("order-type","pre");
            }

            startActivity(i);
            finish();

        });
    }



    void prefatchdata(String s) throws JSONException {

        maindata = new JSONObject(s);

        mainjsondata =maindata.getJSONObject("data");

        Log.e("mainjsondata",mainjsondata.toString());

        mymap=new HashMap<>();
        nameandid=new HashMap<>();
        HashMap<String,String> detail=new HashMap<>();
        detail.put("name",mainjsondata.getString("name"));
        detail.put("image",mainjsondata.getString("image"));
        detail.put("price",mainjsondata.getString("estimated_price"));
        detail.put("id",mainjsondata.getString("id"));
        detail.put("status",mainjsondata.getString("status"));
        detail.put("created_at",mainjsondata.getString("created_at"));
        detail.put("updated_at",mainjsondata.getString("updated_at"));



        mymap.put(mainjsondata.getString("id"),detail);


        nameandid.put("0",mainjsondata.getString("id"));
        nameandid.put("1",detail.get("name"));




        Log.e("hashmap",mymap.toString());
    }

    void fatchdata(String s) throws JSONException{
        maindata = new JSONObject(s);

        mainjsondata =maindata.getJSONObject("data");

        Log.e("mainjsondata",mainjsondata.toString());

        mymap=new HashMap<>();
        nameandid=new HashMap<>();
        HashMap<String,String> detail=new HashMap<>();
        detail.put("id",mainjsondata.getString("id"));
        detail.put("name",mainjsondata.getString("name"));
        detail.put("image",mainjsondata.getString("image"));
        detail.put("price",mainjsondata.getString("price"));
        detail.put("status",mainjsondata.getString("status"));
        detail.put("created_at",mainjsondata.getString("created_at"));
        detail.put("updated_at",mainjsondata.getString("updated_at"));
        detail.put("grade",mainjsondata.getString("grade"));
        detail.put("overview",mainjsondata.getString("overview"));
        detail.put("rating",mainjsondata.getString("rating"));

        mymap.put(mainjsondata.getString("id"),detail);


        nameandid.put("0",mainjsondata.getString("id"));
        nameandid.put("1",detail.get("name"));




        Log.e("hashmap op 1",mymap.toString());
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {

                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
