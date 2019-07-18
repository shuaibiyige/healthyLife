package com.example.fit5046_assignment2;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Report extends Fragment
{
    View report;
    private PieChart mChart;
    private BarChart mBarChart;
    private ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    private Calendar calendar;
    private Button button1;
    private Button button2;
    private EditText start;
    private EditText end;
    private String time;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        report = inflater.inflate(R.layout.fragment_report, container, false);
        mChart = report.findViewById(R.id.pieChart);
        mChart.setVisibility(View.INVISIBLE);
        mBarChart = report.findViewById(R.id.bar_chart);
        start = (EditText) report.findViewById(R.id.start);
        end = (EditText) report.findViewById(R.id.end);
        mBarChart.setVisibility(View.INVISIBLE);
        time = "";

        button1 = (Button) report.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDialogPick();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                datePicker(1);
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            datePicker(2);
        }
        });

        button2 = (Button) report.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String begin = start.getText().toString();
                String finish = end.getText().toString();
                if (!Validation.isEmpty(begin) && !Validation.isEmpty(finish))
                {
                    if (mChart.getVisibility() == View.VISIBLE)
                    {
                        mChart.setVisibility(View.INVISIBLE);
                        //mBarChart.setVisibility(View.VISIBLE);
                    }
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
                    int userId = sharedPreferences.getInt("id", 0);
                    String id = String.valueOf(userId);
                    FindHistorySyncTask findHistorySyncTask = new FindHistorySyncTask();
                    findHistorySyncTask.execute(id, begin, finish);
                }
                else
                {
                    Toast.makeText(getActivity(), "both date should not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return report;
    }

    private void showDialogPick()
    {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", 0);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                if (mBarChart.getVisibility() == View.VISIBLE)
                {
                    mBarChart.setVisibility(View.INVISIBLE);
                    mChart.setVisibility(View.VISIBLE);
                }
                else
                    mChart.setVisibility(View.VISIBLE);
                FindReport findReport = new FindReport();
                findReport.execute(String.valueOf(userId), time);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setData(int consumed, int burned, int remain)
    {
        entries.clear();
        if (remain < 0)
        {
            entries.add(new PieEntry(consumed, "consumed"));
            entries.add(new PieEntry(burned, "burned"));
        }
        else {
            entries.add(new PieEntry(consumed, "consumed"));
            entries.add(new PieEntry(burned, "burned"));
            entries.add(new PieEntry(remain, "remaining"));
        }

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 5, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(time);
        mChart.setCenterTextSizePixels(90);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(50f);
        mChart.setTransparentCircleRadius(31f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(17f);

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(17f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private class FindReport extends AsyncTask<String, Void, List<Double>>
    {
        @Override
        protected List<Double> doInBackground(String... params)
        {
            int id = Integer.parseInt(params[0]);
            time = params[1];
            String result = RestMethod.findReport(id, time);
            if (result != null && !result.equals(""))
            {
                String[] list = result.split(", ");
                Double consumed = Double.valueOf(list[0].substring(list[0].indexOf(":") + 1));
                Double burned = Double.valueOf(list[1].substring(list[1].indexOf(":") + 1));
                Double remain = Double.valueOf(list[2].substring(list[2].indexOf(":") + 1));
                List<Double> data = new ArrayList<>();
                data.add(consumed);
                data.add(burned);
                data.add(remain);
                return data;
            }
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Double> input)
        {
            if (input != null)
            {
                int consumed = input.get(0).intValue();
                int burned = input.get(1).intValue();
                int remain = input.get(2).intValue();
                setData(consumed, burned, remain);
            }
            else
            {
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                mChart.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void datePicker(final int input)
    {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth)
            {
                time = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                if (input == 1)
                    start.setText(time);
                else
                    end.setText(time);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private class FindHistorySyncTask extends AsyncTask<String, Void, List<List>>
    {
        @Override
        protected List<List> doInBackground (String...params)
        {
            List<Map<String, Integer>> list = RestMethod.findHistoryReport(Integer.valueOf(params[0]), params[1], params[2]);
            if (list.size() != 0)
            {
                List<Integer> result1 = new ArrayList<>();
                List<Integer> result2 = new ArrayList<>();
                List<List> result = new ArrayList<>();
                for (Map<String, Integer> a : list) {
                    int consumed = a.get("totalCaloriesConsumed");
                    int burned = a.get("totalCaloriesBurned");
                    result1.add(consumed);
                    result2.add(burned);
                }
                result.add(result1);
                result.add(result2);
                return result;
            }
            else
                return null;
        }

        protected void onPostExecute(List<List> input) {
            if (input != null)
            {
                List<Integer> consumed = input.get(0);
                List<Integer> burned = input.get(1);

                Integer[] group1 = new Integer[consumed.size()];
                Integer[] group2 = new Integer[burned.size()];

                for (int i = 0; i < consumed.size(); i++) {
                    group1[i] = consumed.get(i);
                    group2[i] = burned.get(i);
                }

                List<BarEntry> entriesGroup1 = new ArrayList<>();
                List<BarEntry> entriesGroup2 = new ArrayList<>();

                for (int i = 0; i < group1.length; i++) {
                    entriesGroup1.add(new BarEntry(i, group1[i]));
                    entriesGroup2.add(new BarEntry(i, group2[i]));
                }
                BarDataSet set1 = new BarDataSet(entriesGroup1, "total consumed");
                BarDataSet set2 = new BarDataSet(entriesGroup2, "total burned");

                set1.setColor(Color.rgb(104, 241, 175));
                set2.setColor(Color.rgb(164, 228, 251));

                float groupSpace = 0.06f;
                float barSpace = 0.02f;
                float barWidth = 0.20f;

                BarData data = new BarData(set1, set2);
                data.setBarWidth(barWidth);
                mBarChart.setData(data);
                mBarChart.groupBars(0f, groupSpace, barSpace);
                XAxis xAxis = mBarChart.getXAxis();
                xAxis.setCenterAxisLabels(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setAxisMinimum(0f);
                xAxis.setEnabled(false);
                mBarChart.getDescription().setEnabled(false);

                mBarChart.setScaleEnabled(false);
                mBarChart.setDragEnabled(true);
                mBarChart.setHighlightPerDragEnabled(true);
                mBarChart.setDrawGridBackground(false);
                mBarChart.setAutoScaleMinMaxEnabled(true);
                mBarChart.getAxisRight().setEnabled(false);
                mBarChart.setPinchZoom(true);

                Legend l = mBarChart.getLegend();
                l.setTextSize(13f);
                l.setFormToTextSpace(10f);
                l.setXEntrySpace(30f);

                mBarChart.invalidate();
                mBarChart.setVisibility(View.VISIBLE);
            }
            else {
                mBarChart.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "no data found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
