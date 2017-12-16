package com.me.shots;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by J Girish on 09-10-2017.
 */

public class On_going_Courses_Fragment extends android.support.v4.app.Fragment {
 Context context;

    public On_going_Courses_Fragment (){
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment0


        //return inflater.inflate(R.layout.fragment_calls, container, false);
        View view=inflater.inflate(R.layout.on_going_courses_fragment, container, false);
        ScrollView sv = (ScrollView)view.findViewById(R.id.scrollView1);
        sv.setScrollX(0);
        sv.setScrollY(0);
        ListView listView = (ListView)view.findViewById(R.id.list_display);
        ListViewAdapter listAdapter = new ListViewAdapter(getActivity(),sv);
        listView.setAdapter(listAdapter);

        return view;

    }
}
