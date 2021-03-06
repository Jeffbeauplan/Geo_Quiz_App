package com.techexchange.mobileapps.lab2;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    private TextView questionText;
    private TextView correctText;
    private Button leftButton;
    private Button rightButton;
    private Button nextButton;

    private List<Question> questionList;
    private int currentQuestionIndex;
    private int currentScore;
    private Bundle state;

    static final String KEY_SCORE = "Score";
    private static final String TAG = MainActivity.class.getSimpleName();


    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Extra UI initialization code here...
        questionList = initQuestionList();
        questionText = rootView.findViewById(R.id.question_text);
        leftButton = rootView.findViewById(R.id.left_button);
        leftButton.setOnClickListener(this::onAnswerButtonPressed);

        rightButton = rootView.findViewById(R.id.right_button);
        rightButton.setOnClickListener(this::onAnswerButtonPressed);

        nextButton = rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this::onNextButtonPressed);
        nextButton.setEnabled(false);


        correctText = rootView.findViewById(R.id.correct_incorrect_text);

        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("CurrentIndex");
            currentScore = savedInstanceState.getInt("CurrentScore");
            rightButton.setEnabled(savedInstanceState.getBoolean("rightButtonState"));
            leftButton.setEnabled(savedInstanceState.getBoolean("leftButtonState"));
            state = savedInstanceState;
        }
        else {
            currentQuestionIndex = 0;
            currentScore = 0;
        }

        return rootView;
    }

    private void onAnswerButtonPressed(View v) {
        Button selectedButton = (Button) v;
        Question ques = questionList.get(currentQuestionIndex);
        if (ques.getCorrectAnswer().contentEquals(selectedButton.getText())) {
            correctText.setText("Correct!");
            rightButton.setEnabled(false);
            leftButton.setEnabled(false);
            currentScore++;

        } else {
            correctText.setText("Wrong!");
            rightButton.setEnabled(false);
            leftButton.setEnabled(false);
        }
        nextButton.setEnabled(true);
    }

    private void onNextButtonPressed(View v) {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            leftButton.setEnabled(true);
            rightButton.setEnabled(true);
            updateView();
        } else {
            Intent scoreIntent = new Intent(getActivity(), ScoreActivity.class);
            scoreIntent.putExtra(KEY_SCORE, currentScore);
            startActivityForResult(scoreIntent, 0);
        }

    }

    public void updateView() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestion());
        if (Math.random() < 0.5) {
            leftButton.setText(currentQuestion.getCorrectAnswer());
            rightButton.setText(currentQuestion.getWrongAnswer());
        } else {
            rightButton.setText(currentQuestion.getCorrectAnswer());
            leftButton.setText(currentQuestion.getWrongAnswer());
        }
        nextButton.setEnabled(false);
        correctText.setText(R.string.initial_correct_incorrect);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
        if (state != null) {
            nextButton.setEnabled(state.getBoolean("nextButtonState"));
            correctText.setText(state.getString("correctText"));
        }
        Log.d(TAG, "Frag OnResume() was called");
    }

    private List<Question> initQuestionList() {
        Resources res = getResources();
        String[] questions = res.getStringArray(R.array.questions);
        String[] correctAnswers = res.getStringArray(R.array.correct_answers);
        String[] wrongAnswers = res.getStringArray(R.array.incorrect_answers);

        // Make sure that all arrays have the same length.
        Preconditions.checkState(questions.length == correctAnswers.length);
        Preconditions.checkState(questions.length == wrongAnswers.length);

        List<Question> qList = new ArrayList<>();

        for (int i = 0; i < questions.length; ++i) {
            qList.add(new Question(questions[i], correctAnswers[i], wrongAnswers[i]));
        }
        return qList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != 0 || data == null) {
           getActivity().finish();
        }
        currentScore = 0;
        currentQuestionIndex = 0;
        rightButton.setEnabled(true);
        leftButton.setEnabled(true);
        nextButton.setEnabled(true);
        updateView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called");
        Log.d(TAG, "rightButton is enabled:" + rightButton.isEnabled());
        Log.d(TAG, "leftButton is enabled:" + leftButton.isEnabled());
        outState.putInt("CurrentIndex", currentQuestionIndex);
        outState.putInt("CurrentScore", currentScore);
        outState.putBoolean("rightButtonState", rightButton.isEnabled() );
        outState.putBoolean("leftButtonState", leftButton.isEnabled() );
        outState.putBoolean("nextButtonState", nextButton.isEnabled());
        outState.putString("correctText", correctText.getText().toString());
    }


}
