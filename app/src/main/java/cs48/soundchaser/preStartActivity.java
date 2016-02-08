package cs48.soundchaser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Krassi on 2/7/2016.
 */
public class preStartActivity extends Activity {

    private double distance = 5;
    private double radius = 2.5;
    private boolean customDestination = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_start_activity);
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
            Globals.init(distance,radius, customDestination);
            Intent i = new Intent(this, OurGoogleMap.class);
            startActivity(i);
        } else {
            incorrectInput();
        }

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


