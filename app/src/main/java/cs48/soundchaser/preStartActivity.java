package cs48.soundchaser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.Settings;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krassi on 2/7/2016.
 */
public class preStartActivity extends Activity implements OnItemSelectedListener{

    private double distance = 5;
    private double radius = 2.5;
    private boolean customDestination = false;
    private ActivityType aT = ActivityType.DEFAULT;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_start_activity);
        addSpinnerStuff();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        switch (item){
            case "Walking":
                aT = ActivityType.WALKING;
                break;
            case "Running":
                aT = ActivityType.RUNNING;
                break;
            case "Biking":
                aT = ActivityType.BIKING;
                break;
            default:
                aT = ActivityType.DEFAULT;
        }

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void addSpinnerStuff()
    {
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.activitySpinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Default");
        categories.add("Walking");
        categories.add("Running");
        categories.add("Biking");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void gpsCheck()
    {
        int off = 0;
        try {
            off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if(off==0){
            new AlertDialog.Builder(this)
                    .setTitle("Turn Location On?")
                    .setMessage("You must turn On your Location.")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            onBackPressed();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(onGPS);
                        }
                    }).create().show();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        gpsCheck();
    }

    private boolean validateInput() {
        distance = Double.parseDouble(((EditText) findViewById(R.id.distance)).getText().toString());
        //Log.v("input", "distance: " + Double.toString(distance));
        radius = Double.parseDouble(((EditText) findViewById(R.id.radius)).getText().toString());
        //Log.v("input", "radius: " + Double.toString(radius));
        if (distance <= 0 || radius < 0) {
            return false;
        } else if (!customDestination && (radius > (distance / 2.0))) {
            radius = distance / 2.0;
            ((EditText) findViewById(R.id.radius)).setText(Double.toString(radius));
        }
        return true;

    }

    private void incorrectInput() {
        Context context = getApplicationContext();
        CharSequence text = "Incorrect input, please enter again!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void startActivity(View v) {
        CheckBox c = (CheckBox)findViewById(R.id.customDestination);
        customDestination = c.isChecked();
        if (validateInput()) {
            Globals.init(distance,radius, customDestination, aT);
            Intent i = new Intent(this, OurGoogleMap.class);
            startActivity(i);
            finish();
        } else {
            incorrectInput();
        }

    }

    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    public void editDistance(View v) {


    }

    public void editRadius(View v) {


    }

    public double getRadius()
    {
        return radius;
    }

    public double getDistance()
    {
        return distance;
    }

    public boolean getCustomDestination()
    {
        return customDestination;
    }
}


