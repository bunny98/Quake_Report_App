package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    public CustomAdapter(Activity context, ArrayList<EarthQuake> earthQuakes){
        super(context, 0, earthQuakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if(listView==null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        EarthQuake currentQuake = (EarthQuake) getItem(position);

        TextView magnitude = (TextView) listView.findViewById(R.id.mag);
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        magnitude.setText(magnitudeFormat.format(currentQuake.getMagnitude()));
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(currentQuake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        TextView place = (TextView) listView.findViewById(R.id.place);
        TextView offset = (TextView) listView.findViewById(R.id.offset);

        String addr = currentQuake.getPlace();
        String[] parts;

        if(addr.contains("of")) {
            parts = addr.split("(?<=of)");
            place.setText(parts[1]);
            offset.setText(parts[0]);

        }
        else{
            place.setText(addr);
            offset.setText("Near the");
        }

        TextView date = (TextView) listView.findViewById(R.id.date);
        date.setText(currentQuake.getDate());

        TextView time = (TextView) listView.findViewById(R.id.time);
        time.setText(currentQuake.getTime());

        return listView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
