package com.me.shots.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.me.shots.R;

import java.util.ArrayList;

/**
 * Created by J Girish on 10-12-2017.
 */

public class DomainListAdapter extends BaseAdapter {
    Context context;
    ArrayList <String> courses=new ArrayList<>();
    // String domain_names[]={"Technology","Marketing","Programming","Management","Human Resources","Basic Finance"};

    public DomainListAdapter(Context context, ArrayList<String> courses)
    {
        this.courses=courses;
        this.context=context;
    }
    @Override
    public int getCount() {
        return courses.size();
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
        View myview= LayoutInflater.from(context).inflate(R.layout.domains_list,null);
        TextView domain=(TextView)myview.findViewById(R.id.mydomain);
        domain.setText(courses.get(i));
        return  myview;
    }
}
