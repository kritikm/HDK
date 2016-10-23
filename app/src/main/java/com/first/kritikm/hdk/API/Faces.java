package com.first.kritikm.hdk.API;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.first.kritikm.hdk.Commons;
import com.google.gson.Gson;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
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
    private final static String KEY = "95485ef0d5f0464d84adc2ddfcb6d3ec";
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

    public Face[] test(Context context, Uri uri) {
        try {
            FaceServiceClient faceServiceClient = new FaceServiceRestClient(KEY);
            Face[] faces = faceServiceClient.detect(
                    getInputStream(context, uri),  /* Input stream of image to detect */
                    true,       /* Whether to return face ID */
                    false,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                    null);
            return faces;
            /*
            for (Face face : faces) {
                Log.d(Commons.TAG,face.faceId.toString());
                Faces.insertFace(face,photoId);
            }
            Log.d(Commons.TAG, "Response: Success. Detected " + (faces == null ? 0 : faces.length)
                    + " Face(s)");

*/
        } catch (Exception e) {
            Log.d(Commons.TAG, "Exception ", e);

        }
        return null;
    }


    public static Bitmap drawFaceRectanglesOnBitmap(
            Bitmap originalBitmap, Face[] faces, boolean drawLandmarks) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        int stokeWidth = Math.max(originalBitmap.getWidth(), originalBitmap.getHeight()) / 100;
        if (stokeWidth == 0) {
            stokeWidth = 1;
        }
        paint.setStrokeWidth(stokeWidth);

        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle =
                        calculateFaceRectangle(bitmap, face.faceRectangle, 1.3);

                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);

                if (drawLandmarks) {
                    int radius = face.faceRectangle.width / 30;
                    if (radius == 0) {
                        radius = 1;
                    }
                    paint.setStyle(Paint.Style.FILL);
                    paint.setStrokeWidth(radius);

                    canvas.drawCircle(
                            (float) face.faceLandmarks.pupilLeft.x,
                            (float) face.faceLandmarks.pupilLeft.y,
                            radius,
                            paint);

                    canvas.drawCircle(
                            (float) face.faceLandmarks.pupilRight.x,
                            (float) face.faceLandmarks.pupilRight.y,
                            radius,
                            paint);

                    canvas.drawCircle(
                            (float) face.faceLandmarks.noseTip.x,
                            (float) face.faceLandmarks.noseTip.y,
                            radius,
                            paint);

                    canvas.drawCircle(
                            (float) face.faceLandmarks.mouthLeft.x,
                            (float) face.faceLandmarks.mouthLeft.y,
                            radius,
                            paint);

                    canvas.drawCircle(
                            (float) face.faceLandmarks.mouthRight.x,
                            (float) face.faceLandmarks.mouthRight.y,
                            radius,
                            paint);

                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(stokeWidth);
                }
            }
        }

        return bitmap;
    }

    // Resize face rectangle, for better view for human
    // To make the rectangle larger, faceRectEnlargeRatio should be larger than 1, recommend 1.3
    private static FaceRectangle calculateFaceRectangle(
            Bitmap bitmap, FaceRectangle faceRectangle, double faceRectEnlargeRatio) {
        // Get the resized side length of the face rectangle
        double sideLength = faceRectangle.width * faceRectEnlargeRatio;
        sideLength = Math.min(sideLength, bitmap.getWidth());
        sideLength = Math.min(sideLength, bitmap.getHeight());

        // Make the left edge to left more.
        double left = faceRectangle.left
                - faceRectangle.width * (faceRectEnlargeRatio - 1.0) * 0.5;
        left = Math.max(left, 0.0);
        left = Math.min(left, bitmap.getWidth() - sideLength);

        // Make the top edge to top more.
        double top = faceRectangle.top
                - faceRectangle.height * (faceRectEnlargeRatio - 1.0) * 0.5;
        top = Math.max(top, 0.0);
        top = Math.min(top, bitmap.getHeight() - sideLength);

        // Shift the top edge to top more, for better view for human
        double shiftTop = faceRectEnlargeRatio - 1.0;
        shiftTop = Math.max(shiftTop, 0.0);
        shiftTop = Math.min(shiftTop, 1.0);
        top -= 0.15 * shiftTop * faceRectangle.height;
        top = Math.max(top, 0.0);

        // Set the result.
        FaceRectangle result = new FaceRectangle();
        result.left = (int)left;
        result.top = (int)top;
        result.width = (int)sideLength;
        result.height = (int)sideLength;
        return result;
    }

}

