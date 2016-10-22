package com.first.kritikm.hdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kritikm on 22-Oct-16.
 */

public class ImageAdapter extends ArrayAdapter<Uri>{

    private ArrayList<Uri> uris;

    public ImageAdapter(Context context, int resourceID, ArrayList<Uri> uris)
    {
        super(context, resourceID);
        this.uris = new ArrayList<>(uris);
    }


    @Override
    public int getCount() {
        return uris.size();
    }

    @Override
    public Uri getItem(int position) {
        return uris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(ArrayList<Uri> uris)
    {
        this.uris.addAll(uris);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if(view == null)
        {
            holder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.grid_row, null);
            holder.imageView = (ImageView)view.findViewById(R.id.imageView);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.imageView.setImageBitmap(ImageHelper.getResizedBitmap(getContext(),getItem(position)));
        return view;
    }

    public void add(Uri uri)
    {
        uris.add(uri);
    }
    private static class ViewHolder
    {
        ImageView imageView;
    }

    public Bitmap pathToBitmap(Uri uri)
    {
        final File file = new File(uri.getPath());
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

}
