package com.me.shots.Utils;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toolbar;

//import com.shorts.jgirish.snu_pro.R;
import com.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.me.shots.R;

import org.json.JSONObject;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    private MenuItem menuItem;
    String Link;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Link=getIntent().getStringExtra("Link");
        webView=(WebView)findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(Link);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        // remove the following flag for version < API 19
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                menuItem=item;
                menuItem.setActionView(R.layout.progressbar_webview);
                menuItem.expandActionView();
                webView.loadUrl(Link);
                webView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView view, String url) {
                        menuItem.collapseActionView();
                        menuItem.setActionView(null);
                    }
                });
                break;

            case android.R.id.home:
                this.finish();
                break;

            case R.id.action_copy:
                setClipboard(getApplicationContext(),Link);
                break;

            case R.id.action_browser:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
                break;

            default:
                break;
        }
        return true;
    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    String generateURL()
    {
        sharedPreferences=getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        String nickname=sharedPreferences.getString("nickname","null");
        nickname=nickname.replace(" ","%20");
        String myurl="http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/updatekarma/" +nickname+ "/3";
        return myurl;
    }

    void addkarmapoints()
    {
        String myurl=generateURL();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, myurl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Respone",response.toString());
                try {
                    Log.e("hii","HII");
                    String karmaresponse=response.toString();
                    if(karmaresponse.equalsIgnoreCase("true"))
                    {
                        int karma=sharedPreferences.getInt("karma",1);
                        karma+=3;
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putInt("karma",karma);
                        editor.apply();
                    }
                    else {karmaresponse="error";}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //              Toast.makeText(getApplicationContext(),"Error response ",Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("Karma Url", "addkarmapoints: "+myurl);
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);

    }
}
