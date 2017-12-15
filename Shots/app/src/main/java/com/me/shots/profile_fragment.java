package com.me.shots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by J Girish on 09-10-2017.
 */

public class profile_fragment extends Fragment {

    SharedPreferences sharedPreferences;

    public profile_fragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment0

        sharedPreferences=getActivity().getSharedPreferences("MYSHAREDPREFERENCES", Context.MODE_PRIVATE);
        String uname=sharedPreferences.getString("username",null);
        String nickname=sharedPreferences.getString("nickname","Elon Musk");
        String designation=sharedPreferences.getString("designation","CEO & CTO");
        String organizationname=sharedPreferences.getString("organizationname","SpaceX Tech.");

        Log.e("mytag","nickname==="+nickname);

        //return inflater.inflate(R.layout.fragment_calls, container, false);
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        TextView name=(TextView)view.findViewById(R.id.name);
        TextView position=(TextView)view.findViewById(R.id.position);
        TextView company=(TextView)view.findViewById(R.id.company);
        TextView MIP=(TextView)view.findViewById(R.id.MIP);
        TextView MC=(TextView)view.findViewById(R.id.MC);
        TextView KP=(TextView)view.findViewById(R.id.KP);
        TextView domain=(TextView)view.findViewById(R.id.domain);
        TextView bookmarks=(TextView)view.findViewById(R.id.bookmarks);
        TextView notifications=(TextView)view.findViewById(R.id.notifications);
        TextView AYO=(TextView)view.findViewById(R.id.AYO);
        TextView Logout=(TextView)view.findViewById(R.id.Logout);
        ImageView imageView =(ImageView)view.findViewById(R.id.pro_pic);

        name.setText(nickname);
        position.setText(designation);
        company.setText(organizationname);

        domain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Domain",Toast.LENGTH_SHORT).show();
            }
        });
       imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),Profile_Pic.class);
                startActivity(intent);




            }
        });

        return view;
    }
}