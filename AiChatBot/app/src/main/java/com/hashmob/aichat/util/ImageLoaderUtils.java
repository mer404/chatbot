package com.hashmob.aichat.util;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoaderUtils {

    public static void loadImageFromUrlWithoutResizeViaGlide(Context context, String imgFile, ImageView iv, int placeholderResourceID,
                                                             @Nullable RequestOptions requestOptions) {


        try {
            if (imgFile == null) {
                iv.setImageResource(placeholderResourceID);
            } else {
                if (requestOptions != null) {
                    Glide.with(context)
                            .load(imgFile)
                            .apply(requestOptions)
                            .placeholder(placeholderResourceID)
                            .into(iv);
                } else {
                    Glide.with(context)
                            .load(imgFile)
                            .placeholder(placeholderResourceID)
                            .into(iv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}