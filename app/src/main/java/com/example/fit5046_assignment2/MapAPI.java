package com.example.fit5046_assignment2;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class MapAPI
{
    private static final String API_KEY = "AIzaSyA3JPDb72rmPPdkxKMq09wiRkDisdyIdFc";

    public static JSONArray findParks(LatLng latLng)
    {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        try
        {
            url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?&location=" + latLng.latitude + "," + latLng.longitude + "&radius=5000&types=park&key=" + API_KEY);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine())
            {
                textResult += scanner.nextLine();
            }

            JSONObject jsonObject = new JSONObject(textResult);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            return jsonArray;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            connection.disconnect();
        }
        return null;
    }
}
