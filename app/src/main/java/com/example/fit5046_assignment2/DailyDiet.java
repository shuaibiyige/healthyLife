package com.example.fit5046_assignment2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyDiet extends Fragment
{
    View dailyDiet;
    private boolean initial;
    private String loa;
    private List<String> categoryList;
    private List<String> foodName;
    private Spinner spinner1;
    private Spinner spinner2;
    private TextView text;
    private EditText edit;
    private EditText amountForConsumption;
    private Button search;
    private Button notFound;
    private Button addToConsumption;
    private TextView info;
    private TextView infoOfInfo;
    private TextView notFoundText;
    private EditText notFoundEdit;
    private Button notFoundButton;
    private List<String> foodObject;
    private MyImageView myImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dailyDiet = inflater.inflate(R.layout.fragment_daily_diet, container, false);
        spinner1 = (Spinner) dailyDiet.findViewById(R.id.category);
        spinner2 = (Spinner) dailyDiet.findViewById(R.id.food);

        notFound = (Button) dailyDiet.findViewById(R.id.notFound);
        addToConsumption = (Button) dailyDiet.findViewById(R.id.addAmount);
        text = (TextView) dailyDiet.findViewById(R.id.foodName_text);
        edit = (EditText) dailyDiet.findViewById(R.id.foodName_enter);
        amountForConsumption = (EditText) dailyDiet.findViewById(R.id.amount);
        search = (Button) dailyDiet.findViewById(R.id.search);

        notFoundText = (TextView) dailyDiet.findViewById(R.id.notFoundText);
        notFoundEdit = (EditText) dailyDiet.findViewById(R.id.notFoundAmount);
        notFoundButton = (Button) dailyDiet.findViewById(R.id.addNotFoundAmount);

        infoOfInfo = (TextView) dailyDiet.findViewById(R.id.infoOfInfo);
        info = (TextView) dailyDiet.findViewById(R.id.info);
        myImageView = (MyImageView) dailyDiet.findViewById(R.id.food_image);

        text.setVisibility(View.INVISIBLE);
        edit.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);

        notFoundText.setVisibility(View.INVISIBLE);
        notFoundEdit.setVisibility(View.INVISIBLE);
        notFoundButton.setVisibility(View.INVISIBLE);

        notFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (text.getVisibility() == View.INVISIBLE)
                {
                    text.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    notFoundText.setVisibility(View.VISIBLE);
                    notFoundEdit.setVisibility(View.VISIBLE);
                    notFoundButton.setVisibility(View.VISIBLE);

                    infoOfInfo.setVisibility(View.VISIBLE);
                    info.setVisibility(View.VISIBLE);
                    myImageView.setVisibility(View.VISIBLE);
                    addNewFood();
                }
                else
                {
                    text.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.INVISIBLE);
                    search.setVisibility(View.INVISIBLE);
                    notFoundText.setVisibility(View.INVISIBLE);
                    notFoundEdit.setVisibility(View.INVISIBLE);
                    notFoundButton.setVisibility(View.INVISIBLE);

                    infoOfInfo.setVisibility(View.INVISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    myImageView.setVisibility(View.INVISIBLE);
                }
            }
        });

        FindCategoryAsyncTask findCategoryAsyncTask = new FindCategoryAsyncTask();
        findCategoryAsyncTask.execute();
        return dailyDiet;
    }

    private class FindCategoryAsyncTask extends AsyncTask<String, Void, List<String>>
    {
        @Override
        protected List<String> doInBackground (String...params)
        {
            categoryList = RestMethod.getFoodCategory();
            return categoryList;
        }

        protected void onPostExecute(List<String> input)
        {
            spinner(input, spinner1);
        }
    }

    private class FindFoodNameSyncTask extends AsyncTask<String, Void, List<String>>
    {
        @Override
        protected List<String> doInBackground (String...params)
        {
            foodName = RestMethod.getFoodName(params[0]);
            return foodName;
        }

        protected void onPostExecute(List<String> input)
        {
            spinner(input, spinner2);
        }
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, List<String>>
    {
        @Override
        protected List<String> doInBackground(String... params)
        {
            List<String> result = new ArrayList<>();
            String a = FoodAPI.search(params[0]);
            String b = CustomerSearch.search(params[0], new String[]{"num"}, new String[]{"1"});
            result.add(a);
            result.add(b);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> result)
        {
            info.setText(CustomerSearch.getSnippet(result.get((1))));
            final List<String> foodInfo = FoodAPI.getSnippet(result.get(0));
            if (foodInfo.size() == 4)
            {
                foodObject = new ArrayList<>();
                String newFood = edit.getText().toString();
                final String fat = foodInfo.get((0));
                final String calorie = foodInfo.get((1));
                final String category = foodInfo.get((2));
                String image = foodInfo.get(3);
                myImageView.setImageURL(image);
                foodObject.add(newFood);
                foodObject.add(category);
                foodObject.add(calorie);
                foodObject.add(fat);
                infoOfInfo.setText("FAT: " + fat + ", CALORIE: " + calorie);
                Button add = (Button) getView().findViewById(R.id.addNotFoundAmount);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amount = notFoundEdit.getText().toString();
                        if (!Validation.isEmpty(amount) && Validation.isDigit(amount))
                        {
                            alertDialog(foodObject, Integer.parseInt(amount));
                            foodInfo.clear();
                        }
                        else
                            Toast.makeText(getActivity(), "amount should not be empty or non-numeric", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Button add = (Button) getView().findViewById(R.id.addNotFoundAmount);
                infoOfInfo.setText("Not found related food information");
                myImageView.setImageResource(0);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Not found any food information", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private class FindFoodIdSyncTask extends AsyncTask<String, Void, Integer>
    {
        @Override
        protected Integer doInBackground (String...params)
        {
            int foodId = RestMethod.findIdByFoodName(params[0]);
            return foodId;
        }

        protected void onPostExecute(final Integer input)
        {
            addToConsumption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String middleAmount = amountForConsumption.getText().toString();
                    if (!Validation.isEmpty(middleAmount) || !Validation.isDigit(middleAmount))
                        addConsumption(input, Integer.valueOf(middleAmount));
                    else
                        Toast.makeText(getActivity(), "amount cannot be empty or non-numeric", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void spinner(List<String> input, final Spinner spinner)
    {
        initial = true;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, input);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (initial)
                    view.setVisibility(View.INVISIBLE);
                else
                {
                    loa = spinner.getItemAtPosition(position).toString();
                    if (spinner == spinner1)
                    {
                        FindFoodNameSyncTask findFoodNamesyncTask = new FindFoodNameSyncTask();
                        findFoodNamesyncTask.execute(loa);
                    }
                    else
                    {
                        FindFoodIdSyncTask findFoodIdSyncTask = new FindFoodIdSyncTask();
                        findFoodIdSyncTask.execute(loa);
                    }
                }
                initial = false;
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                loa = "";
            }
        });
    }

    public void addNewFood()
    {
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String newFood = edit.getText().toString();
                if (!Validation.isEmpty(newFood) && !Validation.isDigit(newFood))
                {
                    SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
                    searchAsyncTask.execute(newFood);
                }
                else if (Validation.isEmpty(newFood))
                    Toast.makeText(getActivity(), "Food name cannot be empty", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Food name is not valid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void alertDialog(final List<String> input, final int amount)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(getActivity(), R.layout.alert_category, null);
        dialog.setView(dialogView);
        dialog.show();

        final EditText e = (EditText) dialogView.findViewById(R.id.alert_category);
        TextView recommend = (TextView) dialogView.findViewById(R.id.alert_view2);
        recommend.setText("Recommend category: " + input.get(1));
        Button confirm = (Button) dialogView.findViewById(R.id.alert_confirm);
        Button cancel = (Button) dialogView.findViewById(R.id.alert_cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String newCategory = e.getText().toString();
                if (newCategory.isEmpty() || newCategory == null || Validation.isDigit(newCategory))
                    Toast.makeText(getActivity(), "Enter a correct category", Toast.LENGTH_SHORT).show();
                else
                {
                    FindFoodId findFoodId = new FindFoodId();
                    findFoodId.execute(input.get(0), newCategory, input.get(2), "unknown", input.get(3), String.valueOf(amount));
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

    private class FindFoodId extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground (String...params)
        {
            BigDecimal bd = new BigDecimal("1.0");
            int currentId = RestMethod.countFood();
            Integer id = currentId + 1;
            Food food = new Food(id, params[0], params[1], Double.valueOf(params[2]).intValue(), params[3], Double.valueOf(params[4]).intValue(), bd);
            RestMethod.addFood(food);
            addConsumption(id, Integer.parseInt(params[5]));
            return null;
        }

        protected void onPostExecute(Void param)
        {
            Toast.makeText(getActivity(), "Add food Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void addConsumption(int foodId, int quantity)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", 0);
        if (userId != 0)
        {
            Date consumeDate = new Date(System.currentTimeMillis());
            ConsumptionsPK consumptionsPK = new ConsumptionsPK(userId, consumeDate, foodId);
            Foods foods = new Foods(foodId);
            User user = new User(userId);
            Consumptions consumptions = new Consumptions(consumptionsPK, user, foods, quantity);
            CreateConsumption createConsumption = new CreateConsumption();
            createConsumption.execute(consumptions);
        }
        else
            Toast.makeText(getActivity(), "Add failed", Toast.LENGTH_SHORT).show();
    }

    private class CreateConsumption extends AsyncTask<Consumptions, Void, Void>
    {
        @Override
        protected Void doInBackground (Consumptions...params)
        {
            RestMethod.addConsumption(params[0]);
            return null;
        }

        protected void onPostExecute(Void param)
        {
            Toast.makeText(getActivity(), "Add consumption Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
