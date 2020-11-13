package com.ariful.downloadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    EditText urlEt;
    Button dowloadButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize_Views_With_XML();

        dowloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }
    private void initialize_Views_With_XML(){
        urlEt = findViewById(R.id.urlEtId);
        dowloadButton = findViewById(R.id.downloadUrlButtonId);
        progressBar = findViewById(R.id.progressBarId);
    }
}