package cs48.soundchaser;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Krassi on 2/7/2016.
 */
public class RandomPathGenerator {
    private static final LatLng STORKE_TOWER = new LatLng(34.4126047, -119.8484183);
    private double distanceToRun;
    private double maxRadius;
    private LatLng startLatLng;
    private Context context;
    private GoogleMap mMap;

    public RandomPathGenerator(LatLng startLatLng, Context context, GoogleMap mMap) {
        this.startLatLng = startLatLng;
        this.context = context;
        this.mMap = mMap;
        init();
    }

    /* CURRENTLY ONLY GENERATES *A* PATH. IT IS NON-RANDOM AND OF UNSPECIFIED LENGTH. */
    public void generate(LatLng dest) {
        if (Globals.getCustomDestination() == false)
            dest = STORKE_TOWER;
        String url = "";
        url = getMapsApiDirectionsUrl(dest);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
    }

    private void init() {
        //debbug
        CharSequence text = "random Path initialized";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private String getMapsApiDirectionsUrl(LatLng dest) {
        String waypoints = "waypoints=optimize:true|"
                + startLatLng.latitude + "," + startLatLng.longitude
                + "|" + "|" + dest.latitude + ","
                + dest.longitude;

        String OriDest = "origin="+startLatLng.latitude+","+startLatLng.longitude+"&destination="+dest.latitude+","+dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=walking";
        String params = OriDest+"&%20"+waypoints + "&" + sensor + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                JsonParser parser = new JsonParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            System.out.println(routes.size());
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(5);
                polyLineOptions.color(Color.RED);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }

}