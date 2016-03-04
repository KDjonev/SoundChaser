package cs48.soundchaser;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Krassi on 2/7/2016.
 */
public class RandomPathGenerator {
    // private static final LatLng STORKE_TOWER = new LatLng(34.4126047, -119.8484183);
    private double distanceToRun;
    private double maxRadius;
    private LatLng startLatLng;
    private Context context;
    private GoogleMap mMap;
    private ProgressDialog dialog;
    private boolean isOverDesiredLength = false;
    private List<PolylineOptions> generatedPath =  new ArrayList<PolylineOptions>();
    private double generatedPathLength = 0; // km
    private String subPathLengthString;
    private int FOR_DEBUGGING_COLORS = 0;

    public RandomPathGenerator(LatLng startLatLng, Context context, GoogleMap mMap) {
        this.startLatLng = startLatLng;
        this.context = context;
        this.mMap = mMap;
        distanceToRun = Globals.getDistanceToRun();
        maxRadius = Globals.getMaximumRadius();
    }

    public List<PolylineOptions> getGeneratedPath() {
        return generatedPath;
    }

    /* STILL NEED TO COMBINE AND ADJUST DEFAULT GOOGLE PATHS FOR PATH OF DESIRED LENGTH.
     * CURRENTLY:
     * If no custom destination is set, the default Google path is drawn between source
     * and a RandomPoint. Path should never go outside the radius. Otherwise, default Google path is
     * drawn between source and custom dest WHICH MAY GO OUTSIDE RADIUS.
     */
    public void generate(LatLng startLatLng, LatLng dest) {
        if (!Globals.getCustomDestination()) {
            RandomPoint destPt = new RandomPoint(Globals.getStartLocation(), maxRadius);
            dest = destPt.getCoordinates();
        }

        String url = "";
        url = getMapsApiDirectionsUrl(startLatLng, dest);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
    }

    private String getMapsApiDirectionsUrl(LatLng start, LatLng dest) {
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
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Please Wait", "Generating path...");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
            dialog.dismiss();
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
                JSONObject distanceObj = parser.getDistanceObj();
                subPathLengthString = distanceObj.getString("text");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions polyLineOptions = null;
            boolean isPtInsideRadius = true;

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

                    // If a point lies outside the radius, this path cannot be used.
                    // For now, a path to custom destination can be outside radius though.
                    if (!Globals.getCustomDestination()) {
                        isPtInsideRadius = isInCircle(lat, lng);
                        if (!isPtInsideRadius) {
                            System.out.println("OUTSIDE!!!!!!!!!!"); // DEBUG
                            break;
                        }
                    }
                    points.add(position);
                    System.out.println(points);  // DEBUG
                }

