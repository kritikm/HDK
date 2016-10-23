package com.first.kritikm.hdk;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.Random;

/**
 * Created by ALPHA-BETA on 01-Jun-16.
 */
public class ImageHelper {

    // Resize the image if its side length is larger than the maximum.
    private static final int IMAGE_MAX_SIDE_LENGTH = 1280;

    // Ratio to scale a detected face rectangle, the face rectangle scaled up looks more natural.
    private static final double FACE_RECT_SCALE_RATIO = 1.3;


    public static Bitmap getResizedBitmap(Context context,Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return getResizedBitmap(bitmap,300);
        }
        catch (Exception e) {}
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

        public static String getBytes(Context context,Uri uri) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] imageBytes = stream.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                return encodedImage;
                //return stream.toString();
            }
            catch (Exception e) {}
            return null;
        }


        public static Bitmap getImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }

    public static Bitmap getImage(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.NO_WRAP);
        return getImage(decodedString);


    }




    public static String getString(Bitmap bmp){
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

    public static void saveToInternalStorage(String fileName,Bitmap image,Context context){

        FileOutputStream fos = null;

        try {
            fos =  context.openFileOutput(fileName, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            }
            catch (IOException e) {
                Log.e(Commons.TAG,"exception",e);
            }
        }

    }


    public static Bitmap readImage(String fileName,Context context) {
        FileInputStream in = null;

        try {
            in = context.openFileInput(fileName);
            return BitmapFactory.decodeStream(in);
        }

        catch (Exception e) {
            Log.e(Commons.TAG,"exception",e);
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
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(Commons.TAG, "exception", e);

        }
        return null;
    }

    public static String saveToExternalStorage(Bitmap finalBitmap) {

        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"HDK");
        if(!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = Long.toString(System.currentTimeMillis()/1000) + ".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) {
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

    public static Bitmap loadSizeLimitedBitmapFromUri(
            Uri imageUri,
            ContentResolver contentResolver) {
        try {
            // Load the image into InputStream.
            InputStream imageInputStream = contentResolver.openInputStream(imageUri);

            // For saving memory, only decode the image meta and get the side length.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Rect outPadding = new Rect();
            BitmapFactory.decodeStream(imageInputStream, outPadding, options);

            // Calculate shrink rate when loading the image into memory.
            int maxSideLength =
                    options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
            options.inSampleSize = 1;
            options.inSampleSize = calculateSampleSize(maxSideLength, IMAGE_MAX_SIDE_LENGTH);
            options.inJustDecodeBounds = false;
            imageInputStream.close();


            // Load the bitmap and resize it to the expected size length
            imageInputStream = contentResolver.openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageInputStream, outPadding, options);
            maxSideLength = bitmap.getWidth() > bitmap.getHeight()
                    ? bitmap.getWidth(): bitmap.getHeight();
            double ratio = IMAGE_MAX_SIDE_LENGTH / (double) maxSideLength;
            if (ratio < 1) {
                bitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        (int)(bitmap.getWidth() * ratio),
                        (int)(bitmap.getHeight() * ratio),
                        false);
            }

            return rotateBitmap(bitmap, getImageRotationAngle(imageUri, contentResolver));
        } catch (Exception e) {
            return null;
        }
    }

    // Return the number of times for the image to shrink when loading it into memory.
    // The SampleSize can only be a final value based on powers of 2.
    private static int calculateSampleSize(int maxSideLength, int expectedMaxImageSideLength) {
        int inSampleSize = 1;

        while (maxSideLength > 2 * expectedMaxImageSideLength) {
            maxSideLength /= 2;
            inSampleSize *= 2;
        }

        return inSampleSize;
    }

    // Get the rotation angle of the image taken.
    private static int getImageRotationAngle(
            Uri imageUri, ContentResolver contentResolver) throws IOException {
        int angle = 0;
        Cursor cursor = contentResolver.query(imageUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                angle = cursor.getInt(0);
            }
            cursor.close();
        } else {
            ExifInterface exif = new ExifInterface(imageUri.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                default:
                    break;
            }
        }
        return angle;
    }

    // Rotate the original bitmap according to the given orientation angle
    private static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
        // If the rotate angle is 0, then return the original image, else return the rotated image
        if (angle != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

}

