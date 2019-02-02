package com.example.andi.cobabaca;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.andi.cobabaca.core.TensorFlowClassifier;
import com.example.andi.cobabaca.entity.Classifier;
import com.example.andi.cobabaca.entity.Recognition;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ImageView imgObject;
    private EditText edtUrl;
    private Button btnPredict;

    private static final String MODEL = "mobilenet_quant_v1_224.tflite";
    private static final String LABEL = "labels.txt";
    private static int IMG_SIZE = 224;

    private Recognition recognition;
    private Executor executor = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgObject = (ImageView) findViewById(R.id.img_object);
        edtUrl = (EditText) findViewById(R.id.edt_url);
        btnPredict = (Button) findViewById(R.id.btn_predict);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    recognition = TensorFlowClassifier.init(
                            getAssets(),
                            MODEL,
                            LABEL,
                            IMG_SIZE);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get url
                String url = edtUrl.getText().toString();

                Glide.with(MainActivity.this)
                        .load(url)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource,
                                                        GlideAnimation<? super Bitmap> glideAnimation) {
                                Bitmap bitmap = Bitmap.createScaledBitmap(
                                        resource,
                                        IMG_SIZE,
                                        IMG_SIZE,
                                        false);

                                //showing an image into imageView
                                imgObject.setImageBitmap(bitmap);

                                //predict it!
                                String labels = "";
                                List<Classifier> resultPredicted = recognition.recognize(bitmap);
                                for (Classifier result: resultPredicted) {
                                    labels += result.getTitle() + " (" + result.getConfidence() + ")" + "\n";
                                }

                                Toast.makeText(MainActivity.this,
                                        labels,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }
}
