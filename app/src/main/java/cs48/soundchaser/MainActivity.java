package cs48.soundchaser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;
        switch(id) {
            case R.id.settings :
                i = new Intent(this, Settings.class);
                startActivity(i);
                break;
            case R.id.profile :
                i = new Intent(this, Profile.class);
                startActivity(i);
                break;
            case R.id.viewData :
                i = new Intent(this, ViewData.class);
                startActivity(i);
                break;
            case R.id.help :
                i = new Intent(this, Help.class);
                startActivity(i);
                break;
        }
        return true;
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
        }
    }
}


