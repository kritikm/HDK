package com.first.kritikm.hdk.API;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.first.kritikm.hdk.Commons;
import com.google.gson.Gson;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ALPHA-BETA on 23-10-16.
 */

public class Faces {
    private final static String KEY = "c";
    private VisionServiceClient client;
    private Gson gson;

    public Faces() {
        client = new VisionServiceRestClient(KEY);
        gson = new Gson();
    }

    private ByteArrayInputStream getInputStream(Context context, Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        return inputStream;
    }

    public void test(Context context, Uri uri) {
        try {
            FaceServiceClient faceServiceClient = new FaceServiceRestClient(KEY);
            Face[] faces = faceServiceClient.detect(
                    getInputStream(context, uri),  /* Input stream of image to detect */
                    true,       /* Whether to return face ID */
                    false,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                    null);
            for (Face face : faces) {
                Log.d(Commons.TAG,face.faceId.toString());
            }
            Log.d(Commons.TAG, "Response: Success. Detected " + (faces == null ? 0 : faces.length)
                    + " Face(s)");
        } catch (Exception e) {
            Log.d(Commons.TAG, "Exception ", e);

        }
    }

}

