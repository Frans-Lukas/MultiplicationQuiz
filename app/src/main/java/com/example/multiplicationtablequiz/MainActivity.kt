package com.example.multiplicationtablequiz

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.multiplicationtablequiz.db.AppDatabase
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

    private val nextValue: Int
        get() = rand.nextInt(maxValue - minValue + 1) + minValue

    private val scoreString: String
        get() = "score: $score/$NUMBER_OF_QUESTIONS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    private fun updateQuestionInDB(correct: Boolean) {
        fun Boolean.toInt() = if (this) 1 else 0
        val pair: IntArray = getMultiplicationPair()
        val matchingPairs = getDBInstance().findByProducts(pair[0], pair[1])
        val numCorrectBefore = matchingPairs.get(0).numCorrect
        val numWrongBefore = matchingPairs.get(0).numWrong
        var numCorrect = 0
        var numWrong = 0
        if (matchingPairs.size == 1) {
            if(numWrongBefore != null && numCorrectBefore != null){
                numCorrect = numCorrectBefore + correct.toInt()
                numWrong = numWrongBefore + correct.toInt()
            }
            val pairToUpdateTo = MultiplicationPair(matchingPairs.get(0).uid, pair[0], pair[1], numCorrect, numWrong)
            getDBInstance().updateMultiplicationPair(pairToUpdateTo)
        }
    }

    private fun getMultiplicationPair() : IntArray {
        val splitString = TVnumberCorrect.text.toString().split(" ")
        return intArrayOf(splitString.get(0).toInt(), splitString.get(2).toInt())
    }

    private fun getDBInstance() = AppDatabase.getInstance(applicationContext).multiplicationPairDao()

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
        for (i in 0 until numberOfNewQuestions) {
            numbersToCalc.add(arrayOf(nextValue, nextValue))
        }
        numbersToCalc.addAll(wrongAnswers)
        wrongAnswers.clear()
    }

    private fun updateQuestion() {
        ETInput.setText("")
        currentQuestionIntegers = numbersToCalc.removeAt(0)
        val questionText: String
        val divOrMult = rand.nextInt(3)
        if (divOrMult == 0) {
            // 1x5=?
            questionText = currentQuestionIntegers[0].toString() + "x" + currentQuestionIntegers[1] + " = ?"
            currentExpectedAnswer = currentQuestionIntegers[0] * currentQuestionIntegers[1]
        } else if (divOrMult == 1) {
            // 1x?=5
            questionText = currentQuestionIntegers[0].toString() + "x? = " + currentQuestionIntegers[0] * currentQuestionIntegers[1]
            currentExpectedAnswer = currentQuestionIntegers[1]
        } else {
            // 5/1=?
            questionText = (currentQuestionIntegers[0] * currentQuestionIntegers[1]).toString() + "/" + currentQuestionIntegers[0] + " = ?"
            currentExpectedAnswer = currentQuestionIntegers[1]
        }
        TVnumberQuestion.text = questionText
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

    fun resetGame(view: View) {
        resetGame()
    }

    companion object {

        val NUMBER_OF_QUESTIONS = 10
    }

    fun gotoStatsActivity(view: View) {
        val statsIntent = Intent(this, StatsActivity::class.java)
        startActivity(statsIntent)
    }
}
