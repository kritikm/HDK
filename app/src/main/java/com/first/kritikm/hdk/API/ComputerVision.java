package com.first.kritikm.hdk.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.first.kritikm.hdk.Commons;
import com.first.kritikm.hdk.ImageHelper;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalyzeResult;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Kritikm on 22-Oct-16.
 */
public class ComputerVision {


    private final static String KEY = "3e42b8856174428fa9485e1e2ce570c3";
    private VisionServiceClient client;
    private Gson gson;

    public ComputerVision() {
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

    public String cv(Context context, Uri uri) throws VisionServiceException, IOException {
        String[] features = {"Color", "Faces", "Categories"};
        AnalyzeResult ar = client.analyzeImage(getInputStream(context, uri), features);
        String result = gson.toJson(ar);
        Log.d(Commons.TAG, "COMPUTER VISISON " + result);
        return result;
    }

    public String thumbnail(Context context, Uri uri) throws VisionServiceException, IOException {
        byte[] image = client.getThumbnail(200, 200, true, getInputStream(context, uri));
        Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);
        String result = ImageHelper.saveToExternalStorage(bm);
        Log.d(Commons.TAG, "Thumbnail: " + result);
        return result;
    }

    public String ocr(Context context, Uri uri) throws VisionServiceException, IOException {
        com.microsoft.projectoxford.vision.contract.OCR r = client.recognizeText(getInputStream(context, uri), LanguageCodes.AutoDetect, true);

        String result = "";
        if (r == null)
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
        Log.d(Commons.TAG, "OCR : " + result);
        return result;
    }


}
