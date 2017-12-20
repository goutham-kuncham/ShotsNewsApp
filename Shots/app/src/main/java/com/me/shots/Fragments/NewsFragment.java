package com.me.shots.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.*;
import com.me.shots.Adapter.VerticalCycleAdapter;
import com.me.shots.Adapter.VerticalViewPager;
import com.me.shots.AsyncTasks.GetNewsAsync;
import com.me.shots.On_going_Courses_Fragment;
import com.me.shots.R;
import com.me.shots.Utils.NewsPOGO;
import com.me.shots.Utils.WebViewActivity;
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


    private float x1,x2,y1,y2;
    static final int MIN_DISTANCE = 250;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        VerticalViewPager viewPager= (VerticalViewPager) view.findViewById(R.id.viewPager);
        PagerAdapter adapter=new VerticalCycleAdapter(getContext());
        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(NewsPOGO.currentPosition);
//        RelativeLayout newsLayout= (RelativeLayout) view.findViewById(R.id.newsLayout);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                    Karma Points
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
