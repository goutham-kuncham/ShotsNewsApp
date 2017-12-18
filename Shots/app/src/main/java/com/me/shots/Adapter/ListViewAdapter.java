package com.me.shots.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.Fragments.domain_list;
import com.me.shots.On_going_Courses_Fragment;
import com.me.shots.Profile_Pic;
import com.me.shots.R;
import com.me.shots.Utils.ModulesActivity;
import com.me.shots.domain_courses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by J Girish on 08-10-2017.
 */

public class ListViewAdapter extends BaseAdapter {

    Context context;
    ScrollView scrollView;
    String type;
    String name="",name1="",mip="",mc="",ctitle="";
    String myurl;
    String url1;
    int karma=0;
    Dialog dialog,dialog1;
    String karmaresponse;
    SharedPreferences sharedPreferences;
    int mip_count;
    ArrayList <String> myCourses=new ArrayList<>();                                       //to be taken from shared preferences
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        int check=0;
        sharedPreferences=context.getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        mip=sharedPreferences.getString("mip","lol");
        karma=sharedPreferences.getInt("karma",0);
        mc=sharedPreferences.getString("mc","null");
        mip_count=sharedPreferences.getInt("mip_count",0);

        Log.e("mip",mip);
        StringTokenizer tokenizer=new StringTokenizer(mip,",");
        myCourses.clear();
        while (tokenizer.hasMoreTokens())
        {
            myCourses.add(tokenizer.nextToken());
        }
        final int k=i;
        View lview= LayoutInflater.from(context).inflate(R.layout.listview_box,null);
        scrollView.setScrollX(0);
        scrollView.setScrollY(0);
        final TextView textView=(TextView)lview.findViewById(R.id.info_text);

