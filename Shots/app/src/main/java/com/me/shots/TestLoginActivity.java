package com.me.shots;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class TestLoginActivity extends AppCompatActivity {

    String LOGIN_API_URL = "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/auth";
    String LOGIN_result;
    String nickname,url;
    Bitmap pro;
    String email;
    int karma;
    Dialog dialog;
    //int count=0;
    String name,pass;
    SharedPreferences sharedPreferences;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    private int progressBarStatus = 0;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);

        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        sharedPreferences=getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
          name=sharedPreferences.getString("email",null);
       pass=sharedPreferences.getString("password",null);


        if (!(name == null || pass == null)) {

            Log.e("mytag","sharedprefworking======"+name+"-----"+pass);
            url="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/user?q={\"filters\":[{\"name\":\"email\",\"op\":\"eq\",\"val\":\""+name+"\"}]}";
            getDetails();
            getImage();
            getNews();
            getLikes();
            getBookmarks();
            email=name;
           // if(count==3) {
                Intent homeint = new Intent(this, HomeActivity.class);
                startActivity(homeint);
                finish();
          //  }
//            else{
//                Log.e("lol","lol");
//            }
        }
            else {
            Log.e("mytag","sharedprefnottttttttworking======"+name+"-----"+pass);

             Button loginbtn = (Button) findViewById(R.id.loginbtn);
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("LoginTest", "onClick: ");
                    v.startAnimation(buttonClick);
                    Log.d("LoginTest", "onClick: #2");


                    // loginbtn.setBackgroundColor(Color.BLUE);

                 //   progressBar.setVisibility(View.VISIBLE);

//                    dialog = new Dialog(TestLoginActivity.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
//                    dialog.setContentView(R.layout.loading_dialog);
//                    dialog.show();
                    login();
                }
            });
        }

    }

    private void login() {

        AutoCompleteTextView usernametxt= (AutoCompleteTextView) findViewById(R.id.username);
        String username=usernametxt.getText().toString().trim();

        EditText passwordtxt=(EditText)findViewById(R.id.password);
        String password=passwordtxt.getText().toString();
            String API_URL=LOGIN_API_URL+"/"+username+"/"+password;

        Log.e("mytag","1");


        if(isNetworkAvailable())
        {
            {
                Log.e("mytag", "isNetworkAvailable");
                try {
                    LOGIN_result = new SignInValidation().execute(API_URL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "Value -" + LOGIN_result, Toast.LENGTH_LONG).show();
                if (LOGIN_result.equals("False")||LOGIN_result.equalsIgnoreCase("error")) {
                    usernametxt.setError("Wrong Credentials..");
                    passwordtxt.setError("Wrong Credentials..");
                     //dialog.dismiss();
                    //progressBar.setVisibility(View.GONE);
                } else {
                    usernametxt.setError(null);
                    passwordtxt.setError(null);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("email",username);
                    editor.putString("password",password);
                    editor.apply();
                    email=username;
                    url="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/user?q={\"filters\":[{\"name\":\"email\",\"op\":\"eq\",\"val\":\""+email+"\"}]}";
                    Log.e("url",url);
                    getDetails();
                    getImage();
                    getNews();
                    getLikes();
                    getBookmarks();
                //    if(count==3) {
                        Intent homeint = new Intent(this, HomeActivity.class);
                      //  dialog.dismiss();
                       progressBar.setVisibility(View.GONE);
                        startActivity(homeint);
                        finish();
//                    }
//                    else{
//                        Log.e("lol","lol");
//                    }
//                    Intent homeintent = new Intent(this, HomeActivity.class);
//                    startActivity(homeintent);
//                    finish();
                }

            }
        }
        else
            {
                Log.e("mytag", "3");
                Toast.makeText(this, "Network Not available", Toast.LENGTH_LONG).show();
            }

    }

    private boolean isNetworkAvailable() {
        Log.e("mytag","4");
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.e("mytag","5");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.e("mytag","6");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                    String modules_completed=myobj.getString("modules_completed");
                    StringTokenizer mip_tokenizer=new StringTokenizer(modules_in_progress,",");
                    StringTokenizer mc_tTokenizer=new StringTokenizer(modules_completed,",");
                    int mip_count=mip_tokenizer.countTokens();
                    int mc_count=mc_tTokenizer.countTokens();
                    String karmapoints=myobj.getString("karma")+"";
                    String interests=myobj.getString("interests");
                    String domain=myobj.getString("domain");
                    if(karmapoints.equalsIgnoreCase("null")) karma=0;
                    else
                        karma=myobj.getInt("karma");

                    Log.e("mytag1", "onResponse: karmaaaaaaaaaaaaaaaaaaaaaaaaaaaa "+karma+"====="+karmapoints );
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("userid",id);
                    editor.putString("nickname",nickname);
                    editor.putString("organizationname",organization);
                    editor.putString("designation",position);
                    editor.putString("mip",modules_in_progress);
                    editor.putString("mc",modules_completed);
                    editor.putInt("mip_count",mip_count);
                    editor.putInt("mc_count",mc_count);
                    editor.putInt("karma",karma);
                    editor.putString("interests",interests);
                    editor.putString("mydomain",domain);
                    editor.apply();
                    Log.e("nickname",nickname+"lol");
                    Log.e("nickname_orga",organization+"lol");
                    Log.e("nickname_posi",position+"Lol");
                    Log.e("nickname_id",id+"LOL");
                    //count++;
                    getOrgaDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TestLoginActivity.this,"Error response ",Toast.LENGTH_SHORT).show();
                //count++;
            }
        });
        RequestQueue queue= Volley.newRequestQueue(TestLoginActivity.this);
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
                    //count++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TestLoginActivity.this,"Error response ",Toast.LENGTH_SHORT).show();
                //count++;
            }
        });
        RequestQueue queue= Volley.newRequestQueue(TestLoginActivity.this);
        queue.add(jsonObjectRequest);
    }


    void getImage()                                                                                 //profile pic
    {
        ImageRequest request = new ImageRequest("http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/static/userdata/"+email+"/thumb.png",     ///"+email+" in btw userdata/  /thumb.png
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        pro=bitmap;
                        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                        byte [] b=baos.toByteArray();
                        String temp= Base64.encodeToString(b, Base64.DEFAULT);

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("profile_pic",temp);
                        editor.commit();
                        //   Log.e("mytag","Saved propic"+pro);
                        //count++;
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        // mImageView.setImageResource(R.drawable.image_load_error);
                        Log.e("Home_Acitivity","No img found");
                        //count++;
                    }
                });
