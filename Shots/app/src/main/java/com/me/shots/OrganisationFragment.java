package com.me.shots;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.me.shots.GoogleAnalytics.MyApplication;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrganisationFragment extends Fragment {


    public OrganisationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MyApplication.getInstance().trackScreenView("About Organisation Fragment");

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MYSHAREDPREFERENCES", Context.MODE_PRIVATE);
        String o_n=sharedPreferences.getString("orga_name","lol");
        String o_ceo=sharedPreferences.getString("orga_ceo","lol");
        String o_cto=sharedPreferences.getString("orga_cto","lol");
        String o_body=sharedPreferences.getString("orga_body","lol");
        View view=inflater.inflate(R.layout.fragment_organisation, container, false);

        Button backbtn= (Button) view.findViewById(R.id.back_btnorg);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                fm.popBackStack();

            }
        });
        TextView orga_name=(TextView)view.findViewById(R.id.orga_name);
        orga_name.setText(o_n);
        TextView orga_ceo=(TextView)view.findViewById(R.id.orga_ceo);
        orga_ceo.setText("CEO  - "+o_ceo);
        TextView orga_cto=(TextView)view.findViewById(R.id.orga_cto);
        orga_cto.setText("CTO  - "+o_cto);
        TextView orga_body=(TextView)view.findViewById(R.id.orga_body);
        orga_body.setText(o_body);
        return view;
    }

}
