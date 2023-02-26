package com.example.counterfeitmoneydetector;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class TestActivity extends AppCompatActivity {

    // Name of the TFLite model file in the assets folder
    private static final String MODEL_FILENAME = "model.tflite";

    // Input and output tensor shapes
    private static final int[] INPUT_SHAPE = {1, 6}; // 1 row, 6 columns
    private static final int[] OUTPUT_SHAPE = {1}; // Single output number

    // Interpreter object for running inference with the TFLite model
    private Interpreter tfliteInterpreter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        float[] inputArray = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f}; // Input 2D array with one row and six columns
        TestActivity modelRunner = new TestActivity();
        float outputValue = modelRunner.runInference(inputArray); // Output number
        Log.d("hello", ""+outputValue);
    }


    public TestActivity() {
        try {
            // Load the TFLite model from the assets folder
            MappedByteBuffer tfliteModelBuffer = loadModelFile();
            // Create the Interpreter object
            Interpreter.Options options = new Interpreter.Options();
            tfliteInterpreter = new Interpreter(tfliteModelBuffer, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd(MODEL_FILENAME);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float runInference(float[] inputArray) {
        // Create the input TensorBuffer from the input float array
        TensorBuffer inputBuffer = TensorBuffer.createFixedSize(INPUT_SHAPE, DataType.FLOAT32);
        inputBuffer.loadArray(inputArray);

        // Create the output TensorBuffer to store the output of the model
        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(OUTPUT_SHAPE, DataType.FLOAT32);

        // Run inference with the input TensorBuffer and store the output in the output TensorBuffer
        tfliteInterpreter.run(inputBuffer.getBuffer(), outputBuffer.getBuffer());

        // Get the output value from the output TensorBuffer
        float outputValue = outputBuffer.getFloatValue(0);

        return outputValue;
    }

}