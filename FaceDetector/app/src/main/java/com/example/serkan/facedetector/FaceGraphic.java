package com.example.serkan.facedetector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author Serkan Sorman
 * Makes graphic operations on the faces
 */
public class FaceGraphic {

    private Canvas canvas;
    private Bitmap editedBitmap;

    /**
     * Creates a canvas reference with edited bitmap
     * @param imageBitmap
     */
    FaceGraphic(Bitmap imageBitmap){
        editedBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), imageBitmap.getConfig());
        canvas = new Canvas(editedBitmap);
        canvas.drawBitmap(imageBitmap, 0, 0, null);
    }


    /**
     * Set required properties to frame paint
     * @return frame paint
     */
    public Paint getFaceFramePaint(){

        Paint faceFrame = new Paint();
        faceFrame.setColor(Color.GREEN);
        faceFrame.setStyle(Paint.Style.STROKE);
        faceFrame.setStrokeWidth(5);

        return faceFrame;
    }

    /**
     * Set required properties to landmark paint
     * @return landmark paint
     */
    public Paint getLandMarkPaint(){

        Paint landmarkPaint = new Paint();
        landmarkPaint.setColor(Color.RED);
        landmarkPaint.setStyle(Paint.Style.FILL);

        return landmarkPaint;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Bitmap getBitmap() {
        return editedBitmap;
    }
}
