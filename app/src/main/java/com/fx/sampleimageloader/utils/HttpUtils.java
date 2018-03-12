package com.fx.sampleimageloader.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author Administrator
 * @date 2018/3/9
 */

public class HttpUtils {
    private static OkHttpClient okHttpClient ;
    private HashMap<String,String> params;
    private String url;
    private HttpResponse httpResponse;
    public interface HttpResponse{
        void onSuccess(Response o);
        void onFailure(String str);
    }

    public HttpUtils(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**单利模式获取okHttpClient对象
     * @return
     */
    public static OkHttpClient getInstance(){
        if(okHttpClient==null){
            synchronized (HttpUtils.class){
                if(okHttpClient==null){
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }
    public void sendPost(String url, HashMap<String,String> params){
            sendHttp(url,true,params);
    }
    public void sendGet(String url,HashMap<String,String> params){
        sendHttp(url,false,params);
    }

    public void downLoadFile(String url,HashMap<String,String> params){
        sendGet(url,params);
    }
    private void sendHttp(String url,boolean isPost,HashMap<String,String> params){
        this.url = url;
        this.params = params;
        run(isPost);
    }

    /**
     * 执行网络访问具体逻辑
     * @param isPost
     */
    private void run(boolean isPost) {
        Request request = createRequest(isPost);
        getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpResponse.onFailure("服务器请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                httpResponse.onSuccess(response);
            }
        });
    }

    /**
     * 获取Request对象
     * @param isPost
     * @return
     */
    private Request createRequest(boolean isPost) {
        Request request =null;
        if(isPost){
            //Post请求方式
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            Set<Map.Entry<String, String>> entry = params.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entry.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                builder.addFormDataPart(next.getKey(),next.getValue());
            }
            request = new Request.Builder().url(url).post(builder.build()).build();
        }else{
            //GET请求方式
            if(params==null){
                request =new Request.Builder().url(url).build();
            }else{
                String newUrl = url+"?"+MultpartBodyParam(params);
                request =new Request.Builder().url(newUrl).build();
            }

        }
        return request;
    }

    /**
     * GET请求方式时对参数进行变换
     * @param params
     * @return
     */
    private String MultpartBodyParam(HashMap<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder("");
        Set<Map.Entry<String, String>> entry = params.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entry.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            stringBuilder.append(next.getKey()+"="+next.getValue()+"&");
        }
        return stringBuilder.substring(0,stringBuilder.length()-1);
    }
}
