
package com.devxpart.seraamm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class OrderPageTwo extends AppCompatActivity {


    Button submit,ammbutton,chart;

    TextView name,price,amounttext,totalprice,totaldiscount;
    ImageView ammimage,backbutton,plusbutton,minusbutton;
    Intent intent;
    DatabaseHelper dh;

    @Override
    public void onBackPressed() {
        if(intent.hasExtra("order-type")){
            Cursor c=dh.getData("Request_4");
            StringBuilder sb=new StringBuilder();
            while (c.moveToNext()){
                sb.append(c.getString(0));
            }
            Intent i=new Intent(OrderPageTwo.this,UpComingMangoes.class);
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
            Intent i=new Intent(OrderPageTwo.this,CurrentMangoes.class);
            i.putExtra("server-response",sb.toString());
            Log.e("to op1",sb.toString());
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderpage2);



        totaldiscount=findViewById(R.id.orderpagetwodiscount);
        totalprice=findViewById(R.id.orderpagetotalprice);
        plusbutton=findViewById(R.id.orderpagetwoplusbutton);
        minusbutton=findViewById(R.id.orderpagetwominusbutton);
        ammimage=findViewById(R.id.orderpagetwoimage);
        submit=findViewById(R.id.orderpagetwosubmit);
        amounttext=findViewById(R.id.orderpagetwoamount);
        name=findViewById(R.id.orderpagetwoname);
        backbutton=findViewById(R.id.orderpagetwobackbutton);
        price=findViewById(R.id.orderpagetwoprice);
        dh=new DatabaseHelper(this);

        ammbutton=findViewById(R.id.ammbutton);
        chart=findViewById(R.id.chart);
        Log.e("price",getIntent().getStringExtra("price"));

        double taka=Double.parseDouble(getIntent().getStringExtra("price"));

        amounttext.setText("10 কেজি");
        totaldiscount.setText("0% ছাড়");
        totalprice.setText("মোট টাকাঃ "+10*taka);

        plusbutton.setOnClickListener(v -> {
            if(Integer.parseInt(amounttext.getText().toString().split(" ")[0])==10){
                amounttext.setText("20 কেজি");
                totaldiscount.setText("10% ছাড়");

                totalprice.setText("মোট টাকাঃ "+ ((20*taka)-(20*taka)*(.1)));

            }else if(Integer.parseInt(amounttext.getText().toString().split(" ")[0])==20){
                amounttext.setText("30 কেজি");
                totaldiscount.setText("10% ছাড়");
                totalprice.setText("মোট টাকাঃ "+((30*taka)-(30*taka) * (.1)));

            }else if(Integer.parseInt(amounttext.getText().toString().split(" ")[0])==30){
                amounttext.setText("40 কেজি");
                totaldiscount.setText("10% ছাড়");
                totalprice.setText("মোট টাকাঃ "+((40*taka)- (40 * taka) * (.1)));

            }
        });




        ammbutton.setOnClickListener(v -> {


            if(dh.getData("Request_1")!=null){
                Intent intent12 =new Intent(OrderPageTwo.this,HomePage.class);
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

        chart.setOnClickListener(v -> {
            startActivity(new Intent(OrderPageTwo.this,ListofOrder.class));
            finish();
        });

        minusbutton.setOnClickListener(v -> {
            if(Integer.parseInt(amounttext.getText().toString().split(" ")[0])==40){
                amounttext.setText("30 কেজি");
                totaldiscount.setText("10% ছাড়");
                totalprice.setText("মোট টাকাঃ "+ ((30*taka)-((30*taka) * (.1))));

            }else if(Integer.parseInt(amounttext.getText().toString().split(" ")[0])==30){
                amounttext.setText("20 কেজি");
                totaldiscount.setText("10% ছাড়");
                totalprice.setText("মোট টাকাঃ "+ ((20*taka)-((20*taka) *(.1))));

            }else if(Integer.parseInt(amounttext.getText().toString().split(" ")[0])==20){
                amounttext.setText("10 কেজি");
                totaldiscount.setText("০% ছাড়");
                totalprice.setText("মোট টাকাঃ "+ (10*taka));

            }
        });






         intent=getIntent();
        String mangoname=intent.getStringExtra("name");
        String mangoprice=intent.getStringExtra("price");
        if(intent.hasExtra("overview")){
            TextView overview= findViewById(R.id.orderpage2overview);
            overview.setText(intent.getStringExtra("overview"));
        }


        String mangoimage=intent.getStringExtra("image");
        Log.e("response current",intent.getExtras().toString());


        findViewById(R.id.chat).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });


        findViewById(R.id.readmore).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.facebook.com/SeraAam/"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });

        backbutton.setOnClickListener(v -> onBackPressed());
        name.setText(mangoname);
        price.setText(mangoprice+"  টাকা/কেজি (কুরিয়ার ফ্রি)");

        new DownloadImageTask(ammimage).execute(mangoimage);

        submit.setOnClickListener(v -> {
            if(intent.hasExtra("order-type")){
                String amount=amounttext.getText().toString();
                if(TextUtils.isEmpty(amount)){
                    amounttext.setError("Field Can Not Be Empty!");
                }else {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("mango_id",intent.getStringExtra("id"));
                        jsonObject.put("quantity",amount.split(" ")[0]);
                        jsonObject.put("coupon","WELCOMEAPP");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent i=new Intent(OrderPageTwo.this,Information.class);
                    i.putExtra("details",jsonObject.toString());
                    i.putExtra("order-type","pre");
                    startActivity(i);
                    finish();
                }
            }else {
                String amount=amounttext.getText().toString();
                if(TextUtils.isEmpty(amount)){
                    amounttext.setError("Field Can Not Be Empty!");
                }else {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("mango_id",intent.getStringExtra("id"));
                        jsonObject.put("quantity",amount.split(" ")[0]);
                        jsonObject.put("price",totalprice.getText().toString().split(" ")[2]);
                        Log.e("price",totalprice.getText().toString().split(" ")[2]);
                        jsonObject.put("name",intent.getStringExtra("name"));
                        jsonObject.put("image",intent.getStringExtra("image"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    boolean b = false;
                    b = orderholder.addtolist(jsonObject);
                    if(b){
                        Toast.makeText(OrderPageTwo.this,"অর্ডারটি আপনার চার্টে যোগ করা হয়েছে।",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(OrderPageTwo.this,ListofOrder.class));
                        finish();
                    }else
                        Toast.makeText(OrderPageTwo.this,"অর্ডারটি সম্পুর্ন করা সম্ভব হচ্ছে না।",Toast.LENGTH_LONG).show();

                }
            }

        });

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
