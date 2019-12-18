package com.example.multiplicationtablequiz.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.multiplicationtablequiz.QuestionViewModel
import com.example.multiplicationtablequiz.R
import com.example.multiplicationtablequiz.db.MultiplicationPair
import kotlinx.android.synthetic.main.activity_main.*

import java.util.ArrayList
import java.util.Random

class MainActivity : AppCompatActivity() {
    private var currentExpectedAnswer = 0
    private var currentQuestionIntegers: Array<Int> = arrayOf(1, 1)
    private var score: Int = 0
    private var maxValue = 12
    private var minValue = 1
    private var numbersToCalc: ArrayList<Array<Int>> = ArrayList()
    private var wrongAnswers: ArrayList<Array<Int>> = ArrayList()
    private var rand: Random = Random(System.nanoTime())

    private val activeNumbers:BooleanArray = BooleanArray(100)
    private lateinit var prefs : SharedPreferences

    companion object {
        val NUMBER_OF_QUESTIONS = 10
        val SELECT_ACTIVE_NUMBERS: Int = 1337
    }

    private lateinit var questionViewModel: QuestionViewModel

    private val scoreString: String
        get() = "score: $score/$NUMBER_OF_QUESTIONS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)
        prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        loadActiveNumbers()
        addNewNumbers()
        setScoreText()
        changeQuestion()
        setTextInputListener()
    }

    private fun setTextInputListener() {
        ETInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > 0 && Integer.toString(currentExpectedAnswer).length <= charSequence.length) {

                    if (numbersToCalc.size == 0) {
                        resetGame()
                    } else {
                        //correct answer
                        if (Integer.parseInt(charSequence.toString()) == currentExpectedAnswer) {
                            score++
                            updateQuestionInDB(true)
                            setScoreText()
                            changeQuestion()
                        } else {
                            updateQuestionInDB(false)
                            showCorrectAnswer()
                            changeQuestion()
                            wrongAnswers.add(currentQuestionIntegers)
                        }
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    private fun changeQuestion() {
        updateQuestion()
        setQuestionsLeftText()
    }

    fun updateQuestionInDB(correct: Boolean) {
        fun Boolean.toInt() = if (this) 1 else 0
        val matchingPairs : List<MultiplicationPair>? = questionViewModel.findByPair(currentQuestionIntegers[0], currentQuestionIntegers[1]).value
        if (matchingPairs != null && matchingPairs.size == 1) {
            val numCorrect = matchingPairs.get(0).numCorrect + correct.toInt()
            var numWrong = matchingPairs.get(0).numWrong
            if (!correct){
                numWrong++
            }
            val pairToUpdateTo = matchingPairs.get(0).copy(numCorrect = numCorrect, numWrong = numWrong)
            questionViewModel.update(pairToUpdateTo)
        } else{
            var numWrong = 0
            if(!correct){
                numWrong = 1
            }
            questionViewModel.insert(MultiplicationPair(currentQuestionIntegers[0], currentQuestionIntegers[1], correct.toInt(), numWrong))
        }
    }



    private fun showCorrectAnswer() {
        val num = Integer.toString(currentExpectedAnswer)
        AlertDialog.Builder(this).setTitle("Correct answer is :$num").setMessage("").show()
    }

    private fun setQuestionsLeftText() {
        val questionsLeftString = "Left: " + numbersToCalc.size.toString()
        TVnumberLeft.text = questionsLeftString
    }

    private fun setScoreText() {
        val scoreString = scoreString
        TVnumberCorrect.text = scoreString
    }

    private fun addNewNumbers() {
        val numberOfNewQuestions = NUMBER_OF_QUESTIONS - wrongAnswers.size
        numbersToCalc.clear()
        val potentialNumbers = arrayListOf<Int>()
        for (i in 0 until 100){
            if (activeNumbers[i]){
                potentialNumbers.add(i + 1)
            }
        }
        if(potentialNumbers.isEmpty()){
            potentialNumbers.add(1)
        }
        for (i in 0 until numberOfNewQuestions) {
            numbersToCalc.add(arrayOf(randomizeNextProduct(potentialNumbers), randomizeNextProduct(potentialNumbers)))
        }
        numbersToCalc.addAll(wrongAnswers)
        wrongAnswers.clear()
    }

    private fun randomizeNextProduct(potentialNumbers: ArrayList<Int>): Int {
        return potentialNumbers[(0 until potentialNumbers.size).random()]
    }

    private fun updateQuestion() {
        ETInput.setText("")
        currentQuestionIntegers = numbersToCalc.removeAt(0)
        val questionText: String
        val divOrMult = rand.nextInt(4)
        if (divOrMult == 0) {
            // 1x5=?
            questionText = currentQuestionIntegers[0].toString() + "x" + currentQuestionIntegers[1] + " = ?"
            currentExpectedAnswer = currentQuestionIntegers[0] * currentQuestionIntegers[1]
        } else if (divOrMult == 1) {
            // 1x?=5
            questionText = currentQuestionIntegers[0].toString() + "x? = " + currentQuestionIntegers[0] * currentQuestionIntegers[1]
            currentExpectedAnswer = currentQuestionIntegers[1]
        } else if(divOrMult == 2){
            // 5/1=?
            questionText = (currentQuestionIntegers[0] * currentQuestionIntegers[1]).toString() + "/" + currentQuestionIntegers[0] + " = ?"
            currentExpectedAnswer = currentQuestionIntegers[1]
        } else{
            // 5/?=5
            questionText = (currentQuestionIntegers[0] * currentQuestionIntegers[1]).toString() + "/?" + " = " + (currentQuestionIntegers[1]).toString()
            currentExpectedAnswer = currentQuestionIntegers[0]
        }
        TVnumberQuestion.text = questionText
    }


    //to allow the button to call this function
    fun resetGame(view: View) {
        resetGame()
    }

    private fun resetGame() {
        alertOfFinished()
        addNewNumbers()
        score = 0
        updateQuestion()
        setScoreText()
        setQuestionsLeftText()
    }

    private fun alertOfFinished() {
        val scoreString = scoreString
        AlertDialog.Builder(this).setTitle(scoreString).setMessage("").show()
    }

    fun gotoStatsActivity(view: View) {
        val statsIntent = Intent(this, StatsActivity::class.java)
        startActivity(statsIntent)
    }

    fun gotoNumberSelectorActivity(view: View) {
        val numberSelectIntent = Intent(this, NumberSelectActivity::class.java)
        startActivityForResult(numberSelectIntent, SELECT_ACTIVE_NUMBERS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == SELECT_ACTIVE_NUMBERS){
            if(resultCode == Activity.RESULT_OK){
                loadActiveNumbers()
                resetGame()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun loadActiveNumbers(){
        for(i in activeNumbers.indices){
            activeNumbers[i] = prefs.getBoolean(getString(R.string.activeNumbersKey) + "_" + i, true)
        }
    }
}
