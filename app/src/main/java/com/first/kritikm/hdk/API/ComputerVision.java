package com.first.kritikm.hdk.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.first.kritikm.hdk.Commons;
import com.first.kritikm.hdk.ImageHelper;
import com.first.kritikm.hdk.ServiceHandler;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalyzeResult;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Kritikm on 22-Oct-16.
 */
public class ComputerVision {


    private final static String KEY = "3e42b8856174428fa9485e1e2ce570c3";

    public static String process(Context context, Uri uri) throws VisionServiceException, IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        Gson gson = new Gson();
        String[] features = {"ImageType", "Color", "Faces", "Adult", "Categories"};
        String[] details = {};
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        VisionServiceClient client = new VisionServiceRestClient(KEY);
        AnalyzeResult ar = client.analyzeImage(inputStream, features);

        String result = gson.toJson(ar);
        Log.d(Commons.TAG, "COMPUTER VISISON " + result);

        return result;
    }




}
