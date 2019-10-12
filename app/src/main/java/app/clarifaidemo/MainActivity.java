package app.clarifaidemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import junit.framework.Test;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import clarifai2.dto.prediction.Concept;


public class MainActivity extends Activity {

    public static ArrayList<String> finalFoodItemComps;
    private final int CAMERA_REQUEST = 1888;
    public static Button[] buttons = new Button[4];
    String finalFoodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
  //      this.startActivityForResult(intent, CAMERA_REQUEST);


        Button buttono1 = (Button) findViewById(R.id.buttono1);
        Button buttono2 = (Button) findViewById(R.id.buttono2);
        Button buttono3 = (Button) findViewById(R.id.buttono3);
        Button buttono4 = (Button) findViewById(R.id.buttono4);
        buttons[0] = buttono1;
        buttons[1] = buttono2;
        buttons[2] = buttono3;
        buttons[3] = buttono4;

        for (final Button button : buttons) {
            button.setHeight(getResources().getDisplayMetrics().heightPixels / 4);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalFoodItem = button.getText().toString().trim();
                }
            });
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            assert photo != null;
//            storeImage(photo);
//            new ClarifaiPost().execute("");
//
//        }
//    }

//    private void storeImage(Bitmap bitmap) {
//        FileOutputStream outStream = null;
//
//        // Write to SD Card
//        try {
//
//            File sdCard = Environment.getExternalStorageDirectory();
//            File dir = new File(sdCard + "/profiles");
//            dir.mkdirs();
//            dir.createNewFile();
//
//            String fileName = "cool.jpg";
//            File outFile = new File(dir.getAbsolutePath(), fileName);
//            outStream = new FileOutputStream(outFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//
//            Log.d("POOP", "onPictureTaken - wrote to " + outFile.getAbsolutePath());
//
//        } catch (FileNotFoundException e) {
//            Toast.makeText(this, "FNF", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    }
