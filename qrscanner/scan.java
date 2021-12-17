package com.harshjain22.qrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerObj = new ZXingScannerView(this);
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(ScannerObj);

        dbRef = FirebaseDatabase.getInstance().getReference("qrData");

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
             ScannerObj.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                 permissionToken.continuePermissionRequest();
            }
        });

    }


    @Override
    public void handleResult(Result rawResult) {
        String fetchedData = rawResult.getText();

        dbRef.push().setValue(fetchedData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                MainActivity.qrText.setText("The data has been Successfully inserted");
                onBackPressed();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        ScannerObj.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerObj.setResultHandler(this);
        ScannerObj.startCamera();
    }
}