package com.example.fit5046_assignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity
{
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private TextView passText;
    private boolean isOpenEye = false;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_login);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("Login in");
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginButton);
        register = (Button) findViewById(R.id.register);
        passText = (TextView) findViewById(R.id.paTextView);

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

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = username.getText().toString();
                if (!Validation.isEmpty(name) && !Validation.isEmpty(password.getText().toString()))
                {
                    loginRestAsyncTask getPassword = new loginRestAsyncTask();
                    getPassword.execute(name);
                }
                else
                {
                    if (Validation.isEmpty(username.getText().toString()))
                        Toast.makeText(LoginActivity.this, "username cannot be null", Toast.LENGTH_SHORT).show();
                    if (Validation.isEmpty(password.getText().toString()))
                        Toast.makeText(LoginActivity.this, "password cannot be null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private class loginRestAsyncTask extends AsyncTask<String, Void, List<String>>
    {
        @Override
        protected List<String> doInBackground (String...params)
        {
            List<String> result = new ArrayList<>();
            String firstName = RestMethod.getFirstName(params[0]);
            String passwordHash =  RestMethod.login(params[0]);
            int id = RestMethod.findIdBySurname(params[0]);

            SharedPreferences.Editor editor = getSharedPreferences("user2", MODE_PRIVATE).edit();
            editor.putString("username", params[0]);
            editor.putInt("id", id);
            editor.apply();

            result.add(firstName);
            result.add(passwordHash);
            return result;
        }

        @Override
        protected void onPostExecute (List<String> input)
        {
            String pass = password.getText().toString();
            String hash = HashPassword.MD5(pass);
            if (input.get(1).equals(hash))
            {
                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.putString("firstName", input.get(0));
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(LoginActivity.this, "userName or password is wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}