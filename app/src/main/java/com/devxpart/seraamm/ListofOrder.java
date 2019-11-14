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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class ListofOrder extends AppCompatActivity {

    ImageView ordermore;
    LinearLayout mylayout;
    ImageView backbutton;
    JSONObject jsonObject;
    JSONArray jsonArray;
    TextView totalprice,discount;
    ImageView submit;
    double makku=0;
    int totaldiscount=0;
    int i;
    Button ammbutton,chart;
    DatabaseHelper dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_order);
        backbutton=findViewById(R.id.listoforderbackbutton);
        ordermore=findViewById(R.id.orderpagetwoordermore);
        totalprice=findViewById(R.id.totalprice);
        discount=findViewById(R.id.discount);
        submit=findViewById(R.id.ordernowbutton);
        mylayout=findViewById(R.id.orderlistlayout);
        ammbutton=findViewById(R.id.ammbutton);
        chart=findViewById(R.id.chart);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        dh=new DatabaseHelper(this);

        findViewById(R.id.chat).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });


        ammbutton.setOnClickListener(v -> {


            if(dh.getData("Request_1")!=null){
                Intent intent12 =new Intent(ListofOrder.this,HomePage.class);
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
            startActivity(new Intent(ListofOrder.this,ListofOrder.class));
            finish();
        });

        ordermore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c=dh.getData("Request_2");
                StringBuilder sb=new StringBuilder();
                while (c.moveToNext()){
                    sb.append(c.getString(0));
                }


                Intent i=new Intent(ListofOrder.this,CurrentMangoes.class);
                i.putExtra("server-response",sb.toString());
                Log.e("to op1",sb.toString());
                startActivity(i);
                finish();
            }
        });

        try {
            jsonObject=orderholder.getdata();
            jsonArray=jsonObject.getJSONArray("mangoes");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(jsonArray.length()==0){
            Toast.makeText(this,"আপনার চার্টে কোন আম নেই। দয়া করে অর্ডার করুন।",Toast.LENGTH_LONG).show();
        }


        submit.setOnClickListener(v -> {
        if(makku==0){
            Toast.makeText(ListofOrder.this,"আপনার চার্টে কোন আম নেই। দয়া করে অর্ডার করুন।",Toast.LENGTH_LONG).show();
        }else {
            startActivity(new Intent(ListofOrder.this,Information.class).putExtra("price",makku));
            finish();

        }
    });

        HashMap<String,Integer> list=new HashMap<>();


        for(i=0;i<jsonArray.length();i++){

            Log.e("jsonobject",jsonArray.toString());
            View vi=inflater.inflate(R.layout.singledocument,null,false);
            ImageView imgae=vi.findViewById(R.id.documentimage);
            ImageView cross=vi.findViewById(R.id.documentcross);
            TextView name=vi.findViewById(R.id.documentname);
            TextView price=vi.findViewById(R.id.documentprice);
            TextView quantity=vi.findViewById(R.id.documentquantity);



            try {
                list.put(jsonArray.getJSONObject(i).getString("name"),i);
                new DownloadImageTask(imgae).execute(jsonArray.getJSONObject(i).getString("image"));
                name.setText(jsonArray.getJSONObject(i).getString("name"));
                quantity.setText(jsonArray.getJSONObject(i).getString("quantity")+" কেজি");
                totaldiscount=totaldiscount+Integer.parseInt(jsonArray.getJSONObject(i).getString("quantity").split(" ")[0]);

                price.setText(jsonArray.getJSONObject(i).getString("price")+" টাকা");

                makku=makku+Double.parseDouble(jsonArray.getJSONObject(i).getString("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cross.setOnClickListener(v -> {
                orderholder.removedata(list.get(name.getText()));
                makku=makku-Double.parseDouble(price.getText().toString().split(" ")[0]);
                totalprice.setText("সর্বমোটঃ "+makku+" টাকা");
                totaldiscount=totaldiscount-Integer.parseInt(quantity.getText().toString().split(" ")[0]);
                if(totaldiscount>10){
                    discount.setText(totaldiscount +" ছাড়");
                }else {
                    discount.setText("0% ছাড়");
                }

                startActivity(new Intent(ListofOrder.this,ListofOrder.class));
                finish();
            });



            mylayout.addView(vi);
        }


        backbutton.setOnClickListener(v -> onBackPressed());


        totalprice.setText("সর্বমোটঃ "+makku+" টাকা");
        if(totaldiscount>=20){
            discount.setText("10% ছাড়");
        }

    }

    @Override
    public void onBackPressed() {

        Cursor c=dh.getData("Request_2");
        StringBuilder sb=new StringBuilder();
        while (c.moveToNext()){
            sb.append(c.getString(0));
        }


        Intent i=new Intent(ListofOrder.this,CurrentMangoes.class);
        i.putExtra("server-response",sb.toString());
        Log.e("to op1",sb.toString());
        startActivity(i);
        finish();
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
