package com.btd.wallet.config;

import android.app.Activity;
import android.widget.ImageView;

import com.btd.wallet.pure.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * Created by 杨紫员 on 2017/10/31.
 */

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)//
                .load(new File(path))//
                .apply(new RequestOptions().placeholder(R.drawable.default_image)//
                        .error(R.drawable.default_image)//
                        .override(width,height)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

    }

    @Override
    public void clearMemoryCache() {

    }
}