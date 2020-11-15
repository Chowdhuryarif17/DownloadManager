package com.ariful.downloadmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_STORAGE_CODE = 1000;


    EditText urlEditText;
    Button dowloadButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize_Views_from_xml();

        dowloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //Runtime permission if OS is Marshmallow or above
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){

                        //-----permission denied, request It-----
                        String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show pop up for runtime permission
                        requestPermissions(permissions , PERMISSION_STORAGE_CODE);
                    }
                    else{
                        //permission already granted , perform download
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                            Toast.makeText(MainActivity.this, "Android10 cann't download, need to check code", Toast.LENGTH_SHORT).show();

                            startDownloadingForAndroid10();
                        }
                        startDownloading();
                    }
                }

                // OS is less than Marshmallow , perform download
                else{
                    startDownloading();
                }
            }
        });

    }
    private void initialize_Views_from_xml(){
        urlEditText = findViewById(R.id.urlEditTextId);
        dowloadButton = findViewById(R.id.downloadUrlButtonId);
        progressBar = findViewById(R.id.progressBarId);
    }

    // handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){

                    //---------------permission granted from pop up, perform download-----------

                    //for OS is Android Q(android 10) or above
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        Toast.makeText(MainActivity.this, "Android10 cann't download, need to check code", Toast.LENGTH_LONG).show();
                        startDownloadingForAndroid10(); // its not working :(
                    }
                    else{
                        startDownloading(); //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    }
                }
                else{
                    //permission denied from popup, show error message
                    Toast.makeText(this, "Permission denied!...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startDownloading(){
        //url link set for download
        String url = urlEditText.getText().toString().trim();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //allow types of network to download files
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file....");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis()); //get current timestamp as file name

        //get download service and enque file
        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void startDownloadingForAndroid10(){
        // link set for download
        String url = urlEditText.getText().toString().trim();

        // You can add more columns.. Complete list of columns can be found at
        // https://developer.android.com/reference/android/provider/MediaStore.Downloads
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Downloads.TITLE, "NewVideo");
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME,"demoVideo");
        contentValues.put(MediaStore.Downloads.MIME_TYPE, "mp4");
        contentValues.put(MediaStore.Downloads.SIZE, "30MB");

        // If you downloaded to a specific folder inside "Downloads" folder
        contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "Temp");

        // Insert into the database
        ContentResolver database = getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            database.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        }
    }

}