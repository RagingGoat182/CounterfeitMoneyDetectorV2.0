package com.example.counterfeitmoneydetector;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.common.ops.DequantizeOp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import org.tensorflow.lite.Interpreter;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.counterfeitmoneydetector.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Arrays;
import java.lang.Math;
import org.tensorflow.lite.Interpreter;

public class ModelActivity extends AppCompatActivity {
    Interpreter tflite;
    TextView outp;

    Bundle extras = getIntent().getExtras();
    int length = extras.getInt("length");
    int left_edge = extras.getInt("left edge");
    int right_edge = extras.getInt("right edge");
    int bottom_margin = extras.getInt("bottom margin");
    int top_margin = extras.getInt("top margin");
    int diagonal = extras.getInt("diagonal");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        Button runbutton = findViewById(R.id.runbutton);
        outp=findViewById(R.id.output);

        runbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //outp.setText(Float.toString(prediction));
                int mean = length+left_edge+right_edge+bottom_margin+top_margin+diagonal;
                double sd = 0.0;
                sd += Math.pow((length - mean), 2);
                sd += Math.pow((left_edge - mean), 2);
                sd += Math.pow((right_edge - mean), 2);
                sd += Math.pow((bottom_margin - mean), 2);
                sd += Math.pow((top_margin - mean), 2);
                sd += Math.pow((diagonal - mean), 2);
                sd /= 6;
                sd = Math.sqrt(sd);
                float[][] standardArr = {{(length-mean)/sd(float), (left_edge-mean)/sd, (right_edge-mean)/sd, (bottom_margin-mean)/sd, (top_margin-mean)/sd, (diagonal-mean)/sd}};
                //double[] arr = {length, left_edge, right_edge, bottom_margin, top_margin, diagonal};
                //float[][] arr = {{214.4f, 130.1f, 130.3f, 9.7f, 11.7f, 139.8f}};



                try {
                    Model model = Model.newInstance(ModelActivity.this);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 6}, DataType.FLOAT32);
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(48);
                    byteBuffer.order(ByteOrder.nativeOrder());

                    for(int i = 0; i < standardArr[0].length; i++){
                        byteBuffer.putFloat(standardArr[0][i]);
                    }

                    inputFeature0.loadBuffer(buffer);

                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    outp.setText(Float.toString(outputFeature0.getFloatArray()[0]));

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }


            }
        });
    }


}