package cs48.soundchaser;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Krassi on 2/7/2016.
 */
public class RandomPathGenerator {
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

    private void init()
    {
        //debbug
        CharSequence text = "random Path initialized";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
