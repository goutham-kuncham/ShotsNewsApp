package com.me.shots;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;

/**
 * Created by J Girish on 08-10-2017.
 */

public class ListViewAdapter extends BaseAdapter {

    Context context;
    ScrollView scrollView;
    public  ListViewAdapter(Context context,ScrollView scrollView)
    {
        this.context=context;
        this.scrollView=scrollView;
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
        View lview= LayoutInflater.from(context).inflate(R.layout.listview_box,null);
        scrollView.setScrollX(0);
        scrollView.setScrollY(0);
        return lview;
    }
}
