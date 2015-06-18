package cn.wilson.dataload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;


public class optimized extends Activity {

    private static final String urlzero = "https://raw.githubusercontent.com/jivenbest/testdata/master/citysingle.json";
    private static final String urlOne = "https://raw.githubusercontent.com/jivenbest/testdata/master/cityone.json";
    private static final String urlTwo = "https://raw.githubusercontent.com/jivenbest/testdata/master/citytwo.json";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static HttpUtils http = new HttpUtils();

    private Button btnInital,btnAgain,btnTest,btnFirst;
    private TextView resVolley,resAsyncHttp,resOKhttp,resXUtils;
    private long start2,start3,start4,start5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimized);

        InitView();
        DoEvent();
    }

    private void InitView(){
        btnInital = (Button)findViewById(R.id.btnInital);
        btnAgain = (Button)findViewById(R.id.btnAgain);
        btnTest = (Button)findViewById(R.id.btnTest);
        btnFirst = (Button)findViewById(R.id.btnFirst);

        resVolley = (TextView)findViewById(R.id.resVolley);
        resAsyncHttp = (TextView)findViewById(R.id.resAsyncHttp);
        resOKhttp = (TextView)findViewById(R.id.resOKhttp);
        resXUtils = (TextView)findViewById(R.id.resXUtils);
    }

    private void DoEvent(){
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataByVolley(urlzero);
                GetDataByAsyncHttpClient(urlzero);
                GetDataByXUtils(urlzero);
                GetDataByOKhttp(urlzero);
            }
        });

        btnInital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetDataByVolley(urlOne);
                GetDataByAsyncHttpClient(urlOne);
                GetDataByXUtils(urlOne);
                GetDataByOKhttp(urlOne);
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataByVolley(urlTwo);
                GetDataByAsyncHttpClient(urlTwo);
                GetDataByOKhttp(urlTwo);
                GetDataByXUtils(urlTwo);
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(optimized.this,performance.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void GetDataByVolley(final String url){
        resVolley.setText("获取Volley测试结果中...");
        start2 = System.currentTimeMillis();

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
                Toast.makeText(optimized.this,"Volley Error",Toast.LENGTH_SHORT).show();
            }
        });

        AppContext.getInstance().addToRequestQueue(stringRequest, "test");
    }

    private void GetDataByAsyncHttpClient(final String url){
        resAsyncHttp.setText("获取AsyncHttp测试结果中...");
        start3 = System.currentTimeMillis();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                long res = System.currentTimeMillis() - start3;
                resAsyncHttp.setText("AsyncHttp耗时：" + String.valueOf(res) + "ms");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(optimized.this,"AsyncHttp Error:" + String.valueOf(statusCode),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetDataByOKhttp(final String url){
        resOKhttp.setText("获取OKhttp测试结果中...");
        start4 = System.currentTimeMillis();

        try {
            final Context context = this.getBaseContext();
            new Thread() {
                Handler handler = new Handler(context.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        long res = System.currentTimeMillis() - start4;
                        resOKhttp.setText("OKhttp耗时：" + String.valueOf(res) + "ms");
                    }
                };

                public void run() {
                    try {
                        String result = OkHttpHelper.getStringFromServer(url);
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

    private void GetDataByXUtils(final String url){
        resXUtils.setText("获取XUtils测试结果中...");
        start5 = System.currentTimeMillis();
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
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(optimized.this,"XUtils Error：" + String.valueOf(error.getExceptionCode()), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStop(){
        super.onStop();
        AppContext.getInstance().cancelPendingRequests("test");
    }
}
