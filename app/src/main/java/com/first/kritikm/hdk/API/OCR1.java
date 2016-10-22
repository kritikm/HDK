package com.first.kritikm.hdk.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.first.kritikm.hdk.Commons;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ALPHA-BETA on 23-10-16.
 */

public class OCR1 {

    private final static String KEY = "3e42b8856174428fa9485e1e2ce570c3";


    public static String process(Context context, Uri uri) throws VisionServiceException, IOException {
        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        com.microsoft.projectoxford.vision.contract.OCR ocr;
        VisionServiceClient client = new VisionServiceRestClient(KEY);
        ocr = client.recognizeText(inputStream, LanguageCodes.AutoDetect, true);

        String result = gson.toJson(ocr);
        Log.d(Commons.TAG, "OCR " + result);

        return result;
    }

    public static String post(String data) {
        Gson gson = new Gson();
        com.microsoft.projectoxford.vision.contract.OCR r = gson.fromJson(data, com.microsoft.projectoxford.vision.contract.OCR.class);

        String result = "";
        if(r == null)
            return null;
        for (Region reg : r.regions) {
            for (Line line : reg.lines) {
                for (Word word : line.words) {
                    result += word.text + " ";
                }
                result += "\n";
            }
            result += "\n\n";
        }
        return  result;
    }
}
