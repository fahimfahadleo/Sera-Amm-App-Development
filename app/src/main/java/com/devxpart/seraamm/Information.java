package com.devxpart.seraamm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Information extends AppCompatActivity {
    ImageView backbutton;
    EditText name,phone,address;
    Button submit,ammbutton,chart;
    DatabaseHelper dh;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ListofOrder.class));

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ammbutton=findViewById(R.id.ammbutton);
        chart=findViewById(R.id.chart);
        backbutton=findViewById(R.id.informationbackbutton);
        name=findViewById(R.id.informationname);
        phone=findViewById(R.id.informationphone);
        address=findViewById(R.id.informationaddress);
        submit=findViewById(R.id.informationsubmit);
        dh=new DatabaseHelper(this);




        ammbutton.setOnClickListener(v -> {


            if(dh.getData("Request_1")!=null){
                Intent intent12 =new Intent(Information.this,HomePage.class);
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

        findViewById(R.id.chat).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.messenger.com/t/SeraAam"));
            String title = "Choose From Below!";
            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        });

        chart.setOnClickListener(v -> {
            startActivity(new Intent(Information.this,ListofOrder.class));
            finish();
        });


        backbutton.setOnClickListener(v -> onBackPressed());

        submit.setOnClickListener(v -> {
            String namestring=name.getText().toString();
            String phonestring=phone.getText().toString();
            String addressstring=address.getText().toString();


            if(TextUtils.isEmpty(namestring)){
                name.setError("Field Can Not Be Empty!");
                name.requestFocus();
            }else if(TextUtils.isEmpty(phonestring)){
                phone.setError("Field Can Not Be Empty!");
                phone.requestFocus();
            }else if(TextUtils.isEmpty(addressstring)){
                address.setError("Field Can Not Be Empty!");
                address.requestFocus();
            }else {

                JSONObject orderobject=new JSONObject();
                if(getIntent().hasExtra("details")){

                    try {
                        orderobject=new JSONObject(getIntent().getStringExtra("details"));
                        orderobject.put("name",namestring);
                        orderobject.put("address",addressstring);
                        orderobject.put("phone",phonestring);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(isNetworkAvailable()){
                        try {
                            new RequestToServer("https://seraaam.com/api/v1/pre-order",orderobject,Information.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Information.this,"আপনার অর্ডারটি পাঠানো হয়েছে। অনুগ্রহ করে অপেক্ষা করুন।",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Information.this,"Please Check Your Internet Connection And Try Again!",Toast.LENGTH_SHORT).show();
                    }

                }else{

                    try {


                        JSONArray finaljsonarray=new JSONArray();

                        JSONObject jsonObject=orderholder.getdata();
                        JSONArray jsonArray=jsonObject.getJSONArray("mangoes");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            object.remove("name");
                            object.remove("image");
                            finaljsonarray.put(object);
                        }

                        orderobject.put("name",namestring);
                        orderobject.put("phone",phonestring);
                        orderobject.put("total_price",Double.toString(getIntent().getDoubleExtra("price",0)));

                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);
                        String formattedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(c);
                        String newdate= formattedDate.replace("/","-");
                        String[]str=newdate.split("-");
                        String sb=str[2]+"-"+str[0]+"-"+str[1];
                        orderobject.put("coupon","WELCOMEAPP");
                        orderobject.put("order_date",sb);
                        orderobject.put("mangoes",finaljsonarray);
                        orderobject.put("address",addressstring);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if(isNetworkAvailable()){
                        try {
                            Log.e("data",orderobject.toString());
                            new RequestToServer("https://seraaam.com/api/v1/order",orderobject,Information.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Information.this,"আপনার অর্ডারটি পাঠানো হয়েছে। অনুগ্রহ করে অপেক্ষা করুন।",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Information.this,"Please Check Your Internet Connection And Try Again!",Toast.LENGTH_SHORT).show();
                    }



                }


            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
