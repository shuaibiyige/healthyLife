package com.example.fit5046_assignment2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CustomerSearch
{
    private static final String API_KEY = "AIzaSyA3JPDb72rmPPdkxKMq09wiRkDisdyIdFc";
    private static final String SEARCH_ID_cx = "015315121732145990883:xxd2elusami";

    public static String search(String keyword, String[] params, String[] values)
    {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter="";
        if (params!=null && values!=null)
        {
            for (int i =0; i < params.length; i ++)
            {
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try
        {
            url = new URL("https://www.googleapis.com/customsearch/v1?key="+ API_KEY+ "&cx="+ SEARCH_ID_cx + "&q="+ keyword + query_parameter);
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            connection.disconnect();
        }
        return textResult;
    }

    public static String getSnippet(String result)
    {
        String snippet = null;
        String raw = null;
        try
        {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > 0)
            {
                raw = jsonArray.getJSONObject(0).getString("snippet");
                raw = raw.replace("\n", "");
                raw = raw.replace(":", "");
                snippet = raw.substring(raw.indexOf("1") + 3, raw.indexOf("2") - 1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            snippet = "DEFINITION NOT FOUND, TRY TO USE OTHER KEYWORD";
        }
        return snippet;
    }
}