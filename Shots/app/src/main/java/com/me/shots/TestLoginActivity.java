package com.me.shots;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class TestLoginActivity extends AppCompatActivity {

    String LOGIN_API_URL = "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/auth";
    String LOGIN_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);

        Button loginbtn= (Button)findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    private void login() {

        AutoCompleteTextView usernametxt= (AutoCompleteTextView) findViewById(R.id.username);
        String username=usernametxt.getText().toString();

        EditText passwordtxt=(EditText)findViewById(R.id.password);
        String password=passwordtxt.getText().toString();
            String API_URL=LOGIN_API_URL+"/"+username+"/"+password;

        Log.e("mytag","1");

        if(isNetworkAvailable())
        {
            Log.e("mytag","2");

            try {
                Log.e("mytag","3");

                LOGIN_result=new SignInValidation().execute(API_URL).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Toast.makeText(this,"Value -"+LOGIN_result,Toast.LENGTH_LONG).show();
            if(LOGIN_result.equals("False"))
            {
                usernametxt.setError("Wrong Credentials..");
                passwordtxt.setError("Wrong Credentials..");

            }else {
                usernametxt.setError(null);
                passwordtxt.setError(null);
                Intent homeintent =new Intent(this,HomeActivity.class);
                    startActivity(homeintent);

            }

        }
        else
        {
            Log.e("mytag","3");
            Toast.makeText(this,"Network Not available",Toast.LENGTH_LONG).show();

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

}
