package com.me.shots;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.Fragments.NewsFragment;
import com.me.shots.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String url,url1,nickname;
    Context context=this;
    Fragment fragment=null;
    static Bitmap pro;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences=getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);

        email=sharedPreferences.getString("email",null);
        Log.e("email",email);
        // nickname=sharedPreferences.getString("nickname",null);
        url="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/user?q={\"filters\":[{\"name\":\"email\",\"op\":\"eq\",\"val\":\""+email+"\"}]}";


        //   new Profile_Details().execute();


        Button learn=(Button)findViewById(R.id.btn_learn);
        Button explore=(Button)findViewById(R.id.btn_explore);
        Button profile=(Button)findViewById(R.id.btn_profile);
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick(view);
            }
        });
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick(view);
            //    getDetails();
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick(view);
          //  getDetails();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick(view);
             //  getDetails();

            }
        });
        getDetails();
        getImage();

}
void getDetails()                                                                                    //personal details
{
    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.e("Respone",response.toString());
            try {
                Log.e("hii","HII");
                JSONArray obj_array= response.getJSONArray("objects");
                JSONObject myobj=obj_array.getJSONObject(0);
                nickname=myobj.getString("nickname");
                String organization=myobj.getString("organization");
                String position=myobj.getString("designation");
                String id=Integer.toString(myobj.getInt("id"));
                String modules_in_progress=myobj.getString("modules_in_progress");
                int karma=myobj.getInt("karma");
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("userid",id);
                editor.putString("nickname",nickname);
                editor.putString("organizationname",organization);
                editor.putString("designation",position);
                editor.putString("mip",modules_in_progress);
                editor.putInt("karma",karma);
                editor.apply();
                Log.e("nickname",nickname+"lol");
                Log.e("nickname_orga",organization+"lol");
                Log.e("nickname_posi",position+"Lol");
                Log.e("nickname_id",id+"LOL");
               getOrgaDetails();
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
void getOrgaDetails()                                                                                 //organization details
{
    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,"http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/organization",null, new Response.Listener<JSONObject>() {               //if you want to edit the organisation name in future use organisation/ followed by filter using organization_id obtained in getDetails
        @Override
        public void onResponse(JSONObject response) {
            Log.e("Respone",response.toString());
            try {
                Log.e("hii","HII");
                JSONArray obj_array= response.getJSONArray("objects");
                JSONObject myobj=obj_array.getJSONObject(0);
                String organization_name=myobj.getString("name");
                String body=myobj.getString("body");
                String ceo=myobj.getString("ceo");
                String cto=myobj.getString("cto");
                Log.e("Cto",cto+"lol");
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("orga_name",organization_name);
                editor.putString("orga_body",body);
                editor.putString("orga_ceo",ceo);
                editor.putString("orga_cto",cto);
                editor.apply();
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
    void getImage()                                                                                 //profile pic
    {
        ImageRequest request = new ImageRequest("http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/userdata/"+email+"/thumb.png",     ///"+email+" in btw userdata/  /thumb.png
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        pro=bitmap;
                        Log.e("mytag","Saved propic"+pro);

                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        // mImageView.setImageResource(R.drawable.image_load_error);
                        Log.e("Home_Acitivity","No img found");
                    }
                });
//        MySingleton.getMyInstance(getApplicationContext()).addToReqQue(request);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(request);


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


}
