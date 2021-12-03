package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  MinigameActivityCyber.java
 *  Purpose: Present the Case 1 Minigame
 *  Interaction: Complete 3 multiple choice questions based on the Crime Info data for Polyanin
 */
public class MinigameActivityCyber extends AppCompatActivity {
    public static String EXTRA_INDEX = "index";
    public static String EXTRA_USER = "extra_user";

    public int index;
    private boolean showHint = true;
    private Integer correctAnswerIndex;
    private Integer qIndex;

    CyberGameValues cyberGameValues;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame_cyber);

        user = getIntent().getParcelableExtra(EXTRA_USER);
        index = getIntent().getIntExtra(EXTRA_INDEX, 0);
        qIndex = 0;
        cyberGameValues = new CyberGameValues();

        initQuestion(qIndex, cyberGameValues);
    }

    /**
     * launch the crime info activity
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
     * @param  view  "Map" tab TextView
     */
    public void onClickMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);

        intent.putExtra(MapActivity.EXTRA_INDEX, index);
        intent.putExtra(MapActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * display the hint
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

    /**
     * determine if selected radio button is correct
     * @param  view  RadioButton to select answer
     */
    public void onClickRadioBtn(View view) {
        RadioButton selectedRadioBtn = (RadioButton) view;
        ImageView correct = findViewById(R.id.correct);
        ImageView incorrect = findViewById(R.id.incorrect);
        Button nextBtn = findViewById(R.id.nextBtn);
        Button tryAgainBtn = findViewById(R.id.tryAgainBtn);
        Button finishedBtn = findViewById(R.id.finishedBtn);


        int id = selectedRadioBtn.getId();
        Integer selectedIndex;
        
        switch (id) {
            case R.id.radioBtn_1:
                selectedIndex = 0;
                break;
            case R.id.radioBtn_2:
                selectedIndex = 1;
                break;
            case R.id.radioBtn_3:
                selectedIndex = 2;
                break;
            default:
                selectedIndex = 5;
        }

        if (selectedIndex == correctAnswerIndex) {
            correct.setVisibility(View.VISIBLE);
            if(qIndex < 2) {
                nextBtn.setVisibility(View.VISIBLE);
            } else {
                finishedBtn.setVisibility(View.VISIBLE);
            }

        } else {
            incorrect.setVisibility(View.VISIBLE);
            tryAgainBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Display questions and options for the mingame
     *
     * @param  index investigation number
     * @param  vals CyberGameValues to get the questions and answers from
     * @return someActivity
     */
    private void initQuestion(Integer index, CyberGameValues vals) {
        TextView questionTxt = findViewById(R.id.questionTxt);
        RadioButton radioBtn_1 = findViewById(R.id.radioBtn_1);
        RadioButton radioBtn_2 = findViewById(R.id.radioBtn_2);
        RadioButton radioBtn_3 = findViewById(R.id.radioBtn_3);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        String question = vals.questionsAnswersMap.get(index).second;

        correctAnswerIndex = vals.questionsAnswersMap.get(index).first;
        radioGroup.clearCheck();

        switch(index) {
            case 0:
                questionTxt.setText(question);
                radioBtn_1.setText(vals.optionsQ0.get(0));
                radioBtn_2.setText(vals.optionsQ0.get(1));
                radioBtn_3.setText(vals.optionsQ0.get(2));
                break;
            case 1:
                questionTxt.setText(question);
                radioBtn_1.setText(vals.optionsQ1.get(0));
                radioBtn_2.setText(vals.optionsQ1.get(1));
                radioBtn_3.setText(vals.optionsQ1.get(2));
                break;
            case 2:
                questionTxt.setText(question);
                radioBtn_1.setText(vals.optionsQ2.get(0));
                radioBtn_2.setText(vals.optionsQ2.get(1));
                radioBtn_3.setText(vals.optionsQ2.get(2));
                break;
        }
    }

    /**
     * proceed to the next question
     * @param  view  "Next Question" Button
     */
    public void onClickNextQuestion(View view) {
        Button nextBtn = (Button) view;
        Button finishedBtn = findViewById(R.id.finishedBtn);
        ImageView correct = findViewById(R.id.correct);
        ImageView incorrect = findViewById(R.id.incorrect);
        nextBtn.setVisibility(View.INVISIBLE);

        qIndex += 1;

        initQuestion(qIndex, cyberGameValues);
        correct.setVisibility(View.INVISIBLE);
        incorrect.setVisibility(View.INVISIBLE);
    }

    /**
     * Clear the incorrect label and radio button selection
     * @param  view  "Try Again" Button
     */
    public void onClickTryAgain(View view) {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        ImageView incorrect = findViewById(R.id.incorrect);
        Button tryAgainBtn = (Button) view;

        radioGroup.clearCheck();
        tryAgainBtn.setVisibility(View.INVISIBLE);
        incorrect.setVisibility(View.INVISIBLE);
    }

    /**
     * launch the CrimeInfoActivity
     * set the user's completedMinigame array for the investigation's index to true
     * @param  view  "Finished" Button
     */
    public void onClickFinished(View view) {
        user.setCompletedMinigame(index, true);

        Intent intent = new Intent(this, CrimeInfoActivity.class);
        intent.putExtra(CrimeInfoActivity.EXTRA_INDEX, index);
        intent.putExtra(CrimeInfoActivity.EXTRA_USER, user);

        startActivity(intent);
    }

    /**
     * CyberGameValues class
     * Purpose: create an object to store the questions, answers, and options for the
     * multiple choice minigame activity in the cyber crime case (Investigation 1)
     */
    class CyberGameValues {
        List<Pair<Integer, String>> questionsAnswersMap;
        Map<Integer, List<String>> optionsMap;
        List<String> optionsQ0, optionsQ1, optionsQ2;

        public CyberGameValues() {
            questionsAnswersMap = new ArrayList<>();
            optionsMap = new HashMap<>();
            optionsQ0= new ArrayList<>();
            optionsQ1= new ArrayList<>();
            optionsQ2= new ArrayList<>();

            questionsAnswersMap.add(new Pair(2, "Which is not a ransomware\nmentioned in the case?"));
            questionsAnswersMap.add(new Pair(0, "What type of file did Polyanin\nleave on his victims"));
            questionsAnswersMap.add(new Pair(1, "What FBI field offic is\nresponsible for this case?"));

            optionsQ0.add("Sodinokibi");
            optionsQ0.add("REvil");
            optionsQ0.add("GoldenEye");
            optionsMap.put(0, optionsQ0);

            optionsQ1.add(".txt");
            optionsQ1.add(".py");
            optionsQ1.add(".pdf");
            optionsMap.put(1, optionsQ1);

            optionsQ2.add("Albany");
            optionsQ2.add("Dallas");
            optionsQ2.add("Phoenix");
            optionsMap.put(2, optionsQ2);
        }
    }
}