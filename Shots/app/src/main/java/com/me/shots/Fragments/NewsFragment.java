package com.me.shots.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.*;
import com.me.shots.Adapter.VerticalViewPager;
import com.me.shots.AsyncTasks.GetNewsAsync;
import com.me.shots.R;
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

    Context context;

    public NewsFragment(Context context)
    {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        VerticalViewPager viewPager= (VerticalViewPager) view.findViewById(R.id.viewPager);
        Log.d("Tagged", "onCreateView: ");
        GetNewsAsync get=new GetNewsAsync(context,view);
        get.execute();
        Log.d("Mytag", "onCreateView: after view pager");
        return view;
    }
}
