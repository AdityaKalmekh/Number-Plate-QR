package com.example.scanning_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.zxing.WriterException;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;
import java.util.Timer;
import java.util.TimerTask;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

//import androidmads.library.qrgenearator.QRGContents;
//import androidmads.library.qrgenearator.QRGEncoder;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Registration extends AppCompatActivity {

    protected TextInputLayout textInputEmail;
    protected TextInputLayout textInputPhone;
    protected TextInputLayout textInputName;
    protected TextInputLayout textInputApartment;
    EditText codeAlpha,codeNumeric;
    TextView textValidation,textNumericValidation,textDialog;
    Dialog dialog;
    ActionBar actionBar;
    TextInputEditText Name,Email,Phone;
//    Bitmap bitmap;
//    QRGEncoder qrgEncoder;
//    ImageView imageView;
//    String NameInput = textInputName.getEditText().getText().toString().trim();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#513939")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(Registration.this,R.color.brown));
        }
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>Registration</font>"));

        Name = findViewById(R.id.name);
        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    textInputName.setError(null);
                }
            }
        });
        Email = findViewById(R.id.emailid);
        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    textInputEmail.setError(null);
                }
            }
        });

        Phone = findViewById(R.id.phoneno);
        Phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    textInputPhone.setError(null);
                }
            }
        });

        Name.addTextChangedListener(textWatcher);
        Email.addTextChangedListener(textWatcher);
        Phone.addTextChangedListener(textWatcher);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputPhone = findViewById(R.id.text_input_phone_no);
        textInputName = findViewById(R.id.text_input_name);
        textInputApartment = findViewById(R.id.text_input_apartment);
        codeAlpha = findViewById(R.id.alphacode);
        codeNumeric = findViewById(R.id.numericcode);
        textValidation = findViewById(R.id.VehicleNoValidation);
        textNumericValidation = findViewById(R.id.numericValidation);


        dialog = new Dialog(Registration.this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.custom_dialog_layout,null);
        textDialog = view.findViewById(R.id.dialogtext);
        textDialog.setText("Registration Successfully");

