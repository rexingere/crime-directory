package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;


/**
 *  MinigameActivity.java
 *  Purpose: Present the Case 2 Minigame
 *  Interaction: Click the character who most resembles Thomas Smika
 */
public class MinigameActivity extends AppCompatActivity {
    public static String EXTRA_INDEX = "index";
    public static String EXTRA_USER = "extra_user";

    User user;
    public int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);
        user = getIntent().getParcelableExtra(EXTRA_USER);
        index = getIntent().getIntExtra(EXTRA_INDEX, 0);
    }

    /**
     * launches  CrimeInfoActivity
     * @param  view  "Info" tab TextView
     */
    public void onClickInfo(View view) {
        Intent intent = new Intent(this, CrimeInfoActivity.class);
        intent.putExtra(CrimeInfoActivity.EXTRA_INDEX, index);
        intent.putExtra(CrimeInfoActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * launches MapActivity
     * @param  view  "Map" tab TextView
     */
    public void onClickMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        String investigationStr = "default investigation";

        switch(index) {
            case 0:
                investigationStr = "Cyber Crime";
                break;
            case 1:
                investigationStr = "Murder";
                break;
            case 2:
                investigationStr = "Fraud";
                break;
        }

        intent.putExtra(MapActivity.EXTRA_INDEX, index);
        intent.putExtra(MapActivity.EXTRA_INVESTIGATION, investigationStr);
        intent.putExtra(MapActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * This is the correct selection from the lineup.
     * sets the completedMinigame activity for the given investigation to true
     * @param  view  middle character ImageView
     */
    public void onClickLineupCenter(View view) {
        ImageView answer = findViewById(R.id.center_true);
        answer.setVisibility(View.VISIBLE);
        user.setCompletedMinigame(index, true);
    }

    /**
     * This is an incorrect selection from the lineup.
     * @param  view  left character ImageView
     */
    public void onClickLineupLeft(View view) {
        ImageView answer = findViewById(R.id.left_incorrect);
        answer.setVisibility(View.VISIBLE);
    }

    /**
     * This is an incorrect selection from the lineup.
     * @param  view  right character ImageView
     */
    public void onClickLineupRight(View view) {
        ImageView answer = findViewById(R.id.right_incorrect);
        answer.setVisibility(View.VISIBLE);
    }
}