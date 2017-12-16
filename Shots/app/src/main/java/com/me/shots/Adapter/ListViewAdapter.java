package com.me.shots.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.Fragments.domain_list;
import com.me.shots.On_going_Courses_Fragment;
import com.me.shots.Profile_Pic;
import com.me.shots.R;
import com.me.shots.domain_courses;

import java.util.ArrayList;

/**
 * Created by J Girish on 08-10-2017.
 */

public class ListViewAdapter extends BaseAdapter {

    Context context;
    ScrollView scrollView;
    String type;
//    ArrayList<domain_courses> courses_list;

    ArrayList<String>list_courses;
    public ListViewAdapter(Context context, ScrollView scrollView, String type)
    {
        this.context=context;
        this.scrollView=scrollView;
        this.type=type;
//        this.courses_list=courses_list;
    }
    public  ListViewAdapter(Context context, ScrollView scrollView, String type, ArrayList<String> list_courses,int a)
    {
        this.context=context;
        this.scrollView=scrollView;
        this.type=type;
        this.list_courses=list_courses;
    }
    @Override
    public int getCount() {
        if(type.equalsIgnoreCase("Domain"))
        return On_going_Courses_Fragment.courses_list.size();
        else
            return list_courses.size();
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
        final int k=i;
        View lview= LayoutInflater.from(context).inflate(R.layout.listview_box,null);
        scrollView.setScrollX(0);
        scrollView.setScrollY(0);
        TextView textView=(TextView)lview.findViewById(R.id.info_text);

        if(type.equalsIgnoreCase("Domain"))
        {
            textView.setText(On_going_Courses_Fragment.courses_list.get(i).getTitle().toString());
            Button mybutton=(Button)lview.findViewById(R.id.mycontinue);
            mybutton.setText("Start Course");
            mybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View  Lview) {
                    Log.e("hrllol","hrllo");
                   Log.e("url","http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/courseinprogress/"+domain_list.nickname+"/"+On_going_Courses_Fragment.courses_list.get(k).getId());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,  "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/courseinprogress/"+domain_list.nickname+"/"+On_going_Courses_Fragment.courses_list.get(k).getId(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("myTag",response);
                                    Log.e("myTag","Successful");
                                    Intent intent=new Intent(context, Profile_Pic.class);
                                    context.startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Mytag","Error");
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(stringRequest);
                }
            });
        }
        else
        {
            textView.setText(list_courses.get(i));
            Button myButton=(Button)lview.findViewById(R.id.mycontinue);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, Profile_Pic.class);
                    context.startActivity(intent);

                }
            });

        }
        return lview;
    }
}
