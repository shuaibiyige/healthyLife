package com.example.fit5046_assignment2;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RestMethod
{
    private static final String BASE_URL = "http://10.0.2.2:41416/Assignment1/webresources/";

    public static String login(String username)
    {
        final String methodPath = "assignment1.credentials/findPasswordByUserName/" + username;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static void register(Users user)
    {
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "assignment1.users";
        try
        {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            String stringUserJson = gson.toJson(user);
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
    }

    public static void SignUp(Credentials credentials)
    {
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "assignment1.credentials";
        try
        {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            String stringCredentialsJson = gson.toJson(credentials);
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringCredentialsJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringCredentialsJson);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
    }

    public static int countUser()
    {
        final String methodPath = "assignment1.users/count/";
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result = Integer.parseInt(inStream.nextLine());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static String getFirstName(String username)
    {
        final String methodPath = "assignment1.users/findFirstNameByUserName/" + username;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static ArrayList<String> getFoodCategory()
    {
        final String methodPath = "assignment1.foods/findFoodCategory";
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        ArrayList<Food> result2 = new ArrayList<>();
        ArrayList<String> result3 = new ArrayList<>();
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
            Gson gson = new Gson();
            result2 = gson.fromJson(result, new TypeToken<ArrayList<Food>>(){}.getType());
            for (Food f: result2)
            {
                result3.add(f.getFoodCategory());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result3;
    }

    public static List<String> getFoodName(String input)
    {
        final String methodPath = "assignment1.foods/findByFoodCategory/" + input;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        ArrayList<Food> result2 = new ArrayList<>();
        ArrayList<String> result3 = new ArrayList<>();
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
            Gson gson = new Gson();
            result2 = gson.fromJson(result, new TypeToken<ArrayList<Food>>(){}.getType());
            for (Food f: result2)
            {
                result3.add(f.getFoodName());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result3;
    }

    public static String caloriesBurnedPerStep(int id)
    {
        final String methodPath = "assignment1.users/caloriesBurnedPerStep/" + id;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static String caloriesBurnedAtRest(int id)
    {
        final String methodPath = "assignment1.users/totalCaloriesBurnedAtRest/" + id;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static int findIdBySurname(String username)
    {
        final String methodPath = "assignment1.credentials/findIdByUserName/" + username;
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result = Integer.parseInt(inStream.nextLine());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static String findAddress(int id)
    {
        final String methodPath = "assignment1.users/findAddressById/" + id;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static int countFood()
    {
        final String methodPath = "assignment1.foods/count/";
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result = Integer.parseInt(inStream.nextLine());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static void addFood(Food food)
    {
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "assignment1.foods";
        try
        {
            Gson gson =new Gson();
            String stringFoodJson = gson.toJson(food);
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringFoodJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringFoodJson);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
    }

    public static void addConsumption(Consumptions consumptions)
    {
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "assignment1.consumptions";
        try
        {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            String stringConsumptionsJson = gson.toJson(consumptions);

            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringConsumptionsJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringConsumptionsJson);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
    }

    public static int findIdByFoodName(String foodName)
    {
        final String methodPath = "assignment1.foods/findIdByFoodName/" + foodName;
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result = Integer.parseInt(inStream.nextLine());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static String totalCalorieConsumed(int id, String date)
    {
        final String methodPath = "assignment1.consumptions/totalCaloriesConsumed/" + id + "/" + date;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static void addReport(Reports reports)
    {
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "assignment1.reports";
        try
        {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            String stringConsumptionsJson = gson.toJson(reports);

            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringConsumptionsJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringConsumptionsJson);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
    }

    public static String findReport(int id, String date)
    {
        final String methodPath = "assignment1.reports/findReport/" + id + "/" + date;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return result;
    }

    public static List<Map<String, Integer>> findHistoryReport(int id, String start, String end)
    {
        final String methodPath = "assignment1.reports/findPeriodReport2/" + id + "/" + start + "/" + end;
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        String raw = null;
        List<String> output = new ArrayList<>();
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                result += inStream.nextLine();
            }
            List<Map<String, Integer>> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObj;
            for(int i = 0 ; i < jsonArray.length() ; i ++)
            {
                jsonObj = (JSONObject)jsonArray.get(i);

                JSONObject jsonObject = new JSONObject(jsonObj.toString());
                Iterator<String> keyIter= jsonObject.keys();
                String key = "";
                Integer value = 0;
                Map<String, Integer> valueMap = new HashMap<String, Integer>();
                while (keyIter.hasNext())
                {
                    key = keyIter.next();
                    value = (Integer)jsonObject.get(key);
                    valueMap.put(key, value);
                }
                list.add(valueMap);
            }
            return list;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            conn.disconnect();
        }
        return null;
    }
}
