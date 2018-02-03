package com.me.shots;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.me.shots.Adapter.BookmarksAdapter;
import com.me.shots.Adapter.NotificationsAdapter;
import com.me.shots.Adapter.VerticalViewPager;
import com.me.shots.AsyncTasks.GetNewsString;
import com.me.shots.Utils.NewsPOGO;

public class NotificationsActivity extends AppCompatActivity {


    SwipeRefreshLayout swipeRefreshLayout;
    VerticalViewPager viewPager;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);



        viewPager= (VerticalViewPager) findViewById(R.id.notificationsViewPager);
        adapter=new NotificationsAdapter(this);
        viewPager.setAdapter(adapter);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.action_refresh);


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
                Log.e("myTag",position+" onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
//                    Karma Points
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
                Log.e("myTag",position+" onPageSelected");
                Log.e("myTag","count0                         :"+count);
                NewsPOGO.currentPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
/*
              Log.e("myTag",state+" onPageScrollStateChanged");
                Log.e("myTag","count1                         :"+count);
                if(count>=2 && check==0 && state==0)
                {
                    Log.e("myTag","count1                         :"+count);
                    Log.e("myTag","loading news");
            dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.loading_dialog);
                    dialog.setCanceledOnTouchOutside(false);
            dialog.show();
             myThread mthread=new myThread();
             mthread.start();
                     //   getNews();
                }
                else if(count>=1 && state==0)
                {

                   count++;
                   // progressBar.setVisibility(View.VISIBLE);
                   // getNews();
                }

*/
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }
        return true;
    }
}
