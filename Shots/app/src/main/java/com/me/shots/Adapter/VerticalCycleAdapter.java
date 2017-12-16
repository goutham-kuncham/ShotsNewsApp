package com.me.shots.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.shorts.jgirish.snu_pro.AsyncTasks.AsyncTasks.GetNewsImageAsync;
//import com.shorts.jgirish.snu_pro.R;
//import com.shorts.jgirish.snu_pro.Utils.Utils.NewsPOGO;
//import com.shorts.jgirish.snu_pro.Utils.Utils.WebViewActivity;

import com.*;
import com.me.shots.AsyncTasks.GetNewsImageAsync;
import com.me.shots.LoginActivity;
import com.me.shots.R;
import com.me.shots.Utils.NewsPOGO;
import com.me.shots.Utils.WebViewActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Half_BlooD PrincE on 10/6/2017.
 */

public class VerticalCycleAdapter extends PagerAdapter {

    Context context;
    GetNewsImageAsync getImage;
    static int like=0;
    static int bookmark=0;

    public VerticalCycleAdapter(Context context)
    {
        this.context=context;
    }

    @Override
    public int getCount() {
        Log.d("tagged", "getCount: "+NewsPOGO.newsArray.size());
        return NewsPOGO.newsArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.fragment_card,container,false);
        ImageView imageView= (ImageView) view.findViewById(R.id.newsImage);
        TextView textView= (TextView) view.findViewById(R.id.newsText);
        TextView title= (TextView) view.findViewById(R.id.newsTitle);
        TextView timestamp= (TextView) view.findViewById(R.id.newsTimeStamp);
        Log.d("MYTag", "instantiateItem: "+position);
        if(position==0) {

            GetNewsImageAsync getImage = new GetNewsImageAsync(context, imageView, position);
            getImage.execute();
            getImage = new GetNewsImageAsync(context, imageView, position+1);
            getImage.execute();
            getImage = new GetNewsImageAsync(context, imageView, position+2);
            getImage.execute();
            getImage = new GetNewsImageAsync(context, imageView, position+3);
            getImage.execute();
            getImage = new GetNewsImageAsync(context, imageView, position+4);
            getImage.execute();
        }
        else if(position+5<NewsPOGO.newsArray.size())
        {
            GetNewsImageAsync getImage = new GetNewsImageAsync(context, imageView, position+5);
            getImage.execute();
            imageView.setImageBitmap(NewsPOGO.newsArray.get(position).news_image);
        }
        else
            imageView.setImageBitmap(NewsPOGO.newsArray.get(position).news_image);

//        imageView.setImageResource(R.mipmap.ic_launcher);
        String timeedit=NewsPOGO.newsArray.get(position).timestamp.substring(0,10)+"    "+NewsPOGO.newsArray.get(position).timestamp.substring(11,18);
        textView.setText(""+NewsPOGO.newsArray.get(position).body);
        title.setText(NewsPOGO.newsArray.get(position).title);
        timestamp.setText(timeedit);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, WebViewActivity.class);
                intent.putExtra("Link",NewsPOGO.newsArray.get(position).link);
                context.startActivity(intent);

            }
        });


        final Button like_btn= (Button) view.findViewById(R.id.like_btn);
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(like==0)
                {
                    Log.e("tttttttttttttttttt","---");
                    like++;
                    //set like red
                    new LikeTask(5+"",1+"",0).execute();
                    like_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.love,0,0);

                }
                else
                {
                    like--;
                    new LikeTask(5+"",1+"",1).execute();
                    like_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.unlove,0,0);

                    //set like white
                }
            }
        });


        final Button bookmark_btn=(Button) view.findViewById(R.id.bookmark_btn);
        bookmark_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bookmark==0)
                {
                    Log.e("tttttttttttttttttt","---");
                    like++;
                    new BookmarkTask(5+"",1+"",0).execute();
                    bookmark_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.bookmark,0,0);

                }
                else
                {
                    like--;

                    new BookmarkTask(5+"",1+"",1).execute();
                    bookmark_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.unbookmark,0,0);
                    //set like white
                }
            }
        });


        container.addView(view);
        return view;

    }




    public class LikeTask extends AsyncTask<Void, Void, String> {

        private final String postID;
        private final String userID;
        String API_URL = "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com";

        LikeTask(String postID, String userID,int upOrDown) {
            this.postID = postID;
            this.userID = userID;
            if(upOrDown==0)
                API_URL=API_URL +"/upvote"+ "/" + postID + "/" + userID;
            else
                API_URL=API_URL +"/undoupvote"+ "/" + postID + "/" + userID;

            Log.e("my","---"+API_URL);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Log.e("my","---doin");

            String line="False";
            try {
                Log.e("my","---"+API_URL);
                URL url = new URL(API_URL);
                Log.e("my","---"+API_URL);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    line = bufferedReader.readLine();
                    Log.e("my","---"+line);
                    bufferedReader.close();

                    return urlConnection.getResponseCode()+"";
                } finally {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                Log.e("my","!!!"+line);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("my","---"+line);
            }

            return "Network Error";
        }


        @Override
        protected void onPostExecute(String s) {
            //mProgressView.setVisibility(View.GONE);
            Toast.makeText(context,"--"+s,Toast.LENGTH_LONG).show();
            //check=s;

        }
    }


    public class BookmarkTask extends AsyncTask<Void, Void, String> {

        private final String postID;
        private final String userID;
        String API_URL = "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com";

        BookmarkTask(String postID, String userID,int upOrDown) {
            this.postID = postID;
            this.userID = userID;
            if(upOrDown==0)
                API_URL=API_URL + "/bookmark"+ "/" + postID + "/" + userID;
            else
                API_URL=API_URL + "/undobookmark" + "/" + postID + "/" + userID;

            Log.e("my","---"+API_URL);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Log.e("my","---doin");

            String line="False";
            try {
                Log.e("my","---"+API_URL);
                URL url = new URL(API_URL);
                Log.e("my","---"+API_URL);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    line = bufferedReader.readLine();
                    Log.e("my","---"+line);
                    bufferedReader.close();

                    return urlConnection.getResponseCode()+"";
                } finally {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                Log.e("my","!!!"+line);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("my","---"+line);
            }

            return "Network Error";
        }


        @Override
        protected void onPostExecute(String s) {
            //mProgressView.setVisibility(View.GONE);
            Toast.makeText(context,"--"+s,Toast.LENGTH_LONG).show();
            //check=s;

        }
    }

}
