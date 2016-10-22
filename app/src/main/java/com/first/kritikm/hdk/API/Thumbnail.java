package com.first.kritikm.hdk.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.first.kritikm.hdk.ImageHelper;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ALPHA-BETA on 23-10-16.
 */

public class Thumbnail {
    private final static String KEY = "3e42b8856174428fa9485e1e2ce570c3";

    public static Uri process(Context context, Uri uri) throws VisionServiceException, IOException {

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        VisionServiceClient client = new VisionServiceRestClient(KEY);
        byte[] image = client.getThumbnail(200, 200, true, inputStream);
        Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);
        return ImageHelper.saveToExternalStorage(bm);
    }



}
