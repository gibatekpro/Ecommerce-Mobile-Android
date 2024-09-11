package com.gibatekpro.ecommerceandroid.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {

    public final static String TAG = "ImageLoader";

    public static void loadImage(Context context, ImageView imageView, String imageUrl) {
        InputStream inputStream;

        try {
            inputStream = context.getAssets().open(imageUrl);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            Glide.with(context)
                    .load(bitmap)
                    .into(imageView);
        } catch (IOException e) {
            Log.d(TAG, "loadImage: " + e.getMessage());
        }

    }


}
