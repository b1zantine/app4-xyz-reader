package com.example.xyzreader.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class ImageLoaderHelper {
    private static ImageLoaderHelper sInstance;

    public static ImageLoaderHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ImageLoaderHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(20);
    private ImageLoader mImageLoader;
    private ImageLoaderHelper.Callbacks callbacks;

    public ImageLoaderHelper requestFrom(Activity activity){
        if(activity instanceof ArticleDetailActivity){
            Log.e("CONTECT", "ImageLoaderHelper: ");
            callbacks = (ImageLoaderHelper.Callbacks) activity;
        }
        return sInstance;
    }

    private ImageLoaderHelper(Context applicationContext) {
        RequestQueue queue = Volley.newRequestQueue(applicationContext);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                mImageCache.put(key, value);
                Log.e("TTTTTTT", "putBitmap: " );
                if(callbacks != null) {
                    callbacks.onAddedToCache(key, value);
                }
            }

            @Override
            public Bitmap getBitmap(String key) {
                return mImageCache.get(key);
            }
        };
        mImageLoader = new ImageLoader(queue, imageCache);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public Bitmap getBitmap(String key){
        return mImageCache.get(key);
    }

    public interface Callbacks{
        void onAddedToCache(String key,Bitmap bitmap);
    }
}
