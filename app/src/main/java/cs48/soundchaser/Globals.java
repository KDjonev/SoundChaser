package cs48.soundchaser;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krassi on 2/1/2016.
 */
public class Globals {
    //in Kilometers
    private static double distanceToRun = 1;
    private static double maximumRadius = 0.5;
    private static boolean drawRadius = true;
    private static LatLng startLocation = null;
    private static LatLng currentLocation = null;
    private static List<LatLng> listOfLocations = new ArrayList<LatLng>();

    public static List<LatLng> getListOfLocations() {
        return listOfLocations;
    }

    public static LatLng getStartLocation() {
        return startLocation;
    }

    public static void setStartLocation(LatLng startLocation) {
        Globals.startLocation = startLocation;
        setCurrentLocation(startLocation);
    }

    public static LatLng getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(LatLng currentLocation) {
        Globals.currentLocation = currentLocation;
        listOfLocations.add(currentLocation);
    }

    public static double getDistanceToRun() {
        return distanceToRun;
    }

    public static void setDistanceToRun(double distanceToRun) {
        if(distanceToRun > 0) {
            Globals.distanceToRun = distanceToRun;
        }
        setMaximumRadius(distanceToRun/2);
    }

    public static double getMaximumRadius() {
        return maximumRadius;
    }

    public static void setMaximumRadius(double maximumRadius) {
        if(distanceToRun > 0) {
            Globals.maximumRadius = maximumRadius;
        }
    }

    public static boolean getDrawRadius() {
        return drawRadius;
    }

    public static void setDrawRadius(boolean drawRadius) {
        Globals.drawRadius = drawRadius;
    }
}
