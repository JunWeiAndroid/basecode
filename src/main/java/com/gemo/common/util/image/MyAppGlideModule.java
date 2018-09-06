package com.gemo.common.util.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import java.io.InputStream;

/**
 * Created by DELL on 2017年12月1日 001.
 * Glide注解生成GlideApp类
 */
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @SuppressLint("CheckResult")
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//        RequestOptions options = new GlideOptions();
//        options.format(DecodeFormat.PREFER_ARGB_8888).encodeQuality(100).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        builder.setDefaultRequestOptions(options);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

}
