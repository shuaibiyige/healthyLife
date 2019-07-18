package com.example.fit5046_assignment2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class Steps extends Fragment
{
    View steps;
    private EditText addSteps;
    private EditText newSteps;
    private ListView listView;
    DailyStepsDatabase db = null;
    List<HashMap<String, String>> listArray;

    String[] head = new String[] {"history", "time"};
    int[] dataCell = new int[] {R.id.step_history, R.id.time_history};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        steps = inflater.inflate(R.layout.fragment_steps, container, false);
        db = Room.databaseBuilder(getActivity(), DailyStepsDatabase.class, "dailySteps_database")
                .fallbackToDestructiveMigration()
                .build();
        addSteps = (EditText) steps.findViewById(R.id.addStep);
        Button addButton = (Button) steps.findViewById(R.id.addStepButton);
        Button showButton = (Button) steps.findViewById(R.id.showStepButton);
        listView = (ListView) steps.findViewById(R.id.listView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertDatabase addDatabase = new InsertDatabase();
                addDatabase.execute();
                addSteps.setText("");
            }
        });
        showButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
            }
        });
        return steps;
    }

    private class InsertDatabase extends AsyncTask<Void, Void, Integer>
    {

        @Override
        protected Integer doInBackground(Void... params)
        {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            if (!Validation.isEmpty(addSteps.getText().toString()) && Validation.isDigit(addSteps.getText().toString()))
            {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                String time = "" + hour + ":" + minute + ":" + second;

                String step = addSteps.getText().toString();
                int steps = Integer.parseInt(step);
                DailyStepsDB dailyStepsDB = new DailyStepsDB(username, steps, time);
                long id = db.dailyStepsDAO().insert(dailyStepsDB);
                return 0;
            }
            else
                return 1;
        }
        @Override
        protected void onPostExecute(Integer result)
        {
            if (result == 0)
                Toast.makeText(getActivity(), "Add successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "steps cannot be empty or non-numeric", Toast.LENGTH_SHORT).show();
        }
    }

    private class ReadDatabase extends AsyncTask<Void, Void, List<HashMap<String, String>>>
    {
        @Override
        protected List<HashMap<String, String>> doInBackground(Void... params)
        {
            int total_steps = 0;
            List<DailyStepsDB> dailyStepsDBS = db.dailyStepsDAO().getAll();
            listArray = new ArrayList<>();
            if (!(dailyStepsDBS.isEmpty() || dailyStepsDBS == null))
            {
                for (DailyStepsDB a: dailyStepsDBS)
                {
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("history", String.valueOf(a.getSteps()));
                    map1.put("time", a.getTime());
                    listArray.add(map1);
                    total_steps = total_steps + a.getSteps();
                }
            }
            else
            {
                HashMap<String, String> map = new HashMap<>();
                map.put("history", "no data");
                map.put("time", "no data");
                listArray.add(map);
            }
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
            String id = String.valueOf(sharedPreferences.getInt("id", 0));

            SharedPreferences.Editor editor = getActivity().getSharedPreferences(id, Context.MODE_PRIVATE).edit();
            editor.putInt("totalStep", total_steps);
            editor.apply();
            return listArray;
        }

        @Override
        protected void onPostExecute(final List<HashMap<String, String>> input)
        {
            SimpleAdapter myListAdapter = new SimpleAdapter(getActivity(), listArray, R.layout.list_view, head, dataCell);

            listView.setAdapter(myListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    for(String key : input.get(position).keySet())
                    {
                        if (key.equals("time"))
                            alertDialog(input.get(position).get(key));
                    }
                }
            });
        }

        public void alertDialog(final String position)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final AlertDialog dialog = builder.create();
            View dialogView = View.inflate(getActivity(), R.layout.alert_dialog, null);
            dialog.setView(dialogView);
            dialog.show();

            newSteps = (EditText) dialogView.findViewById(R.id.newStep);
            Button confirm = (Button) dialogView.findViewById(R.id.btn_confirm);
            Button cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String newStep = newSteps.getText().toString();
                    if (newStep.isEmpty() || !Validation.isDigit(newStep))
                        Toast.makeText(getActivity(), "your step can not be empty or non-numeric", Toast.LENGTH_SHORT).show();
                    else
                    {
                        UpdateDatabase updateDatabase = new UpdateDatabase();
                        updateDatabase.execute(String.valueOf(position), newStep);
                        dialog.dismiss();
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });
        }
    }

    private class UpdateDatabase extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            DailyStepsDB dailyStepsDB = db.dailyStepsDAO().findByTime(params[0]);
            if (dailyStepsDB == null)
                return "no data found";
            else
            {
                dailyStepsDB.setSteps(Integer.parseInt(params[1]));
                db.dailyStepsDAO().updateUsers(dailyStepsDB);
            }
            return "";
        }
        @Override
        protected void onPostExecute(String details)
        {
            if(details.equals("no data found"))
            {
                Toast.makeText(getActivity(), "update failed", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getActivity(), "update successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
