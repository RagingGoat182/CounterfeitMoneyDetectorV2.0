package com.example.counterfeitmoneydetector;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button takephotobutton = (Button) findViewById(R.id.takephotobutton);
        takephotobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openActivityCamera();
            }
        });
        Button chooseimagebutton = (Button) findViewById(R.id.chooseimagebutton);
        chooseimagebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openActivityGallery();
            }
        });
        ImageButton settingsbutton = (ImageButton) findViewById(R.id.settingsbutton);
        settingsbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openActivitySettings();

            }
        });

        ImageButton helpbutton = (ImageButton) findViewById(R.id.helpbutton);
        helpbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    public void openActivitySettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void openActivityCamera(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
    public void openActivityGallery(){
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }
}