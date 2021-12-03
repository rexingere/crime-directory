package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;


import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


/**
 *  StoreProfile.java
 *  Purpose: External and Internal data storage functionality
 */
public class StoreProfile {
    static final String FILE_NAME = "user_profile";
    static final String FILE_NAME_USER = "user";

    Context context;
    User user;
    Profile profile;

    public StoreProfile(Context context, User user) {
        this.context = context;
        this.user = user;
        profile = new Profile(user);

        updateProfile();
        createAccount();
    }

    public StoreProfile(Context context) {
        this.context = context;
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
    public void createAccount() {
        Gson gson1 = new Gson();
        String userString1 = gson1.toJson(user);
        ObjectMapper mapper = new ObjectMapper();

        try {
            String userStringFormatted1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(userString1));
            File file1 = new File(context.getFilesDir(), FILE_NAME_USER);
            FileWriter fileWriter4 = new FileWriter(file1);
            BufferedWriter bufferedWriter4 = new BufferedWriter(fileWriter4);

            bufferedWriter4.write(userStringFormatted1);
            bufferedWriter4.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void updateProfile() {
        try {
            Gson gson = new Gson();
            ObjectMapper mapper = new ObjectMapper();
            String userString = gson.toJson(profile);
            String userStringFormatted = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(userString));

            // Define the File Path and its Name
            File file = new File(context.getFilesDir(), FILE_NAME);
            File jsonFile = new File("/sdcard/Download/"+"user_profile.json");
            File textFile = new File("/sdcard/Download/"+"user_profile.txt");

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();

            FileWriter fileWriter2 = new FileWriter(jsonFile);
            BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
            bufferedWriter2.write(userStringFormatted);
            bufferedWriter2.close();

            FileWriter fileWriter3 = new FileWriter(textFile);
            BufferedWriter bufferedWriter3 = new BufferedWriter(fileWriter3);
            bufferedWriter3.write(userStringFormatted);
            bufferedWriter3.close();
        } catch (Exception e) {

        }
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
    public User getProfile() {
        String response = "default response";
        Gson g = new Gson();

        try {
            File file = new File(context.getFilesDir(), FILE_NAME_USER);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            // This response will have Json Format String
            response = stringBuilder.toString();

            //studentString = response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = g.fromJson(response, User.class);
        //return response;
        return user;
    }
}