                if (!isPtInsideRadius) {
                    break;
                }
                else {
                    polyLineOptions.addAll(points);
                    polyLineOptions.width(10);
                    polyLineOptions.color(pickColor());
                }
            }

            if (isPtInsideRadius) {
                //mMap.addPolyline(polyLineOptions);
                double lengthKm = convertLengthToKm(subPathLengthString);
                System.out.println("SUBPATH LENGTH");
                System.out.println(subPathLengthString); // DEBUG
                generatedPathLength = generatedPathLength + lengthKm;
                if (generatedPathLength >= distanceToRun) {
                    isOverDesiredLength = true;
                }
                generatedPath.add(polyLineOptions);
                System.out.println("GENERATED PATH SIZE"); // DEBUG
                System.out.println(generatedPath.size()); // DEBUG

                LatLng lastPt = points.get(points.size()-1);
                Globals.setNonCustomDest(lastPt);
                startLatLng = lastPt; /* If there is to be another sub-path, it will start at
                                        the end off the last sub-path */
                System.out.println("START LATLNG:"); // DEBUG
                System.out.println(startLatLng); // DEBUG

                System.out.println(generatedPathLength); // DEBUG
                //String generatedPathLengthStr = Double.toString(generatedPathLength) + " km";
                //Toast.makeText(context, generatedPathLengthStr, Toast.LENGTH_SHORT).show(); // DEBUG

                if (!isOverDesiredLength && !Globals.getCustomDestination()) {
                    generate(startLatLng, null); // Need to add another sub-path to generatedPath to make it longer.
                }
                else {  // Draw path to map
                    if (!Globals.getCustomDestination()) { // shorten path first
                        List<LatLng> newLastSegment = shavePath(generatedPath);
                        Globals.setNonCustomDest(newLastSegment.get(newLastSegment.size() - 1));
                        PolylineOptions lastSegment = new PolylineOptions();
                        lastSegment.addAll(newLastSegment);
                        lastSegment.width(10);
                        ++FOR_DEBUGGING_COLORS;
                        lastSegment.color(pickColor());
                        generatedPath.set(generatedPath.size() - 1, lastSegment);
                    }
                    for (int i = 0; i < generatedPath.size(); i++) {
                        mMap.addPolyline(generatedPath.get(i));
                        System.out.println("NUMBER OF SUBPATHS:"); // DEBUG
                        System.out.println(generatedPath.size()); // DEBUG
                    }
                }
            }
            else {
                generate(startLatLng, null); // Sub-path was outside radius. Generate a new one.
            }
        }
    }

    private boolean isInCircle(double lat, double lng) {
        double startLat = Globals.getStartLocation().latitude;
        double startLng = Globals.getStartLocation().longitude;
        float[] dist = new float[2];
        Location.distanceBetween(startLat, startLng, lat, lng, dist);
        float distInKm = dist[0]/1000;
        if (distInKm > maxRadius) {
            return false;
        }
        return true;
    }

    private double convertLengthToKm(String subPathLengthString) {
        double NUM_OF_KM_IN_MILE = 1.60934;
        double NUM_OF_KM_IN_FOOT = 0.0003048;
        double lengthKm = 0;
        //get the units
        String units = subPathLengthString.substring(subPathLengthString.indexOf(' ')+1, subPathLengthString.length());
        // get the value of length without the units
        String number = subPathLengthString.substring(0, subPathLengthString.indexOf(' '));
        if (units.equals("ft")) {
            lengthKm = Double.parseDouble(number) * NUM_OF_KM_IN_FOOT;
        }
        else if (units.equals("mi")){
            lengthKm = Double.parseDouble(number) * NUM_OF_KM_IN_MILE;
        }
        return lengthKm;
    }


    /* Given a path of length >= distanceToRun, this method will find
       the cutoff point to make length = distanceToRun and then return the
       coordinates of the shortened path.
     */
   private List<LatLng> shavePath(List<PolylineOptions> path) {
       PolylineOptions lastPathSegment = path.get(generatedPath.size() - 1);
       List<LatLng> lastSegment = lastPathSegment.getPoints();
       if (generatedPathLength == distanceToRun)
           return lastSegment;
       List<LatLng> toRemove = new ArrayList<LatLng>();
       int cutoffIndx = lastSegment.size()-2; // Starting from end, going backwards
       double lengthToCut = generatedPathLength - distanceToRun;
       double measuredLength = 0; // km
       while (measuredLength < lengthToCut && cutoffIndx != -1) {
           measuredLength += (distanceBetweenLatLngs(lastSegment.get(cutoffIndx), lastSegment.get(cutoffIndx+1)))/1000;
           toRemove.add(lastSegment.get(cutoffIndx+1));
           --cutoffIndx;
       }
       lastSegment.removeAll(toRemove);
       return lastSegment;
    }

    private float distanceBetweenLatLngs(LatLng p1, LatLng p2)
    {
        //location of first postition
        Location loc1 = new Location("");
        loc1.setLatitude(p1.latitude);
        loc1.setLongitude(p1.longitude);
        //location of second postition
        Location loc2 = new Location("");
        loc2.setLatitude(p2.latitude);
        loc2.setLongitude(p2.longitude);
        //distance
        float distance = loc1.distanceTo(loc2);
        return distance;
    }

    private int pickColor() {
        int color;
        if (FOR_DEBUGGING_COLORS % 2 == 0) // DEBUG
            color = Color.RED;
        else
            color = Color.DKGRAY;
        ++FOR_DEBUGGING_COLORS;
        return color;
    }
}

