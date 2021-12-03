package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 *  MinigameActivityFraud.java
 *  Purpose: Present the Case 3 Minigame
 *  Interaction: Click the bill that appears to be counterfeit
 */
public class MinigameActivityFraud extends AppCompatActivity {
    public static String EXTRA_INDEX = "index";
    public static String EXTRA_USER = "extra_user";

    public int index;
    private boolean showHint = true;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame_fraud);

        user = getIntent().getParcelableExtra(EXTRA_USER);
        index = getIntent().getIntExtra(EXTRA_INDEX, 0);
    }

    /**
     * launch CrimeInformation activity
     * @param  view  "Info" tab TextView
     */
    public void onClickInfo(View view) {
        Intent intent = new Intent(this, CrimeInfoActivity.class);

        intent.putExtra(CrimeInfoActivity.EXTRA_INDEX, index);
        intent.putExtra(CrimeInfoActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * launch the map activity
     * @param  view "Map" tab TextView
     */
    public void onClickMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);

        intent.putExtra(MapActivity.EXTRA_INDEX, index);
        intent.putExtra(MapActivity.EXTRA_USER, user);

        startActivity(intent);
    }


    /**
     * incorrect selection of bill
     * @param  view  valid bill text view
     */
    public void onClickBill_1(View view) {
        ImageView answer = findViewById(R.id.top_incorrect);

        answer.setVisibility(View.VISIBLE);
        user.setCompletedMinigame(index, true);
    }

    /**
     * incorrect selection of bill
     * @param  view  valid bill text view
     */
    public void onClickBill_2(View view) {
        ImageView answer = findViewById(R.id.center_incorrect);
        answer.setVisibility(View.VISIBLE);
    }

    /**
     * set the user's completedMinigame array at the investigation index to true
     * @param  view  counterfeit bill text view
     */
    public void onClickBill_3(View view) {
        ImageView answer = findViewById(R.id.bottom_correct);

        answer.setVisibility(View.VISIBLE);
        user.setCompletedMinigame(index, true);
    }

    /**
     * display the hint text
     * @param  view  lightbulb icon ImageView
     */
    public void onClickLightbulb(View view) {
        GridLayout grid = findViewById(R.id.grid);
        TextView hintText = findViewById(R.id.txtHint);

        if (showHint) {
            grid.setBackground(getDrawable(R.drawable.white_back));
            hintText.setVisibility(View.VISIBLE);
        } else {
            grid.setBackground(getDrawable(R.drawable.transparent_bg));
            hintText.setVisibility(View.GONE);
        }

        showHint = !showHint;
    }
}