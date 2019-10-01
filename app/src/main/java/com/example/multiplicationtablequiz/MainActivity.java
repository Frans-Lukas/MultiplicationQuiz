package com.example.multiplicationtablequiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int NUMBER_OF_QUESTIONS = 10;
    int currentExpectedAnswer = 0;
    EditText textInput;
    TextView question;
    Integer[] currentQuestionIntegers;
    TextView scoreTV;
    int score;
    int maxValue = 12;
    int minValue = 1;
    TextView questionsLeft;
    NumberPicker npMax;
    NumberPicker npMin;
    ArrayList<Integer[]> numbersToCalc;
    ArrayList<Integer[]> wrongAnswers;
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numbersToCalc = new ArrayList<>();
        wrongAnswers = new ArrayList<>();
        rand = new Random(System.nanoTime());
        addNewNumbers();
        question = findViewById(R.id.TVnumberQuestion);
        textInput = findViewById(R.id.ETInput);
        scoreTV = findViewById(R.id.TVnumberCorrect);
        setupNumberPickers();
        score = 0;
        setScoreText();
        updateQuestion();
        questionsLeft = findViewById(R.id.TVnumberLeft);
        setQuestionsLeftText();
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0 && Integer.toString(currentExpectedAnswer).length() <= charSequence.length()){

                    if(numbersToCalc.size() == 0){
                        resetGame();
                    } else{
                        if(Integer.parseInt(charSequence.toString()) == currentExpectedAnswer){
                            score++;
                            setScoreText();
                            updateQuestion();
                            setQuestionsLeftText();
                        } else{
                            showCorrectAnswer();
                            updateQuestion();
                            setQuestionsLeftText();
                            wrongAnswers.add(currentQuestionIntegers);

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupNumberPickers() {

        npMin = findViewById(R.id.minPicker);
        npMin.setMinValue(1);
        npMin.setMaxValue(99);
        npMin.setWrapSelectorWheel(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        npMin.setValue(1);
        npMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                minValue = newVal;
                if(minValue >= maxValue){
                    minValue = maxValue - 1;
                    npMin.setValue(minValue);
                }
                wrongAnswers.clear();
            }
        });


        npMax = findViewById(R.id.maxPicker);
        npMax.setMinValue(1);
        npMax.setMaxValue(99);
        npMax.setWrapSelectorWheel(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        npMax.setValue(12);
        npMax.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                maxValue = newVal;
                if(maxValue <= minValue){
                    maxValue = minValue + 1;
                    npMax.setValue(maxValue);
                }
                wrongAnswers.clear();
            }
        });


    }

    private void showCorrectAnswer() {
        String num = Integer.toString(currentExpectedAnswer);
        new AlertDialog.Builder(this).setTitle("Correct answer is :" + num).setMessage("").show();
    }

    private void setQuestionsLeftText() {
        String questionsLeftString = "Left: " + String.valueOf(numbersToCalc.size());
        questionsLeft.setText(questionsLeftString);
    }

    private void setScoreText() {
        String scoreString = getScoreString();
        scoreTV.setText(scoreString);
    }

    private void addNewNumbers() {
        int numberOfNewQuestions = NUMBER_OF_QUESTIONS - wrongAnswers.size();
        numbersToCalc.clear();
        for (int i = 0; i < numberOfNewQuestions; i++) {
            numbersToCalc.add(new Integer[]{getNextValue(), getNextValue()});
        }
        numbersToCalc.addAll(wrongAnswers);
        wrongAnswers.clear();
    }

    private int getNextValue() {
        int val = rand.nextInt(maxValue - minValue + 1) + minValue;
        return val;
    }

    private void updateQuestion() {
        textInput.setText("");
        currentQuestionIntegers = numbersToCalc.remove(0);
        String questionText;
        int divOrMult = rand.nextInt(3);
        if(divOrMult == 0){
            questionText = currentQuestionIntegers[0] + "x" + currentQuestionIntegers[1] + " = ?";
            currentExpectedAnswer = currentQuestionIntegers[0] * currentQuestionIntegers[1];
        } else if(divOrMult == 1){
            questionText = currentQuestionIntegers[0] + "x? = " + (currentQuestionIntegers[0] * currentQuestionIntegers[1]);
            currentExpectedAnswer = currentQuestionIntegers[1];
        } else{
            questionText = (currentQuestionIntegers[0] * currentQuestionIntegers[1]) + "/" + currentQuestionIntegers[0] + " = ?";
            currentExpectedAnswer = currentQuestionIntegers[1];
        }
        question.setText(questionText);
    }

    private void resetGame() {
        alertOfFinished();
        addNewNumbers();
        score = 0;
        updateQuestion();
        setScoreText();
        setQuestionsLeftText();

    }

    private void alertOfFinished() {
        String scoreString = getScoreString();
        new AlertDialog.Builder(this).setTitle(scoreString).setMessage("").show();
    }

    private String getScoreString() {
        return "score: " + score + "/" + NUMBER_OF_QUESTIONS;
    }

    public void resetGame(View view) {
        resetGame();
    }
}
