package com.example.fit5046_assignment2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FoodAPI
{
    public static String search(String query)
    {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter="";
        try
        {
            url = new URL("https://api.edamam.com/api/food-database/parser?ingr=" + query + "&app_id=4891b688&app_key=d6989e4ce2e3418010f8499be6baba19");
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

    public static List<String> getSnippet(String result)
    {
        List<String> output = new ArrayList<>();
        String raw = null;
        String fat = null;
        String calorie = null;
        String category = null;
        String image = null;
        try
        {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("parsed");
            if(jsonArray != null && jsonArray.length() > 0)
            {
                raw = jsonArray.getJSONObject(0).getString("food");
                JSONObject jsonObject2 = new JSONObject(raw);
                JSONObject jsonObject3 = jsonObject2.getJSONObject("nutrients");
                try
                {
                    fat = jsonObject3.getString("FAT");
                    calorie = jsonObject3.getString("ENERC_KCAL");
                    image = jsonObject2.getString("image");
                    String middleCategory = jsonObject2.getString("label");
                    if (middleCategory.contains(","))
                        category = middleCategory.substring(0, middleCategory.indexOf(","));
                    else
                        category = middleCategory;
                }
                catch (Exception e)
                {
                    if (e.getMessage().equals("No value for FAT"))
                    {
                        fat = "0";
                        calorie = jsonObject3.getString("ENERC_KCAL");
                        image = jsonObject2.getString("image");
                        String middleCategory = jsonObject2.getString("label");
                        category = middleCategory.substring(0, middleCategory.indexOf(","));
                    }
                    if (e.getMessage().equals("No value for ENERC_KCAL"))
                    {
                        calorie = "0";
                        image = jsonObject2.getString("image");
                        String middleCategory = jsonObject2.getString("label");
                        category = middleCategory.substring(0, middleCategory.indexOf(","));
                    }
                }
                output.add(fat);
                output.add(calorie);
                output.add(category);
                output.add(image);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            output.add("Not Found");
        }
        return output;
    }
}
