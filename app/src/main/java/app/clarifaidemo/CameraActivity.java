package app.clarifaidemo;

import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private FrameLayout frameLayout;
    ShowCamera showCamera;
    ImageButton cameraButton;

    // change the way that I do this later
    public static String predictedFoodName = "banana";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        camera = Camera.open();
        cameraButton = (ImageButton) findViewById(R.id.cameraButton);

        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

    }
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera){
            File picture_file = getOutputMediaFile();
            if(picture_file == null){
                return;
            }else{
                try {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(bytes);
                    fos.close();
                    camera.startPreview();
                    new ClarifaiPost().execute("");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    };

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }else{
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "profiles");
            if(!folder_gui.exists()){
                folder_gui.mkdir();

            }
            File outputFile = new File(folder_gui,"testImage.jpg");
            return outputFile;
        }
    }

    private void captureImage(){
        if(camera!=null){
            camera.takePicture(null,null, pictureCallback);
        }
    }
}
