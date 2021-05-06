package com.example.scanning_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCode_generator extends AppCompatActivity {

    ImageView imageView;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    String encodeString;
    ActionBar actionBar;
    String NumericInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#513939")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(QRCode_generator.this,R.color.brown));
        }
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>QR Code</font>"));


        imageView = findViewById(R.id.imageView2);
        Intent intent = getIntent();
        String NameInput = intent.getStringExtra("name");
        String PhoneInput = intent.getStringExtra("phone");
        String EmailInput = intent.getStringExtra("email");
        String ApartmentInput = intent.getStringExtra("apartment");
        String AlphaInput = intent.getStringExtra("code");
         NumericInput = intent.getStringExtra("number");

        String data = "\n\n"+"Name : " +NameInput+
                      "\n\n"+"Mobile.No : "+PhoneInput+
                      "\n\n"+"Email : "+EmailInput+
                      "\n\n"+"Apartment : "+ApartmentInput+
                      "\n\n"+"Vehicle.No : "+AlphaInput+"-"+NumericInput;
//        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
            imageStore(bitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void imageStore(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

        byte[] imageBytes = stream.toByteArray();

        encodeString = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.24/LoginRegister/uploadimage.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(QRCode_generator.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRCode_generator.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("image",encodeString);
                params.put("number",NumericInput);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(QRCode_generator.this);
        requestQueue.add(request);
    }
}