        if(type.equalsIgnoreCase("Domain")) {
            for (int m = 0; m < myCourses.size(); m++) {
                Log.e("mycheck","me"+myCourses.size());
                if (myCourses.get(m).equals(On_going_Courses_Fragment.courses_list.get(k).getTitle())) {
                    check = 1;
                    break;
                }
            }

            if (check == 0) {
                textView.setText(On_going_Courses_Fragment.courses_list.get(i).getTitle().toString());
                Button mybutton = (Button) lview.findViewById(R.id.mycontinue);
                mybutton.setText("Start Course");
                mybutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View Lview) {
                        dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                        dialog.setContentView(R.layout.loading_dialog);
                        dialog.show();
                        StringTokenizer tokenizer = new StringTokenizer(domain_list.nickname, " ");
                        name = "";
                        ArrayList<String> tokens = new ArrayList<>();
                        while (tokenizer.hasMoreTokens()) {
                            tokens.add(tokenizer.nextToken());
                        }
                        for (int j = 0; j < tokens.size(); j++) {
                            if (j == tokens.size() - 1) {
                                name += tokens.get(j);
                            } else
                                name += tokens.get(j) + "%20";
                        }
                        Log.e("hrllol", "hrllo");
                        Log.e("url", "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/courseinprogress/" + name + "/" + On_going_Courses_Fragment.courses_list.get(k).getId());
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/courseinprogress/" + name + "/" + On_going_Courses_Fragment.courses_list.get(k).getId(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        
                                        Log.e("myTag", response);
                                        Log.e("myTag", "Successful");
                                        Intent intent = new Intent(context, ModulesActivity.class);
                                        Bundle bundle = new Bundle();
                                        ctitle=On_going_Courses_Fragment.courses_list.get(k).getTitle();
                                        
                                        if(mip.contains(ctitle)){}
                                        else {
                                            int mykarma=karma;
                                            bundle.putString("title", On_going_Courses_Fragment.courses_list.get(k).getTitle());
                                            bundle.putString("link", On_going_Courses_Fragment.courses_list.get(k).getUrl());
                                            bundle.putInt("couresid", On_going_Courses_Fragment.courses_list.get(k).getId());
                                            intent.putExtra("mybundle", bundle);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("mip", mip + "," + On_going_Courses_Fragment.courses_list.get(k).getTitle());
                                            editor.putInt("mip_count",mip_count+1);

                                            if (mc.contains(ctitle)) {
                                                Log.e("mytag3", "onResponse: mcccccc= "+mc+"---ctitle= "+ctitle+"------working" );

                                            } else {
                                                Log.e("mytag3", "onResponse: mcccccc= "+mc+"---ctitle= "+ctitle+"---- not working" );
                                                myurl = "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/updatekarma/" + name + "/2";
                                                addkarmapoints(myurl);
                                                mykarma=mykarma+2;
                                            }
                                            Log.e("mytag3", "onResponse: "+myurl );
                                            editor.putInt("karma",mykarma);
                                            editor.commit();
                                        }
                                        Toast.makeText(context,"Course added",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        context.startActivity(intent);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Mytag", "Error");
                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(stringRequest);
                    }
                });
            }
            else
            {
                textView.setText(On_going_Courses_Fragment.courses_list.get(i).getTitle().toString());
                Button myButton=(Button)lview.findViewById(R.id.mycontinue);
                myButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringTokenizer tokenizer=new StringTokenizer(textView.getText().toString()," ");
                        name1="";
                        ArrayList <String> tokens=new ArrayList<>();
                        dialog1 = new Dialog(context,R.style.Theme_AppCompat_Light_Dialog_Alert);
                        dialog1.setContentView(R.layout.loading_dialog);
                        dialog1.show();
                        while (tokenizer.hasMoreTokens())
                        {
                            tokens.add(tokenizer.nextToken());
                        }
                        for(int j=0;j<tokens.size();j++)
                        {
                            if(j==tokens.size()-1)
                            {
                                name1+=tokens.get(j);
                            }
                            else
                                name1+=tokens.get(j)+"%20";
                        }
                        url1="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/courses?q={\"filters\":[{\"name\":\"title\",\"op\":\"eq\",\"val\":\""+name1+"\"}]}";
                        getDetails();

                    }
                });
            }
        }
        else
        {
            textView.setText(list_courses.get(i));
            Button myButton=(Button)lview.findViewById(R.id.mycontinue);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringTokenizer tokenizer=new StringTokenizer(textView.getText().toString()," ");
                    name1="";
                    ArrayList <String> tokens=new ArrayList<>();
                    dialog1 = new Dialog(context,R.style.Theme_AppCompat_Light_Dialog_Alert);
                    dialog1.setContentView(R.layout.loading_dialog);
                    dialog1.show();
                    while (tokenizer.hasMoreTokens())
                    {
                        tokens.add(tokenizer.nextToken());
                    }
                    for(int j=0;j<tokens.size();j++)
                    {
                        if(j==tokens.size()-1)
                        {
                            name1+=tokens.get(j);
                        }
                        else
                            name1+=tokens.get(j)+"%20";
                    }
                    url1="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/courses?q={\"filters\":[{\"name\":\"title\",\"op\":\"eq\",\"val\":\""+name1+"\"}]}";
                     getDetails();

                }
            });

        }
        return lview;
    }

    void addkarmapoints(String myurl)
    {

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, myurl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Respone",response.toString());
                try {
                    Log.e("hii","HII");
                   karmaresponse=response.toString();
                   if(karmaresponse.equalsIgnoreCase("true")){}else {karmaresponse="error";}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error response ",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);

    }


    void getDetails()
    {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url1,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Respone",response.toString());
                try {
                    Log.e("hii","HII");
                    JSONArray obj_array= response.getJSONArray("objects");
                    JSONObject myobj=obj_array.getJSONObject(0);

                    String link=myobj.getString("link");
                    String title=myobj.getString("title");
                    int courseid=myobj.getInt("id");
                    Intent intent=new Intent(context,ModulesActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("title",title);
                    bundle.putString("link",link);
                    bundle.putInt("couresid",courseid);
                    dialog1.dismiss();
                    intent.putExtra("mybundle",bundle);
                    context.startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error response ",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);

    }

}
