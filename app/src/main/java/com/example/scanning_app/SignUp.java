package com.example.scanning_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    TextInputLayout Fullname,Email,Username,Password;
    TextInputEditText fullname,email,username,password;
    Button Signup;
    ProgressBar progressBar;
    TextView Logintxt;
    CheckBox box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(SignUp.this,R.color.darkgray));
        }

        fullname =findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fullname.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        fullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Fullname.setError(null);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Email.setError(null);
                }
            }
        });
        username = findViewById(R.id.username);

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Password.setError(null);
                }
            }
        });
        box = findViewById(R.id.checkBox2);
        Fullname = findViewById(R.id.textInputLayoutFullname);
        Username = findViewById(R.id.textInputLayoutUsername);
        Password = findViewById(R.id.textInputLayoutPassword);
        Email = findViewById(R.id.textInputLayoutEmail);
        Signup = findViewById(R.id.buttonSignUp);
        progressBar = findViewById(R.id.progress);
        Logintxt = findViewById(R.id.loginText);

        Logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,login.class);
                startActivity(intent);
                finish();
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = Fullname.getEditText().getText().toString().trim();
                String email = Email.getEditText().getText().toString().trim();
                String username = Username.getEditText().getText().toString().trim();
                String password = Password.getEditText().getText().toString().trim();

                if(!validateEmail() | !fullName() | !userName() | !passWord() | !msg()){
                    return;
                }
              else {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "username";
                            field[2] = "password";
                            field[3] = "email";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = fullname;
                            data[1] = username;
                            data[2] = password;
                            data[3] = email;
                            PutData putData = new PutData("http://192.168.43.24/LoginRegister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")){
                                        Intent intent = new Intent(getApplicationContext(),login.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(SignUp.this, result, Toast.LENGTH_SHORT).show();
                                    }
                                    //End ProgressBar (Set visibility to GONE)
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
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
            String pass = password.getText().toString().trim();
            String full_name = fullname.getText().toString().trim();
            String wemail = email.getText().toString().trim();
            if (!full_name.isEmpty()){
                for (int i = 0; i < full_name.length(); i++) {
                    char c = full_name.charAt(i);
                    if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z') && c != ' ') {
                        Fullname.setError("Filed can't include numeric value");
                    }else{
                        Fullname.setError(null);
                    }
                }
            }
            if (!wemail.isEmpty()){
                String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
                if (wemail.matches(emailPattern)) {
                    Email.setError(null);
                }
                else {
                    Email.setError("Field contain invalid content");
                }
            }
            if (!pass.isEmpty()) {
                if (pass.length() < 8) {
                    Password.setError("Password length must be atleast 8 characters");
                }else{
                    Password.setError(null);
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private  boolean msg(){
        if (box.isChecked()){
            box.setError(null);
            return true;
        }else{
            box.setError("Check the button");
            return false;
        }
    }
    private boolean fullName(){
        String fullname = Fullname.getEditText().getText().toString().trim();

        if (fullname.isEmpty()) {
            Fullname.setError("Field can't be empty");
            return false;
        } else if (fullname.length() < 2){
            Fullname.setError("Name must contain more than 2 letters");
        }
        else {
            for (int i = 0; i < fullname.length(); i++) {
                char c = fullname.charAt(i);
                if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z') && c != ' ') {
                    Fullname.setError("Filed can't include numeric value");
                    return false;
                }
            }
        }
        Fullname.setError(null);
        return true;
    }

    private boolean validateEmail() {
        String EmailInput = Email.getEditText().getText().toString().trim();

        if (EmailInput.isEmpty()) {
            Email.setError("Field can't be empty");
            return false;
        } else {
            String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (EmailInput.matches(emailPattern))
            {
                Email.setError(null);
                return true;
            }
            else
            {
                Email.setError("Field contain invalid content");
                return false;
            }
        }
    }

    private boolean userName(){
        String username = Username.getEditText().getText().toString().trim();

        if (username.isEmpty()) {
            Username.setError("Field can't be empty");
            return false;
        }
        Username.setError(null);
        return true;

    }
    private boolean passWord(){
        String password = Password.getEditText().getText().toString().trim();

        if(password.isEmpty()){
            Password.setError("Field can't be empty");
            return false;
        }
        if(password.length() < 8){
            Password.setError("Password must contain minimum 8 letters");
            return false;
        }
        Password.setError(null);
        return true;
    }
}