//        MySingleton.getMyInstance(getApplicationContext()).addToReqQue(request);
        RequestQueue queue= Volley.newRequestQueue(TestLoginActivity.this);
        queue.add(request);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */


    class SignInValidation extends AsyncTask<String,Void,String>
    {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];

            String result="ERROR";
            String inputLine;
            try{
                URL loginUrl=new URL(stringUrl);
                HttpURLConnection connection =(HttpURLConnection)
                        loginUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.connect();

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

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(result.length()<6)
            return result;
            else
                return null;
        }

        @Override
        protected void onPostExecute(String s) {
                super.onPostExecute(s);
        }
    }
    void getNews()
    {
        String jsonURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/post";
        try {
            String jsonString=new getNewsString().execute(jsonURL).get();
            if(jsonString.equalsIgnoreCase("error"))
            {
                //ERROR FETCHING NEWS!!!
                //count++;
            }
            else
            {
                //count++;
                JSONObject response=new JSONObject(jsonString);
                JSONArray jsonArray=response.getJSONArray("objects");
                if(jsonArray.length()!= NewsPOGO.newsArray.size())
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
                        newsPOGO.notification_worthy=jsonObject.getString("notification_worthy");
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


    void getLikes()
    {
        String jsonURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/like?q={\"filters\":[{\"name\":\"user_email\",\"op\":\"eq\",\"val\":\""+name+"\"}]}";
        try {
            String jsonString=new getNewsString().execute(jsonURL).get();
            if(jsonString.equalsIgnoreCase("error"))
            {
                //ERROR FETCHING Likes Details!!!
                //count++;
            }
            else
            {
                //count++;
                JSONObject response=new JSONObject(jsonString);
                JSONArray jsonArray=response.getJSONArray("objects");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    int post_id=jsonObject.getInt("post_id");
                    for(int j=0;j<NewsPOGO.newsArray.size();j++)
                    {
                        if(post_id==NewsPOGO.newsArray.get(j).id)
                        {
                            NewsPOGO.newsArray.get(j).liked=true;
                            break;
                        }
                    }
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

    void getBookmarks()
    {

        String jsonURL="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/bookmark?q={\"filters\":[{\"name\":\"user_email\",\"op\":\"eq\",\"val\":\""+name+"\"}]}";
        try {

            String jsonString=new getNewsString().execute(jsonURL).get();
            if(jsonString.equalsIgnoreCase("error"))
            {
                //ERROR FETCHING Likes Details!!!
                //count++;
            }
            else
            {
                //count++;
                JSONObject response=new JSONObject(jsonString);
                JSONArray jsonArray=response.getJSONArray("objects");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    int post_id=jsonObject.getInt("post_id");
                    for(int j=0;j<NewsPOGO.newsArray.size();j++)
                    {
                        if(post_id==NewsPOGO.newsArray.get(j).id)
                        {
                            NewsPOGO.newsArray.get(j).bookmarked=true;
                            break;
                        }
                    }
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


}
