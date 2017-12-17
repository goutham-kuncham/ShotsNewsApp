package com.me.shots.Utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.me.shots.Adapter.ModuleAdapter;
import com.me.shots.R;

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
    String[] modulenames=null;
    String link="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/courses/Intro to Machine Learning/1.pptx,http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/courses/Intro to Machine Learning/2.pptx,http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/courses/Intro to Machine Learning/3.pptx,";
    String coursetitle="Intro to Machine Learning";
    String []courselinks=null;
    String temp1[]=null;
    String temp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        Intent intent=getIntent();
        Bundle b=intent.getBundleExtra("mybundle");
        link=b.getString("link");
        courselinks=link.split(",");
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
        ListAdapter listAdapter=new ModuleAdapter(modulecount,modulenames,getApplicationContext());
        ListView listView= (ListView) findViewById(R.id.modules_listview);
        listView.setAdapter(listAdapter);
        listView.setEnabled(false);
        listView.setFocusable(false);


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
