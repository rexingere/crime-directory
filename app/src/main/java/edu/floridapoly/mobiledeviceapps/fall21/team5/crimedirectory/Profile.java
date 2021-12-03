package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import java.util.ArrayList;
import java.util.List;


/**
 *  Profile.java
 *  Purpose: POJO for a user profile
 */
public class Profile {
    public String name;
    public String password;
    public String completed_investigations_count;
    public List<String> completed_investigations;
    public List<DetailedTaskCompletion> detailed_task_completion;

    // generate a Profile object for the user
    public Profile(User user) {
        this.name = user.getName();
        this.password = user.getPassword();
        this.completed_investigations_count = String.valueOf(user.getCountCompletedCrimes());
        this.completed_investigations = user.getCompletedInvestigations();
        this.detailed_task_completion = getDetailedTaskCompletion(user);
    }

    // return an array of detailed task completion
    public List<DetailedTaskCompletion> getDetailedTaskCompletion(User user) {
        detailed_task_completion = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            DetailedTaskCompletion detailedTaskObj = new DetailedTaskCompletion(user, i);
            detailed_task_completion.add(detailedTaskObj);
        }

        return detailed_task_completion;
    }

    // class to represent a detailed task completion object
    class DetailedTaskCompletion {
        int crimeId;
        boolean minigame;
        boolean map;
        boolean report;

        public DetailedTaskCompletion(User user, int crimeId) {
            this.crimeId = crimeId;
            this.minigame = user.isCompletedMinigame(crimeId);
            this.map = user.isCompletedMap(crimeId);
            this.report = user.isCompletedReport(crimeId);
        }
    }
}
