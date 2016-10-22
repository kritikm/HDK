package com.first.kritikm.hdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Kritikm on 22-Oct-16.
 */

public class ImageAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Bitmap> bitmaps;

    public ImageAdapter(Context context, ArrayList<Bitmap> bitmaps)
    {
        this.context = context;
        this.bitmaps = bitmaps;
    }


    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null)
        {
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new GridView.LayoutParams(300,300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            imageView = (ImageView)convertView;
        }
        imageView.setImageBitmap(this.bitmaps.get(position));
        return imageView;
    }
}
