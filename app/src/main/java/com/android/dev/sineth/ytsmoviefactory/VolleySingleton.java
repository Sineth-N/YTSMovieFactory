package com.android.dev.sineth.ytsmoviefactory;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Sineth on 2/20/2016.
 */
public class VolleySingleton {
    private RequestQueue requestQueue;
    private static VolleySingleton volleySingleton=null;
    private ImageLoader imageLoader;
    private VolleySingleton(){
        requestQueue = Volley.newRequestQueue(YTSApplication.getAppContext());
        imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap> lruCache=new LruCache<>((int) (Runtime.getRuntime().maxMemory()/1024)/8);
            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);

            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url,bitmap);
            }
        });
    }

    public static VolleySingleton getInstance(){
        if (volleySingleton==null){
            volleySingleton=new VolleySingleton();
            return volleySingleton;
        }
        else return  volleySingleton;
    }

    public  RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
