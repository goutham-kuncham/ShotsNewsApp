package com.me.shots.AsyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
//import com.shorts.jgirish.snu_pro.Utils.Utils.MySingleton;
//import com.shorts.jgirish.snu_pro.Utils.Utils.NewsPOGO;
import com.*;
import com.me.shots.Utils.MySingleton;
import com.me.shots.Utils.NewsPOGO;

/**
 * Created by Half_BlooD PrincE on 10/9/2017.
 */

public class GetNewsImageAsync extends AsyncTask {


    ImageView imageView;
    String imgURL="";
    Context context;
    Bitmap imgResponse;
    int position;

    public GetNewsImageAsync(Context context, ImageView view,int position)
    {
        this.position=position;
        this.imageView=view;
        this.context=context;
        //mProgressView=view.findViewById(R.id.login_progress);
        //viewPager= (VerticalViewPager) view.findViewById(R.id.viewPager);
    }

        public void callImageRequest(){
            imgURL= NewsPOGO.newsArray.get(position).image;
            ImageRequest imageRequest=new ImageRequest(imgURL, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                        NewsPOGO.newsArray.get(position).news_image=response;

                }
            }, 0, 0, ImageView.ScaleType.FIT_CENTER, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error fetching image"+error.toString(), Toast.LENGTH_SHORT).show();;
                }
            });
            MySingleton.getMyInstance(context).addToReqQue(imageRequest);

        }


    @Override
    protected Object doInBackground(Object[] params) {
        callImageRequest();
        if(imgResponse==null){}
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        //imageView.setImageBitmap(NewsPOGO.newsArray.get(position).news_image);
        super.onPostExecute(o);
    }
}
