package com.me.shots.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.shots.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by J Girish on 18-12-2017.
 */

public class FullDomainListAdapter extends BaseAdapter {
    Context context;
    String mydomain,interests;
    ArrayList <String> my_interests=new ArrayList<>();
     String domain_names[]={"Technology","Marketing","Programming","Management","Human Resources","Basic Finance"};
    SharedPreferences sharedPreferences;
    public FullDomainListAdapter(Context context)
    {

        this.context=context;
        sharedPreferences=context.getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        interests=sharedPreferences.getString("interests","lol");
        mydomain=sharedPreferences.getString("mydomain","Nodomain");
        Log.e("mydomain",mydomain);
        Log.e("interests",interests);
        if(mydomain.equals("Nodomain")||mydomain.equals("null"))
        {
            mydomain="Programming";
        }
        if(interests.equals("lol"))
        {
            interests="";
        }
        StringTokenizer st1=new StringTokenizer(interests,",");

        my_interests.add(mydomain);
        while (st1.hasMoreTokens())
        {
            my_interests.add(st1.nextToken());
        }
    }
    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
      //  sharedPreferences=context.getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        View myview= LayoutInflater.from(context).inflate(R.layout.domains_list,null);
        CardView cardView=(CardView)myview.findViewById(R.id.card_view);
        TextView domain=(TextView)myview.findViewById(R.id.mydomain);
        ImageView add=(ImageView)myview.findViewById(R.id.AddRemove);
        domain.setText(domain_names[i]);
        if(domain_names[i].equals(mydomain)) {

            add.setVisibility(View.VISIBLE);
            cardView.setBackgroundColor(Color.parseColor("#dcdcdc"));
        }
        for(int j=0;j<my_interests.size();j++)
        {
            if(domain_names[i].equals(my_interests.get(j)))
            {
                add.setVisibility(View.VISIBLE);
            }
        }

        return  myview;
    }
}
