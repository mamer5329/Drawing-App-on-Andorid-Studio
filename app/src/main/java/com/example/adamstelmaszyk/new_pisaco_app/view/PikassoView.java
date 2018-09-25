package com.example.adamstelmaszyk.new_pisaco_app.view;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.adamstelmaszyk.new_pisaco_app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

public class PikassoView extends View {

    public static final  float TOUCH_TOLERANCE = 10;

    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint paintScreen;
    private Paint paintLine;
    //Update project
    private Paint figures;

    private HashMap<Integer, Path> pathMap;
    private HashMap<Integer, Point> previousPointMap;

    public PikassoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();


    }

    void init()
    {
        paintScreen = new Paint();

        figures = new Paint();
        figures.setColor(Color.YELLOW);
        figures.setStrokeWidth(30);
        figures.setStrokeCap(Paint.Cap.ROUND);
        figures.setStyle(Paint.Style.STROKE);

        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.BLACK);
        paintLine.setStrokeWidth(7);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
        paintLine.setStyle(Paint.Style.STROKE);

        pathMap = new HashMap<>();
        previousPointMap = new HashMap<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0,paintScreen);


        for(Integer key: pathMap.keySet())
        {
            canvas.drawPath(pathMap.get(key),paintLine);
        }

        //canvas.drawRect(canvas.getWidth()/4, canvas.getHeight()/4, canvas.getWidth() / 2, canvas.getHeight() / 2, figures);
        //Tak sie rysuje kulke
        //canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,78,figures);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked(); //vent type
        int actionIndex =event.getActionIndex();

        if(action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_POINTER_UP)
        {
            touchStarted(event.getX(actionIndex), event.getY(actionIndex),event.getPointerId(actionIndex));
            
        } else if (action == MotionEvent.ACTION_UP || action== MotionEvent.ACTION_POINTER_UP)
        {
            touchEnded(event.getPointerId(actionIndex));
        }
        else
        {
            touchMoved(event);
        }
        invalidate();

        Log.d("Screen Touched", "Heyyyy");

        return true;
    }

    public void setDrawingColor(int color)
    {
        paintLine.setColor(color);
    }

    public int getDrawingcolor()
    {
        return paintLine.getColor();
    }

    public void setLineWidth(int width)
    {
        paintLine.setStrokeWidth(width);
    }

    public int getLineWidth()
    {
        return (int) paintLine.getStrokeWidth();
    }

    public void clear_Point()
    {
        paintLine.setColor(Color.argb(255,255,255,255));
        paintLine.setStrokeWidth(70);
    }


    public void  clear()
    {
        pathMap.clear();
        previousPointMap.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate(); // refresh screen
    }

    private void touchEnded(int pointerId)
    {
        Path path = pathMap.get(pointerId);
        bitmapCanvas.drawPath(path, paintLine);
        path.reset();
    }

    private void touchStarted(float x, float y, int pointerId) {

        Path path;
        Point point;

        if(pathMap.containsKey(pointerId))
        {
            path = pathMap.get(pointerId);
            point = previousPointMap.get(pointerId);
        }
        else
        {
            path = new Path();
            pathMap.put(pointerId, path);
            point = new Point();
            previousPointMap.put(pointerId, point);
        }

        path.moveTo(x,y);
        point.x = (int) x;
        point.y = (int) y;
    }

    private void touchMoved(MotionEvent event)
    {
         for(int i = 0; i<event.getPointerCount(); i++)
         {
             int pointerId = event.getPointerId(i);
             int pointerIndex = event.findPointerIndex(pointerId);

             if(pathMap.containsKey(pointerId))
             {
                 float newX = event.getX(pointerIndex);
                 float newY = event.getY(pointerIndex);

                 Path path = pathMap.get(pointerId);
                 Point point = previousPointMap.get(pointerId);

                 float dellaX = Math.abs(newX - point.x);
                 float dellaY = Math.abs(newY - point.y);

                 if(dellaX == TOUCH_TOLERANCE || dellaY >= TOUCH_TOLERANCE) {
                     path.quadTo(point.x, point.y, (newX + point.x) / 2, (newY + point.y) / 2);

                     point.x = (int) newX;
                     point.y = (int) newY;
                 }
             }
         }

    }


    //Problems I am working on it
    /*public String saveImage()
    {
        ContextWrapper cw = new ContextWrapper(getContext());
        String filename = "Pikasso" + System.currentTimeMillis();

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        File mypath = new File(Environment.getExternalStorageDirectory()+"/"+"folderapp/"+ UUID.randomUUID().toString()+".jpg");

        OutputStream fos = null;

        try {
            fos = new FileOutputStream(mypath);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.flush();
                fos.close();

                MediaScannerConnection.scanFile(getContext(), new String[] { mypath.toString() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d("appname", "image is saved in gallery and gallery is refreshed.");
                            }
                        }
                );



                /*Log.d("Image", mypath.getAbsolutePath());
                Toast message = Toast.makeText(getContext(), "Image Saved", Toast.LENGTH_LONG);
                message.setGravity(Gravity.CENTER, message.getXOffset() / 2, message.getYOffset() / 2);
                message.show();

            } catch (IOException e) {

                Toast message = Toast.makeText(getContext(), "Image Not Saved", Toast.LENGTH_LONG);
                message.setGravity(Gravity.CENTER, message.getXOffset() / 2, message.getYOffset() / 2);
                message.show();
                //e.printStackTrace();

            }
        }

        return mypath.getAbsolutePath();
    }

    public void loadImageFromStorage(String path)
    {
       try{
           File f = new File(path, "profile.jpg");
           Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
           bitmap = b;
       }
       catch (FileNotFoundException e)
       {
           e.printStackTrace();
       }
    }*/

}
