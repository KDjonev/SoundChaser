package cs48.soundchaser;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Krassi on 1/23/2016.
 */
public class ProfileSettings extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isProfileCreated = false;

        if (!isProfileCreated) /* If profile has not yet been created, create it */
            setContentView(R.layout.profile_creation);
        else    /* If profile has been created, show profile info. */
            setContentView(R.layout.profile_view);
    }

}
