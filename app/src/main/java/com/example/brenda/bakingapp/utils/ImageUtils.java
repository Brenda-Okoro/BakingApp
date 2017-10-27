package com.example.brenda.bakingapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by brenda on 10/24/17.
 */

public class ImageUtils {
    public static void loadImageFromResourcesToImageView(Context context, int resourceId, ImageView imageView) {
        Picasso.with(context).load(resourceId).into(imageView);
    }

    public static void loadImageFromRemoteServerIntoImageView(Context context, String imageURL, ImageView imageView) {
        Picasso.with(context).load(imageURL).into(imageView);
    }
}
