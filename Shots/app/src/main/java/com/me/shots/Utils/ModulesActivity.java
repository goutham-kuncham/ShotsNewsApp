package com.me.shots.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.Adapter.ModuleAdapter;
import com.me.shots.On_going_Courses_Fragment;
import com.me.shots.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class ModulesActivity extends AppCompatActivity {

    String MYURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/courses/Intro%20to%20Machine%20Learning";
    int modulecount=0;
    Context context=this;
    String[] modulenames=null;
    String link="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/courses/Intro to Machine Learning/1.pptx,http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/courses/Intro to Machine Learning/2.pptx,http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/courses/Intro to Machine Learning/3.pptx,";
    String coursetitle="Intro to Machine Learning";
    String []courselinks=null;
    String temp1[]=null;
    String temp2;
    String response1="hello";
    SharedPreferences sharedPreferences;
    String nickname;
    String mip;
    String tempnick;
    String mc;
    String myurl2;
    String karmaresponse;
    int mykarma;
    int karma;
    int courseid;
    int mc_count,mip_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences= sharedPreferences=getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        nickname=sharedPreferences.getString("nickname","null");
        mc=sharedPreferences.getString("mc","null");
        karma=sharedPreferences.getInt("karma",0);
        mc_count=sharedPreferences.getInt("mc_count",0);
        mip_count=sharedPreferences.getInt("mip_count",0);
        Button completed_btn=(Button)findViewById(R.id.completed_btn);
        completed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int qw=completed();
                if(response1.equalsIgnoreCase("true")){Toast.makeText(context,"Course completed"+response1,Toast.LENGTH_SHORT).show();}
                else {Toast.makeText(context,"Course cannot be completed"+response1,Toast.LENGTH_SHORT).show();}
                mip=sharedPreferences.getString("mip","lol");
                Log.e("mytag1", "onResponse: ---------"+mip );

                mip=mip.replace(","+coursetitle,"");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mip", mip);

                Log.e("mytag1", "onResponse: ---------"+mip );

                mykarma=karma;
                Log.e("mytag3", "onClick: module mccccc"+mc+"----"+coursetitle);


                if(mc.contains(coursetitle))
                {}
                else {
                    // TODO: 18-12-2017 add karma points +30
                     myurl2="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/updatekarma/" +tempnick+ "/30";
                    addkarmapoints(myurl2);
                    mykarma=karma+30;
                    editor.putInt("mc_count",mc_count+1);


                }
                editor.putInt("mip_count",mip_count-1);
                editor.putString("mc",mc+","+coursetitle);
                editor.putInt("karma",mykarma);
                Log.e("mytag3", "onClick: module link"+myurl2);
                editor.commit();
                finish();

            }
        });
        Intent intent=getIntent();
        Bundle b=intent.getBundleExtra("mybundle");
        link=b.getString("link");
        courseid=b.getInt("couresid");
        courselinks=link.split(",");
        coursetitle=b.getString("title",null);
        modulecount=courselinks.length;
        modulenames=new String[modulecount];
        for(int i=0;i<modulecount;i++) {
            temp1 = courselinks[i].split("/");
            temp2 = temp1[temp1.length - 1];
            modulenames[i] = temp2;
        }


        /*try {
            String responseData=new CallingHTML().execute(MYURL).get();

            if(responseData.equalsIgnoreCase("error")){
                Toast.makeText(this,"Unabe to load this module",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Document doc = Jsoup.parse(responseData);
                Element table = doc.select("th[colspan]").last();

                String linkHref = table.attr("colspan");
                Log.e("mytag", "onCreate: "+linkHref );
                Log.e("mytag", "onCreate: "+doc.body().toString() );
                modulecount= Integer.parseInt(linkHref)-2;
                modulenames=new String[modulecount];
                Element links=doc.select("a").get(5);
                for(int i=0;i<modulecount;i++){
                    modulenames[i]=doc.select("a").get(5+i).text();
                    Log.e("mytag", "onCreate: "+modulenames[i]+"======"+i );
                }
*/
        ListAdapter listAdapter=new ModuleAdapter(modulecount,modulenames,getApplicationContext(),courselinks);
        ListView listView= (ListView) findViewById(R.id.modules_listview);
        listView.setAdapter(listAdapter);
        listView.setEnabled(false);
        listView.setFocusable(false);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {



        super.onBackPressed();
    }

    void addkarmapoints(String myurl2)
    {

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, myurl2,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Respone",response.toString());
                try {
                    Log.e("hii","HII");
                    karmaresponse=response.toString();
                    if(karmaresponse.equalsIgnoreCase("true")){}else {karmaresponse="error";}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error response ",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);

    }

    private int completed() {


         tempnick=nickname;
        tempnick=tempnick.replace(" ","%20");
        Log.e("mytag1", "completed: nicknameeeeeeeeeeeee"+tempnick+"-------"+nickname );
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,"http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/coursecompleted/"+tempnick+"/"+courseid,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Respone",response.toString());
                try {
                    Log.e("hii","HII");
                     response1=response.toString();
                    if(response1.length()>6)response1="error";



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error response ",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);

        return 1;
    }


  /*      } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }*/

    class CallingHTML extends AsyncTask<String,Void,String>
    {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];

            Log.e("mytag", "doInBackground: "+"inside aclling"+stringUrl );
            String result="ERROR";
            String inputLine;
            int responsecode=0;
            try{
                URL loginUrl=new URL(stringUrl);
                HttpURLConnection connection =(HttpURLConnection)
                        loginUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.connect();
                responsecode=connection.getResponseCode();
                Log.e("mytag", "doInBackground: "+responsecode );
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();


                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();
                Log.e("mytag","Value==="+stringBuilder.toString());
                result = stringBuilder.toString();
                Log.e("mytag", "doInBackground: "+result );
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("mytag", "doInBackground: "+result );
            if(responsecode==200)
                return result;
            else return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
