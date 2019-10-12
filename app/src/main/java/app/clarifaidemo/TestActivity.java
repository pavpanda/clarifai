package app.clarifaidemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class TestActivity extends Activity {

    public static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = findViewById(R.id.textView);


        ArrayList<String> comps = MainActivity.finalFoodItemComps;
        for (String comp : comps) {
            textView.setText("" + textView.getText() + "\n" + comp);
        }
    }
}
