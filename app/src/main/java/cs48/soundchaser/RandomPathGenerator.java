package cs48.soundchaser;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Krassi on 2/7/2016.
 */
public class RandomPathGenerator {
    private double distanceToRun;
    private double maxRadius;
    private Location startLocation;
    private Context context;
    private GoogleMap mMap;

    public RandomPathGenerator(Location startLocation, Context context, GoogleMap mMap) {
        this.startLocation = startLocation;
        this.context = context;
        this.mMap = mMap;
        init();
    }

    private void init()
    {
        //debbug
        CharSequence text = "random Path initialized";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
