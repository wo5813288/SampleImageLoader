package com.fx.sampleimageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 *
 * @author Administrator
 * @date 2018/3/12
 */

public class BitmapUtils {

    /**
     *
     * @param filePath 图片的本地存储路径
     * @param pixW 要显示的宽
     * @param pixH 要显示的高
     * @return
     */
    public static Bitmap getSamllBitmap(String filePath,int pixW,int pixH){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设为true则不加载到内存当中，返回bitmap的width和height
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filePath,options);
        //获取图片实际的宽和高
        int relWidth = options.outWidth;
        int relHeight = options.outHeight;
        options.inSampleSize = getBitmapInsampleSize(relWidth,relHeight,pixW,pixH);
        //加载到内存当中
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeFile(filePath,options);
    }

    /**
     * 计算inSampleSize
     * @param relWidth 实际的宽
     * @param relHeight 实际的高
     * @param pixW 要显示的宽
     * @param pixH 要显示的高
     * @return
     */
    private static int getBitmapInsampleSize(int relWidth, int relHeight, int pixW, int pixH) {
        //设置初始值，不压缩
        int inSampleSize =1;
        int samWidth =0;
        int samHeight =0;
        if(relWidth>pixW){
            samWidth = relWidth/pixW;
        }
        if(relHeight>pixH){
            samHeight = relHeight/pixH;
        }
        inSampleSize = Math.max(samWidth,samHeight);
        if(inSampleSize<=0){
            inSampleSize=1;
        }
        return  inSampleSize;
    }
}
