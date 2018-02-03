package com.me.shots.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.shots.R;
import com.me.shots.Utils.NewsPOGO;
import com.me.shots.Utils.WebViewActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Hi on 19-Dec-17.
 */

public class BookmarksAdapter extends PagerAdapter {

    ArrayList<NewsPOGO> bookmarks=new ArrayList<>();
    Context context;

    public BookmarksAdapter(Context context)
    {
        for(int i=0;i<NewsPOGO.newsArray.size();i++)
        {
            if(NewsPOGO.newsArray.get(i).bookmarked)
            {
                bookmarks.add(NewsPOGO.newsArray.get(i));
            }
        }
        this.context=context;
    }



    @Override
    public int getCount() {
        return bookmarks.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.fragment_card,container,false);
        TextView textView= (TextView) view.findViewById(R.id.newsText);
        TextView title= (TextView) view.findViewById(R.id.newsTitle);
        TextView timestamp= (TextView) view.findViewById(R.id.newsTimeStamp);
        Log.d("bookmark", "instantiateItem: "+position);
        ImageView imageView= (ImageView) view.findViewById(R.id.newsImage);
        final TextView likeCount= (TextView) view.findViewById(R.id.like_count);
        likeCount.setText(bookmarks.get(position).likes+"");

        if(bookmarks.get(position).news_image==null)
        {
            new ImageDownloaderTask(imageView,position).execute(bookmarks.get(position).image);
        }
        else
        {
            imageView.setImageBitmap(bookmarks.get(position).news_image);
        }

        if(position==0)
        {
            for(int i=1;i<=10;i++)       //123456789                                                                                           //fetching next 9 images
            {
                if(position+i>=bookmarks.size())
                    break;
                if(bookmarks.get(position+i).news_image==null)
                    new ImageDownloaderTask(imageView,position+i,false).execute(NewsPOGO.newsArray.get(position+i).image);
            }
        }
        else
        {
            int i=10;
            if(((position+i)<bookmarks.size())&&bookmarks.get(position+i).news_image==null)
                new ImageDownloaderTask(imageView,position+i,false).execute(bookmarks.get(position+i).image);
        }

        String timeedit=bookmarks.get(position).timestamp.substring(0,10)+"    "+bookmarks.get(position).timestamp.substring(11,18);
        textView.setText(""+bookmarks.get(position).body);
        title.setText(bookmarks.get(position).title);
        timestamp.setText(timeedit);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, WebViewActivity.class);
                intent.putExtra("Link",bookmarks.get(position).link);
                context.startActivity(intent);

            }
        });


        SharedPreferences sharedPreferences=context.getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        final String userId=sharedPreferences.getString("userid",null);

        final Button like_btn= (Button) view.findViewById(R.id.like_btn);


        if(bookmarks.get(position).liked)
        {
            like_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.love,0,0);
        }
        else
        {
            like_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.unlove,0,0);
        }

        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bookmarks.get(position).liked)
                {
                    bookmarks.get(position).liked=true;
                    bookmarks.get(position).likes++;
                    likeCount.setText(bookmarks.get(position).likes+"");
                    new LikeTask(bookmarks.get(position).id+"",userId,0).execute();
                    like_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.love,0,0);
                }
                else
                {
                    bookmarks.get(position).liked=false;
                    bookmarks.get(position).likes--;
                    likeCount.setText(bookmarks.get(position).likes+"");
                    new LikeTask(bookmarks.get(position).id+"",userId,1).execute();
                    like_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.unlove,0,0);
                }
            }
        });


        final Button bookmark_btn=(Button) view.findViewById(R.id.bookmark_btn);

        if(bookmarks.get(position).bookmarked)
        {
            bookmark_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.bookmark,0,0);
        }
        else
        {
            bookmark_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.unbookmark,0,0);
        }

        bookmark_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bookmarks.get(position).bookmarked)
                {
                    bookmarks.get(position).bookmarked=true;
                    new BookmarkTask(bookmarks.get(position).id+"",userId,0).execute();
                    bookmark_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.bookmark,0,0);

                }
                else
                {
                    bookmarks.get(position).bookmarked=false;
                    new BookmarkTask(bookmarks.get(position).id+"",userId,1).execute();
                    bookmark_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.unbookmark,0,0);
                }
            }
        });

        final Button share_btn= (Button) view.findViewById(R.id.share_btn);

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Shots is great: "+bookmarks.get(position).link);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });


        container.addView(view);
        return view;

    }




    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        int position=-1;
        boolean flag=true;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        public ImageDownloaderTask(ImageView imageView,int position) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.position=position;
        }

        public ImageDownloaderTask(ImageView imageView,int position,boolean flag) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.position=position;
            this.flag=flag;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        if(flag)
                            imageView.setImageBitmap(bitmap);
                        bookmarks.get(position).news_image=bitmap;
                        Log.d("imgDownload", "onPostExecute: "+position+"  "+flag);
                    } else {
                        Drawable placeholder = null;
                        if(flag)
                            imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();

                final int responseCode = urlConnection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                try{urlConnection.disconnect();}catch (Exception ex){}
                Log.w("ImageDownloader", "Errore durante il download da " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
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
//            Toast.makeText(context,"--"+s,Toast.LENGTH_LONG).show();
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
            //Toast.makeText(context,"--"+s,Toast.LENGTH_LONG).show();
            //check=s;

        }
    }
}
