package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;


public final class QueryUtils {

    /**
     * Return a list of {@link Location} objects that has been built up from
     * parsing a JSON response.
     */
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    public static List<Location> extractEarthquakes(String USGS_REQUEST_URL) {


        URL url = createUrl(USGS_REQUEST_URL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Location> earthquakes = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return earthquakes;
    }

    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            return null;
        }
        return url;
    }

    private static  String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
                return "";
        } catch (IOException e) {
            return "";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Location> extractFeatureFromJson(String jsonresponse) {

        if (TextUtils.isEmpty(jsonresponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<Location> earthquakes = new LinkedList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject root = new JSONObject(jsonresponse);
            JSONArray feat = root.optJSONArray("features");

            for(int i=0; i<feat.length(); i++) {
                JSONObject arrInd = feat.getJSONObject(i);
                JSONObject prop = arrInd.getJSONObject("properties");
                Double mag = prop.getDouble("mag");
                String place = prop.getString("place");
                String url = prop.getString("url");
                long time = prop.getLong("time");
                DecimalFormat formatter = new DecimalFormat("0.0");
                String output = formatter.format(mag);
                //Date dateObject = new Date(time);
                //SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                //String dateToDisplay = dateFormat.format(dateObject);
                //SimpleDateFormat  timeFormat = new SimpleDateFormat("HH:mm a");
                //String timeToDisplay = timeFormat.format(dateObject);
                earthquakes.add(new Location(output, place, time, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}

