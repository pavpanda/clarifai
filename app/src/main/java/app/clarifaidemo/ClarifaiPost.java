package app.clarifaidemo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.ModelVersion;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.model.output_info.ConceptOutputInfo;
import clarifai2.dto.prediction.Concept;
import clarifai2.dto.prediction.Prediction;
import okhttp3.OkHttpClient;

public class ClarifaiPost extends AsyncTask<String, Void, List<Concept>> {

    @Override
    protected List<Concept> doInBackground(String... imageLocation) {

        File sdCard = Environment.getExternalStorageDirectory();

        File clarImages = new File(sdCard + "/profiles/testImage.jpg");

        ClarifaiClient clarifaiClient = new ClarifaiBuilder("d55ed2dcf8aa45aca324c0f08c2644f1")
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync(); // or use .build() to get a Future<ClarifaiClient>


        ClarifaiResponse<List<ClarifaiOutput<Concept>>> response = clarifaiClient.getDefaultModels().foodModel().predict()
                .withInputs(ClarifaiInput.forImage(clarImages))
                .executeSync();

        Log.i("Result", Integer.toString(response.responseCode()));


        if (response.isSuccessful()) {
            List<ClarifaiOutput<Concept>> guesses = response.get();
            for (ClarifaiOutput<Concept> guess : guesses) {
                List<Concept> predictions = guess.data();
                for (Concept prediction : predictions) {
                    String s  = prediction.name();
                    Log.i("Result", prediction.name() + " " + (prediction.value() * 100));
                }
                return predictions;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Concept> concepts) {
        super.onPostExecute(concepts);
        Button[] buttons = MainActivity.buttons;
//        for (int i = 0; i < buttons.length; i++) {
//            buttons[i].setText(concepts.get(i).name());
//        }

        new NutritionixPost().execute(concepts.get(0).name());
    }

    public class NutritionixPost extends AsyncTask<String, Void, ArrayList<String>> {
        private static final String TAG = "Result";
        private static final int NUMBER_OF_COMPONENTS = 16;

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                URL url = new URL("https://trackapi.nutritionix.com/v2/natural/nutrients");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-app-id", "733140a8");
                conn.setRequestProperty("x-app-key", "88a5e8a966e7464435b63e38a000c753");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("query", "1 " + params[0]);
                jsonParam.put("timezone", "US/Eastern");

                Log.i(TAG, jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                Log.i(TAG, String.valueOf(conn.getResponseCode()));
                Log.i(TAG , conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine = in.readLine();
                int startingIndex = inputLine.indexOf("\"food_name");
                int cutoffIndex = inputLine.indexOf(",\"full_nutrients");
                inputLine = inputLine.substring(startingIndex, cutoffIndex);

                ArrayList<String> components = splitStringIntoComponents(inputLine);

                for (String component : components) {
                    Log.i(TAG, component);
                }

                in.close();
                conn.disconnect();

                return components;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<String> splitStringIntoComponents(String string) {
            ArrayList<String> returnArrayList = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_COMPONENTS; i++) {
                String appendingString;
                int commaIndex = string.indexOf(",");
                if (i < 15) {
                    appendingString = string.substring(0, commaIndex);
                }
                else {
                    appendingString = string;
                }
                string = string.substring(commaIndex + 1);
                returnArrayList.add(appendingString);
            }
            return returnArrayList;
        }
    }
}
