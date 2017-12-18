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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.me.shots.Adapter.BookmarksAdapter;
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
        String profilepic=sharedPreferences.getString("profile_pic",null);
        int mip_count=sharedPreferences.getInt("mip_count",0);
        Log.e("mip_count",mip_count+"");

        int mc_count=sharedPreferences.getInt("mc_count",0);
        Log.e("mc_count",mc_count+"");
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
        byte [] encodeByte=Base64.decode(profilepic,Base64.DEFAULT);
        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        imageView =(CircleImageView)view.findViewById(R.id.SetPic);
        imageView.setImageBitmap(bitmap);
        KP.setText(karma+"");
        MIP.setText(mip_count+"");
        MC.setText(mc_count+"");
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
        bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), BookmarksActivity.class);
                startActivity(intent);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Notification",Toast.LENGTH_SHORT).show();
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
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext(),R.style.Theme_AppCompat_Light_Dialog_Alert);
                dialog.setContentView(R.layout.exit_dialogue);
                TextView text=(TextView)dialog.findViewById(R.id.text_dia);
                text.setText("Do you really whish to Logout?");
                Button cancel=(Button)dialog.findViewById(R.id.cancel_dia);
                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                Button exit=(Button)dialog.findViewById(R.id.exit_dia);
                exit.setText("Logout");
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // super.onBackPressed();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        getActivity().finish();
                        Intent intent=new Intent(getContext(),TestLoginActivity.class);
                        startActivity(intent);

                    }
                });
                dialog.show();
            }
        });
//        while (HomeActivity.pro==null)
//        {
//        }
//           imageView.setImageBitmap(HomeActivity.pro);

        return view;
    }


}