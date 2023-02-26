package com.example.counterfeitmoneydetector;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CameraActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    private static final int SCALE_FACTOR = 4;
    private float lengthInMm, leftWidthInMm, rightWidthInMm, bottomMarginWidthInMm, topMarginWidthInMm, diagonalInMm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageview);
        button = findViewById(R.id.button);

        if(ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            calculateDimensions(bitmap);
        }
    }

    private void calculateDimensions(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int leftEdgeStart = width/3;
        int rightEdgeStart = (width/3)*2;
        int bottomMarginStart = height/3;
        int topMarginStart = (height/3)*2;

        lengthInMm = width/SCALE_FACTOR;
        leftWidthInMm = (leftEdgeStart - 0)/SCALE_FACTOR;
        rightWidthInMm = (width - rightEdgeStart)/SCALE_FACTOR;
        bottomMarginWidthInMm = (height - bottomMarginStart)/SCALE_FACTOR;
        topMarginWidthInMm = topMarginStart/SCALE_FACTOR;
        diagonalInMm = (float) Math.sqrt(Math.pow(lengthInMm, 2) + Math.pow(topMarginWidthInMm, 2));

        Intent intent = new Intent(this, ModelActivity.class);
        intent.putExtra("length",lengthInMm);
        intent.putExtra("left edge",leftWidthInMm);
        intent.putExtra("right edge",rightWidthInMm);
        intent.putExtra("bottom margin",bottomMarginWidthInMm);
        intent.putExtra("top margin",topMarginWidthInMm);
        intent.putExtra("diagonal",diagonalInMm);
        startActivity(intent);
    }
}