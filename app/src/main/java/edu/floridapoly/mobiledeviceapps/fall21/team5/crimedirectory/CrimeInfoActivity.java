package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;


/**
 *  CrimeInfoActivity.java
 *  Purpose: Activity to present Criminal Information, show task completion, and do report feature
 */
public class CrimeInfoActivity extends AppCompatActivity {
    public static String EXTRA_INDEX = "index";
    public static String EXTRA_USER = "user";
    public int index;
    User user;
    TextView detailsText;
    ArrayList<String> tabStrings;
    CheckBox chkReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_info);

        // pass user, get page index
        user = getIntent().getParcelableExtra(EXTRA_USER);
        index = getIntent().getIntExtra(EXTRA_INDEX,0);
        detailsText = findViewById(R.id.details);

        // set for keys extracted from API call
        tabStrings = new ArrayList<>();
        if (index == 0) {
            tabStrings.add("caution");
            tabStrings.add("remarks");
            tabStrings.add("description");
            tabStrings.add("subjects");
        } else {
            tabStrings.add("warning_message");
            tabStrings.add("reward_text");
            tabStrings.add("caution");
            tabStrings.add("remarks");
        }

        // check box setting
        CheckBox chkMap = findViewById(R.id.chkMap);
        CheckBox chkMinigame = findViewById(R.id.chkMinigame);
        chkReport = findViewById(R.id.chkReport);

        Button btnReport = findViewById(R.id.btnReport);
        btnReport.setText("Complete Report");

        chkMap.setChecked(user.isCompletedMap(index));
        chkMinigame.setChecked(user.isCompletedMinigame(index));
        chkReport.setChecked(user.isCompletedReport(index));

        // creates a CrimeInfo object (CrimeInfo contains the api call in it's own getCrimeInfo function)
        getCrimeInfo();
    }

    /**
     * Set text of crime detail text views with API results
     */
    public void getCrimeInfo() {
        TextView name = findViewById(R.id.criminal_name);
        TextView dob = findViewById(R.id.criminal_dob);
        TextView eyes = findViewById(R.id.criminal_eye);
        TextView weight = findViewById(R.id.criminal_weight);
        TextView height = findViewById(R.id.criminal_height);
        TextView race = findViewById(R.id.criminal_race);
        TextView gender = findViewById(R.id.criminal_gender);
        TextView tab1 = findViewById(R.id.tab_1);
        TextView tab2 = findViewById(R.id.tab_2);
        TextView tab3 = findViewById(R.id.tab_3);
        TextView tab4 = findViewById(R.id.tab_4);

        CrimeInfo ci = new CrimeInfo(index, this, this);
        ci.fillTabs(tab1, tab2, tab3, tab4);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(ci.getCrimeInfo("title", name));
        requestQueue.add(ci.getCrimeInfoArray("dates_of_birth_used", dob));
        requestQueue.add(ci.getCrimeInfo("eyes_raw", eyes));
        requestQueue.add(ci.getCrimeInfo("weight_min", weight));
        requestQueue.add(ci.getCrimeInfo("height_min", height));
        requestQueue.add(ci.getCrimeInfo("sex", gender));
        requestQueue.add(ci.getCrimeInfo("race", race));
    }

    /**
     * Starts the minigame activity
     * @param  view  Minigame Button
     */
    public void onClickMinigame(View view) {
        User user = getIntent().getParcelableExtra(EXTRA_USER);

        switch (index) {
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
                intent2.putExtra(MinigameActivity.EXTRA_INDEX, index);
                intent2.putExtra(MinigameActivity.EXTRA_USER, user);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(this, MinigameActivityKidnapping.class);
                intent3.putExtra(MinigameActivity.EXTRA_INDEX, index);
                intent3.putExtra(MinigameActivity.EXTRA_USER, user);
                startActivity(intent3);
                break;
        }
    }

    /**
     * Start the map activity
     * @param  view  Map Button
     */
    public void onClickMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);

        intent.putExtra(MapActivity.EXTRA_INDEX, index);
        intent.putExtra(MapActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * Start the Home Activity
     * @param  view  Home Button
     */
    public void onClickHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        User user = getIntent().getParcelableExtra(EXTRA_USER);

        intent.putExtra(HomeActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * Set the user's completed report array for the investigation index to true
     * @param  view  Complete Report button
     */
    public void onClickChkCompleteReport(View view) {
        Button btn = (Button) view;
        CheckBox chkReport = findViewById(R.id.chkReport);

        if (btn.getText().toString() == "Complete Report") {
            btn.setText("Clear Report");
        } else {
            btn.setText("Complete Report");
        }

        user.setCompletedReport(index, true);
        chkReport.setChecked(true);
    }

    /**
     * Move the scroll view to a postion where the report is in frame
     * @param  view  report icon ImageView
     */
    public void onClickReport(View view) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.scrollTo(0, 2400);
    }

    /**
     * Move the scroll view to the position of the More Info section
     * @param  view  more info icon ImageView
     */
    public void onClickMoreInfo(View view) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.scrollTo(0, 1000);
    }

    /**
     * Retrieve the data for the given tab (sticky tab picture)
     * @param  param  string with the text of the key required from the API call
     */
    public void onClickTab(String param) {
        CrimeInfo ci = new CrimeInfo(index, this, this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(ci.getCrimeInfo(param, detailsText));
        TextView details = findViewById(R.id.details);

        if (details.getVisibility() == View.GONE) {
            details.setVisibility(View.VISIBLE);
        }
    }

    public void onClickTab1(View view) {
        onClickTab(tabStrings.get(0));
    }

    public void onClickTab2(View view) {
        onClickTab(tabStrings.get(1));
    }

    public void onClickTab3(View view) {
        onClickTab(tabStrings.get(2));
    }

    public void onClickTab4(View view) {
        onClickTab(tabStrings.get(3));
    }
}