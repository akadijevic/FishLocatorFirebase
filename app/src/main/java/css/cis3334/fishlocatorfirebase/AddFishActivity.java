package css.cis3334.fishlocatorfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class AddFishActivity extends AppCompatActivity {

    Button buttonSave;
    EditText editTextSpecies, editTextWeight, editTextDate, editTextLat, editTextLong;
    Double lattitude, longiture;
    FishFirebaseData fishDataSource;
    int PLACE_PICKER_REQUEST = 1001;
    Intent placePickerIntent;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fish);

        // link each editText variable to the xml layout
        editTextSpecies = (EditText) findViewById(R.id.editTextSpecies);
        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextLat = (EditText) findViewById(R.id.editTextLat);

        fishDataSource = new FishFirebaseData();
        fishDataSource.open();

        setupSaveButton();
        setupMaputton();

    }

    private void setupSaveButton() {
        // set up the button listener
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Add the fish to the database
                String species = editTextSpecies.getText().toString();
                String weight = editTextWeight.getText().toString();
                String dateCaught = editTextDate.getText().toString();
                Double longitude, latitude;
                if (place != null) {
                    longitude = place.getLatLng().longitude;
                    latitude = place.getLatLng().latitude;
                    fishDataSource.createFish(species, weight, dateCaught, latitude, longitude);
                } else {
                    fishDataSource.createFish(species, weight, dateCaught);
                }
                Intent mainActivityIntent = new Intent(view.getContext(), MainActivity.class);
                finish();
                startActivity(mainActivityIntent);
            }
        });
    }

    private void setupMaputton() {
        // set up the button listener
        buttonSave = (Button) findViewById(R.id.buttonMap);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddFishActivity.this), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    Log.e("CIS3334", "error with PlacePicker " +  e.toString());
                    place = null;

                }
            }
        });
    }

    /**
     * The following code snippet retrieves the place that the user has selected:
     * From -- https://developers.google.com/places/android-api/placepicker
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                editTextLat.setText(place.getLatLng().toString());
            }
        }
    }



}
