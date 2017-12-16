package com.me.shots;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.Adapter.DomainListAdapter;
import com.me.shots.Adapter.ListViewAdapter;
import com.me.shots.Fragments.domain_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by J Girish on 09-10-2017.
 */

public class On_going_Courses_Fragment extends android.support.v4.app.Fragment {
 Context context;
    public static ArrayList<domain_courses> courses_list=new ArrayList<>();
    SharedPreferences sharedPreferences;
    public On_going_Courses_Fragment (){
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment0


        //return inflater.inflate(R.layout.fragment_calls, container, false);
        View view=inflater.inflate(R.layout.on_going_courses_fragment, container, false);
        sharedPreferences=this.getActivity().getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        String mip=sharedPreferences.getString("mip","lol");
        ArrayList <String> in_progress_courses=new ArrayList<>();
        TextView promt=(TextView)view.findViewById(R.id.no_courses);
        StringTokenizer st = new StringTokenizer(mip,",");
        while (st.hasMoreTokens()) {
            in_progress_courses.add(st.nextElement().toString());
        }
        if(in_progress_courses.size()==0)
        {
            in_progress_courses.clear();

            promt.setVisibility(View.VISIBLE);
        }
        else
        {
            promt.setVisibility(View.INVISIBLE);
        }

        ScrollView sv = (ScrollView)view.findViewById(R.id.scrollView1);
        sv.setScrollX(0);
        sv.setScrollY(0);
        ListView listView = (ListView)view.findViewById(R.id.list_display);
        ListViewAdapter listAdapter = new ListViewAdapter(getActivity(),sv,"On going",in_progress_courses,0);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Button mycontinue=(Button)view.findViewById(R.id.mycontinue);

            }
        });

        ListView domainList=(ListView)view.findViewById(R.id.dlist_display);
        DomainListAdapter domainListAdapter=new DomainListAdapter(getActivity());
        domainList.setAdapter(domainListAdapter);
        domainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView=(TextView)view.findViewById(R.id.mydomain);
                //  Toast.makeText(getContext(),textView.getText(),Toast.LENGTH_SHORT).show();
                String course_name="";
                StringTokenizer tokenizer=new StringTokenizer(textView.getText().toString()," ");
                ArrayList <String> tokens=new ArrayList<>();
                while (tokenizer.hasMoreTokens())
                {
                    tokens.add(tokenizer.nextToken());
                }
                for(int j=0;j<tokens.size();j++)
                {
                    if(j==tokens.size()-1)
                    {
                        course_name+=tokens.get(j);
                    }
                    else
                        course_name+=tokens.get(j)+"%20";
                }
                Log.e("mytag","http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/courses?q={\"filters\":[{\"name\":\"domain\",\"op\":\"eq\",\"val\":\"" +course_name+ "\"}]}");
               String url="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/courses?q={\"filters\":[{\"name\":\"domain\",\"op\":\"eq\",\"val\":\""+course_name+"\"}]}";
                final Fragment fragment=new domain_list();
                final Bundle args = new Bundle();
                args.putString("Domain_Name", textView.getText().toString());
                FragmentManager fragmentManager=getFragmentManager();
                final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.homeFrame_PlaceHolder,fragment);

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Respone",response.toString());
                        try {
                            courses_list.clear();
                            JSONArray myarry=response.getJSONArray("objects");
                            for(int i=0;i<myarry.length();i++)
                            {
                                JSONObject myobject=myarry.getJSONObject(i);
                                domain_courses ncourse=new domain_courses();
                                ncourse.setDomain(myobject.getString("domain"));
                                ncourse.setId(myobject.getInt("id"));
                                if(myobject.getString("in_progress_count").equals("null"))
                                {
                                    ncourse.setIn_progress_count(0);
                                }
                                else
                                ncourse.setIn_progress_count(myobject.getInt("in_progress_count"));
                                ncourse.setTitle(myobject.getString("title"));
                                ncourse.setUrl(myobject.getString("link"));
                                courses_list.add(ncourse);
                            }
                            args.putSerializable("Domain_Courses_List",courses_list);
                            Log.e("mytag",courses_list.size()+"lol.l.");
                            fragment.setArguments(args);
                            fragmentTransaction.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Error response ",Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue queue= Volley.newRequestQueue(getContext());
                queue.add(jsonObjectRequest);

                //fragmentTransaction.commit();

            }
        });

        return view;

    }
}
