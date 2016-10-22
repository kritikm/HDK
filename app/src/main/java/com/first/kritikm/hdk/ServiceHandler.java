package com.first.kritikm.hdk;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.TRUE;

class ServiceHandler {

    public static String post(String url) {
         return post(url,null);
    }

    /**
     * Making service call
     * @param url - url to make request
     * @param hMap  - HashMap object containing query parameters to be sent to the server
     * */
    public static String post(String url,HashMap<String,String> hMap){
        Log.i(Commons.TAG,"POST " + url);

        String response = null;
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection connection;
            try {
            String query = getQuery(hMap);
            Log.i(Commons.TAG, "Parameters:" + query);

            String charset = "UTF-8";
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            if(query != null) {
                connection.setDoOutput(true);

                os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, charset));
                writer.write(query);
                writer.flush();
                writer.close();
            }





                is = connection.getInputStream();

                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
                for (String line; (line = reader.readLine()) != null; ) {
                    sb.append(line);
                }
                response = sb.toString();


        }
        catch (Exception e) {
            e.printStackTrace();
            response = null;
            Log.e(Commons.TAG, "exception", e);
        }

        finally {
            try {
                if(is != null)
                    is.close();
                if(os != null)
                    os.close();
            }catch (IOException e) {
                e.printStackTrace();
                Log.e(Commons.TAG, "exception", e);

            }
        }

        Log.i(Commons.TAG,"Result:" + response);

            return response;
    }

    private static String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        if(params == null)
            return null;
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}