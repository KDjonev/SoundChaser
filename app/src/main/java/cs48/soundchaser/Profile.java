package cs48.soundchaser;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import java.util.ArrayList;

/**
 * Created by Krassi on 1/23/2016.
 */
public class Profile extends Activity implements View.OnClickListener {
    ArrayAdapter<CharSequence> adapterMonth;
    ArrayAdapter<Integer> adapterDay, adapterYear, adapterFeet,
                          adapterInch, adapterWeight;
    Spinner spinnerMonth, spinnerDay, spinnerYear, spinnerFeet,
            spinnerInch, spinnerWeight;

    Button createProfileButton;
    EditText nameEditText;
    TextView setNameViewText, setDobMonthViewText, setDobDayViewText,
             setDobYearViewText, setFtViewText, setInViewText, setWeightViewText;

    UserLocalStore userLocalStore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);
        boolean isProfileCreated = userLocalStore.getIsProfileCreated();

        if (!isProfileCreated)
            setProfile();
        else
            displayProfile();
    }

    private void setProfile() {
        setContentView(R.layout.profile_creation);
        setUpDropdowns();
        createProfileButton = (Button) findViewById(R.id.createProfileButton);
        createProfileButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        String name = nameEditText.getText().toString();
        String dobMonth = spinnerMonth.getSelectedItem().toString();
        String dobDay = spinnerDay.getSelectedItem().toString();
        String dobYear = spinnerYear.getSelectedItem().toString();
        String heightFt = spinnerFeet.getSelectedItem().toString();
        String heightIn = spinnerInch.getSelectedItem().toString();
        String weight = spinnerWeight.getSelectedItem().toString();

        User user = new User(name, dobMonth, dobDay, dobYear, heightFt, heightIn, weight);
        userLocalStore.storeUserData(user);
        userLocalStore.setIsProfileCreated(true);
        displayProfile();
    }

    public void displayProfile() {
        setContentView(R.layout.profile_view);
        User user = userLocalStore.getUserData();
        setNameViewText = (TextView) findViewById(R.id.setNameViewText);
        setNameViewText.setText(user.getName());
        setDobMonthViewText = (TextView) findViewById(R.id.setDobMonthViewText);
        setDobMonthViewText.setText(user.getDobMonth());
        setDobDayViewText = (TextView) findViewById(R.id.setDobDayViewText);
        setDobDayViewText.setText(user.getDobDay());
        setDobYearViewText = (TextView) findViewById(R.id.setDobYearViewText);
        setDobYearViewText.setText(user.getDobYear());
        setFtViewText = (TextView) findViewById(R.id.setFtViewText);
        setFtViewText.setText(user.getHeightFt());
        setInViewText = (TextView) findViewById(R.id.setInViewText);
        setInViewText.setText(user.getHeightIn());
        setWeightViewText = (TextView) findViewById(R.id.setWeightViewText);
        setWeightViewText.setText(user.getWeight());
    }

    private void setUpDropdowns() {
        spinnerMonth = (Spinner)findViewById(R.id.months_spinner);
        adapterMonth = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);

        ArrayList<Integer> day = new ArrayList<>();
        for (int i = 1; i <= 31; i++) { day.add(i);}
        spinnerDay = (Spinner)findViewById(R.id.days_spinner);
        adapterDay = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, day);
        spinnerDay.setAdapter(adapterDay);

        ArrayList<Integer> year = new ArrayList<>();
        for (int i = 2016; i >= 1920; i--) { year.add(i); }
        spinnerYear = (Spinner)findViewById(R.id.years_spinner);
        adapterYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, year);
        spinnerYear.setAdapter(adapterYear);

        ArrayList<Integer> feet = new ArrayList<>();
        for (int i = 1; i <= 8; i++) { feet.add(i); }
        spinnerFeet = (Spinner)findViewById(R.id.feet_spinner);
        adapterFeet = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, feet);
        spinnerFeet.setAdapter(adapterFeet);

        ArrayList<Integer> inch = new ArrayList<>();
        for (int i = 0; i <= 11; i++) { inch.add(i); }
        spinnerInch = (Spinner)findViewById(R.id.inches_spinner);
        adapterInch = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, inch);
        spinnerInch.setAdapter(adapterInch);

        ArrayList<Integer> weight = new ArrayList<>();
        for (int i = 1; i <= 500; i++) { weight.add(i); }
        spinnerWeight = (Spinner)findViewById(R.id.weight_spinner);
        adapterWeight = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weight);
        spinnerWeight.setAdapter(adapterWeight);
    }

}
