package com.example.fit5046_assignment2;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.Date;

public class ScheduledIntentService extends Service
{
    DailyStepsDatabase db = null;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                db = Room.databaseBuilder(ScheduledIntentService.this, DailyStepsDatabase.class, "dailySteps_database")
                        .fallbackToDestructiveMigration()
                        .build();

                SharedPreferences sharedPreferences = getSharedPreferences("user2", Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("id", 0);
                String strId = String.valueOf(userId);

                SharedPreferences sharedPreferences3 = getSharedPreferences(strId, MODE_PRIVATE);
                String goal = sharedPreferences3.getString("everyGoal", "0");

                SharedPreferences sharedPreferences2 = getSharedPreferences(strId, Context.MODE_PRIVATE);
                int totalStep = sharedPreferences2.getInt("totalStep",0);
                int burned = sharedPreferences2.getInt("burned", 0);
                int totalConsumed = sharedPreferences2.getInt("totalConsumed", 0);

                Date date = new Date(System.currentTimeMillis());
                User user = new User(userId);
                ReportsPK reportsPK = new ReportsPK(userId, date);
                Reports reports = new Reports(reportsPK, totalConsumed, burned, totalStep, goal, user);

                CreateReport createReport = new CreateReport();
                createReport.execute(reports);

                DeleteDatabase deleteDatabase = new DeleteDatabase();
                deleteDatabase.execute();

                SharedPreferences.Editor editor = getSharedPreferences(strId, Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int hour = 24 * 60 * 60 * 1000;            //repeat for each 24 hours
        long triggerTime = SystemClock.elapsedRealtime() + hour;
        Intent i = new Intent(this, ScheduledIntentService.class);
        PendingIntent p = PendingIntent.getService(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, p);
        return super.onStartCommand(intent,flags,startId);
    }

    protected class DeleteDatabase extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            db.dailyStepsDAO().deleteAll();
            return null;
        }

        protected void onPostExecute(Void param)
        {
            Toast.makeText(ScheduledIntentService.this, "upload successfully", Toast.LENGTH_SHORT).show();
        }
    }

    protected class CreateReport extends AsyncTask<Reports, Void, Void>
    {
        @Override
        protected Void doInBackground (Reports...params)
        {
            RestMethod.addReport(params[0]);
            return null;
        }

        protected void onPostExecute(Void param)
        {
            Toast.makeText(ScheduledIntentService.this, "Add consumption Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}


