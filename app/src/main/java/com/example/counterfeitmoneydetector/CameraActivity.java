package com.example.counterfeitmoneydetector;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;


import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import com.example.counterfeitmoneydetector.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

public class CameraActivity extends AppCompatActivity {
    ImageView imageView;
    TextView lengthtext;
    TextView leftwidthtext;
    TextView rightwidthtext;
    TextView bottommargintext;
    TextView topmargintext;
    TextView diagonaltext;
    Button button;
    private static final int SCALE_FACTOR = 4;
    private float lengthInMm, leftWidthInMm, rightWidthInMm, bottomMarginWidthInMm, topMarginWidthInMm, diagonalInMm;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //imageView = findViewById(R.id.imageview);
        button = findViewById(R.id.button);
        lengthtext = findViewById(R.id.lengthtext);
        leftwidthtext = findViewById(R.id.leftwidthtext);
        rightwidthtext = findViewById(R.id.rightwidthtext);
        bottommargintext = findViewById(R.id.bottommargintext);
        topmargintext = findViewById(R.id.topmargintext);
        diagonaltext = findViewById(R.id.diagonaltext);

        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        ImageButton button1 = findViewById(R.id.backbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(bitmap);
            calculateDimensions(bitmap);
        }
    }

    private void calculateDimensions(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int leftEdgeStart = width / 3;
        int rightEdgeStart = (width / 3) * 2;
        int bottomMarginStart = height / 3;
        int topMarginStart = (height / 3) * 2;

        lengthInMm = width / SCALE_FACTOR;
        leftWidthInMm = (leftEdgeStart - 0) / SCALE_FACTOR;
        rightWidthInMm = (width - rightEdgeStart) / SCALE_FACTOR;
        bottomMarginWidthInMm = (height - bottomMarginStart) / SCALE_FACTOR;
        topMarginWidthInMm = topMarginStart / SCALE_FACTOR;
        diagonalInMm = (float) Math.sqrt(Math.pow(lengthInMm, 2) + Math.pow(topMarginWidthInMm, 2));

        Intent intent = new Intent(getApplicationContext(), ModelActivity.class);
        intent.putExtra("length", lengthInMm);
        intent.putExtra("left edge", leftWidthInMm);
        intent.putExtra("right edge", rightWidthInMm);
        intent.putExtra("bottom margin", bottomMarginWidthInMm);
        intent.putExtra("top margin", topMarginWidthInMm);
        intent.putExtra("diagonal", diagonalInMm);
        intent.putExtra("str", "yo");
        Log.d("tagger", "" + lengthInMm);
        Log.d("tagger", "" + leftWidthInMm);
        Log.d("tagger", "" + rightWidthInMm);
        Log.d("tagger", "" + bottomMarginWidthInMm);
        Log.d("tagger", "" + topMarginWidthInMm);
        Log.d("tagger", "" + diagonalInMm);
        lengthtext.setText("Length: " + String.valueOf(lengthInMm));
        leftwidthtext.setText("Left Width: " +String.valueOf(leftWidthInMm));
        rightwidthtext.setText("Right Width: " +String.valueOf(rightWidthInMm));
        bottommargintext.setText("Bottom Margin: " +String.valueOf(bottomMarginWidthInMm));
        topmargintext.setText("Top Margin: " +String.valueOf(topMarginWidthInMm));
        diagonaltext.setText("Diagonal: " +String.valueOf(diagonalInMm));


        //startActivity(intent);


        float mean = lengthInMm + leftWidthInMm + rightWidthInMm + bottomMarginWidthInMm + topMarginWidthInMm + diagonalInMm;
        mean /= 6.0;
        double sd = 0.0;
        sd += Math.pow((lengthInMm - mean), 2);
        sd += Math.pow((leftWidthInMm - mean), 2);
        sd += Math.pow((rightWidthInMm - mean), 2);
        sd += Math.pow((bottomMarginWidthInMm - mean), 2);
        sd += Math.pow((topMarginWidthInMm - mean), 2);
        sd += Math.pow((diagonalInMm - mean), 2);
        sd /= 6.0;
        sd = Math.sqrt(sd);

        float[] standardArr = {(float) ((lengthInMm - mean) / sd), (float) ((leftWidthInMm - mean) / sd), (float) ((rightWidthInMm - mean) / sd), (float) ((bottomMarginWidthInMm - mean) / sd), (float) ((topMarginWidthInMm - mean) / sd), (float) ((diagonalInMm - mean) / sd)};

    }
}