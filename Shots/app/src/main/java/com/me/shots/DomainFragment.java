package com.me.shots;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.shots.Adapter.FullDomainListAdapter;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DomainFragment extends Fragment {

    SharedPreferences sharedPreferences;
  String mydomain,interests;
    public DomainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        sharedPreferences=this.getActivity().getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        interests=sharedPreferences.getString("interests","lol");
        mydomain=sharedPreferences.getString("mydomain","Nodomain");
        final ArrayList<String> my_interests=new ArrayList<>();
        StringTokenizer st1=new StringTokenizer(interests,",");





        my_interests.add(mydomain);
        if(mydomain.equals("Nodomain")||mydomain.equals("null"))
        {
            mydomain="Programming";
        }
        while (st1.hasMoreTokens())
        {
            my_interests.add(st1.nextToken());
        }
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_domain, container, false);

        Button backbt= (Button) view.findViewById(R.id.bac_btn);
        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm=getActivity().getSupportFragmentManager();
                fm.popBackStack();

            }
        });

        ListView list=(ListView)view.findViewById(R.id.domainlist_display);
        FullDomainListAdapter adapter=new FullDomainListAdapter(getContext());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView add=(ImageView)view.findViewById(R.id.AddRemove);
                TextView textView=(TextView)view.findViewById(R.id.mydomain);
                int count=0;
                for (int j=0;j<my_interests.size();j++)
                {
                    if(textView.getText().toString().equals(mydomain))
                    {
                        Toast.makeText(getContext(),textView.getText().toString()+" is your Core Domain",Toast.LENGTH_SHORT).show();
                        count++;
                        break;
                    }
                   else if(textView.getText().toString().equals(my_interests.get(j)))
                    {
                        String interests1="";
                        count++;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Log.e("interests",interests1+"lol");
                        my_interests.remove(j);
                        Log.e("interests",interests1+"lol");
                        for(int k=1;k<my_interests.size();k++)
                        {
                             interests1+=my_interests.get(k)+",";
                            Log.e("interests",my_interests.get(k));
                        }
                        Log.e("interest afchange1",interests1);
                        editor.putString("interests",interests1);
                        add.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(),textView.getText().toString()+" has been removed from your Interests",Toast.LENGTH_SHORT).show();
                        editor.commit();
                        break;
                    }
                }
                if(count==0)
                {
                 String interests2=sharedPreferences.getString("interests","lol");
                    Log.e("interest bfchange123",interests2);
                    add.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Log.e("interest afchange123",interests2);
                    my_interests.add(textView.getText().toString());
                    Log.e("interest afchange123",interests2);
                    editor.putString("interests",interests2+textView.getText().toString()+",");
                    Toast.makeText(getContext(),textView.getText().toString()+" added to your Interests",Toast.LENGTH_SHORT).show();
                    editor.commit();
                }
              //  add.setVisibility(View.VISIBLE);
            }
        });
        return  view;
    }



}
