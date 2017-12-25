package com.me.shots;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.Adapter.VerticalCycleAdapter;
import com.me.shots.Adapter.VerticalViewPager;
import com.me.shots.Fragments.NewsFragment;
import com.me.shots.Utils.MySingleton;
import com.me.shots.Utils.NewsPOGO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String url,url1,nickname;
    Context context=this;
    Fragment fragment=null;
    static Bitmap pro;

    String email;
    int karma=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences=getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);

        email=sharedPreferences.getString("email",null);
        Log.e("email",email);
        // nickname=sharedPreferences.getString("nickname",null);
        url="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/user?q={\"filters\":[{\"name\":\"email\",\"op\":\"eq\",\"val\":\""+email+"\"}]}";

//        getNews();
//        getLikes();
//        getBookmarks();
        fragment=new NewsFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrame_PlaceHolder,fragment);
        fragmentTransaction.commit();

        Log.d("explore", "oncreate: "+NewsPOGO.newsArray.size());

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
      //  getDetails();
       //  getImage();
    }
    void btnclick(View view)
    {
        //Fragment fragment=null;
        switch (view.getId()){
            case R.id.btn_learn:
                fragment=new On_going_Courses_Fragment();
                break;
            case R.id.btn_explore:
          //      getNews();
                fragment=new NewsFragment();
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



    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(HomeActivity.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.exit_dialogue);
        Button cancel=(Button)dialog.findViewById(R.id.cancel_dia);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button exit=(Button)dialog.findViewById(R.id.exit_dia);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// super.onBackPressed();
                finish();
            }
        });
        dialog.show();

    }


    void getNews()
    {
        String jsonURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/post";
        try {
            String jsonString=new getNewsString().execute(jsonURL).get();
            if(jsonString.equalsIgnoreCase("error"))
            {
                //ERROR FETCHING NEWS!!!
            }
            else
            {
                JSONObject response=new JSONObject(jsonString);
                JSONArray jsonArray=response.getJSONArray("objects");
                if(jsonArray.length()!=NewsPOGO.newsArray.size())
                {
                    NewsPOGO.newsArray.clear();
                    for(int i=jsonArray.length()-1;i>=0;i--)
                    {
                        NewsPOGO newsPOGO;
                        JSONObject jsonObject= jsonArray.getJSONObject(i);

                        newsPOGO=new NewsPOGO();
                        newsPOGO.body=jsonObject.getString("body");
                        newsPOGO.category=jsonObject.getString("category");
                        newsPOGO.content_type=jsonObject.getString("content_type");
                        newsPOGO.id=jsonObject.getInt("id");
                        newsPOGO.image=jsonObject.getString("image");
                        newsPOGO.likes=jsonObject.getInt("likes");
                        newsPOGO.link=jsonObject.getString("link");
                        newsPOGO.timestamp=jsonObject.getString("timestamp");
                        newsPOGO.title=jsonObject.getString("title");
                        newsPOGO.types=jsonObject.getString("types");
                        newsPOGO.user_id=jsonObject.getInt("user_id");
                        NewsPOGO.newsArray.add(newsPOGO);

                    }
                    Log.d("explore", "getNews: "+NewsPOGO.newsArray.size());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    class getNewsString extends AsyncTask<String,Void,String>
    {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];

            Log.e("mytag", "doInBackground: "+"inside aclling"+stringUrl );
            String result="ERROR";
            String inputLine;
            int responsecode=0;
            try{
                URL loginUrl=new URL(stringUrl);
                HttpURLConnection connection =(HttpURLConnection)
                        loginUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.connect();
                responsecode=connection.getResponseCode();
                Log.e("mytag", "doInBackground: "+responsecode );
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();


                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();
                Log.e("mytag","Value==="+stringBuilder.toString());
                result = stringBuilder.toString();
                Log.e("mytag", "doInBackground: "+result );
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("mytag", "doInBackground: "+result );
            if(responsecode==200)
                return result;
            else return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }



//    void getDetails()                                                                                    //personal details
//    {
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("Respone",response.toString());
//                try {
//                    Log.e("hii","HII");
//                    JSONArray obj_array= response.getJSONArray("objects");
//                    JSONObject myobj=obj_array.getJSONObject(0);
//                    nickname=myobj.getString("nickname");
//                    String organization=myobj.getString("organization");
//                    String position=myobj.getString("designation");
//                    String id=Integer.toString(myobj.getInt("id"));
//                    String modules_in_progress=myobj.getString("modules_in_progress");
//                    String modules_completed=myobj.getString("modules_completed");
//                    StringTokenizer mip_tokenizer=new StringTokenizer(modules_in_progress,",");
//                    StringTokenizer mc_tTokenizer=new StringTokenizer(modules_completed,",");
//                    int mip_count=mip_tokenizer.countTokens();
//                    int mc_count=mc_tTokenizer.countTokens();
//                    String karmapoints=myobj.getString("karma")+"";
//                    if(karmapoints.equalsIgnoreCase("null")) karma=0;
//                    else
//                     karma=myobj.getInt("karma");
//
//                    Log.e("mytag1", "onResponse: karmaaaaaaaaaaaaaaaaaaaaaaaaaaaa "+karma+"====="+karmapoints );
//                    SharedPreferences.Editor editor=sharedPreferences.edit();
//                    editor.putString("userid",id);
//                    editor.putString("nickname",nickname);
//                    editor.putString("organizationname",organization);
//                    editor.putString("designation",position);
//                    editor.putString("mip",modules_in_progress);
//                    editor.putString("mc",modules_completed);
//                    editor.putInt("mip_count",mip_count);
//                    editor.putInt("mc_count",mc_count);
//                    editor.putInt("karma",karma);
//                    editor.apply();
//                    Log.e("nickname",nickname+"lol");
//                    Log.e("nickname_orga",organization+"lol");
//                    Log.e("nickname_posi",position+"Lol");
//                    Log.e("nickname_id",id+"LOL");
//                   getOrgaDetails();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context,"Error response ",Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue queue= Volley.newRequestQueue(context);
//        queue.add(jsonObjectRequest);
//
//
//
//    }
//
//
//    void getOrgaDetails()                                                                                 //organization details
//    {
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,"http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/organization",null, new Response.Listener<JSONObject>() {               //if you want to edit the organisation name in future use organisation/ followed by filter using organization_id obtained in getDetails
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("Respone",response.toString());
//                try {
//                    Log.e("hii","HII");
//                    JSONArray obj_array= response.getJSONArray("objects");
//                    JSONObject myobj=obj_array.getJSONObject(0);
//                    String organization_name=myobj.getString("name");
//                    String body=myobj.getString("body");
//                    String ceo=myobj.getString("ceo");
//                    String cto=myobj.getString("cto");
//                    Log.e("Cto",cto+"lol");
//                    SharedPreferences.Editor editor=sharedPreferences.edit();
//                    editor.putString("orga_name",organization_name);
//                    editor.putString("orga_body",body);
//                    editor.putString("orga_ceo",ceo);
//                    editor.putString("orga_cto",cto);
//                    editor.apply();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context,"Error response ",Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue queue= Volley.newRequestQueue(context);
//        queue.add(jsonObjectRequest);
//    }
//
//
//    void getImage()                                                                                 //profile pic
//    {
//        ImageRequest request = new ImageRequest("http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/userdata/"+email+"/thumb.png",     ///"+email+" in btw userdata/  /thumb.png
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap bitmap) {
//                        pro=bitmap;
//                        Log.e("mytag","Saved propic"+pro);
//
//                    }
//                }, 0, 0, null,
//                new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError error) {
//                        // mImageView.setImageResource(R.drawable.image_load_error);
//                        Log.e("Home_Acitivity","No img found");
//                    }
//                });
////        MySingleton.getMyInstance(getApplicationContext()).addToReqQue(request);
//        RequestQueue queue= Volley.newRequestQueue(context);
//        queue.add(request);
//    }
//
//
//    void getNewsVolley() {
//        Context context=getApplicationContext();
//        String resp="";
//        String jsonURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/post";
//        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, jsonURL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    ArrayList<NewsPOGO> newsArray =new ArrayList<>();
//                    Log.d("tagged", "onResponse: "+response.toString());
//                    JSONArray jsonArray=response.getJSONArray("objects");
//                    for(int i=0;i<jsonArray.length();i++)
//                    {
//                        NewsPOGO newsPOGO;
//                        JSONObject jsonObject= jsonArray.getJSONObject(i);
//
//                        newsPOGO=new NewsPOGO();
//                        newsPOGO.body=jsonObject.getString("body");
//                        newsPOGO.category=jsonObject.getString("category");
//                        newsPOGO.content_type=jsonObject.getString("content_type");
//                        newsPOGO.id=jsonObject.getInt("id");
//                        newsPOGO.image=jsonObject.getString("image");
//                        newsPOGO.likes=jsonObject.getInt("likes");
//                        newsPOGO.link=jsonObject.getString("link");
//                        newsPOGO.timestamp=jsonObject.getString("timestamp");
//                        newsPOGO.title=jsonObject.getString("title");
//                        newsPOGO.types=jsonObject.getString("types");
//                        newsPOGO.user_id=jsonObject.getInt("user_id");
//                        newsArray.add(newsPOGO);
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                String resp=error.toString();
//                Log.d("tagged", "onResponse: "+error.toString());
//                //name.setText("ERROR...");
//                Toast.makeText(getApplicationContext(), "Error Fetching News", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue queue= Volley.newRequestQueue(context);
//        queue.add(jsonObjectRequest);
//    }
//
//
//
//
//

}
