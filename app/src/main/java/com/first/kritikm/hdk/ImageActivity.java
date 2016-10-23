package com.first.kritikm.hdk;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.first.kritikm.hdk.API.ComputerVision;
import com.first.kritikm.hdk.API.Faces;
import com.first.kritikm.hdk.Databases.Photos;
import com.microsoft.projectoxford.face.contract.Face;

public class ImageActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    Uri uri;
    Face[] f;
    String ocr = "OCR: ",ar = "Data " ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Photos mDb = new Photos(this);
        String thumbnail = getIntent().getExtras().getString("imageUri");
        thumbnail = thumbnail.substring(thumbnail.lastIndexOf('/') + 1);
        String[] result = mDb.getPathFromThumbnail(thumbnail);
        ocr += result[1];
        //Log.d(Commons.TAG,a);
        uri = Uri.parse(result[0]);
        TextView t = (TextView)findViewById(R.id.ocr);
        t.setText(ocr);



        new GetInfoTask().execute();


    }

    private class GetInfoTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ImageActivity.this);
            pDialog.setMessage("Fetching Info...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(Void... params) {
            try {
                ComputerVision cv = new ComputerVision();
             //  ar += cv.cv(ImageActivity.this, uri);
               ocr += cv.ocr(ImageActivity.this, uri);

                //Long photo_id =  mDb.insertPhotos(mImage);

                Faces faces = new Faces();
                f = faces.test(ImageActivity.this,uri);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(Commons.TAG, "Exception ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
            catch (Exception e) {}

            Bitmap c = Faces.drawFaceRectanglesOnBitmap(bitmap,f,false);
            ImageView b =  (ImageView)findViewById(R.id.imageView2);
            b.setImageBitmap(c);


             //   TextView x = (TextView)findViewById(R.id.ar);
             //   x.setText(ar);




        }


        @Override
        protected void onCancelled() {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

}
