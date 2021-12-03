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
 *  MinigameActivityKidnapping.java
 *  Purpose: Present the Kidnapping Minigame (cut from game play)
 *  Interaction: Click red headband that appears in the image of Lori Boffmane
 */
public class MinigameActivityKidnapping extends AppCompatActivity {
    public static String EXTRA_INDEX = "index";
    public static String EXTRA_USER = "extra_user";

    public int index;
    private boolean showHint = true;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame_kidnapping);

        user = getIntent().getParcelableExtra(EXTRA_USER);
        index = getIntent().getIntExtra(EXTRA_INDEX, 0);
    }

    /**
     * If username and password match a user object in the user list json user array,
     *      Create a User based on the user in the user file with the same username and password
     *      Start the home activity
     * if username or password are incorrect, a Toast will indicate that.
     *
     * @param  view  login TextView
     * @return someActivity
     */
    public void onClickInfo(View view) {
        Intent intent = new Intent(this, CrimeInfoActivity.class);

        intent.putExtra(CrimeInfoActivity.EXTRA_INDEX, index);
        intent.putExtra(CrimeInfoActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * If username and password match a user object in the user list json user array,
     *      Create a User based on the user in the user file with the same username and password
     *      Start the home activity
     * if username or password are incorrect, a Toast will indicate that.
     *
     * @param  view  login TextView
     * @return someActivity
     */
    public void onClickMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);

        intent.putExtra(MapActivity.EXTRA_INDEX, index);
        intent.putExtra(MapActivity.EXTRA_USER, user);

        startActivity(intent);
    }


    /**
     * display the hint for the kidnapping case minigame
     * @param  view  lightbulb icon ImageView
     */
    public void onClickLightbulb(View view) {
        GridLayout grid = findViewById(R.id.grid);
        TextView hintText = findViewById(R.id.txtHint);

        if (showHint) {
            grid.setBackground(getDrawable(R.drawable.white_back));
            hintText.setVisibility(View.VISIBLE);
        } else {
            grid.setBackground(getDrawable(R.drawable.black_bg));
            hintText.setVisibility(View.INVISIBLE);
        }

        showHint = !showHint;
        hintText.setVisibility(View.VISIBLE);
    }

    /**
     * set the user's completedMinigame array at the investigation's index to true
     * @param  view  red bandana ImageView
     */
    public void onClickEvidence(View view) {
        ImageView answer = findViewById(R.id.answer);

        answer.setVisibility(View.VISIBLE);
        user.setCompletedMinigame(index, true);
    }
}