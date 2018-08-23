package com.example.serkan.facedetector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Serkan Sorman
 * Loads image and detects faces
 */
public class FaceDetection extends AppCompatActivity {

    private FaceDetector faceDetector;
    private Bitmap imageBitmap;
    private ImageView imageView;
    private TextView faceDetectionResult;
    private final int LOAD_IMG_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        faceDetector = new FaceDetector.Builder(this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();


        imageView = (ImageView) findViewById(R.id.image);
        faceDetectionResult = (TextView) findViewById(R.id.faceResult);

    }

    /**
     * Detects face on given image, draw frame around the faces,
     * set landmark on faces and shows information about all images
     */
    private void findFaces() {

        FaceGraphic faceGraphic = new FaceGraphic(imageBitmap);

        Paint faceFramePaint = faceGraphic.getFaceFramePaint();
        Paint landmarkPaint = faceGraphic.getLandMarkPaint();
        Canvas canvas = faceGraphic.getCanvas();

        Paint faceIdPaint = new Paint();
        faceIdPaint.setColor(Color.GREEN);

        Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);


        if(faces.size() != 0) {
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);

                // Draw a frame around the detected face
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), faceFramePaint);

                //Draw id on detected face
                faceIdPaint.setTextSize(face.getWidth() / 3);
                canvas.drawText(String.valueOf(i+1), (face.getWidth() + 2 * face.getPosition().x) / 2,
                        (2 * face.getPosition().y + face.getWidth()) / 2, faceIdPaint);


                //Show each face detection result on screen
                faceDetectionResult.append(String.format("Face %d%s%s%s\n", i + 1, String.format(": Left eye open: %.2f",
                        face.getIsLeftEyeOpenProbability()), String.format("  Right eye open: %.2f",
                        face.getIsRightEyeOpenProbability()), String.format("\nSmile: %.2f",
                        face.getIsSmilingProbability())));

                //Put landmarks on mouth, nose and eyes
                for (Landmark landmark : face.getLandmarks())
                    canvas.drawCircle(landmark.getPosition().x,
                            landmark.getPosition().y, 3, landmarkPaint);

            }

            // Show edited image view
            imageView.setImageBitmap(faceGraphic.getBitmap());

            Toast.makeText(this,faces.size()+" Faces found",Toast.LENGTH_LONG);
            faceDetectionResult.setMovementMethod(new ScrollingMovementMethod());
            faceDetectionResult.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(this, "No faces detected", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load image from gallery
     * @param view
     */
    public void loadImage(View view){

        if(faceDetectionResult != null)
            faceDetectionResult.setText(null);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, LOAD_IMG_REQUEST);
    }


    /**
     * After image is loaded, starts find faces
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK && requestCode == LOAD_IMG_REQUEST) {
            Log.i("FaceDetection","Image load request is verified");
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(imageBitmap);
                findFaces();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(this, "You haven't chosen Image",Toast.LENGTH_LONG).show();
        }
    }
    
}
