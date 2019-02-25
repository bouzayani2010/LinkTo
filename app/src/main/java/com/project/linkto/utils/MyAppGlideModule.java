package com.project.linkto.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

@GlideModule
public class MyAppGlideModule extends AppGlideModule { @Override
public void applyOptions(Context context, GlideBuilder builder) {
    // set disk cache size & external vs. internal
    int cacheSize100MegaBytes = 104857600;

    builder.setDiskCache(
            new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));

    //builder.setDiskCache(
    //        new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
}

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // nothing to do here
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }
}
