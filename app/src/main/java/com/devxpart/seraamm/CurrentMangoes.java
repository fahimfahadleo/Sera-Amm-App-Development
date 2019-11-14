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

public class CurrentMangoes extends AppCompatActivity {

    LinearLayout mylinearlayout;
    ImageView backbutton;
    Button ambutton,chart,chat;
    JSONObject maindata;
    JSONArray mainjsonArray;

    JSONObject [] mainjsonarrays;

    int i=0;
    HashMap<String,HashMap<String,String>> mymap;
    HashMap<String,String> nameandid;
    DatabaseHelper databaseHelper;


    @Override
    public void onBackPressed() {
        if(databaseHelper.getData("Request_1")!=null){
            Intent intent=new Intent(CurrentMangoes.this,HomePage.class);
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
            new RequestToServer("https://seraaam.com/api/v1/android-app/home/offer",CurrentMangoes.this,"home-page");
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondpage);
        mylinearlayout=findViewById(R.id.mylinearlayout);
        chart=findViewById(R.id.chart);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        backbutton=findViewById(R.id.nextpage);
        ambutton=findViewById(R.id.ambutton);
        databaseHelper=new DatabaseHelper(this);
        chat=findViewById(R.id.chat);

        findViewById(R.id.chat).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });


        Intent intent=getIntent();
        String serverresponse=intent.getStringExtra("server-response");

        Log.e("response current",serverresponse);


        try {
            fatchdata(serverresponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ambutton.setOnClickListener(v -> {


            if(databaseHelper.getData("Request_1")!=null){
                Intent intent12 =new Intent(CurrentMangoes.this,HomePage.class);
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
            startActivity(new Intent(CurrentMangoes.this,ListofOrder.class));
            finish();
        });


        backbutton.setOnClickListener(v -> {
            onBackPressed();
        });

        for( i=0;i<mymap.size();i++){
            View vi=  inflater.inflate(R.layout.singleblockofcurrentmangos,null,false);
            ImageView orderkorun =vi.findViewById(R.id.currentmangoorder);
            TextView name=vi.findViewById(R.id.name);
            TextView price=vi.findViewById(R.id.price);
            ImageView rating1=vi.findViewById(R.id.rating1);
            ImageView rating2=vi.findViewById(R.id.rating2);
            ImageView rating3=vi.findViewById(R.id.rating3);
            ImageView rating4=vi.findViewById(R.id.rating4);
            ImageView rating5=vi.findViewById(R.id.rating5);
            LinearLayout bodylayout=vi.findViewById(R.id.bodylinearlyaout);
            ImageView ammimage=vi.findViewById(R.id.ammimage);



            HashMap<String,String> details=new HashMap<>();

            details=mymap.get(String.valueOf(i+1));
            String newurl = details.get("image");
            new DownloadImageTask(ammimage).execute(newurl);



            name.setText(details.get("name"));
            price.setText(details.get("price")+" টাকা/কেজি (কুরিয়ার ফ্রি)");

            bodylayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(databaseHelper.getData("Request_3")!=null){
                        Cursor d=databaseHelper.getSingleRaw("Request_3",name.getText().toString());
                        StringBuilder sb=new StringBuilder();
                        while (d.moveToNext()){
                            sb.append(d.getString(0));
                        }

                        Log.e("db response",sb.toString());
                        Intent intent1=new Intent(CurrentMangoes.this,OrderPageOne.class);
                        intent1.putExtra("server-response",sb.toString());
                        Log.e("to op1",sb.toString());
                        startActivity(intent1);
                        finish();

                    }else {
                        new RequestToServer("https://seraaam.com/api/v1/mangoes/"+nameandid.get(name.getText()), CurrentMangoes.this,"Order-Page-One");
                        finish();
                    }

                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(databaseHelper.getData("Request_3")!=null){
                        Cursor d=databaseHelper.getSingleRaw("Request_3",name.getText().toString());
                        StringBuilder sb=new StringBuilder();
                        while (d.moveToNext()){
                            sb.append(d.getString(0));
                        }
                        Intent intent1=new Intent(CurrentMangoes.this,OrderPageOne.class);
                        intent1.putExtra("server-response",sb.toString());
                        Log.e("to op1",sb.toString());
                        startActivity(intent1);
                        finish();
                    }else {
                        new RequestToServer("https://seraaam.com/api/v1/mangoes/"+nameandid.get(name.getText()), CurrentMangoes.this,"Order-Page-One");
                        finish();
                    }
                }
            });

            ammimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(databaseHelper.getData("Request_3")!=null){
                        Cursor d=databaseHelper.getSingleRaw("Request_3",name.getText().toString());
                        StringBuilder sb=new StringBuilder();
                        while (d.moveToNext()){
                            sb.append(d.getString(0));
                        }
                        Intent intent1=new Intent(CurrentMangoes.this,OrderPageOne.class);
                        intent1.putExtra("server-response",sb.toString());
                        Log.e("to op1",sb.toString());
                        startActivity(intent1);
                        finish();

                    }else {
                        new RequestToServer("https://seraaam.com/api/v1/mangoes/"+nameandid.get(name.getText()), CurrentMangoes.this,"Order-Page-One");
                        finish();
                    }
                }
            });

            int ratingvalue=Integer.parseInt(details.get("rating"));

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


            HashMap<String, String> finalDetails = details;
            orderkorun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Intent i=new Intent(CurrentMangoes.this,OrderPageTwo.class);
                    i.putExtra("name", finalDetails.get("name"));
                    i.putExtra("price", finalDetails.get("price"));
                    i.putExtra("image", finalDetails.get("image"));
                    //putextra descryption
                    if(!getIntent().hasExtra("order-type")){
                        Cursor c=databaseHelper.getSingleRaw("Request_3",finalDetails.get("name"));
                        StringBuilder sb=new StringBuilder();
                        while (c.moveToNext()){
                            sb.append(c.getString(0));
                        }
                        try {
                            JSONObject jsonObject=new JSONObject(sb.toString());
                            String overview =jsonObject.getJSONObject("data").getString("overview");
                            i.putExtra("overview",overview);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    i.putExtra("id",nameandid.get(finalDetails.get("name")));




                    startActivity(i);
                    finish();

                 }
            });
            mylinearlayout.addView(vi);
        }


    }



    void fatchdata(String s) throws JSONException{

        maindata = new JSONObject(s);

        mainjsonArray =maindata.getJSONArray("data");
       // Log.e("jsonobject", mainjsonArray.toString());
        mainjsonarrays=new JSONObject[mainjsonArray.length()];
        mymap=new HashMap<>();
        nameandid=new HashMap<>();


       for(int i=0;i<mainjsonArray.length();i++){
           mainjsonarrays [i] = mainjsonArray.getJSONObject(i);
           HashMap<String,String> detail=new HashMap<>();
           detail.put("name",mainjsonarrays[i].getString("name"));
           detail.put("image",mainjsonarrays[i].getString("image"));
           detail.put("price",mainjsonarrays[i].getString("price"));
           detail.put("rating",mainjsonarrays[i].getString("rating"));
           mymap.put(mainjsonarrays[i].getString("id"),detail);
           nameandid.put(detail.get("name"),Integer.toString(i+1));
       }


       // Log.e("hashmap",mymap.toString());
    }



    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
