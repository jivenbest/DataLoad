package cn.wilson.dataload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.apache.http.Header;

import java.io.IOException;

/*
* 比较4大主流框架获取数据的便捷性，使用体验
* 设计不同获取数据的场景，测试各自性能和稳定性
* */
public class performance extends Activity {
    private static final String urlzero = "https://raw.githubusercontent.com/jivenbest/testdata/master/citysingle.json";
    private static final String urlOne = "https://raw.githubusercontent.com/jivenbest/testdata/master/cityone.json";
    private static final String urlTwo = "https://raw.githubusercontent.com/jivenbest/testdata/master/citytwo.json";

    private Button btnInital,btnAgain,btnTest,btnFirst;
    private TextView resRawHttp,resVolley,resAsyncHttp,resOKhttp,resXUtils;
    private long start1,start2,start3,start4,start5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        InitView();
        DoEvent();
    }

    private void InitView(){
        btnInital = (Button)findViewById(R.id.btnInital);
        btnAgain = (Button)findViewById(R.id.btnAgain);
        btnTest = (Button)findViewById(R.id.btnTest);
        btnFirst = (Button)findViewById(R.id.btnFirst);

        resRawHttp = (TextView)findViewById(R.id.resRawHttp);
        resVolley = (TextView)findViewById(R.id.resVolley);
        resAsyncHttp = (TextView)findViewById(R.id.resAsyncHttp);
        resOKhttp = (TextView)findViewById(R.id.resOKhttp);
        resXUtils = (TextView)findViewById(R.id.resXUtils);
    }

    private void DoEvent(){
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataByRawHttp(urlzero);
                GetDataByVolley(urlzero);
                GetDataByAsyncHttpClient(urlzero);
                GetDataByOKhttp(urlzero);
                GetDataByXUtils(urlzero);
            }
        });

        btnInital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetDataByRawHttp(urlOne);
                GetDataByVolley(urlOne);
                GetDataByAsyncHttpClient(urlOne);
                GetDataByOKhttp(urlOne);
                GetDataByXUtils(urlOne);
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataByRawHttp(urlTwo);
                GetDataByVolley(urlTwo);
                GetDataByAsyncHttpClient(urlTwo);
                GetDataByOKhttp(urlTwo);
                GetDataByXUtils(urlTwo);
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(performance.this,optimized.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void GetDataByRawHttp(final String url){
        resRawHttp.setText("获取RawHttp测试结果中...");
        start1 = System.currentTimeMillis();
        try {
            final Context context = this.getBaseContext();
            new Thread() {
                Handler handler = new Handler(context.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        long res = System.currentTimeMillis() - start1;
                        resRawHttp.setText("RawHttp耗时：" + String.valueOf(res) + "ms");
                    }
                };

                public void run() {
                    try {
                        String result = HttpHelper.getString(url);
                        if(!TextUtils.isEmpty(result)) {
                            handler.sendEmptyMessage(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetDataByVolley(final String url){
        resVolley.setText("获取Volley测试结果中...");
        start2 = System.currentTimeMillis();
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!TextUtils.isEmpty(response)){
                    long res = System.currentTimeMillis() - start2;
                    resVolley.setText("Volley耗时：" + String.valueOf(res) + "ms");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue.add(stringRequest);
    }

    private void GetDataByAsyncHttpClient(final String url){
        resAsyncHttp.setText("获取AsyncHttp测试结果中...");
        start3 = System.currentTimeMillis();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                long res = System.currentTimeMillis() - start3;
                resAsyncHttp.setText("AsyncHttp耗时：" + String.valueOf(res) + "ms");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private void GetDataByOKhttp(final String url){
        resOKhttp.setText("获取OKhttp测试结果中...");
        start4 = System.currentTimeMillis();
        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                if (response.isSuccessful()){
                    long res = System.currentTimeMillis() - start4;
                    resOKhttp.setText("OKhttp耗时：" + String.valueOf(res) + "ms");
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        });
    }

    private void GetDataByXUtils(final String url){
        resXUtils.setText("获取XUtils测试结果中...");
        start5 = System.currentTimeMillis();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,url,
                new RequestCallBack<String>(){
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        long res = System.currentTimeMillis() - start5;
                        resXUtils.setText("XUtils耗时：" + String.valueOf(res) + "ms");
                    }

                    @Override
                    public void onStart() {}

                    @Override
                    public void onFailure(HttpException error, String msg) {}
                });
    }

    @Override
    protected void onStop(){
        super.onStop();

    }
}
