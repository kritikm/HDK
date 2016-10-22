package com.first.kritikm.hdk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by ALPHA-BETA on 22-10-16.
 */

public class Test extends Activity {
    private final int RESULT_CAMERA = 1;
    private final int RESULT_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectImage();
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose a Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, RESULT_CAMERA);
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_GALLERY);
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CAMERA || requestCode == RESULT_GALLERY) {
                String[] photoUrl = new String[1];
                photoUrl[0] = "temp.jpg";
                if (requestCode == RESULT_GALLERY) {
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        ImageHelper.saveToInternalStorage(photoUrl[0], bm, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (requestCode == RESULT_CAMERA) {

                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ImageHelper.saveToInternalStorage(photoUrl[0],imageBitmap,this);
                }

                Log.d(Commons.TAG,photoUrl[0]);

              //  Intent intent = new Intent(this, CartActivity.class);
              //  intent.putExtra("image_url", photoUrl);
              //  intent.putExtra("type", "I");
              //  startActivity(intent);
            }

        }
    }

}
