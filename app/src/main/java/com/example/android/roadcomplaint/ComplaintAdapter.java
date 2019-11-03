package com.example.android.roadcomplaint;

/**
 * Created by Nichay Jain on 11-06-2019.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ComplaintAdapter extends ArrayAdapter<Details> {

    private Context mContext;
    private List<Details> complaintlist = new ArrayList<>();

    public ComplaintAdapter(@NonNull Context context,   ArrayList<Details> list) {
        super(context,0,list);
        mContext = context;
        complaintlist= list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        Details currentcomplaint = complaintlist.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.name);
         name.setText(currentcomplaint.getName());

        TextView description = (TextView) listItem.findViewById(R.id.description);
        description.setText(currentcomplaint.getDescription());
        TextView address=(TextView)listItem.findViewById(R.id.address);
        address.setText(currentcomplaint.getAddress());

        TextView latitude = (TextView) listItem.findViewById(R.id.latitude);
        latitude.setText(currentcomplaint.getLatitude());

        TextView longitude = (TextView) listItem.findViewById(R.id.longitude);
        longitude.setText(currentcomplaint.getLongitude());

        byte[] decodedString = Base64.decode(currentcomplaint.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView imageView=(ImageView)listItem.findViewById(R.id.retriveimage);
        imageView.setImageBitmap(decodedByte);


        return listItem;
    }
}