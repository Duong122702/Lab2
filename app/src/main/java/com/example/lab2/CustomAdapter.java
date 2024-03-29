package com.example.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Model> {
    ArrayList<Model> modelArrayList;
    Context context;

    public CustomAdapter(ArrayList<Model> modelArrayList, Context context) {
        super(context, R.layout.list_view, modelArrayList);
        this.modelArrayList = modelArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.nameText.setText(modelArrayList.get(position).getName());
        viewHolder.phoneNumberText.setText(modelArrayList.get(position).getPhoneNumber());
        viewHolder.imageView.setImageURI(modelArrayList.get(position).getFilePath());

        return  view ;
    }

    private static class ViewHolder  {

        TextView nameText;
        TextView phoneNumberText;
        ImageView imageView;


        ViewHolder(View view) {
            nameText = (TextView) view.findViewById(R.id.name);
            phoneNumberText = (TextView) view.findViewById(R.id.phoneNumber);

            imageView = (ImageView) view.findViewById(R.id.imageListView);
        }
    }




}
