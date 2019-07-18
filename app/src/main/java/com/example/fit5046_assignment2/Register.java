package com.example.fit5046_assignment2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Register extends BaseActivity
{
    private String loa;
    private String gd;
    private Button register;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText height;
    private EditText weight;
    private EditText address;
    private EditText postcode;
    private EditText stepsPerMile;
    private EditText username;
    private EditText password;
    private EditText rePassword;
    private EditText birth;
    private Calendar calendar;
    private TextView passText;
    private TextView passText2;
    private boolean isOpenEye = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_register);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("Register");

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        address = (EditText) findViewById(R.id.address);
        postcode = (EditText) findViewById(R.id.postcode);
        stepsPerMile = (EditText) findViewById(R.id.stepsPerMile);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        rePassword = (EditText) findViewById(R.id.confirm_password);
        birth = (EditText) findViewById(R.id.dob);
        passText = (TextView) findViewById(R.id.password_textView);
        passText2 = (TextView) findViewById(R.id.password_textView2);

        final Spinner spinner = (Spinner) findViewById(R.id.levelOfActivity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                loa = spinner.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                loa = "1";
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton gender = (RadioButton) findViewById(checkedId);
                if (gender.getText().toString().equals("male"))
                    gd = "m";
                else
                    gd = "f";
            }
        });

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                datePicker();
            }
        });

        passText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye)
                {
                    isOpenEye = true;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    isOpenEye = false;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        passText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye)
                {
                    isOpenEye = true;
                    rePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    isOpenEye = false;
                    rePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String fn = firstName.getText().toString();
                String ln = lastName.getText().toString();
                String em = email.getText().toString();
                String he = height.getText().toString();
                String we = weight.getText().toString();
                String ad = address.getText().toString();
                String pc = postcode.getText().toString();
                String spm = stepsPerMile.getText().toString();
                String un = username.getText().toString();
                String pw = password.getText().toString();
                String repw = rePassword.getText().toString();
                String dob_middle = birth.getText().toString();
                String hash = HashPassword.MD5(pw);

                if(!Validation.isEmpty(fn) && !Validation.isEmpty(ln) && !Validation.isEmpty(em) && !Validation.isEmpty(he) && Validation.isDigit(he) && !Validation.isEmpty(we) && Validation.isDigit(we) && !Validation.isEmpty(ad) && !Validation.isEmpty(pc) && Validation.isDigit(pc) &&
                        !Validation.isEmpty(spm) && Validation.isDigit(spm) && !Validation.isEmpty(un) && !Validation.isEmpty(pw) && repw.equals(pw) && !Validation.isEmpty(dob_middle) && !Validation.isEmpty(gd) && Validation.isDigit(loa))
                {
                    RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask();
                    registerAsyncTask.execute(fn, ln, em, dob_middle, he, we, gd, ad, pc, loa, spm, un, hash);
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if (!repw.equals(pw))
                        Toast.makeText(Register.this, "The passwords do not match", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Register.this, "Some data type is wrong or empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class RegisterAsyncTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params)
        {
            SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = null;
            Date sign_up_date = null;
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String middleDate = year + "-" + month + "-" + day;
            try
            {
                dob = a.parse(params[3]);
                sign_up_date = a.parse(middleDate);
            }
            catch(Exception e)
            {
            }
            int currentId = RestMethod.countUser();
            Integer id = currentId + 1;
            Users user = new Users(id, params[0],params[1], params[2], dob, Double.valueOf(params[4]), Double.valueOf(params[5]), params[6], params[7], Integer.valueOf(params[8]), Integer.valueOf(params[9]), Double.valueOf(params[10]));
            Credentials credentials = new Credentials(id, params[11], params[12], sign_up_date);
            RestMethod.register(user);
            RestMethod.SignUp(credentials);

            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putString("firstName", params[0]);
            editor.apply();

            SharedPreferences.Editor editor2 = getSharedPreferences("user2", MODE_PRIVATE).edit();
            editor2.putString("username", params[11]);
            editor2.putInt("id", id);
            editor2.apply();

            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }

    private void datePicker()
    {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth)
            {
                String time = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                birth.setText(time);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
