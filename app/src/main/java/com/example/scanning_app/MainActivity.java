package com.example.scanning_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Dialog dialogbox;
    ListView listView;
    MyAdapter adapter;
    String url = "http://192.168.43.24/LoginRegister/retrivedata.php";
    Vehicledata vehicle;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.brown));
        }
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#513939")));

        listView = findViewById(R.id.listView);
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
                                finish();
                                break;
                            case 2:
                                adapter.deleteData(Arryavehicle.vehicledata.get(position).getNumber());
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
        inflater.inflate(R.menu.admin_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(getApplicationContext(), login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void retrieveData(){

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Arryavehicle.vehicledata.clear();
                try {
//                    Log.d("hii",response);
                    JSONObject jsonObject = new JSONObject(response);
//                    Toast.makeText(MainActivity.this,, Toast.LENGTH_SHORT).show();
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("registration");

                    if (success.equals("1")){
                        for(int i=0;i<jsonArray.length();i++){
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this,login.class));
    }
}