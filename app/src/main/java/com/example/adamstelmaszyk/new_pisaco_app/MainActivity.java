package com.example.adamstelmaszyk.new_pisaco_app;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.adamstelmaszyk.new_pisaco_app.view.PikassoView;

public class MainActivity extends AppCompatActivity {

    private boolean ChangePhoto = true;
    private int Drawing_Color;
    private int Drawing_Width;

    private String pathsave;

    private PikassoView DrawView;
    private AlertDialog.Builder currentAlerDialog;
    private ImageView widthImageView;
    private AlertDialog dialogLineWidth;

    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;

    private View colorView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawView = (PikassoView) findViewById(R.id.Pikassoview);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.eraseId:
                if(ChangePhoto == true)
                {
                    Drawing_Color = DrawView.getDrawingcolor();
                    Drawing_Width = DrawView.getLineWidth();
                    DrawView.clear_Point();
                    item.setIcon(R.drawable.penphoto);
                    ChangePhoto = false;
                }
                else
                {
                    DrawView.setDrawingColor(Drawing_Color);
                    DrawView.setLineWidth(Drawing_Width);
                    item.setIcon(R.drawable.eraser);
                    ChangePhoto = true;
                }
                break;
            case R.id.newcardId:
                DrawView.clear();
                break;
            case R.id.saveId:
                //pathsave = DrawView.saveImage();
                break;
            case R.id.lineWidth:
                showLineWidthDialog();
                break;
            case R.id.gotoimage:
                //gotosaveview();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotosaveview() {
        Intent intent = new Intent(MainActivity.this, SaveActivity.class);
        intent.putExtra("photo", pathsave);
        startActivity(intent);

    }

    void showLineWidthDialog()
    {
        currentAlerDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.width_dialog, null);


        final SeekBar widthSeekbar = view.findViewById(R.id.WI_widthDSeekBar);
        alphaSeekBar = view.findViewById(R.id.alphaSeekBar);
        redSeekBar = view.findViewById(R.id.redaSeekBar);
        greenSeekBar = view.findViewById(R.id.greenSeekBar);
        blueSeekBar = view.findViewById(R.id.blueSeekBar);


        Button setLineWidthButton = view.findViewById(R.id.WI_widthDialogButton);
        Button setColorButton = view.findViewById(R.id.WI_colorDialogButton);

        widthImageView = view.findViewById(R.id.WI_imageView);
        colorView = view.findViewById(R.id.WI_imageColor);

        setLineWidthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawView.setLineWidth(widthSeekbar.getProgress());
                dialogLineWidth.dismiss();
                currentAlerDialog = null;

            }
        });

        widthSeekbar.setOnSeekBarChangeListener(widthSeekbarChange);
        alphaSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
        redSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
        greenSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
        blueSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);

        int color = DrawView.getDrawingcolor();
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.red(color));
        greenSeekBar.setProgress(Color.green(color));
        blueSeekBar.setProgress(Color.blue(color));

        setColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawView.setDrawingColor(Color.argb(
                        alphaSeekBar.getProgress(),
                        redSeekBar.getProgress(),
                        greenSeekBar.getProgress(),
                        blueSeekBar.getProgress()
                ));
                dialogLineWidth.dismiss();
                currentAlerDialog = null;

            }
        });



        currentAlerDialog.setView(view);
        dialogLineWidth = currentAlerDialog.create();
        dialogLineWidth.setTitle("Set Line Width and Color");

        dialogLineWidth.show();
    }

    private SeekBar.OnSeekBarChangeListener colorSeekBarChanged = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            DrawView.setBackgroundColor(Color.argb(
                    alphaSeekBar.getProgress(),redSeekBar.getProgress(),
                    greenSeekBar.getProgress(),blueSeekBar.getProgress()));

            colorView.setBackgroundColor(Color.argb(
                    alphaSeekBar.getProgress(),
                    redSeekBar.getProgress(),
                    greenSeekBar.getProgress(),
                    blueSeekBar.getProgress()));

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };



    private SeekBar.OnSeekBarChangeListener widthSeekbarChange = new SeekBar.OnSeekBarChangeListener() {
        Bitmap bitmap = Bitmap.createBitmap(400,100,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            //really nice it shows how the line looks
            Paint p = new Paint();
            p.setColor(DrawView.getDrawingcolor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(progress);

            bitmap.eraseColor(Color.WHITE);
            canvas.drawLine(30,50,370,50,p);
            widthImageView.setImageBitmap(bitmap);


        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
