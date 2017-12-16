package com.me.shots;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.me.shots.Utils.MySingleton;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by J Girish on 09-10-2017.
 */

public class profile_fragment extends Fragment {
    String nickname;
    CircleImageView imageView;
    SharedPreferences sharedPreferences;
    public profile_fragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment0

        sharedPreferences=getActivity().getSharedPreferences("MYSHAREDPREFERENCES", Context.MODE_PRIVATE);
        String uname=sharedPreferences.getString("username",null);
        nickname=sharedPreferences.getString("nickname","Elon Musk");
        String designation=sharedPreferences.getString("designation","CEO & CTO");
        String organizationname=sharedPreferences.getString("organizationname","SpaceX Tech.");
        String profilepic=sharedPreferences.getString("profilepic",null);
        int karma=sharedPreferences.getInt("karma",1);
        Log.e("mytag","nickname==="+nickname);
        Log.e("mytag","Profilepic==="+profilepic+"lol");

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
        imageView =(CircleImageView)view.findViewById(R.id.SetPic);
        KP.setText(karma+"");
        name.setText(nickname);
        position.setText(designation);
        company.setText(organizationname);
        AYO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new OrganisationFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.homeFrame_PlaceHolder,fragment);
                fragmentTransaction.commit();
            }
        });
        domain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Domain",Toast.LENGTH_SHORT).show();
            }
        });
//       imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getContext(),Profile_Pic.class);
//                startActivity(intent);
//
//
//
//
//            }
//        });
        while (HomeActivity.pro==null)
        {
        }
           imageView.setImageBitmap(HomeActivity.pro);

        return view;
    }


}