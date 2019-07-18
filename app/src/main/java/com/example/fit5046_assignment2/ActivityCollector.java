package com.example.fit5046_assignment2;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityCollector
{
    public static ArrayList<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity)
    {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity)
    {
        activities.remove(activity);
    }

    public static void finishAll()
    {
        for (Activity activity: activities)
        {
            if (!activity.isFinishing())
            {
                activity.finish();
            }
        }
        activities.clear();
    }
}
