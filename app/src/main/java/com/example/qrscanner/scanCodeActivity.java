package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.os.Bundle;

import com.google.zxing.Result;

import java.util.Scanner;

public class scanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(R.layout.activity_scan_code);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result rawResult) {

        MainActivity.textViewResult.setText(rawResult.getText());
        onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
