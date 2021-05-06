package com.example.scanning_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Loginuser extends AppCompatActivity {

    ListView listView;
    MyAdapter adapter;
    String url = "http://192.168.43.24/LoginRegister/Userdata.php";
    Vehicledata vehicle;
    ActionBar actionBar;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginuser);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.brown));
        }
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#513939")));
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>Vehicle Details</font>"));
        listView = findViewById(R.id.listView);
        floatingActionButton = findViewById(R.id.Vehicleregistration);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginuser.this,Registration.class));
            }
        });
        adapter = new MyAdapter(this,Arryavehicle.vehicledata);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"View Data","Edit Data","Delete Data"};
                builder.setTitle(Arryavehicle.vehicledata.get(position).getNumber());
//                builder.setView(R.layout.img1);

                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        switch (i){

                            case 0:
                                adapter.viewData(position);
                                break;
                            case 1:
                                startActivity(new Intent(getApplicationContext(),UpdateData.class).
                                        putExtra("position",position));
                                break;
                            case 2:
                                adapter.deleteFromUser(Arryavehicle.vehicledata.get(position).getNumber());
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        retrieveData();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.scan:
                Intent intent = new Intent(this,Scanner.class);
                startActivity(intent);
                Toast.makeText(this, "Scanner is selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                startActivity(new Intent(getApplicationContext(),login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void retrieveData(){
        Arryavehicle.vehicledata.clear();
        SharedPreferences getData = getSharedPreferences("id",MODE_PRIVATE);
        String Id = getData.getString("lid","0");
        String[] field = new String[1];
        field[0] = "id";
        String[] data = new String[1];
        data[0] = Id;
        PutData putData = new PutData(url, "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("registration");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("Name");
                            String phone = object.getString("Phone");
                            String email = object.getString("Email");
                            String apartment = object.getString("Apartment");
                            String code = object.getString("Code");
                            String number = object.getString("Number");

                            vehicle = new Vehicledata(name,phone,email,apartment,code,number);
                            Arryavehicle.vehicledata.add(vehicle);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this,login.class));
    }
}