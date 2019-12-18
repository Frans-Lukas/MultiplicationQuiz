package com.example.multiplicationtablequiz.activity

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.ToggleButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.multiplicationtablequiz.QuestionViewModel
import com.example.multiplicationtablequiz.R
import com.example.multiplicationtablequiz.db.MultiplicationPair
import com.example.multiplicationtablequiz.fragment.ToggleSettingsFragment

class NumberSelectActivity : AppCompatActivity() {

    private val activeNumbers: BooleanArray = BooleanArray(100)
    private val buttons: ArrayList<ToggleButton> = arrayListOf<ToggleButton>()
    private lateinit var prefs: SharedPreferences
    private val toggleSettingsDialog = ToggleSettingsFragment()
    private lateinit var questionViewModel: QuestionViewModel
    private  var allMultPairs : List<MultiplicationPair> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_select)
        prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        loadArray()
        initTable()

        questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)

        questionViewModel.allMultPairs.observe(this, Observer { pairs -> pairs?.let { setPairs(it) } })

    }

    private fun setPairs(pairs: List<MultiplicationPair>) {
        allMultPairs = pairs
    }

    fun storeArray(): Boolean {
        val editor = prefs.edit()
        for (i in activeNumbers.indices) {
            editor.putBoolean(getString(R.string.activeNumbersKey) + "_" + i, activeNumbers[i])
        }
        return editor.commit()
    }

    fun loadArray() {
        for (i in activeNumbers.indices) {
            activeNumbers[i] = prefs.getBoolean(getString(R.string.activeNumbersKey) + "_" + i, true)
        }
    }

    private fun initTable() {
        val table: TableLayout = findViewById(R.id.tableLayoutNumberSelector)
        for (i in 1..25) {
            val row = TableRow(this)
            for (j in 1..4) {
                val numberButton = ToggleButton(ContextThemeWrapper(this, R.style.AppTheme_ToggleButton))
                val currentNumber = ((i - 1) * 4 + j - 1) + 1
                numberButton.text = currentNumber.toString()
                numberButton.textOff = currentNumber.toString()
                numberButton.textOn = currentNumber.toString()

                numberButton.isChecked = activeNumbers[currentNumber - 1]
                numberButton.setOnCheckedChangeListener { buttonView, isChecked ->
                    activeNumbers[buttonView.text.toString().toInt() - 1] = isChecked
                }
                buttons.add(numberButton)
                row.addView(numberButton)
            }
            table.addView(row)
        }
    }


    fun enablePrimes(view: View) {
        val primes = arrayOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97)
        for (i in buttons.indices) {
            buttons[i].isChecked = (i + 1) in primes
        }
        toggleSettingsDialog.dismiss()
    }

    fun toggleSettings(view: View) {
        toggleSettingsDialog.show(supportFragmentManager, "togglesettings")
    }

    fun toggleAll(view: View) {
        var count = 0
        for (i in buttons.indices) {
            if (buttons[i].isChecked)
                count++
        }
        for (i in buttons.indices) {
            buttons[i].isChecked = count < 50
        }
        toggleSettingsDialog.dismiss()

    }

    fun save(view: View) {
        storeArray()
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun toggleEvenNumbers(view: View) {
        for (i in buttons.indices) {
            buttons[i].isChecked = (i + 1) % 2 == 0
        }
        toggleSettingsDialog.dismiss()
    }

    fun toggleUpToTen(view: View) {
        for (i in buttons.indices) {
            buttons[i].isChecked = (i + 1) <= 10
        }
        toggleSettingsDialog.dismiss()
    }

    fun toggleOddNumbers(view: View) {
        for (i in buttons.indices) {
            buttons[i].isChecked = (i + 1) % 2 == 1
        }
        toggleSettingsDialog.dismiss()
    }

    fun toggleDifficultNumbers(view: View) {
        for (button in buttons.iterator()){
            button.isChecked = false
        }
        for (i in allMultPairs.indices){
            if(allMultPairs[i].numCorrect < allMultPairs[i].numWrong){
                buttons[allMultPairs[i].firstProduct].isChecked = true
                buttons[allMultPairs[i].secondProduct].isChecked = true
            }
        }

    }
}
