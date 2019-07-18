package com.example.fit5046_assignment2;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CalorieTracker extends Fragment
{
    View tracker;
    DailyStepsDatabase db = null;
    private  ProgressBar progressBar;
    private String goal;
    private TextView consumed;
    private Button deleteButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        tracker = inflater.inflate(R.layout.fragment_tracker, container, false);
        progressBar = tracker.findViewById(R.id.myprogressbar);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(String.valueOf(id), MODE_PRIVATE);
        goal = sharedPreferences2.getString("everyGoal", "0");

        TextView goalText = tracker.findViewById(R.id.totalGoalView2);
        if (!Validation.isEmpty(goal))
            goalText.setText(goal);
        consumed = (TextView) tracker.findViewById(R.id.consumedGoalView1);
        deleteButton = (Button) tracker.findViewById(R.id.deleteAll);

        CalorieBurned calorieBurned = new CalorieBurned();
        calorieBurned.execute();

        db = Room.databaseBuilder(getActivity(), DailyStepsDatabase.class, "dailySteps_database")
                .fallbackToDestructiveMigration()
                .build();

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("id", 0);
                String strId = String.valueOf(userId);

                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(strId, Context.MODE_PRIVATE);
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

                SharedPreferences.Editor editor = getActivity().getSharedPreferences(strId, Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour == 23 && minute == 59)
        {
            Intent intent = new Intent(getActivity(), ScheduledIntentService.class);
            getActivity().startService(intent);
        }
        return tracker;
    }

    private class CalorieBurned extends AsyncTask<Void, Void, List<String>>
    {
        @Override
        protected List<String> doInBackground(Void... params)
        {
            Date now = new Date(System.currentTimeMillis());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String time = df.format(now);

            List<String> output = new ArrayList<>();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", MODE_PRIVATE);
            int id = sharedPreferences.getInt("id", 0);

            String calPerStep = RestMethod.caloriesBurnedPerStep(id);
            String calAtRest = RestMethod.caloriesBurnedAtRest(id);
            String totalCalorieConsumed = RestMethod.totalCalorieConsumed(id, time);    //simulate intentService, not in real, date is not changed

            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(String.valueOf(id), Context.MODE_PRIVATE);
            int totalStep = sharedPreferences2.getInt("totalStep",0);

            output.add(String.valueOf(totalStep));
            output.add(calPerStep);
            output.add(calAtRest);
            output.add(totalCalorieConsumed);
            return output;
        }

        @Override
        protected void onPostExecute(List<String> input)
        {
            int steps = Integer.parseInt(input.get(0));

            double calPerStep = Double.parseDouble(input.get(1));
            double calAtRest = Double.parseDouble(input.get(2));
            String tc = input.get(3);
            int totalConsumed = Double.valueOf(tc).intValue();

            int burned = (int)(calAtRest + calPerStep * steps);

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
            String id = String.valueOf(sharedPreferences.getInt("id", 0));

            SharedPreferences.Editor editor = getActivity().getSharedPreferences(id, Context.MODE_PRIVATE).edit();
            editor.putInt("burned", burned);
            editor.putInt("totalConsumed", totalConsumed);
            editor.apply();

            TextView burnedText = tracker.findViewById(R.id.burnedGoalView1);
            TextView remainingText = tracker.findViewById(R.id.remainingGoalView1);
            TextView stepTaken = tracker.findViewById(R.id.totalStepsText);

            burnedText.setText(String.valueOf(burned));
            stepTaken.setText(String.valueOf(steps));
            consumed.setText(String.valueOf(totalConsumed));

            if (!Validation.isEmpty(goal) && Integer.valueOf(goal) != 0)
            {
                int remaining = Integer.valueOf(goal) - burned + totalConsumed;
                if (remaining < 0)
                {
                    progressBar.setProgress(100);
                    remainingText.setText("0");
                }
                else if (remaining >= Integer.valueOf(goal))
                {
                    progressBar.setProgress(0);
                    remainingText.setText(String.valueOf(remaining));
                }
                else
                {
                    Double progress1 = ((Double.valueOf(goal) - remaining) / Double.valueOf(goal)) * 100;
                    int progress2 = progress1.intValue();
                    progressBar.setProgress(progress2);
                    remainingText.setText(String.valueOf(remaining));
                }
            }
            else
            {
                remainingText.setText("0");
            }
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
            Toast.makeText(getActivity(), "Add consumption Successfully", Toast.LENGTH_SHORT).show();
        }
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
            Toast.makeText(getActivity(), "upload successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
