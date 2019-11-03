package com.example.android.roadcomplaint;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.Manifest;
import java.io.*;

/**
 * Created by Nichay Jain on 01-06-2019.
 */

public class RoadComplaint extends AppCompatActivity implements LocationListener {
    private EditText name, description;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private DatabaseReference UsersRef;
    private String currentUserID;
    private Button button;
    private Button autodetect;
    private EditText latitude;
    private EditText longitude;
    private EditText locationText;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Button photoButton;
    private Bitmap photo;
    LocationManager locationManager;
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadcomplaint);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        initialise();
        autodetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectlocation();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaint();
            }
        });
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });


    }

    private void complaint() {
        String n = name.getText().toString();
        String d = description.getText().toString();
        String ad=locationText.getText().toString();
        String la=latitude.getText().toString();
        String lo=longitude.getText().toString();
        DatabaseReference ref = UsersRef;
        ref = ref.child(d);

        /*Map<String,String> users = new HashMap<>();
        //Details det=new Details(n,d,la,lo,photo);
        users.put("name",n);
        users.put("description",d);
        users.put("latitude",la);
        users.put("longitude",lo);

       // ref=ref.child("image");
        //Map<String,String> image=new HashMap<>();
        /*image.put("image",photo);
        ref.setValue(image);*/

        /*ByteArrayOutputStream bao = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bao); // bmp is bitmap from user image file
        photo.recycle();
        byte[] byteArray = bao.toByteArray();
        String imageB64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        image.put("image",imageB64);
        ref.setValue(image);*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        //users.put("image",imageEncoded);
        //ref.setValue(users);
        Details details=new Details(n,d,ad,la,lo,imageEncoded);
        ref.setValue(details);

    }

    private void initialise() {
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        button = (Button) findViewById(R.id.complaint);
        latitude=(EditText) findViewById(R.id.latitude);
        longitude=(EditText)findViewById(R.id.longitude);
        autodetect=(Button)findViewById(R.id.auto);
        imageView = (ImageView)this.findViewById(R.id.imageView1);
        photoButton = (Button) this.findViewById(R.id.button1);
        locationText=(EditText)findViewById(R.id.address);
    }

    public void detectlocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        /*try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lo = location.getLongitude();
            double la = location.getLatitude();


            longitude.setText(""+lo, TextView.BufferType.EDITABLE);
            latitude.setText(""+la, TextView.BufferType.EDITABLE);



        } catch (SecurityException e) {
            Toast.makeText(this, "please give location access permission", Toast.LENGTH_SHORT).show();
        }*/
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
             photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
























    public void onLocationChanged(Location location) {
        double lo = location.getLongitude();
        double la = location.getLatitude();


        longitude.setText(""+lo, TextView.BufferType.EDITABLE);
        latitude.setText(""+la, TextView.BufferType.EDITABLE);
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(addresses.get(0).getAddressLine(0) + ", " +
                    addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2),TextView.BufferType.EDITABLE);

        } catch (Exception e) {

        }


        }

    @Override
    public void onProviderDisabled(String provider) {
            }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

}

