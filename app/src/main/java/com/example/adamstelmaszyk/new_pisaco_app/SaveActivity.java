package com.example.adamstelmaszyk.new_pisaco_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SaveActivity extends AppCompatActivity {


    //The class now is not exist on the projects
    private ImageView imageView;
    private TextView textView;

    private Intent intentReciveData;
    private Bundle extras;
    private String extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);


        intentReciveData = getIntent();
        extras = intentReciveData.getExtras();
        extra = extras.getString("photo");

        imageView = (ImageView) findViewById(R.id.SA_image);
        textView = (TextView) findViewById(R.id.SA_name);

        textView.setText(extra);






    }
}
