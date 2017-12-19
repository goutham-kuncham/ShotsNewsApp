package com.me.shots.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.Adapter.ListViewAdapter;
import com.me.shots.On_going_Courses_Fragment;
import com.me.shots.Profile_Pic;
import com.me.shots.R;
import com.me.shots.domain_courses;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class domain_list extends Fragment {
    SharedPreferences sharedPreferences;
String name,url;
public static String nickname;
int id_selected;

    public domain_list() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences=this.getActivity().getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
         nickname=sharedPreferences.getString("nickname","Gj");
        name=getArguments().getString("Domain_Name");
//        ArrayList<domain_courses> courses_list=(ArrayList<domain_courses>)getArguments().getSerializable("Domain_Courses_list");
//        Log.e("mytag",courses_list.size()+"lololol");
        View view = inflater.inflate(R.layout.fragment_domain_list, container, false);
        Button backbtn= (Button) view.findViewById(R.id.back_btncourse);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.homeFrame_PlaceHolder,new On_going_Courses_Fragment());

                fragmentTransaction.commit();
                Log.e("mytag3", "onClick: backkkkkkkk" );

            }
        });
        TextView dn=(TextView)view.findViewById(R.id.domain_name);
        TextView promt=(TextView)view.findViewById(R.id.no_courses);
        dn.setText(name);
                ScrollView sv = (ScrollView) view.findViewById(R.id.scrollMe);
        sv.setScrollX(0);
        sv.setScrollY(0);
        if(On_going_Courses_Fragment.courses_list.size()==0)
        {

            promt.setVisibility(View.VISIBLE);
        }
        else
        {
            promt.setVisibility(View.INVISIBLE);
        }
        ListView listView = (ListView) view.findViewById(R.id.list_display_domain);     // For Displaying On-Going Courses
        final ListViewAdapter listAdapter = new ListViewAdapter(getActivity(), sv,"Domain");
        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView course_name = (TextView) view.findViewById(R.id.info_text);
                String cn = course_name.getText().toString();
                Log.e("hrllol","hrllo");

//                for (int i=0;i<On_going_Courses_Fragment.courses_list.size();i++)
//                {
//                    if(On_going_Courses_Fragment.courses_list.get(i).getTitle().equals(cn))
//                    {
//                        id_selected=On_going_Courses_Fragment.courses_list.get(i).getId();
//
//                    }
//                }

            //    Button mycontinue = (Button) view.findViewById(R.id.mycontinue);



            }
        });
        return view;
    }
}
