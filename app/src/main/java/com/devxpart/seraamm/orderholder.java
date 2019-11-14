package com.devxpart.seraamm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class orderholder {

    private static JSONArray orderlist=new JSONArray();


    public static boolean addtolist(JSONObject jsonObject) {


        boolean state=false;

        try{
            orderlist.put(jsonObject);
            state=true;
        }catch (Exception e){
            state=false;
        }


        return state;

    }

    public static JSONObject getdata() throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("mangoes", orderlist);
        return jsonObject ;
    }

    public static void removedata(int index){
      orderlist=  remove(index,orderlist);
    }


    public static JSONArray remove(final int index, final JSONArray from) {
        final List<JSONObject> objs = getList(from);
        objs.remove(index);

        final JSONArray jarray = new JSONArray();
        for (final JSONObject obj : objs) {
            jarray.put(obj);
        }

        return jarray;
    }

    public static List<JSONObject> getList(final JSONArray jarray) {
        final int len = jarray.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = jarray.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

    public static void clearalldata(){
        orderlist=new JSONArray();
    }
}
