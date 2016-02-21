package cs48.soundchaser;

import android.graphics.Color;
import android.location.Location;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Krassi on 2/20/2016.
 */
public class postWorkoutActivity {
    long duration;
    GoogleMap mMap;

    public postWorkoutActivity(long d, GoogleMap m)
    {
        duration = d;
        mMap = m;
        displayRun();
    }

    private void displayRun()
    {
        drawPath();

    }

    private void drawPath()
    {
        if(mMap == null)
        {
            return;
        }
        mMap.addPolyline(new PolylineOptions()
                        .addAll(Globals.getListOfLocations())
                        .width(12)
                        .color(Color.parseColor("#05b1fb"))//Google maps blue color
                        .geodesic(true)
        );

    }


}
