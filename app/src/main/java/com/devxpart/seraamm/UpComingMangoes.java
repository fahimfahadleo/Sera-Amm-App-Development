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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class UpComingMangoes extends AppCompatActivity {

    ImageView backbutton;
    LinearLayout mylinearlayout2;
    JSONObject maindata;
    JSONArray mainjsonArray;

    JSONObject [] mainjsonarrays;
    HashMap<String,HashMap<String,String>> mymap;
    HashMap<String,String> nameandid;
    Button ammbutton,chart;
    DatabaseHelper databaseHelper;
    int i;

    @Override
    public void onBackPressed() {


        if(databaseHelper.getData("Request_1")!=null){
            Intent intent=new Intent(UpComingMangoes.this,HomePage.class);
            Cursor cursor=databaseHelper.getData("Request_1");
            StringBuilder sb=new StringBuilder();
            while (cursor.moveToNext()){
                sb.append(cursor.getString(0));
            }
            String str=sb.toString();
            intent.putExtra("server-response",str);
            startActivity(intent);
            finish();
        }else {
            new RequestToServer("https://seraaam.com/api/v1/android-app/home/offer",UpComingMangoes.this,"home-page");
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcomingmangos);
        backbutton=findViewById(R.id.upcomingmangoesbackbutton);
        ammbutton=findViewById(R.id.ammbutton);
        chart=findViewById(R.id.chart);
        databaseHelper=new DatabaseHelper(this);



        ammbutton.setOnClickListener(v -> {


            if(databaseHelper.getData("Request_1")!=null){
                Intent intent12 =new Intent(UpComingMangoes.this,HomePage.class);
                Cursor cursor=databaseHelper.getData("Request_1");
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

        chart.setOnClickListener(v -> {
            startActivity(new Intent(UpComingMangoes.this,ListofOrder.class));
            finish();
        });


        findViewById(R.id.chat).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });
        backbutton.setOnClickListener(v -> onBackPressed());

        Intent intent=getIntent();
        String serverresponse=intent.getStringExtra("server-response");
        Log.e("server-response",serverresponse);


        try {
            fatchdata(serverresponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mylinearlayout2=findViewById(R.id.mylinearlayout2);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);






        for( i=0;i<mymap.size();i++){
            View vi=inflater.inflate(R.layout.singleblockoffuturemangos,null,false);

            ImageView orderkorun =vi.findViewById(R.id.futuremangoorder);
            TextView name=vi.findViewById(R.id.futuremangoname);
            TextView price=vi.findViewById(R.id.estimated_price);
            TextView date=vi.findViewById(R.id.dateofarrival);
            LinearLayout futuremangoeslinearlayout=vi.findViewById(R.id.futuremangoeslinearlayotu);
            ImageView futuremangoimage=vi.findViewById(R.id.futuremangoimage);
            HashMap<String,String> details=new HashMap<>();
            details=mymap.get(String.valueOf(i+1));
            String newurl = details.get("image");


            futuremangoeslinearlayout.setOnClickListener(v -> {



                if(databaseHelper.getData("Request_5")!=null){
                    Cursor d=databaseHelper.getSingleRaw("Request_5",name.getText().toString());
                    StringBuilder sb=new StringBuilder();
                    while (d.moveToNext()){
                        sb.append(d.getString(0));
                    }
                    Intent intent1=new Intent(UpComingMangoes.this,OrderPageOne.class);
                    intent1.putExtra("server-response",sb.toString());
                    intent1.putExtra("order-type","pre");
                    startActivity(intent1);
                    finish();
                }else {
                    new RequestToServer("https://seraaam.com/api/v1/mangoes/upcoming/"+nameandid.get(name.getText()), UpComingMangoes.this,"Order-Page-One");
                    finish();
                }

            });


            new DownloadImageTask(futuremangoimage).execute(newurl);
            futuremangoimage.setOnClickListener(v -> {
                if(databaseHelper.getData("Request_5")!=null){
                    Cursor d=databaseHelper.getSingleRaw("Request_5",name.getText().toString());
                    StringBuilder sb=new StringBuilder();
                    while (d.moveToNext()){
                        sb.append(d.getString(0));
                    }
                    Intent intent1=new Intent(UpComingMangoes.this,OrderPageOne.class);
                    intent1.putExtra("server-response",sb.toString());
                    intent1.putExtra("order-type","pre");
                    startActivity(intent1);
                    finish();
                }else {
                    new RequestToServer("https://seraaam.com/api/v1/mangoes/upcoming/"+nameandid.get(name.getText()), UpComingMangoes.this,"Order-Page-One");
                    finish();
                }
            });

            name.setText(details.get("name"));
            name.setOnClickListener(v -> {
                if(databaseHelper.getData("Request_5")!=null){
                    Cursor d=databaseHelper.getSingleRaw("Request_5",name.getText().toString());
                    StringBuilder sb=new StringBuilder();
                    while (d.moveToNext()){
                        sb.append(d.getString(0));
                    }
                    Intent intent1=new Intent(UpComingMangoes.this,OrderPageOne.class);
                    intent1.putExtra("server-response",sb.toString());
                    intent1.putExtra("order-type","pre");
                    startActivity(intent1);
                    finish();
                }else {
                    new RequestToServer("https://seraaam.com/api/v1/mangoes/upcoming/"+nameandid.get(name.getText()), UpComingMangoes.this,"Order-Page-One");
                    finish();
                }
            });
            price.setText("আনুমানিক মুল্যঃ "+details.get("price")+" টাকা/কেজি (কুরিয়ার ফ্রি)");
            date.setText("বাজারে আসবে "+details.get("launch_date"));

            HashMap<String, String> finalDetails = details;
            orderkorun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(UpComingMangoes.this,OrderPageTwo.class);
                    i.putExtra("name", finalDetails.get("name"));
                    i.putExtra("price", finalDetails.get("price"));
                    i.putExtra("image", finalDetails.get("image"));
                    i.putExtra("id",nameandid.get(finalDetails.get("name")));
                    i.putExtra("order-type","pre");


                    startActivity(i);
                    finish();
                }
            });
            mylinearlayout2.addView(vi);
        }
    }



    void fatchdata(String s) throws JSONException {

        maindata = new JSONObject(s);

        mainjsonArray =maindata.getJSONArray("data");
        mainjsonarrays=new JSONObject[mainjsonArray.length()];
        mymap=new HashMap<>();
        nameandid=new HashMap<>();


        for(int i=0;i<mainjsonArray.length();i++){
            mainjsonarrays [i] = mainjsonArray.getJSONObject(i);
            HashMap<String,String> detail=new HashMap<>();
            detail.put("name",mainjsonarrays[i].getString("name"));
            detail.put("image",mainjsonarrays[i].getString("image"));
            detail.put("price",mainjsonarrays[i].getString("estimated_price"));
            detail.put("launch_date",mainjsonarrays[i].getString("launch_date"));
            mymap.put(mainjsonarrays[i].getString("id"),detail);
            nameandid.put(detail.get("name"),Integer.toString(i+1));
        }

         Log.e("hashmap",mymap.toString());
    }



    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
       static ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            DownloadImageTask.bmImage = bmImage;
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
