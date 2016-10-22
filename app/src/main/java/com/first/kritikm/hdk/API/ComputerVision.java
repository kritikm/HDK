package com.first.kritikm.hdk.API;

import android.net.Uri;
import android.util.Log;

import com.first.kritikm.hdk.Commons;
import com.first.kritikm.hdk.ImageHelper;
import com.first.kritikm.hdk.ServiceHandler;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Kritikm on 22-Oct-16.
 */
public class ComputerVision {

private final static String URL = "https://api.projectoxford.ai/vision/v1.0/analyze";
    private final static String KEY = "3e42b8856174428fa9485e1e2ce570c3";

    private ComputerVision(){}


    public static String getInfo(Uri uri) {
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Ocp-Apim-Subscription-Key",KEY);
        Log.d(Commons.TAG,ImageHelper.getImageBinary(uri).substring(0,100));
        return ServiceHandler.post(URL, ImageHelper.getImageBinary(uri),headers);


    }



}
