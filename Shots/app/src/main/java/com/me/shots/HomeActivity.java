package com.me.shots;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.Fragments.NewsFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String url;
    Context context=this;
    Fragment fragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences=getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);

        String email=sharedPreferences.getString("email",null);

        url="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/user?q={\"filters\":[{\"name\":\"email\",\"op\":\"eq\",\"val\":"+email+"}]}";

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrame_PlaceHolder,new NewsFragment(this));
        fragmentTransaction.commit();

//        fragment=new NewsFragment(this);
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.homeFrame_PlaceHolder,fragment);
//        fragmentTransaction.commit();

        Button learn=(Button)findViewById(R.id.btn_learn);
        Button explore=(Button)findViewById(R.id.btn_explore);
        Button profile=(Button)findViewById(R.id.btn_profile);
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick(view);
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick(view);
                new Profile_Details().execute();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick(view);
                new Profile_Details().execute();
            }
        });

    }
    void btnclick(View view){
        //Fragment fragment=null;
        switch (view.getId()){
            case R.id.btn_learn:
                fragment=new On_going_Courses_Fragment();
                break;
            case R.id.btn_explore:
                fragment=new NewsFragment(this);
                break;
            case R.id.btn_profile:
                fragment=new profile_fragment();
                break;

        }
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrame_PlaceHolder,fragment);
        fragmentTransaction.commit();

    }

    class Profile_Details extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Respone",response.toString());
                        try {
                            String userid= (String) response.getString("id");
                            String nickname=response.getString("nickname");
                            String organization=response.getString("organization");
                            String position=response.getString("designation");
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("userid",userid);
                            editor.putString("nickname",nickname);
                            editor.putString("organizationname",organization);
                            editor.putString("designation",position);

                            editor.apply();
                            Log.e("mytag","homeee nickname==="+nickname);


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



            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

    }

}