//        textDialog = findViewById(R.id.dialogtext);
//        textDialog.setText("Registration Successfully");
//        View view = inflater.inflate(R.layout.qrimage,null);
//        imageView = findViewById(R.id.scanimg);
//        imageView.setImageResource();

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phoneno = Phone.getText().toString().trim();
//            String user = username.getText().toString().trim();
            String full_name = Name.getText().toString().trim();
            String wemail = Email.getText().toString().trim();
            if (!full_name.isEmpty()){
                for (int i = 0; i < full_name.length(); i++) {
                    char c = full_name.charAt(i);
                    if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z') && c != ' ') {
                        Name.setError("Filed can only include alphabetical values");
                    }else{
                        Name.setError(null);
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
            if (!phoneno.isEmpty()) {
                if (phoneno.length() != 10) {
                    Phone.setError("Phone number contain 10 digits");
                }else{
                    Phone.setError(null);
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    protected boolean validateName() {
        String NameInput = textInputName.getEditText().getText().toString().trim();

        if (NameInput.isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else {
            for (int i = 0; i < NameInput.length(); i++) {
                char c = NameInput.charAt(i);
                if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z') && c != ' ') {
                    textInputName.setError("Filed can't include numeric value");
                    return false;
                }
            }
            textInputName.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phoneInput = textInputPhone.getEditText().getText().toString().trim();

        if (phoneInput.isEmpty()) {
            textInputPhone.setError("Field can't be empty");
            return false;
        } else if (phoneInput.length() != 10) {
            textInputPhone.setError("Field contain invalid content");
            return false;
        } else {
            textInputPhone.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String EmailInput = textInputEmail.getEditText().getText().toString().trim();

        if (EmailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else {
            String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (EmailInput.matches(emailPattern))
            {
                textInputEmail.setError(null);
                return true;
            }
            else
            {
                textInputEmail.setError("Field contain invalid content");
                return false;
            }
        }
    }
    private boolean validateApartment() {
        String ApartmentInput = textInputApartment.getEditText().getText().toString().trim();

        if (ApartmentInput.isEmpty()) {
            textInputApartment.setError("Field can't be empty");
            return false;
        } else {
            textInputApartment.setError(null);
            return true;
        }
    }

    private boolean validateCodeAlpha(){
        String AlphaInput = codeAlpha.getText().toString();
        String Emptytext = "Field can't be empty";
        if (AlphaInput.isEmpty()){
            textValidation.setText(Emptytext);
            return false;
        }
        else {
            for (int i = 0; i < AlphaInput.length(); i++) {
                char c = AlphaInput.charAt(i);
                if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z') && c != ' ') {
                    textValidation.setText("Filed can't include numeric value");
                    return false;
                }
            }
            textValidation.setText("");
            return true;
        }
    }
    public boolean validateCodeNumeric(){
        String NumericInput = codeNumeric.getText().toString();
        String Emptytext = "Field can't be empty";
        if (NumericInput.isEmpty()){
            textNumericValidation.setText(Emptytext);
            return false;
        }
        return true;
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validatePhone() | !validateApartment() | !validateName() | !validateCodeAlpha() | !validateCodeNumeric()) {
            return;
        }
        else{
//            dialog.show();
//            final Timer timer2 = new Timer();
//            timer2.schedule(new TimerTask() {
//                public void run() {
//                    dialog.dismiss();
//                    timer2.cancel(); //this will cancel the timer of the system
//                }
//            }, 2000);
            storeData();
        }
    }
    public void storeData(){
        if (validateEmail() && validatePhone() && validateApartment() && validateName() && validateCodeAlpha() && validateCodeNumeric()) {
            String NameInput = textInputName.getEditText().getText().toString().trim();
            String phoneInput = textInputPhone.getEditText().getText().toString().trim();
            String EmailInput = textInputEmail.getEditText().getText().toString().trim();
            String ApartmentInput = textInputApartment.getEditText().getText().toString().trim();
            String AlphaInput = codeAlpha.getText().toString();
            String NumericInput = codeNumeric.getText().toString();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    SharedPreferences getData = getSharedPreferences("id",MODE_PRIVATE);
                    String Id = getData.getString("lid","0");
//                    Toast.makeText(Registration.this, Id, Toast.LENGTH_SHORT).show();
                    String[] field = new String[7];
                    field[0] = "Name";
                    field[1] = "Phone";
                    field[2] = "Email";
                    field[3] = "Apartment";
                    field[4] = "Code";
                    field[5] = "Number";
                    field[6] = "uid";
                    //Creating array for data
                    String[] data = new String[7];
                    data[0] = NameInput;
                    data[1] = phoneInput;
                    data[2] = EmailInput;
                    data[3] = ApartmentInput;
                    data[4] = AlphaInput;
                    data[5] = NumericInput;
                    data[6] = Id;
                    PutData putData = new PutData("http://192.168.43.24/LoginRegister/registration.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
//                            progressBar.setVisibility(View.GONE);
                            String result = putData.getResult();
                            if (result.equals("Sign Up Success")){
//                                Intent intent = new Intent(getApplicationContext(),login.class);
//                                startActivity(intent);
//                                finish();
//                                Toast.makeText(Registration.this, "successfully register", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Registration.this,QRCode_generator.class);
                                intent.putExtra("name",NameInput);
                                intent.putExtra("phone",phoneInput);
                                intent.putExtra("email",EmailInput);
                                intent.putExtra("apartment",ApartmentInput);
                                intent.putExtra("code",AlphaInput);
                                intent.putExtra( "number",NumericInput);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Registration.this, result, Toast.LENGTH_SHORT).show();
                            }
                            //End ProgressBar (Set visibility to GONE)
                        }
                    }
                    //End Write and Read data with URL
                }
            });
        }
    }
}