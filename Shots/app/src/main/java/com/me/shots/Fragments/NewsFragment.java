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
import com.me.shots.AsyncTasks.GetNewsString;
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
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    VerticalViewPager viewPager;
    PagerAdapter adapter;
    private float x1,x2,y1,y2;
    static final int MIN_DISTANCE = 50;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);

        progressBar=(ProgressBar)view.findViewById(R.id.login_progress);
        viewPager= (VerticalViewPager) view.findViewById(R.id.viewPager);
        adapter=new VerticalCycleAdapter(getContext());
        viewPager.setAdapter(adapter);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.action_refresh);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String jsonURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/post";
                Log.e("swipe", "onRefresh: "+jsonURL );
                new GetNewsString(swipeRefreshLayout,viewPager,adapter).execute(jsonURL);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int count=2,check;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                check=position;

                if(position==0)
                {
                    count++;
                    swipeRefreshLayout.setEnabled(true);
                }
                else if(position!=0)
                {
                    count=0;
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                }
                NewsPOGO.currentPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
}
