package com.example.fit5046_assignment2;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard extends Fragment
{
    View dashboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);
        AnalogClock time = (AnalogClock) dashboard.findViewById(R.id.time);

        TextView date = (TextView) dashboard.findViewById(R.id.date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date showDate = new Date(System.currentTimeMillis());
        date.setText(simpleDateFormat.format(showDate));

        TextView welcome = (TextView) dashboard.findViewById(R.id.welcome);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString("firstName", null);
        welcome.setText("Welcome, " + firstName);

        final EditText goalText = (EditText) dashboard.findViewById(R.id.setGoal);

        Button setGoal = (Button) dashboard.findViewById(R.id.goalButton);
        setGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goal = goalText.getText().toString();
                if (!Validation.isEmpty(goal) && Validation.isDigit(goal))
                {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
                    String id = String.valueOf(sharedPreferences.getInt("id", 0));

                    if (!id.equals("0") && id != null)
                    {
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(id, Context.MODE_PRIVATE).edit();
                        editor.putString("everyGoal", goal);
                        editor.apply();
                        Toast.makeText(getActivity(), "Set Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getActivity(), "Couldn't find user", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Goal cannot be empty or non-numeric", Toast.LENGTH_SHORT).show();
            }
        });
        return dashboard;
    }
}
