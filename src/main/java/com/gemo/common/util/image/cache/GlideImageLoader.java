package com.gemo.common.util.image.cache;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.gemo.common.CommonConfig;
import com.gemo.common.R;
import com.gemo.common.util.image.GlideApp;
import com.imnjh.imagepicker.ImageLoader;

/** 图片选取加载
 * Created by WJW on 2018/5/11.
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void bindImage(ImageView imageView, Uri uri, int width, int height) {
        GlideApp.with(CommonConfig.getContext()).load(uri)
//                .placeholder(R.drawable.header_default)
//                .error(R.drawable.header_default)
                .override(width, height).dontAnimate().into(imageView);
    }

    @Override
    public void bindImage(ImageView imageView, Uri uri) {
        GlideApp.with(CommonConfig.getContext()).load(uri)
//                .placeholder(R.drawable.header_default)
//                .error(R.drawable.header_default)
                .dontAnimate().into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public ImageView createFakeImageView(Context context) {
        return new ImageView(context);
    }
}
