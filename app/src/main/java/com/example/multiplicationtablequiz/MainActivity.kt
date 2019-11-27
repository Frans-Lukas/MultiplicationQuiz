package com.example.multiplicationtablequiz

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.room.Room
import com.example.multiplicationtablequiz.db.AppDatabase
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
        setupNumberPickers()
        setScoreText()
        updateQuestion()
        setQuestionsLeftText()
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
                        if (Integer.parseInt(charSequence.toString()) == currentExpectedAnswer) {
                            score++
                            setScoreText()
                            updateQuestion()
                            setQuestionsLeftText()
                        } else {
                            showCorrectAnswer()
                            updateQuestion()
                            setQuestionsLeftText()
                            wrongAnswers.add(currentQuestionIntegers)

                        }
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    private fun setupNumberPickers() {
        minPicker.minValue = 1
        minPicker.maxValue = 99
        minPicker.wrapSelectorWheel = true
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        minPicker.value = 1
        minPicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            minValue = newVal
            if (minValue >= maxValue) {
                minValue = maxValue - 1
                minPicker.value = minValue
            }
            wrongAnswers.clear()
        }


        maxPicker.minValue = 1
        maxPicker.maxValue = 99
        maxPicker.wrapSelectorWheel = true
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        maxPicker.value = 12
        maxPicker.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            maxValue = newVal
            if (maxValue <= minValue) {
                maxValue = minValue + 1
                maxPicker.value = maxValue
            }
            wrongAnswers.clear()
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
            questionText = currentQuestionIntegers[0].toString() + "x" + currentQuestionIntegers[1] + " = ?"
            currentExpectedAnswer = currentQuestionIntegers[0] * currentQuestionIntegers[1]
        } else if (divOrMult == 1) {
            questionText = currentQuestionIntegers[0].toString() + "x? = " + currentQuestionIntegers[0] * currentQuestionIntegers[1]
            currentExpectedAnswer = currentQuestionIntegers[1]
        } else {
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
}
