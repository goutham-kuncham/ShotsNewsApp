package com.me.shots.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.*;
import com.me.shots.Adapter.VerticalCycleAdapter;
import com.me.shots.Adapter.VerticalViewPager;
import com.me.shots.AsyncTasks.GetNewsAsync;
import com.me.shots.HomeActivity;
import com.me.shots.On_going_Courses_Fragment;
import com.me.shots.R;
import com.me.shots.Utils.NewsPOGO;
import com.me.shots.Utils.WebViewActivity;

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
import java.util.concurrent.ExecutionException;
//
//import com.shorts.jgirish.snu_pro.Adapter.VerticalViewPager;
//import com.shorts.jgirish.snu_pro.AsyncTasks.AsyncTasks.GetNewsAsync;
//import com.shorts.jgirish.snu_pro.R;

//import com.me.shots.Adapters.VerticalViewPager;
//import com.me.shots.AsyncTasks.GetNewsAsync;
//import com.me.shots.R;

/**
 * Created by Half_BlooD PrincE on 10/9/2017.
 */

public class NewsFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    private float x1,x2,y1,y2;
    static final int MIN_DISTANCE = 250;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
      //  swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe)
        progressBar=(ProgressBar)view.findViewById(R.id.login_progress);
        VerticalViewPager viewPager= (VerticalViewPager) view.findViewById(R.id.viewPager);
        PagerAdapter adapter=new VerticalCycleAdapter(getContext());
        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(NewsPOGO.currentPosition);
//        RelativeLayout newsLayout= (RelativeLayout) view.findViewById(R.id.newsLayout);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int count=2,check;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
             Log.e("myTag",position+" onPageScrolled");

            }

            @Override
            public void onPageSelected(int position) {
//                    Karma Points
                check=position;
                if(position==0)
                    count++;
                else if(position!=0)
                    count=0;
                Log.e("myTag",position+" onPageSelected");
                Log.e("myTag","count0                         :"+count);
                NewsPOGO.currentPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
              Log.e("myTag",state+" onPageScrollStateChanged");
                Log.e("myTag","count1                         :"+count);
                if(count>=2 && check==0 && state==0)
                {
                    Log.e("myTag","count1                         :"+count);
                    Log.e("myTag","loading news");
            dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.loading_dialog);
            dialog.show();

                     //   getNews();
                }
                else if(count>=1 && state==0)
                {

                   count++;
                   // progressBar.setVisibility(View.VISIBLE);
                   // getNews();
                }

            }
        });


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch(ev.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = ev.getX();
                        y1=ev.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = ev.getX();
                        y2=ev.getY();

                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;

                        if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaY) <= MIN_DISTANCE)
                        {
                            if (x2 > x1)
                            {
//                                FragmentManager fragmentManager2 = getFragmentManager();
//                                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
//                                On_going_Courses_Fragment fragment2 = new On_going_Courses_Fragment();
//                                fragmentTransaction2.hide(NewsFragment.this);
//                                fragmentTransaction2.add(android.R.id.content, fragment2);
//                                fragmentTransaction2.commit();
                            }

                            // Right to left swipe action
                            else
                            {
//                                Toast.makeText(getContext(),"Horizontal",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getContext(), WebViewActivity.class);
                                intent.putExtra("Link",NewsPOGO.newsArray.get(NewsPOGO.currentPosition).link);
                                getContext().startActivity(intent);
                            }

                        }

                        if (Math.abs(deltaY) > MIN_DISTANCE && Math.abs(deltaX) <= MIN_DISTANCE )
                        {
//                            Toast.makeText(getContext(),"Vertical",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });




        return view;
    }
    void getNews() {
        String jsonURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/post";
        Log.e("url",jsonURL);
        try {
            String jsonString=new getNewsString().execute(jsonURL).get();
            Log.e("url",jsonURL);
            if(jsonString.equalsIgnoreCase("error"))
            {
                //ERROR FETCHING NEWS!!!
                Log.e("hi","lol");
            }
            else
            {
                JSONObject response=new JSONObject(jsonString);
                JSONArray jsonArray=response.getJSONArray("objects");
                if(jsonArray.length()!=NewsPOGO.newsArray.size())
                {
                    NewsPOGO.newsArray.clear();
                    for(int i=jsonArray.length()-1;i>=0;i--)
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
                        newsPOGO.user_id=jsonObject.getInt("user_id");
                        NewsPOGO.newsArray.add(newsPOGO);

                    }
                    Log.d("explore", "getNews: "+NewsPOGO.newsArray.size());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    class getNewsString extends AsyncTask<String,Void,String>
    {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

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
       //     progressBar.setVisibility(View.INVISIBLE);
            dialog.dismiss();
            super.onPostExecute(s);
          //  swipeRefreshLayout.clearAnimation();
        }
    }



}
