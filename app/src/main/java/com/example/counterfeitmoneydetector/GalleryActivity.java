package com.example.counterfeitmoneydetector;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
public class GalleryActivity extends AppCompatActivity {
    TextView lengthtext;
    TextView leftwidthtext;
    TextView rightwidthtext;
    TextView bottommargintext;
    TextView topmargintext;
    TextView diagonaltext;
    private static final int SCALE_FACTOR = 4;
    private float lengthInMm, leftWidthInMm, rightWidthInMm, bottomMarginWidthInMm, topMarginWidthInMm, diagonalInMm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Button gallery = findViewById(R.id.gallery);
        lengthtext = findViewById(R.id.textView2);
        leftwidthtext = findViewById(R.id.textView3);
        rightwidthtext = findViewById(R.id.textView4);
        bottommargintext = findViewById(R.id.textView5);
        topmargintext = findViewById(R.id.textView6);
        diagonaltext = findViewById(R.id.textView7);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton button1 = findViewById(R.id.backbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(selectedImage);

            Drawable drawable = imageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
             //imageView.buildDrawingCache();
            //Bitmap bitmap = imageView.getDrawingCache();
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
    }
}