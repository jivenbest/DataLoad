package cn.wilson.dataload;

import android.app.Activity;
import android.os.Bundle;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loopj.android.http.AsyncHttpClient;

/*
* 比较4大主流框架获取数据的便捷性，使用体验
* 设计不同获取数据的场景，测试各自性能和稳定性
* */
public class performance extends Activity {

    private final static String urlOne = "https://raw.githubusercontent.com/jivenbest/testdata/master/cityone.json";
    private final static String urlTwo = "https://raw.githubusercontent.com/jivenbest/testdata/master/citytwo.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

    }


    private void GetDataByRawHttp(){

    }

    private void GetDataByVolley(){

    }

    private void GetDataByAsyncHttpClient(){
        AsyncHttpClient client = new AsyncHttpClient();
    }

    private void GetDataByOKhttp(){

    }

    private void GetDataByXUtils(){
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,"http://www.lidroid.com",
                new RequestCallBack<String>(){
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                    }

                    @Override
                    public void onStart() {}

                    @Override
                    public void onFailure(HttpException error, String msg) {}
                });
    }
}
