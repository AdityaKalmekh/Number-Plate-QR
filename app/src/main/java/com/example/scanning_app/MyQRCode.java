package com.example.scanning_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.WriterException;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MyQRCode extends AppCompatActivity {

     String VehicleNo;
     ImageView imageView;
     QRGEncoder qrgEncoder;
     Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_q_r_code);

        imageView = findViewById(R.id.imageView4);

        Intent intent = getIntent();
        VehicleNo = intent.getStringExtra("Vehicleno");
        Toast.makeText(this, String.valueOf(VehicleNo), Toast.LENGTH_SHORT).show();


//        File path = new File(Environment.getExternalStorageDirectory().getPath()+ "/LoginRegister/images");
//        String[] filename= path.list();
//        System.out.println(Arrays.toString(filename));
//
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.24/LoginRegister/retriveimg.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(MyQRCode.this, response, Toast.LENGTH_SHORT).show();
                        qrgEncoder = new QRGEncoder(response, null, QRGContents.Type.TEXT, 500);
                        try {
                            bitmap = qrgEncoder.encodeAsBitmap();
                            imageView.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            Toast.makeText(MyQRCode.this, "not set", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyQRCode.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("vehicleno",VehicleNo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MyQRCode.this);
        requestQueue.add(request);
    }
}