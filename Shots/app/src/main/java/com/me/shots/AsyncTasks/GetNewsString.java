package com.me.shots.AsyncTasks;

import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.me.shots.Adapter.VerticalViewPager;
import com.me.shots.Utils.NewsPOGO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Half_BlooD PrincE on 2/1/2018.
 */


public class GetNewsString extends AsyncTask<String, Void, String> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    SwipeRefreshLayout swipeRefreshLayout;
    VerticalViewPager viewPager;
    PagerAdapter adapter;

    public GetNewsString(SwipeRefreshLayout swipeRefreshLayout,VerticalViewPager verticalViewPager,PagerAdapter adapter)
    {
        this.adapter=adapter;
        this.swipeRefreshLayout=swipeRefreshLayout;
        this.viewPager=verticalViewPager;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = params[0];

        Log.e("mytag", "doInBackground: " + "inside aclling" + stringUrl);
        String result = "ERROR";
        String inputLine;
        int responsecode = 0;
        try {
            URL loginUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection)
                    loginUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();
            responsecode = connection.getResponseCode();
            Log.e("mytag", "doInBackground: " + responsecode);
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();


            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();
            Log.e("mytag", "Value===" + stringBuilder.toString());
            result = stringBuilder.toString();
            Log.e("mytag", "doInBackground: " + result);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("swipe", "doInBackground: " + result);
        if (responsecode == 200)
        {
            getNews(result);
            return result;
        }
        else {
            getNews("error");
            return "error";
        }
    }


    void getNews(String jsonString)
    {
        try {
            if(jsonString.equalsIgnoreCase("error"))
            {
                //ERROR FETCHING NEWS!!!
            }
            else
            {
                JSONObject response=new JSONObject(jsonString);
                JSONArray jsonArray=response.getJSONArray("objects");
                if(jsonArray.length()!= NewsPOGO.newsArray.size())
                {
//                        NewsPOGO.newsArray.clear();
                    for(int i=jsonArray.length()-1;i>=NewsPOGO.newsArray.size();i--)
                    {
                        NewsPOGO newsPOGO;
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
                        newsPOGO.notification_worthy=jsonObject.getString("notification_worthy");
                        newsPOGO.user_id=jsonObject.getInt("user_id");
                        NewsPOGO.newsArray.add(newsPOGO);

                    }
                    Log.d("swipe", "getNews: "+NewsPOGO.newsArray.size());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onPostExecute(String s) {
        //     progressBar.setVisibility(View.INVISIBLE);
//            dialog.dismiss();
        Log.e("Swipe", "onPostExecute: "+s );
        swipeRefreshLayout.setRefreshing(false);
        viewPager.setAdapter(adapter);
        super.onPostExecute(s);
        //  swipeRefreshLayout.clearAnimation();
    }
}

