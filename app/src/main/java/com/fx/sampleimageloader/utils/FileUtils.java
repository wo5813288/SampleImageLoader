package com.fx.sampleimageloader.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Response;

/**
 *
 * @author Administrator
 * @date 2018/3/9
 */

public class FileUtils {

    public interface FileDownLoadListener{
        /**现在完成
         * @param file
         */
        void downLoadSuccess(File file);

        /** 下载失败
         * @param string
         */
        void downLoadFailure(String string);
    }
    public static void downLoadFile(final String url, HashMap<String,String> params, final FileDownLoadListener listener){
            HttpUtils httpUtils = new HttpUtils(new HttpUtils.HttpResponse() {
                @Override
                public void onSuccess(Response response)  {
                    byte[] buf = new byte[1024];
                    int len =0;
                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/imageLoader/");
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    String fileName = url.substring(url.lastIndexOf("/"),url.length());
                    File imageFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/imageLoader/"+fileName);
                    InputStream is = response.body().byteStream();
                    FileOutputStream fos = null;
                    try {
                         fos = new FileOutputStream(imageFile);
                        while ((len=is.read(buf))!=-1){
                            fos.write(buf,0,len);
                        }
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if(is!=null){
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(fos!=null){
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    listener.downLoadSuccess(imageFile);
                }

                @Override
                public void onFailure(String str) {
                    listener.downLoadFailure(str);
                }
            });
        httpUtils.sendGet(url,null);
    }
}
