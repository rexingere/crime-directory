package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 *  LoginActivity.java
 *  Purpose: Activity to login or create account
 *  Initial page of application
 */
public class LoginActivity extends AppCompatActivity {
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * If username and password match a user object in the user list json user array,
     *      Create a User based on the user in the user file with the same username and password
     *      Start the home activity
     * if username or password are incorrect, a Toast will indicate that.
     *
     * @param  view  login TextView
     */
    public void onClickLogin (View view) {
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        StoreProfile storeProfile = new StoreProfile(this);
        User correctUser = storeProfile.getProfile();

        Intent intent = new Intent(this, HomeActivity.class);

        user.setName(username.getText().toString());
        user.setPassword(password.getText().toString());

        if ( (username.getText().toString().equals(correctUser.getName())) && (password.getText().toString().equals(correctUser.getPassword()))){
            user = correctUser;
            intent.putExtra(HomeActivity.EXTRA_USERNAME, username.getText().toString());
            intent.putExtra(HomeActivity.EXTRA_PASSWORD, password.getText().toString());
            intent.putExtra(HomeActivity.EXTRA_USER, user);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create a User object and save the user to the user list file
     * @param  view  create account TextView
     */
    public void onClickCreateAccount(View view) {
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        StoreProfile storeProfile = new StoreProfile(this, user);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.EXTRA_USERNAME, username.getText().toString());
        intent.putExtra(HomeActivity.EXTRA_PASSWORD, password.getText().toString());
        user.setName(username.getText().toString());
        user.setPassword(password.getText().toString());

        storeProfile.createAccount();
        intent.putExtra(HomeActivity.EXTRA_USER, user);
        startActivity(intent);
    }
}