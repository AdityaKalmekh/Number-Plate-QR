package com.example.scanning_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends ArrayAdapter<Vehicledata> {
    Context context;
    List<Vehicledata> arrayListVehicle;
    Dialog dialog;


    public MyAdapter(@NonNull Context context, List<Vehicledata> arrayListVehicle) {
        super(context,R.layout.custom_list_item,arrayListVehicle);

        this.context = context;
        this.arrayListVehicle = arrayListVehicle;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item,null,true);

        TextView txtCode = view.findViewById(R.id.text_code);
        TextView txtNumber = view.findViewById(R.id.text_number);

        txtCode.setText(arrayListVehicle.get(position).getCode());
        txtNumber.setText(arrayListVehicle.get(position).getNumber());

        return view;
    }
    protected void deleteData(String number){
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.24/LoginRegister/Deletedata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Data Deleted")){
                            context.startActivity(new Intent(context,MainActivity.class));
//                            Toast.makeText(context, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Data Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put("Number",number);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    protected void deleteFromUser(String number){
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.24/LoginRegister/Deletedata.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Data Deleted")){
                            context.startActivity(new Intent(context,Loginuser.class));
//                            Toast.makeText(context, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Data Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put("Number",number);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    protected void viewData(int number){

        String name = "Name : "+arrayListVehicle.get(number).getName()+"\n\n"+
                      "Phone.No : "+arrayListVehicle.get(number).getNumber()+"\n\n"+
                      "Email : "+arrayListVehicle.get(number).getEmail()+"\n\n"+
                      "Apartment : "+arrayListVehicle.get(number).getApartment()+"\n\n"+
                      "Number Plate : "+arrayListVehicle.get(number).getCode()+"-"+
                                        arrayListVehicle.get(number).getNumber();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle()
        builder.setMessage(name);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
