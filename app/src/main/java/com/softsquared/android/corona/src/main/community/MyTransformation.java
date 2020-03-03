package com.softsquared.android.corona.src.main.community;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class MyTransformation extends BitmapTransformation{



    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap original, int width, int height) {
        Log.d("MyTag", "imageView 사이즈 width : " + width + " , height : " + height); // imageView 사이즈

        int orgWidth = original.getWidth();
        int orgHeight = original.getHeight();

        Log.d("MyTag", "받아온 이미지 사이즈 orgWidth : " + orgWidth + " , orgHeight : " + orgHeight); // 받아온 이미지의 사이즈

        float scaleX = (float) width / orgWidth;
        float scaleY = (float) height / orgHeight;

        float scaledWidth;
        float scaledHeight;

        if (orgWidth >= orgHeight) {
            scaledWidth = scaleY * orgWidth;
            scaledHeight = height;
        } else {
            scaledWidth = width;
            scaledHeight = scaleX * orgHeight;
        }

        Log.d("MyTag","스케일 사이즈 scaledWidth : " + scaledWidth + " , scaledHeight : " + scaledHeight);

        Bitmap result = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        //캔버스 준비
        Canvas canvas = new Canvas(result);

        RectF targetRect = new RectF(0, 0, scaledWidth, scaledHeight);
        canvas.drawBitmap(original, null, targetRect, null);

        return result;

    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
