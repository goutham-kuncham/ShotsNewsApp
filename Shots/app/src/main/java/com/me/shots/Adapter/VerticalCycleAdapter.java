package com.me.shots.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

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
        ImageView imageView= (ImageView) view.findViewById(R.id.blurredNewsImage);
        ImageView actualNewsImage= (ImageView) view.findViewById(R.id.newsImage);
        TextView textView= (TextView) view.findViewById(R.id.newsText);
        TextView title= (TextView) view.findViewById(R.id.newsTitle);
        TextView timestamp= (TextView) view.findViewById(R.id.newsTimeStamp);
        Log.d("MYTag", "instantiateItem: "+position);

        GetXMLTask getImage = new GetXMLTask(imageView,actualNewsImage, position);
        try {
            getImage.execute().get();//
        } catch (Exception e) {
            e.printStackTrace();
        }


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


    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;
        int position;
        ImageView actualNewsImage;

        public GetXMLTask(ImageView imageView,ImageView actualNewsImage,int position)
        {
            this.position=position;
            this.imageView=imageView;
            this.actualNewsImage=actualNewsImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;

            if(NewsPOGO.newsArray.get(position)==null) {
                String url;
                url = NewsPOGO.newsArray.get(position).image;
                map = downloadImage(url);
                Log.d("mytagg", "doInBackground: "+"image"+position);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            if(result==null)
            {
                actualNewsImage.setImageBitmap(NewsPOGO.newsArray.get(position).news_image);
                imageView.setImageBitmap(NewsPOGO.newsArray.get(position).blurred_news_image);
            }
            else
            {
                NewsPOGO.newsArray.get(position).news_image=result;
                actualNewsImage.setImageBitmap(result);
                imageView.setImageBitmap(result);
                imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = imageView.getDrawingCache();
                Bitmap blurred = fastblur(bitmap, 1, 10000);//second parametre is radius
                NewsPOGO.newsArray.get(position).blurred_news_image=blurred;
                imageView.setImageBitmap(blurred);
            }
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

            int width = Math.round(sentBitmap.getWidth() * scale);
            int height = Math.round(sentBitmap.getHeight() * scale);
            sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1) { return (null); } int w = bitmap.getWidth(); int h = bitmap.getHeight(); int[] pix = new int[w * h]; Log.e("pix", w + " " + h + " " + pix.length); bitmap.getPixels(pix, 0, w, 0, 0, w, h); int wm = w - 1; int hm = h - 1; int wh = w * h; int div = radius + radius + 1; int r[] = new int[wh]; int g[] = new int[wh]; int b[] = new int[wh]; int rsum, gsum, bsum, x, y, i, p, yp, yi, yw; int vmin[] = new int[Math.max(w, h)]; int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) { p = pix[yi + Math.min(wm, Math.max(i, 0))]; sir = stack[i + radius]; sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) { r[yi] = dv[rsum]; g[yi] = dv[gsum]; b[yi] = dv[bsum]; rsum -= routsum; gsum -= goutsum; bsum -= boutsum; stackstart = stackpointer - radius + div; sir = stack[stackstart % div]; routsum -= sir[0]; goutsum -= sir[1]; boutsum -= sir[2]; if (y == 0) { vmin[x] = Math.min(x + radius + 1, wm); } p = pix[yw + vmin[x]]; sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) { yi = Math.max(0, yp) + x; sir = stack[i + radius]; sir[0] = r[yi]; sir[1] = g[yi]; sir[2] = b[yi]; rbs = r1 - Math.abs(i); rsum += r[yi] * rbs; gsum += g[yi] * rbs; bsum += b[yi] * rbs; if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);

            return (bitmap);
        }
    }


}
