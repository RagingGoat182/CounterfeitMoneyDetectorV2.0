package com.example.counterfeitmoneydetector;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

public class ModelActivity extends AppCompatActivity {
    Interpreter tflite;
    TextView outp;

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    float[] arr = intent.getFloatArrayExtra("thing");

    //float length = extras.getFloat("length", 0.0f);
    //float left_edge = intent.getFloatExtra("left edge", 0.0f);
    //float right_edge = intent.getFloatExtra("right edge", 0.0f);
    //float bottom_margin = intent.getFloatExtra("bottom margin", 0.0f);
    //float top_margin = intent.getFloatExtra("top margin", 0.0f);
    //float diagonal = intent.getFloatExtra("diagonal", 0.0f);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        Button runbutton = findViewById(R.id.runbutton);
        Log.d("yo", ""+arr);
        //outp=findViewById(R.id.output);
/*
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
                float[][] standardArr = {{(float) ((length-mean)/sd), (float) ((left_edge-mean)/sd), (float) ((right_edge-mean)/sd), (float) ((bottom_margin-mean)/sd), (float) ((top_margin-mean)/sd), (float) ((diagonal-mean)/sd)}};
                //double[] arr = {length, left_edge, right_edge, bottom_margin, top_margin, diagonal};
                //float[][] arr = {{214.4f, 130.1f, 130.3f, 9.7f, 11.7f, 139.8f}};



                try {
                    Model model = Model.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 6}, DataType.FLOAT32);
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(24);
                    byteBuffer.order(ByteOrder.nativeOrder());

                    for(int i = 0; i < standardArr[0].length; i++){
                        byteBuffer.putFloat(standardArr[0][i]);
                    }

                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    float[] conf = outputFeature0.getFloatArray();
                    if(conf[0] == 0){
                        outp.setText("Real bill");
                    }
                    else if(conf[0] == 1){
                        outp.setText("Counterfeit bill");
                    }
                    else{
                        outp.setText("ERROR");
                    }


                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });
 */
    }


}