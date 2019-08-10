package com.example.android.quakereport;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Location> {

    public EarthquakeAdapter(Context context, List<Location> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View ConvertView, ViewGroup parent) {

        View listItemView = ConvertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.location, parent, false);
        }

        final Location currLocation = getItem(position);

        TextView magTextView = listItemView.findViewById(R.id.mag);
        magTextView.setText(currLocation.getMagnitude());

        TextView loc_offset = listItemView.findViewById(R.id.location_offset);
        TextView pri_loc = listItemView.findViewById(R.id.primary_location);
        String str = currLocation.getCityName();
        int ind = str.indexOf("of");
        if(ind != -1) {
            loc_offset.setText(str.substring(0, ind+2));
            pri_loc.setText(str.substring(ind+3, str.length()));
        }
        else {
            loc_offset.setText(R.string.near_the);
            pri_loc.setText(str);
        }

        TextView dateTextView = listItemView.findViewById(R.id.date);
        Date dateObject = new Date(currLocation.getTime());
        String dateDisplay = formatDate(dateObject);
        dateTextView.setText(dateDisplay);
        TextView timeTextView = listItemView.findViewById(R.id.time);
        String timeDisplay= formatTime(dateObject);
        timeTextView.setText(timeDisplay);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currLocation.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
        return timeFormat.format(dateObject);
    }

    private int getMagnitudeColor(String mag) {
        int magFloor = (int) Math.floor(Double.parseDouble(mag));
        int magnitudeColorResourceId;
        switch (magFloor) {
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
