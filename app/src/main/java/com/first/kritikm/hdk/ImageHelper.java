package com.first.kritikm.hdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ALPHA-BETA on 01-Jun-16.
 */
public class ImageHelper {

    public static Bitmap getResizedBitmap(Context context, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return getResizedBitmap(bitmap, 200);
        } catch (Exception e) {
        }
        return null;

    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getBytes(Context context, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] imageBytes = stream.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
            //return stream.toString();
        } catch (Exception e) {
        }
        return null;
    }


    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static Bitmap getImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.NO_WRAP);
        return getImage(decodedString);


    }


    public static String getString(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static void saveToInternalStorage(String fileName, Bitmap image, Context context) {

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                Log.e(Commons.TAG, "exception", e);
            }
        }

    }


    public static Bitmap readImage(String fileName, Context context) {
        FileInputStream in = null;

        try {
            in = context.openFileInput(fileName);
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(Commons.TAG, "exception", e);
            return null;
        }


    }

    public static String getImageBinary(Uri uri) {
        File file = new File(uri.getPath());
        try {
            FileInputStream is = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Commons.CHARSET));
            for (String line; (line = reader.readLine()) != null; ) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Commons.TAG, "exception", e);

        }
        return null;
    }

    public static String saveToExternalStorage(Bitmap finalBitmap) {

        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HDK");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = Long.toString(System.currentTimeMillis() / 1000) + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri.fromFile(file);
        return fname;
    }
}

