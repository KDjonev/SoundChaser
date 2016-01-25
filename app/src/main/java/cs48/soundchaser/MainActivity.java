package cs48.soundchaser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v)
    {
        Intent i;
        switch (v.getId())
        {
            case R.id.startActivityButton :
            i = new Intent(this, OurGoogleMap.class);
            startActivity(i);
            break;

            case R.id.helpButton :
                i = new Intent(this, Help.class);
                startActivity(i);
                break;

            case R.id.settingsButton :
                i = new Intent(this, Settings.class);
                startActivity(i);
                break;

            case R.id.profileButton:
                i = new Intent(this, ProfileSettings.class);
                startActivity(i);
                break;

            case R.id.viewDataButton :
                i = new Intent(this, ViewData.class);
                startActivity(i);
                break;

        }
    }
}


