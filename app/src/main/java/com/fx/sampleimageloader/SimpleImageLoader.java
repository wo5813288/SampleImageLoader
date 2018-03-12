package com.fx.sampleimageloader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.fx.sampleimageloader.utils.BitmapUtils;
import com.fx.sampleimageloader.utils.FileUtils;

import java.io.File;
import java.util.logging.Handler;

/**
 * Created by Administrator on 2018/3/12.
 */

public class SimpleImageLoader {

    private static SimpleImageLoader mImageLoder;
    /**android内存缓存*/
    private LruCache<String,Bitmap> lruCache;
    private ImageView view;
    private String url;
    private File imageFile;
    @SuppressLint("HandlerLeak")
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = BitmapUtils.getSamllBitmap(imageFile.getPath(),view.getWidth(),view.getHeight());
            view.setImageBitmap(bitmap);
            putBitmapToCache(url,bitmap);
        }
    };
    /**
     * 单利模式获取SimpleImageLoader对象
     * @return
     */
    public static SimpleImageLoader getInstance(){
        if(mImageLoder==null){
            synchronized (SimpleImageLoader.class){
                if(mImageLoder==null){
                    mImageLoder = new SimpleImageLoader();
                }
            }
        }
        return  mImageLoder;
    }

    /**
     * 初始化SimpleImageLoder
     */
    private SimpleImageLoader(){
        //获取开辟的最大内存空间
        int max= (int) (Runtime.getRuntime().maxMemory()/8);
        lruCache =new LruCache<String,Bitmap>(max){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 显示图片
     * @param view
     * @param url
     */
    public void displayImageView(ImageView view,String url){
        Bitmap bitmap = lruCache.get(url);
        if(bitmap!=null){
            view.setImageBitmap(bitmap);
        }else{
            downLoadImage(view,url);
        }
    }

    /**
     * 将图片进行缓存
     * @param url
     * @param bitmap
     */
    private void putBitmapToCache(String url,Bitmap bitmap){
        lruCache.put(url,bitmap);
    }


    /**
     * 如果该图片没有缓存，则下载
     * @param view
     * @param url
     */
    private void downLoadImage(final ImageView view, final String url) {
        this.url = url;
        this.view =view;
        FileUtils.downLoadFile(url, null, new FileUtils.FileDownLoadListener() {
            @Override
            public void downLoadSuccess(File file) {
                imageFile = file;
                handler.sendEmptyMessage(0x001);
            }

            @Override
            public void downLoadFailure(String string) {

            }
        });
    }
}
