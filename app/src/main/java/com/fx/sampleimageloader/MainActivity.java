package com.fx.sampleimageloader;

import android.Manifest;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fx.sampleimageloader.utils.FileUtils;
import com.fx.sampleimageloader.utils.HttpUtils;

import java.io.File;

/**
 * @author Andy
 */
public class MainActivity extends AppCompatActivity {

    private ImageView imageShow;
    private String url ="http://a.hiphotos.baidu.com/image/pic/item/0dd7912397dda144a5db01a2beb7d0a20df486cb.jpg";
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions,0x01 );
        }
        imageShow = findViewById(R.id.image_show);
    }

    public void showImage(View view) {
        SimpleImageLoader.getInstance().displayImageView(imageShow,url);
    }
}
