package com.devxpart.seraamm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestToServer {


    private String serverresponse2;
    private String serverresponse;

    private HashMap<String,HashMap<String,String>> mymap;

    RequestToServer(String URL, Context context, String page){

        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                Request request=chain.request();

                Request newRequest= request.newBuilder().addHeader("Authorization", "c2VyYS1hYW0tYXBpLXYxLWJ5LWRldnhwYXJ0 ").addHeader("Content-Type","application/json").build();
                return chain.proceed(newRequest);
            }
        }).build();

        String url = URL;

        Request request=new Request.Builder().url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                serverresponse2=response.body().string();

                switch (page) {
                    case "home-page":

                        Date currentdate = Calendar.getInstance().getTime();
                        String formattedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(currentdate);



                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                databaseHelper.insartData("RequestTime", formattedDate);
                               boolean b= databaseHelper.insartData("Request_1", serverresponse2);
                               if(b){
                                   new RequestToServer("https://seraaam.com/api/v1/mangoes/", context, "current-mangoes");
                               }else {
                                   ( (Activity)context).runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           Toast.makeText(context,"Failed to save Data!",Toast.LENGTH_SHORT).show();
                                       }
                                   });
                               }
                            }
                        });




                        break;
                    case "current-mangoes":
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = databaseHelper.insartData("Request_2", serverresponse2);
                                if (b) {
                                    Cursor cu = databaseHelper.getData("Request_2");
                                    StringBuilder sb = new StringBuilder();
                                    while (cu.moveToNext()) {
                                        sb.append(cu.getString(0));
                                    }
                                    try {
                                        fatchdata(sb.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    for (int i = 0; i < mymap.size(); i++) {
                                        Request("https://seraaam.com/api/v1/mangoes/" + mymap.get(Integer.toString(i + 1)).get("id"), context,mymap.get(Integer.toString(i+1)).get("name"));
                                    }
                                }else {
                                    ( (Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context,"Failed to save Data!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });


                        new RequestToServer("https://seraaam.com/api/v1/mangoes/upcoming", context, "future-mangoes");

                        break;
                    case "future-mangoes":
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = databaseHelper.insartData("Request_4", serverresponse2);
                                if (b) {
                                    Cursor cu = databaseHelper.getData("Request_4");
                                    StringBuilder sb = new StringBuilder();
                                    while (cu.moveToNext()) {
                                        sb.append(cu.getString(0));
                                    }
                                    try {
                                        prefatchdata(sb.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    for (int i = 0; i < mymap.size(); i++) {
                                        preRequest("https://seraaam.com/api/v1/mangoes/upcoming/" + mymap.get(Integer.toString(i + 1)).get("id"), context,mymap.get(Integer.toString(i+1)).get("name"));
                                    }
                                }else {
                                    ( (Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context,"Failed to save Data!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                        break;
                }

                Log.e("Response",serverresponse2);
            }
        });


    }


    public RequestToServer(String url, JSONObject json,Context context) throws IOException {




            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization","c2VyYS1hYW0tYXBpLXYxLWJ5LWRldnhwYXJ0 ")
                    .addHeader("Content-Type","application/json")
                    .build();

             //Response response = client.newCall(request).execute();

             client.newCall(request).enqueue(new Callback() {
                 @Override
                 public void onFailure(@NotNull Call call, @NotNull IOException e) {

                 }

                 @Override
                 public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        serverresponse =response.body().string();
                     try {
                         JSONObject jsonObject=new JSONObject(serverresponse);
                         JSONObject essential =jsonObject.getJSONObject("data");
                         if(essential.has("order_id")){
                             Intent i=new Intent(context,Result.class);
                             i.putExtra("order_id",essential.getString("order_id"));
                             context.startActivity(i);
                             orderholder.clearalldata();
                             ((Activity)context).finish();
                         }else {
                             context.startActivity(new Intent(context,ResultUnsuccessful.class));
                             ((Activity)context).finish();
                         }

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }


                     Log.e("response",serverresponse);

                 }
             });



    }

    private void fatchdata(String s) throws JSONException{
        int i=0;

        JSONObject maindata = new JSONObject(s);

        JSONArray mainjsonArray = maindata.getJSONArray("data");
        JSONObject[] mainjsonarrays = new JSONObject[mainjsonArray.length()];
        mymap=new HashMap<>();



        for(i = 0; i< mainjsonArray.length(); i++){
            mainjsonarrays[i] = mainjsonArray.getJSONObject(i);
            HashMap<String,String> detail=new HashMap<>();
            detail.put("name",mainjsonarrays[i].getString("name"));
            detail.put("id",mainjsonarrays[i].getString("id"));
            mymap.put(mainjsonarrays[i].getString("id"),detail);

        }

    }


    private void prefatchdata(String s) throws JSONException {
        int i=0;

        JSONObject maindata = new JSONObject(s);

        JSONArray mainjsonArray = maindata.getJSONArray("data");
        JSONObject[] mainjsonarrays;
        mymap=new HashMap<>();


        maindata = new JSONObject(s);

        mainjsonArray =maindata.getJSONArray("data");
        // Log.e("jsonobject", mainjsonArray.toString());
        mainjsonarrays=new JSONObject[mainjsonArray.length()];
        mymap=new HashMap<>();



        for( i=0;i<mainjsonArray.length();i++){
            mainjsonarrays [i] = mainjsonArray.getJSONObject(i);
            HashMap<String,String> detail=new HashMap<>();
            detail.put("id",mainjsonarrays[i].getString("id"));
            detail.put("name",mainjsonarrays[i].getString("name"));
            mymap.put(mainjsonarrays[i].getString("id"),detail);

        }

        Log.e("hashmap",mymap.toString());
    }


   private void Request(String URL, Context context,String name){

       DatabaseHelper databaseHelper=new DatabaseHelper(context);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                Request request=chain.request();

                Request newRequest= request.newBuilder().addHeader("Authorization", "c2VyYS1hYW0tYXBpLXYxLWJ5LWRldnhwYXJ0 ").addHeader("Content-Type","application/json").build();
                return chain.proceed(newRequest);
            }
        }).build();

       Request request=new Request.Builder().url(URL).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                serverresponse2=response.body().string();

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        databaseHelper.insartData("Request_3",name,serverresponse2);
                    }
                });
                Log.e("Response",serverresponse2);
            }
        });


    }


    private void preRequest(String URL, Context context,String name){

        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                Request request=chain.request();

                Request newRequest= request.newBuilder().addHeader("Authorization", "c2VyYS1hYW0tYXBpLXYxLWJ5LWRldnhwYXJ0 ").addHeader("Content-Type","application/json").build();
                return chain.proceed(newRequest);
            }
        }).build();

        Request request=new Request.Builder().url(URL).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                serverresponse2=response.body().string();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        databaseHelper.insartData("Request_5",name,serverresponse2);
                    }
                });
                Log.e("Response",serverresponse2);
                Intent i=new Intent(context,HomePage.class);
                Cursor c=databaseHelper.getData("Request_1");
                StringBuilder sb=new StringBuilder();
                while (c.moveToNext()){
                    sb.append(c.getString(0));
                }
                i.putExtra("server-response",sb.toString());
                context.startActivity(i);

                ((Activity)context).finish();
            }
        });


    }
}
