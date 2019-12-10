package me.shagor.epathagarcom;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
  Created by shagor on 10/17/2017.
 */

public class MySingleTon {

    private static MySingleTon mySingleTon;
    private RequestQueue requestQueue;
    private static Context context;
    private ImageLoader mImageLoader;

    // Constractor
    public MySingleTon(Context context) {
        this.context= context;
        this.requestQueue = getRequestQueue();
    }

    // Create Method
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static synchronized MySingleTon getInstance(Context context) {
        if (mySingleTon == null) {
            mySingleTon = new MySingleTon(context);
        }
        return mySingleTon;
    }

    public ImageLoader getmImageLoader() {
        getRequestQueue();

        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.getRequestQueue(),new LruBitmapCache());
        }

        return mImageLoader;
    }

    public <T> void addToRequestMethod(Request<T>request) {
        requestQueue.add(request);
    }
}