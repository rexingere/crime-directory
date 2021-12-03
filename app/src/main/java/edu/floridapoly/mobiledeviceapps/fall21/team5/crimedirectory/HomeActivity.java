package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 *  HomeActivity.java
 *  Purpose: Activity to access profile settings and navigation to investigations
 */
public class HomeActivity extends AppCompatActivity {
    public static String EXTRA_USERNAME = "extra_username";
    public static String EXTRA_PASSWORD = "extra_password";
    public static String EXTRA_USER = "extra_user";
    User user;
    boolean showProfile = true;
    boolean isEditUsername = true;
    boolean isEditPassword = true;
    EditText editPassword;
    EditText editUsername;
    TextView password;
    TextView username;
    ImageView btnEditPassword;
    ImageView btnEditUsername;
    ImageView btnSignOut;
    TextView gridBackground;
    TextView usernameText;
    ImageView exportProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usernameText = findViewById(R.id.usernameText);
        user = getIntent().getParcelableExtra(EXTRA_USER);
        usernameText.setText("Welcome,\n Detective " + user.getName());
        editPassword = findViewById(R.id.editPassword);
        editUsername = findViewById(R.id.editUsername);
        password = findViewById(R.id.textPassword);
        username = findViewById(R.id.textUsername);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnEditUsername = findViewById(R.id.btnEditUsername);
        btnSignOut = findViewById(R.id.btnSignOut);
        gridBackground = findViewById(R.id.gridBackground);
        exportProfile = findViewById(R.id.exportProfile);

        username.setText("Username: " + user.getName());
        password.setText("Password: " + user.getPassword());
        editUsername.setHint(user.getName());
        editPassword.setHint(user.getPassword());
    }

    /**
     * Sets the index to the chosen investigation number
     *
     * @param  view  tab (with investigation index) TextView
     * @return      the image at the specified URL
     */
    public void tabHandler(View view) {
        TextView tab = (TextView) view;
        int index = Integer.parseInt(tab.getText().toString());

        Intent intent = new Intent(this, CrimeInfoActivity.class);
        intent.putExtra(CrimeInfoActivity.EXTRA_INDEX, Integer.parseInt(String.valueOf(index)) - 1);
        intent.putExtra(CrimeInfoActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * Show the profile options grid view
     * @param  view  profile icon ImageVie
     */
    public void onClickProfile(View view) {
        ImageView magnifying = findViewById(R.id.magnifying);
        TextView textExport = findViewById(R.id.textExport);
        TextView textSignout = findViewById(R.id.textSignout);
        TextView textReset = findViewById(R.id.textReset);
        ImageView btnReset = findViewById(R.id.btnReset);

        if (showProfile) {
            password.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            btnEditPassword.setVisibility(View.VISIBLE);
            btnEditUsername.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams layoutParams = gridBackground.getLayoutParams();
            layoutParams.width = (int)(java.lang.Math.max(((password.getWidth() * 1.5)),(username.getWidth() * 1.5)) + 20);
            layoutParams.height = 780;

            gridBackground.setLayoutParams(layoutParams);
            gridBackground.setVisibility(View.VISIBLE);
            magnifying.setVisibility(View.INVISIBLE);
            usernameText.setVisibility(View.INVISIBLE);
            exportProfile.setVisibility(View.VISIBLE);
            textExport.setVisibility(View.VISIBLE);
            textSignout.setVisibility(View.VISIBLE);
            textReset.setVisibility(View.VISIBLE);
            btnReset.setVisibility(View.VISIBLE);

        } else {
            password.setVisibility(View.INVISIBLE);
            username.setVisibility(View.INVISIBLE);
            editPassword.setVisibility(View.INVISIBLE);
            editUsername.setVisibility(View.INVISIBLE);
            btnEditPassword.setVisibility(View.INVISIBLE);
            btnEditUsername.setVisibility(View.INVISIBLE);
            btnSignOut.setVisibility(View.INVISIBLE);
            gridBackground.setVisibility(View.INVISIBLE);
            magnifying.setVisibility(View.VISIBLE);
            usernameText.setVisibility(View.VISIBLE);
            exportProfile.setVisibility(View.INVISIBLE);
            textExport.setVisibility(View.INVISIBLE);
            textSignout.setVisibility(View.INVISIBLE);
            textReset.setVisibility(View.INVISIBLE);
            btnReset.setVisibility(View.INVISIBLE);
        }

        showProfile = !showProfile;
    }

    /**
     * make the edit username EditView visible
     * @param  view  edit icon ImageView next to the username text field
     */
    public void onClickEditUsername(View view) {
        ImageView btn = (ImageView) view;

        if (isEditUsername) {
            username.setVisibility(View.INVISIBLE);
            editUsername.setVisibility(View.VISIBLE);
            btn.setImageResource(R.drawable.save);
        } else {
            btn.setImageResource(R.drawable.edit);
            username.setVisibility(View.VISIBLE);
            editUsername.setVisibility(View.INVISIBLE);

            user.setName(editUsername.getText().toString());
            username.setText("Username: " + user.getName());
            usernameText.setText("Welcome,\n Detective " + user.getName());
        }

        isEditUsername =! isEditUsername;
    }

    /**
     * make the edit password EditView visible
     * @param  view  edit icon ImageView next to the password text field
     */
    public void onClickEditPassword(View view) {
        ImageView btn = (ImageView) view;

        if (isEditPassword) {
            password.setVisibility(View.INVISIBLE);
            editPassword.setVisibility(View.VISIBLE);
            btn.setImageResource(R.drawable.save);
        } else {
            password.setVisibility(View.VISIBLE);
            editPassword.setVisibility(View.INVISIBLE);

            user.setPassword(editPassword.getText().toString());
            password.setText("Password: " + user.getPassword());
            btn.setImageResource(R.drawable.edit);
        }

        isEditPassword =! isEditPassword;
    }

    /**
     * Exit the application
     * will save the current progress to the user's file
     * @param  view  exit icon ImageView
     */
    public void onClickSignOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        StoreProfile storeProfile = new StoreProfile(this, user);

        storeProfile.updateProfile();
        startActivity(intent);
    }

    /**
     * generate .json and .txt file representing the user object
     * files are saved in the phone's downloads folder
     * @param  view  export icon ImageView
     */
    public void onClickExportProfile(View view) {
        StoreProfile storeProfile = new StoreProfile(this, user);

        storeProfile.updateProfile();
        Toast.makeText(this, "Successfully Exported to Downloads", Toast.LENGTH_SHORT).show();
    }

    /**
     * reset the game - clears all task completion
     * @param  view  reset icon ImageView
     */
    public void onClickReset(View view) {
        user.resetGame();
        Toast.makeText(this, "Game Reset Successful", Toast.LENGTH_SHORT).show();
    }
}