package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;


/**
 *  User.java
 *  Purpose: POJO for a user
 */
public class User implements Parcelable {
    private String name;
    private String password;
    private boolean[] completedMaps = new boolean[3];
    private boolean[] completedMinigames = new boolean[3];
    private boolean[] completedReports = new boolean[3];

    public User() {

    }

    protected User(Parcel in) {
        name = in.readString();
        password = in.readString();

        completedMaps = in.createBooleanArray();
        completedMinigames = in.createBooleanArray();
        completedReports = in.createBooleanArray();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getCountCompletedCrimes() {
        int countCompletedCrimes = 0;

        for (int i = 0; i < 3; i++) {
            if (crimeCompleted(i)) {
                countCompletedCrimes++;
            }
        }

        return countCompletedCrimes;
    }

    public List<String> getCompletedInvestigations() {
        List<String> completedInvestigations = new ArrayList<>();

        if (crimeCompleted(0)) {
            completedInvestigations.add("Crime 1: Ransomware");
        }

        if (crimeCompleted(1)) {
            completedInvestigations.add("Crime 2: Murder");
        }

        if (crimeCompleted(2)) {
            completedInvestigations.add("Crime 3: Fraud");
        }

        return completedInvestigations;
    }

    public String getPassword(){return password;}

    public void resetGame() {
        for (int i = 0; i < 3; i++) {
            completedMaps[i] = false;
            completedReports[i] = false;
            completedMinigames[i] = false;
        }
    }

    public void setCompletedMap(int index, boolean value) {
        completedMaps[index] = value;
    }

    public void setCompletedMinigame(int index, boolean value) {
        completedMinigames[index] = value;
    }
    public void setCompletedReport(int index, boolean value) {
        completedReports[index] = value;
    }

    public boolean isCompletedMap(int index) {
        return completedMaps[index];
    }

    public boolean isCompletedMinigame(int index) {
        return completedMinigames[index];
    }

    public boolean isCompletedReport(int index) {
        return completedReports[index];
    }

    public boolean crimeCompleted(int i) {
        if(completedMaps[i] && completedMinigames[i] && completedReports[i]){
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(password);
        parcel.writeBooleanArray(completedMaps);
        parcel.writeBooleanArray(completedMinigames);
        parcel.writeBooleanArray(completedReports);
    }
}
