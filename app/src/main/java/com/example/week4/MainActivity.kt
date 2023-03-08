package com.example.week4

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.week4.types.ButtonLists
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val gridSizes = arrayOf(
        Pair(3, 3),
        Pair(3, 4),
        Pair(4, 3),
        Pair(5, 5),
        Pair(4, 5),
        Pair(5, 4)
    )

    private lateinit var buttonGrid: GridLayout
    private lateinit var scoreTextView: TextView
    private lateinit var gridSize: Pair<Int,Int>

    private var score = 0
    private var buttons = mutableListOf<Button>()
    private var correctButtons = mutableListOf<Button>()
    private var selectedCorrectButtons = mutableListOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.score_tf)
        scoreTextView.text = resources.getString(R.string.score_text, score)

        generateChallenge()
    }

    private fun generateChallenge() {
        score = 0

        gridSize = gridSizes[Random.nextInt(gridSizes.size -1)]
        val (columnCount, rowCount) = gridSize
        val buttonCount = columnCount * rowCount

        val buttonLists = generateButtonLists(buttonCount)

        buttons = buttonLists.buttonList
        correctButtons = buttonLists.correctButtons

        buttons
            .filter { !correctButtons.contains(it) }
            .forEach { btn ->
                btn.setOnClickListener{
                    val currentButton = it as Button
                    currentButton.setBackgroundColor(Color.RED)
                    currentButton.text = "X"
                }
            }

        correctButtons.forEach { button ->
            button.setOnClickListener {
                it.setBackgroundColor(Color.GREEN)
                if(!selectedCorrectButtons.contains(it)){
                    selectedCorrectButtons.add(it as Button)
                    score++
                    scoreTextView.text = resources.getString(R.string.score_text, score)
                }

                if (correctButtons.size == score){
                    this.recreate()
                }
            }
        }

        buttonGrid = findViewById(R.id.button_grid)
        buttonGrid.rowCount = rowCount
        buttonGrid.columnCount = columnCount

        for(button in buttons) {
            button.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            buttonGrid.addView(button)
        }
    }

    private fun generateButtonLists(buttonCount: Int): ButtonLists {
        val allButtons = MutableList(buttonCount) {
            Button(this)
        }

        val correctButtons = mutableListOf<Button>()

        val greenButtonCount = Random.nextInt(4,7)
        for (i in 0 until greenButtonCount){
            var button: Button
            do {
                button = allButtons[Random.nextInt(allButtons.size)]
            } while (correctButtons.contains(button))
            correctButtons.add(button)
        }

        return ButtonLists(allButtons,correctButtons)
    }
}