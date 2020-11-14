package com.ariful.downloadmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_STORAGE_CODE = 1000;

    DownloadManager manager;
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

                //Runtime permission if OS is Marshmallow or above
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                        //permission denied, request It
                        String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show pop up for runtime permission
                        requestPermissions(permissions , PERMISSION_STORAGE_CODE);
                    }
                    else{
                        //permission already granted , perform download
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
    private void startDownloading(){
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
        manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    // handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission granted from pop up, perform download
                    startDownloading();
                }
                else{
                    //permission denied from popup, show error message
                    Toast.makeText(this, "Permission denied!...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}