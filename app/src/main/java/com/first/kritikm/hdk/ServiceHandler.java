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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class ServiceHandler {

    public static final String charset = "UTF-8";

    public static String post(String url, HashMap<String, String> data, HashMap<String, String> headers) {
        try {
            return post(url, getQuery(data), headers);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Commons.TAG, "exception", e);
            return null;
        }

    }

    public static String post(String url) {
        return post(url, null);
    }

    public static String post(String url, HashMap<String, String> data) {
        return post(url, data, null);
    }


    public static String post(String url, String data, HashMap<String, String> headers) {
        Log.i(Commons.TAG, "POST " + url);

        String response = null;
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection connection;
        try {
            Log.i(Commons.TAG, "Parameters:" + data);

            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("POST");

            // Create the SSL connection
          //  SSLContext sc;
           // sc = SSLContext.getInstance("TLS");
           // sc.init(null, null, new java.security.SecureRandom());
         //   connection.setSSLSocketFactory(sc.getSocketFactory());

            setHeaders(connection, headers);

            if (data != null) {
             //  connection.setDoOutput(true);

                os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, charset));
                writer.write(data);
                writer.flush();
                writer.close();
            }
            int responseCode = connection.getResponseCode(); //can call this instead of con.connect()

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
            } else {
     /* error from server */
                is = connection.getErrorStream();
            }
           // is = connection.getInputStream();

            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
            for (String line; (line = reader.readLine()) != null; ) {
                sb.append(line);
            }
            response = sb.toString();


        } catch (Exception e) {
            e.printStackTrace();
            response = null;
            Log.e(Commons.TAG, "exception", e);
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(Commons.TAG, "exception", e);

            }
        }

        Log.i(Commons.TAG, "Result:" + response);

        return response;
    }

    private static String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        if (params == null)
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

    private static void setHeaders(HttpURLConnection connection, HashMap<String, String> headers) {
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/octet-stream");

        if (headers == null)
            return;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        try {
            Log.d(Commons.TAG,getQuery(headers));
        }
        catch (Exception e) {}
    }
}