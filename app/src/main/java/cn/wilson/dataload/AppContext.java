package cn.wilson.dataload;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by KingFlyer on 2015/6/18.
 */
public class AppContext extends Application {

    private RequestQueue mRequestQueue;
    private static AppContext sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this; // initialize the singleton
        //主要本地调试用
        AppCrashHelper.getInstance().init(getApplicationContext());
    }

    //创建单例,并且保证线程安全
    public static synchronized AppContext getInstance() {

        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setRetryPolicy(new DefaultRetryPolicy(18 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); //设置18秒超时时间，重试次数1次
        req.setTag(TextUtils.isEmpty(tag) ? "VolleyPatterns" : tag); // set the default tag if tag is empty
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
