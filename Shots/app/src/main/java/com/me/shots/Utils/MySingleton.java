package com.me.shots.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Half_BlooD PrincE on 6/30/2017.
 */

public class MySingleton {

    private static Context mContext;
    private static MySingleton  myInstance;
    private RequestQueue requestQueue;

    private MySingleton(Context context)
    {
        mContext=context;
        requestQueue=getRequestQueue();
    }


    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(mContext);

        }
        return requestQueue;
    }

    public static synchronized MySingleton getMyInstance(Context context)
    {
        if(myInstance==null)
        {
            myInstance= new MySingleton(context);
        }
        return myInstance;
    }

    public void addToReqQue(Request request)
    {

        requestQueue.add(request);
    }

}
