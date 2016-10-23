package com.first.kritikm.hdk.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.kritikm.hdk.Commons;
import com.first.kritikm.hdk.ImageHelper;
import com.google.gson.Gson;
import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Kritikm on 23-Oct-16.
 */

public class Emotions {
    private static final int REQUEST_SELECT_IMAGE = 0;
    private Button selectImageButton;
    private TextView resultText;
    private Uri imageUri;
    private Bitmap bitmap;
    private EmotionServiceClient emotionServiceClient;

    public Emotions(Context context, Uri uri)
    {
        imageUri = uri;
        if(emotionServiceClient == null)
            emotionServiceClient = new EmotionServiceRestClient("3aeb4b55cc804054b606a55660b0ea8e");

        bitmap = ImageHelper.loadSizeLimitedBitmapFromUri(imageUri, context.getContentResolver());
        if(bitmap != null)
            doRecognize();
    }

    public void doRecognize() {
        // Do emotion detection using auto-detected faces.
        try {
            new doRequest(false).execute();
        } catch (Exception e) {
            Log.d(Commons.TAG, e.getMessage());
        }

//        String faceSubscriptionKey = getString(R.string.faceSubscription_key);
//        if (faceSubscriptionKey.equalsIgnoreCase("Please_add_the_face_subscription_key_here")) {
//            resultText.append("\n\nThere is no face subscription key in res/values/strings.xml. Skip the sample for detecting emotions using face rectangles\n");
//        } else {
//            // Do emotion detection using face rectangles provided by Face API.
//            try {
//                new doRequest(true).execute();
//            } catch (Exception e) {
//                resultText.append("Error encountered. Exception is: " + e.toString());
//            }
//        }
    }

    private List<RecognizeResult> processWithAutoFaceDetection() throws EmotionServiceException, IOException
    {
        Gson gson = new Gson();

        //put image into input stream for auto detection

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());

        List<RecognizeResult> result = null;
        //
        // Detect emotion by auto-detecting faces in the image.
        //
        result = this.emotionServiceClient.recognizeImage(input);
        String json = gson.toJson(result);
        Log.d("Result", json);

        return result;
    }

    private class doRequest extends AsyncTask<String, String, List<RecognizeResult>>
    {
        private Exception e;
        private boolean useFaceRectangles = false;

        public doRequest(boolean useFaceRectangles)
        {
            this.useFaceRectangles = useFaceRectangles;
        }

        @Override
        protected List<RecognizeResult> doInBackground(String... args)
        {
            if(this.useFaceRectangles == false)
            {
                try
                {
                    Log.d("Method", "Auto Detecting");
                    return processWithAutoFaceDetection();
                }
                catch (Exception e)
                {
                    this.e = e;
                    Log.d("Problem", this.e.getMessage());
                }
            }
            else
            {
                //process with rectangles
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            super.onPostExecute(result);


            if (this.useFaceRectangles == false) {
                Log.d(Commons.TAG, "Auto Detecting Faces");
            } else
                Log.d(Commons.TAG, "Pre determined rectangles");
            if (e != null)
                Log.d(Commons.TAG, e.getMessage());
            else {
                if (result.size() == 0)
                    Log.d(Commons.TAG, "No emotions detected");
                else {
                    Integer count = 0;

                    for (RecognizeResult r : result) {
                        Log.d(Commons.TAG, "Face " + count);
                        Log.d(Commons.TAG, "Anger " + r.scores.anger);
                        Log.d(Commons.TAG, "Contempt " + r.scores.contempt);
                        Log.d(Commons.TAG, "Disgust " + r.scores.disgust);
                        Log.d(Commons.TAG, "Fear " + r.scores.fear);
                        Log.d(Commons.TAG, "Happiness " + r.scores.happiness);
                        Log.d(Commons.TAG, "Neutral " + r.scores.neutral);
                        Log.d(Commons.TAG, "Sadness " + r.scores.sadness);
                        Log.d(Commons.TAG, "Surprise " + r.scores.surprise);

                        count++;
                    }
                }
            }
        }
    }



}
