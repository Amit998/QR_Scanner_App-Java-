package com.example.qrscanner;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static TextView textViewResult;
    Button buttonScan;
    String TAG = "GenerateQrCode", inputValue;
    ImageView qrimg;
    Button start, save, share;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    EditText editText;

    BitmapDrawable bitmapDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResult = (TextView) findViewById(R.id.result_scan);
        buttonScan = (Button) findViewById(R.id.btn_scan);
        qrimg = (ImageView) findViewById(R.id.qr_code);
        editText = (EditText) findViewById(R.id.txt_qr);
        start = (Button) findViewById(R.id.btn_generate);
        save = (Button) findViewById(R.id.btn_save);
        share = (Button) findViewById(R.id.btn_share);


        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), scanCodeActivity.class));

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValue = editText.getText().toString().trim();
                if (inputValue.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int Width = point.x;
                    int Height = point.y;
                    int smallerdimension = Width < Height ? Width : Height;
                    smallerdimension = smallerdimension * 3 / 4;
                    qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerdimension);
                    try {
                        bitmap = qrgEncoder.getBitmap();
                        qrimg.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.v(TAG, e.toString());
                    }
                } else {
                    editText.setError("Required");
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapDrawable = (BitmapDrawable) qrimg.getDrawable();
                bitmap = bitmapDrawable.getBitmap();

                FileOutputStream outputStream = null;

                File sdCard = Environment.getExternalStorageDirectory();

                File directory = new File(sdCard.getAbsolutePath() + "/QRCODE");
                directory.mkdir();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(directory, fileName);

                Toast.makeText(MainActivity.this, "Saved Succesfully", Toast.LENGTH_SHORT).show();

                try {
                    outputStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));

                    sendBroadcast(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
                BitmapDrawable bitmapDrawable=((BitmapDrawable)qrimg.getDrawable());
                Bitmap bitmap=bitmapDrawable.getBitmap();
                String bitmapPath= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"QR Code",null);
                Uri uri=Uri.parse(bitmapPath);
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(intent,"Share Using"));
            }
        });

    }
}





