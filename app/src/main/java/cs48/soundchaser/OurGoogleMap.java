package cs48.soundchaser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Marker;

public class OurGoogleMap extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    public static final String TAG = OurGoogleMap.class.getSimpleName();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private float zoomLevel;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Marker mCurrLocation;
    Marker startLocation;
    Marker finishLocation;
    protected int mDpi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("runnin", "onCreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        setUpMapIfNeeded();
        mDpi = getResources().getDisplayMetrics().densityDpi;


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(9 * 1000)        // 9 seconds, in milliseconds
                .setFastestInterval(3 * 1000); // 3 second, in milliseconds
        Log.i("runnin", "onCreate finished");
    }

    @Override
    protected void onResume() {
        Log.i("runnin", "onResume start");
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
        Log.i("runnin", "onResume finish");
    }

    @Override
    protected void onPause() {
        Log.i("runnin", "onPause start");
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        Log.i("runnin", "onPause finsished");
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        Log.i("runnin", "setUpMapIFneeded start");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
        Log.i("runnin", "setUpMapIFneeded finish");
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        Log.i("runnin", "setUpMap start");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("runnin", "onConnected start");
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            else {
                handleStart(location);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        Log.i("runnin", "onConnected finsihed");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("runnin", "onConnectionSuspended start");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    /*
     * Google Play services can resolve some errors it detects.
     * If the error has a resolution, try sending an Intent to
     * start a Google Play services activity that can resolve
     * error.
     */
        Log.i("runnin", "onConnectionFailed start");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            /*
             * Thrown if Google Play services canceled the original
             * PendingIntent
             */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
        /*
         * If no resolution is available, display a dialog to the
         * user with the error.
         */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
        Log.i("runnin", "onConnectionFailed finsih");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("runnin", "********onLocationChanged start");
        handleNewLocation(location);
        Log.i("runnin", "********onLocationChanged finish");

    }

    public void handleNewLocation(Location location) {
        Log.i("runnin", "handleNewLocation start");
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        if(Globals.getStartLocation() == null)
        {
            Globals.setStartLocation(latLng);
        }
        else
        {
            Globals.setCurrentLocation(latLng);
        }
        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }
        placeMarker(latLng, Icon.RUN_MAN);
        //debbug
        Context context = getApplicationContext();
        CharSequence text = "new Location!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Log.i("runnin", "handleNewLocation finish");
    }

    private void drawCircle( LatLng location ) {
        if (mMap == null || !Globals.getDrawRadius()) {
            return;
        }
        CircleOptions options = new CircleOptions();
        options.center(location);
        //Radius in meters
        double radius = Globals.getMaximumRadius() * 1000;
        options.radius(radius);
        options.strokeWidth(10);
        mMap.addCircle(options);
        Globals.setDrawRadius(false);
        radius = radius + radius/2;
        double scale = radius / 500;
        zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));


    }

    private void drawPath()
    {
        Polyline line = mMap.addPolyline(new PolylineOptions()
                        .addAll(Globals.getListOfLocations())
                        .width(12)
                        .color(Color.parseColor("#05b1fb"))//Google maps blue color
                        .geodesic(true)
        );
    }

    public void handleStart(Location location) {
        Log.i("runnin", "handleStart start");
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        if (Globals.getStartLocation() == null) {
            Globals.setStartLocation(latLng);
        } else {
            Globals.setCurrentLocation(latLng);
        }

        placeMarker(latLng, Icon.START_FLAG);
        zoomLevel = 15; //This goes up to 21
        drawCircle(latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        generateRandomPath(location);
        handleNewLocation(location);
        Log.i("runnin", "handleStart finsih");
    }

    private void generateRandomPath(Location location)
    {
        RandomPathGenerator r = new RandomPathGenerator(location, this, mMap);
    }

    private void placeMarker(LatLng latLng, Icon i)
    {
        int deviceW = getResources().getDisplayMetrics().widthPixels;
        int deviceH = getResources().getDisplayMetrics().heightPixels;
        int scale = Math.min(deviceH,deviceW);
        Log.i("size", "width = " + deviceW + " and height = " + deviceH);
        int newWidth = scale/15;
        int newHeight = scale/15;
        Log.i("size", "width = " + newWidth + " and height = " + newHeight);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Position");
        Bitmap b;

        switch(i)
        {
            case START_FLAG:
                b = resizeBitmap(R.drawable.start_flag, newWidth, newHeight);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                startLocation = mMap.addMarker(markerOptions);
                return;
            case BIKER:
                //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.biker));
                b = resizeBitmap(R.drawable.biker, newWidth, newHeight);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                break;
            case HIKER:
                b = resizeBitmap(R.drawable.hiker, newWidth, newHeight);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                break;
            case RUN_MAN:
                b = resizeBitmap(R.drawable.run_man, newWidth, newHeight);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                break;
            case WALK_MAN:
                b = resizeBitmap(R.drawable.walk_man, newWidth, newHeight);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                break;
            case FINISH_FLAG:
                b = resizeBitmap(R.drawable.finish_flag, newWidth, newHeight);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                finishLocation = mMap.addMarker(markerOptions);
                return;
        }
        mCurrLocation = mMap.addMarker(markerOptions);
    }

    private Bitmap resizeBitmap(int id, int w, int h)
    {
        Bitmap bitMap = BitmapFactory.decodeResource(getResources(),id,null);
        Bitmap resized = Bitmap.createScaledBitmap(bitMap, w, h, true);
        return resized;
    }


}