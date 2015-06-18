package cn.wilson.dataload;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by KingFlyer on 2015/6/18.
 */
public class HttpHelper {

    public static String getString(String path) {
        String result = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(5 * 1000); //5秒未响应则断开连接
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();

            if (code == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
            }

            if (inputStream != null) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, "utf-8")); //防止中文乱码
                String inputLine = null;
                while ((inputLine = buffer.readLine()) != null) {
                    result = result + inputLine;
                }
                buffer.close();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result;
    }
}
