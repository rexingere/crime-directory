package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;


/**
 *  MapActivity.java
 *  Purpose: Activity with map instructions and navigation to MapsActivity
 */
public class MapActivity extends AppCompatActivity {
    public static String EXTRA_INVESTIGATION = "extra investigation";
    public static String EXTRA_INDEX = "extra index";
    public static String EXTRA_USER = "extra user";
    public static String EXTRA_MAP_CHECK = "extra map check";
    public static String EXTRA_TOAST_STR = "extra toast string";

    User user;
    int index;
    CheckBox checkBox;
    boolean showHint = true;
    int difficulty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        user = getIntent().getParcelableExtra(EXTRA_USER);
        index = getIntent().getIntExtra(EXTRA_INDEX, 0);

        checkBox = findViewById(R.id.chkMap);
        checkBox.setChecked(user.isCompletedMap(index));
    }

    // when press back button from MapsActivity, passes if they got the correct location
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intentData = result.getData();
                        boolean approved = intentData.getBooleanExtra(EXTRA_MAP_CHECK,false);

                        if (approved) {
                            user.setCompletedMap(index, true);
                            checkBox.setChecked(true);
                        }
                    }
                }
            });

    /**
     * launch the maps activity
     * @param  view  Button labeled "Go to Map"
     */
    public void onClickMaps(View view) {
        Intent intent = new Intent(this, MapsActivity.class);

        intent.putExtra(MapsActivity.EXTRA_INDEX, index);
        intent.putExtra(MapsActivity.EXTRA_USER, user);
        intent.putExtra(MapsActivity.EXTRA_DIFFICULTY, difficulty);
        someActivityResultLauncher.launch(intent);
    }

    /**
     * set the user's completedMinigame array for the index of the investigation to true
     * @param  view  CheckBox for the completing the map activity
     */
    public void onClickChk(View view) {
        CheckBox chk = (CheckBox) view;
        user.setCompletedMap(index, chk.isChecked());
    }

    /**
     * launch the MinigameActivity
     * @param  view  tab labeled "Minigame" TextView
     */
    public void onClickMinigame(View view) {
        switch(index) {
            case 0:
                Intent intent = new Intent(this, MinigameActivityCyber.class);
                intent.putExtra(MinigameActivityCyber.EXTRA_INDEX, index);
                intent.putExtra(MinigameActivityCyber.EXTRA_USER, user);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, MinigameActivity.class);
                intent1.putExtra(MinigameActivity.EXTRA_INDEX, index);
                intent1.putExtra(MinigameActivity.EXTRA_USER, user);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(this, MinigameActivityFraud.class);
                intent2.putExtra(MinigameActivityFraud.EXTRA_INDEX, index);
                intent2.putExtra(MinigameActivityFraud.EXTRA_USER, user);
                startActivity(intent2);
                break;
        }
    }

    /**
     * launch the CrimeInfoActivity
     * @param  view  tab labeled "Info" TextView
     */
    public void onClickInfo(View view) {
        Intent intent = new Intent(this, CrimeInfoActivity.class);

        intent.putExtra(CrimeInfoActivity.EXTRA_INDEX, index);
        intent.putExtra(CrimeInfoActivity.EXTRA_USER, user);
        startActivity(intent);
    }

    /**
     * set the difficulty for the MapsActivity
     * difficulty -> sets the max distance that a point on the map is considered correct
     *
     * @param  view  radio button for the difficulty setting
     */
    public void onClickRadioBtn(View view) {
        RadioButton selectedRadioBtn = (RadioButton) view;
        int id = selectedRadioBtn.getId();

        switch (id) {
            case R.id.radioBtn_0:
                difficulty = 0;
                break;
            case R.id.radioBtn_1:
                difficulty = 1;
                break;
            case R.id.radioBtn_2:
                difficulty = 2;
                break;
        }
    }

    /**
     * Display a GridLayout containing details on the difficuly settings
     * for the MapsActivity
     *
     * @param  view  hint icon ImageView
     */
    public void onClickHint(View view) {
        GridLayout grid = findViewById(R.id.grid);
        LinearLayout linearLayout = findViewById(R.id.instructionLayout);
        LinearLayout tabsLayout = findViewById(R.id.tabsLayout);

        if (showHint) {
            grid.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);
            tabsLayout.setVisibility(View.INVISIBLE);
        } else {
            grid.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            tabsLayout.setVisibility(View.VISIBLE);
        }

        showHint = !showHint;
    }
}