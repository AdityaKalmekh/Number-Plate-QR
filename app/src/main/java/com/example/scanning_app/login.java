package com.example.scanning_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class login extends AppCompatActivity {

    TextInputLayout Username,Password;
    TextInputEditText user,pass;
    Button Login;
    ProgressBar progressBar;
    TextView Signtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(login.this,R.color.darkgray));
        }

        pass = findViewById(R.id.password);
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Password.setError(null);
                }
            }
        });
        user =findViewById(R.id.username);
        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Username.setError(null);
                }
            }
        });
        pass.addTextChangedListener(textWatcher);
        Username = findViewById(R.id.textInputLayoutUsername);
        Password = findViewById(R.id.textInputLayoutPassword);
        Login = findViewById(R.id.buttonLogin);
        Signtxt = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.DKGRAY));
        }
        Signtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,SignUp.class);
                startActivity(intent);
            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password;
                username = Username.getEditText().getText().toString().trim();
                password = Password.getEditText().getText().toString().trim();

                Username.setError(null);
                Password.setError(null);

                if (!username.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            PutData putData = new PutData("http://192.168.43.24/LoginRegister/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                     String result = putData.getResult();
                                     String[] separated = result.split("#");
                                    if (separated[0].equals("Login Success")){
//                                        Toast.makeText(login.this, separated[1], Toast.LENGTH_LONG).show();
                                        SharedPreferences sharedPreferences = getSharedPreferences("id",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("lid",separated[1]);
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(),Loginuser.class);
                                        startActivity(intent);
                                    }
                                    else if(result.equals("Admin Success")){
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(login.this, result, Toast.LENGTH_SHORT).show();
                                        Password.setError("Username or password incorrect");
                                        Username.setError("Username or password incorrect");
                                    }
                                    //End ProgressBar (Set visibility to GONE)
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else {
//                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    Password.setError("Field can't be empty");
                    Username.setError("Field can't be empty");
                }
            }
        });
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String password = pass.getText().toString().trim();
            if (password.length() < 8){
                Password.setError("Password length must be minimum 8 characters");
            }
            else {
                Password.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}