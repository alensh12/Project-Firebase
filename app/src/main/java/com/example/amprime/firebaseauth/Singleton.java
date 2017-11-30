package com.example.amprime.firebaseauth;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by amprime on 11/12/17.
 */

public class Singleton {
    private static Singleton mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    public Singleton(Context context) {
        mContext =context;
        mRequestQueue =getmRequestQueue();
    }

    public RequestQueue getmRequestQueue() {
        if(mRequestQueue==null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static  synchronized  Singleton getmInstance(Context context){
        if(mInstance==null){
            mInstance = new Singleton(context);
        }
        return mInstance;
    }

    public<T> void addRequestQueue(Request<T> request){

        getmRequestQueue().add(request);
    }
}
