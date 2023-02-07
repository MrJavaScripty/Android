package com.example.week2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var numberGenerators: Array<NumberGenerator>

//    For understanding the difference between dp and sp are
//    https://stackoverflow.com/questions/2025282/what-is-the-difference-between-px-dip-dp-and-sp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numberGenerators = arrayOf(
            findViewById(R.id.number_generator_1),
            findViewById(R.id.number_generator_2),
            findViewById(R.id.number_generator_3),
            findViewById(R.id.number_generator_4),
            findViewById(R.id.number_generator_5),
            findViewById(R.id.number_generator_6)
        )

        val generatedNumbers = generateRandomNumbers(0, 49)

        for (i in numberGenerators.indices) {
            val generatorTextView = numberGenerators[i].findViewById<TextView>(R.id.generatorItemTv)
            generatorTextView.text = generatedNumbers[i].toString()

            val generatorBtn = numberGenerators[i].findViewById<Button>(R.id.generatorItemBtn)
            generatorBtn.setOnClickListener{
                generatorTextView.text = Random.nextInt(0,49).toString();
            }
        }

    }

    private fun generateRandomNumbers(from: Int, to:Int): IntArray {
        return IntArray(6) {
            Random.nextInt(from, to);
        }
    }
}