package cn.wilson.dataload;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KingFlyer on 2015/6/18.
 */
public class FileHelper {
    //检测SDCard
    public static boolean HasSDCard() {
        String sdStatus = Environment.getExternalStorageState();
        if(sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return true;
        }else {
            return false;
        }
    }

    //创建文件夹
    public static void CreateDir(String path){
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    //按字母创建文件
    public static void CreateFile(String path,String filename,String descp){
        String fullDir = Environment.getExternalStorageDirectory() + path;
        File destDir = new File(fullDir);
        if(!destDir.exists()){
            destDir.mkdirs();
        }

        try{
            String name = fullDir + "/" + filename;
            File myfile = new File(name);
            if(myfile.exists()){
                myfile.delete();
            }

            FileWriter fw = new FileWriter(myfile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(descp);
            bw.flush();
            bw.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //文件是否存在
    public static boolean HasFilename(String filePath){
        File myfile = new File(Environment.getExternalStorageDirectory() + filePath);
        if(myfile.exists()){
            return true;
        }else{
            return false;
        }
    }

    //读取文件
    public static String ReadFilename(String filePath){
        File myfile = new File(Environment.getExternalStorageDirectory() + filePath);
        InputStream inputStream = null;
        String str = "";

        if(myfile.exists()){
            try{
                inputStream= new FileInputStream(myfile);
                int size = inputStream.available();
                byte[]buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();

                str= new String(buffer);

            }catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

        return str;
    }
}
