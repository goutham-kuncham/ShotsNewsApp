package com.me.shots.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.*;
import com.me.shots.Adapter.VerticalCycleAdapter;
import com.me.shots.Adapter.VerticalViewPager;
import com.me.shots.R;
import com.me.shots.Utils.MySingleton;
import com.me.shots.Utils.NewsPOGO;
//import com.shorts.jgirish.snu_pro.Adapter.VerticalCycleAdapter;
//import com.shorts.jgirish.snu_pro.Adapter.VerticalViewPager;
//import com.shorts.jgirish.snu_pro.R;
//import com.shorts.jgirish.snu_pro.Utils.Utils.MySingleton;
//import com.shorts.jgirish.snu_pro.Utils.Utils.NewsPOGO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Half_BlooD PrincE on 10/9/2017.
 */

public class GetNewsAsync extends AsyncTask<Void,Void,String> {

    Context context;
    View view;
    String resp="";
    VerticalViewPager viewPager;
    View mProgressView;
    NewsPOGO newsPOGO;
    String jsonURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/post";

    public GetNewsAsync(Context context, View view)
    {
        this.view=view;
        this.context=context;
        mProgressView=view.findViewById(R.id.login_progress);
        viewPager= (VerticalViewPager) view.findViewById(R.id.viewPager);
        Log.d("tagged", "GetNewsAsync: ");
    }

    @Override
    protected void onPreExecute() {
        mProgressView.setVisibility(View.VISIBLE);
        Log.d("tagged", "onPreExecute: ");
        //super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.d("tagged", "doInBackground: ");
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, jsonURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<NewsPOGO> newsArray =new ArrayList<>();
//                    NewsPOGO.newsArray.clear();
                    Log.d("tagged", "onResponse: "+response.toString());
                    resp=response.toString();
                    JSONArray jsonArray=response.getJSONArray("objects");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject= jsonArray.getJSONObject(i);

                        newsPOGO=new NewsPOGO();
                        newsPOGO.body=jsonObject.getString("body");
                        newsPOGO.category=jsonObject.getString("category");
                        newsPOGO.content_type=jsonObject.getString("content_type");
                        newsPOGO.id=jsonObject.getInt("id");
                        newsPOGO.image=jsonObject.getString("image");
                        newsPOGO.likes=jsonObject.getInt("likes");
                        newsPOGO.link=jsonObject.getString("link");
                        newsPOGO.timestamp=jsonObject.getString("timestamp");
                        newsPOGO.title=jsonObject.getString("title");
                        newsPOGO.types=jsonObject.getString("types");
                        newsPOGO.user_id=jsonObject.getInt("user_id");
                        newsArray.add(newsPOGO);

                    }

                    if(newsArray.size()!=NewsPOGO.newsArray.size())
                    {
                        NewsPOGO.newsArray.clear();
                        NewsPOGO.newsArray=newsArray;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resp=error.toString();
                Log.d("tagged", "onResponse: "+error.toString());
                //name.setText("ERROR...");
                Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        });


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getMyInstance(context).addToReqQue(jsonObjectRequest);

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        Log.d("tagged", "onPostExecute: ");
        mProgressView.setVisibility(View.GONE);
        PagerAdapter adapter=new VerticalCycleAdapter(context);
        viewPager.setAdapter(adapter);
        super.onPostExecute(s);
    }
}
