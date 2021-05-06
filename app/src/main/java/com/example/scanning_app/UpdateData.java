package com.example.scanning_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateData extends AppCompatActivity {

    Dialog dialog;
    protected TextInputLayout textInputEmail;
    protected TextInputLayout textInputPhone;
    protected TextInputLayout textInputName;
    protected TextInputLayout textInputApartment;
    EditText codeAlpha,codeNumeric;
    TextView textValidation,textNumericValidation,dialogtext;
    public static ArrayList<Vehicledata> vehicledata = new ArrayList<>();
    String url = "http://192.168.43.24/LoginRegister/retrivedata.php";
    Vehicledata vehicle;
    ActionBar actionBar;

    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(UpdateData.this);
        dialog.setContentView(R.layout.custom_dialog_layout);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#513939")));
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>Edit Details</font>"));
//        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>Scan</font>"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.brown));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
//        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();

        textInputEmail = findViewById(R.id.text_input_email);
        textInputPhone = findViewById(R.id.text_input_phone_no);
        textInputName = findViewById(R.id.text_input_name);
        textInputApartment = findViewById(R.id.text_input_apartment);
        codeAlpha = findViewById(R.id.alphacode);
        codeNumeric = findViewById(R.id.numericcode);
        textValidation = findViewById(R.id.VehicleNoValidation);
        textNumericValidation = findViewById(R.id.numericValidation);

        autoText();

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
        spinner.setEnabled(false);
    }

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


    public void confirmInput(View v) {
        if (!validateEmail() | !validatePhone() | !validateApartment() | !validateName()) {
            return;
        }
        else{
            updateData();
//            Intent in = new Intent(this,MainActivity.class);
//            startActivity(in);
        }
    }
    protected void autoText(){
        textInputEmail.getEditText().setText(Arryavehicle.vehicledata.get(position).getEmail());
        textInputPhone.getEditText().setText(Arryavehicle.vehicledata.get(position).getPhone());
        textInputName.getEditText().setText(Arryavehicle.vehicledata.get(position).getName());
        textInputApartment.getEditText().setText(Arryavehicle.vehicledata.get(position).getApartment());
        codeAlpha.setText(Arryavehicle.vehicledata.get(position).getCode());
        codeNumeric.setText(Arryavehicle.vehicledata.get(position).getNumber());
    }

    public void updateData(){
        String NameInput = textInputName.getEditText().getText().toString().trim();
        String phoneInput = textInputPhone.getEditText().getText().toString().trim();
        String EmailInput = textInputEmail.getEditText().getText().toString().trim();
        String ApartmentInput = textInputApartment.getEditText().getText().toString().trim();
        String AlphaInput = codeAlpha.getText().toString();
        String NumericInput = codeNumeric.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.24/LoginRegister/Updatedata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(UpdateData.this,response,Toast.LENGTH_SHORT).show();
                retrieveData();
//                autoText();
                progressDialog.dismiss();
                dialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        dialog.dismiss();

                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 2000);
//                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("Name",NameInput);
                params.put("Phone",phoneInput);
                params.put("Email",EmailInput);
                params.put("Apartment",ApartmentInput);
                params.put("Code",AlphaInput);
                params.put("Number",NumericInput);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateData.this);
        requestQueue.add(request);
    }
    public void retrieveData(){

        String NumericInput = codeNumeric.getText().toString();
//        Toast.makeText(this, NumericInput, Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vehicledata.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("registration");

                    if (success.equals("1")){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
//                            Toast.makeText(UpdateData.this, String.valueOf(object), Toast.LENGTH_SHORT).show();
                            String name = object.getString("Name");
                            String phone = object.getString("Phone");
                            String email = object.getString("Email");
                            String apartment = object.getString("Apartment");
                            String code = object.getString("Code");
                            String number = object.getString("Number");

                            if (NumericInput.equals(number)){
                                textInputName.getEditText().setText(name);
                                textInputEmail.getEditText().setText(email);
                                textInputPhone.getEditText().setText(phone);
                                textInputApartment.getEditText().setText(apartment);
                            }

                            vehicle = new Vehicledata(name,phone,email,apartment,code,number);
                            vehicledata.add(vehicle);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this,Loginuser.class));
    }
}