package com.example.week3

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.week3.types.Breed
import com.example.week3.types.BreedImageData
import com.example.week3.types.QuestionData
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.schedule
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var dogBreedMap: Map<String, String>
    private lateinit var questionTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var answer: String
    private lateinit var question: QuestionData
    private lateinit var imageContainer: LinearLayout
    private lateinit var resultContainer: LinearLayout
    private val dogBreedFolderName = "dog_breeds"

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        questionTextView = findViewById(R.id.question)
        resultTextView = findViewById(R.id.result)
        imageContainer = findViewById(R.id.image_container)
        resultContainer = findViewById(R.id.result_container)

        findViewById<ProgressBar>(R.id.loader).isIndeterminate = true

        resultContainer.visibility = View.VISIBLE
        imageContainer.visibility = View.GONE

        dogBreedMap = getDogBreedMap()

        question = generateQuestion()
        assignImageToViews(question.dogs)
    }

    private fun generateQuestion(): QuestionData {
        val correctDog = getRandomDog()
        val incorrectDog = getRandomDog()

        this.answer = correctDog.id
        if (correctDog.name != null){
          /*
          When you concatenate a string that will go on a text view, be sure to use
          a string resource similar to below. Check strings.xml for the string with the name
          "question"
           */
            questionTextView.text = resources.getString(R.string.question, getFormattedDogBreed(correctDog.name))
        }

        val dogs = arrayOf(correctDog,incorrectDog)
        dogs.shuffle()

        assignImageToViews(dogs)
        return QuestionData(dogs)
    }

    private fun assignImageToViews (dogs: Array<Breed> ) {
        val imageViews = arrayOf<ImageView>(
            findViewById(R.id.dog_1),
            findViewById(R.id.dog_2)
        )

        for (imageViewIndex in imageViews.indices) {
            imageViews[imageViewIndex].setImageBitmap(dogs[imageViewIndex].imageData?.image)
            imageViews[imageViewIndex].setOnClickListener{
//                Check if ID of current image is the same as the answer
                 if (question.dogs[imageViewIndex].id === answer) {
                     resultTextView.text = resources.getText(R.string.answer_correct)
                     resultTextView.setTextColor(Color.rgb(0, 255, 100))
                 } else {
                     resultTextView.text = resources.getText(R.string.answer_wrong)
                     resultTextView.setTextColor(Color.rgb(255,0,0))
                 }

//                After assigning write/wrong, then we hide the images and show spinner and result
                imageContainer.visibility = View.GONE
                resultContainer.visibility = View.VISIBLE

                /*
                 * We need to update the question on the main thread (UI Thread) so create an object
                 * of the main executor object.
                 */
                val mainExecutor = ContextCompat.getMainExecutor(this)

                /**
                 * After creating main executor, run a timer on the background thread
                 * using Executors.newSingleThreadExecutor to simulate an API call while we
                 * get the new question
                 */
                Executors.newSingleThreadExecutor().execute {
                    /**
                     * In the background thread, after 2 seconds we reset the answer via reset()
                     * and we generate a new question and assign it to the question variable
                     */
                    Timer().schedule(2000){
                        mainExecutor.execute {
                            reset()
                            question = generateQuestion()
                        }
                    }
                }
            }

            /**
             * Hide the spinner and result once the new question image views have been set
             */
            imageContainer.visibility = View.VISIBLE
            resultContainer.visibility = View.GONE
        }
    }

    private fun getFormattedDogBreed(breed: String): String {
        return breed
            .replace(Regex("[_-]"), " ")
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            }
    }

    private fun getRandomDog(): Breed {
        val randomBreedId = getRandomBreedId(dogBreedMap)
        val randomBreedName = dogBreedMap[randomBreedId]
        val breedImageData = getBreedImage(randomBreedId)

        return Breed(randomBreedId, randomBreedName, breedImageData)
    }

    private fun getBreedImage(breedId: String): BreedImageData? {
        val resourceRoot = "$dogBreedFolderName/$breedId-${dogBreedMap[breedId]}"
        val imageList = assets.list(resourceRoot)

        if (imageList != null && imageList.isNotEmpty()){
            val randomImageIndex = Random.nextInt(0, imageList.size - 1)
            val imageResourceName = imageList[randomImageIndex]
            val imagePath = "$resourceRoot/$imageResourceName"

            val asset = assets.open(imagePath,AssetManager.ACCESS_STREAMING)
            if (asset.available() > 0) {
                return BreedImageData(
                    imageResourceName,
                    BitmapFactory.decodeStream(asset)
                )
            }
        }

        return null
    }

    private fun getRandomBreedId(breedMap: Map<String, String>): String {
        val randomNumber = Random.nextInt(0, breedMap.keys.size - 1)
        return breedMap.keys.toList()[randomNumber]
    }

    private fun reset() {
        resultTextView.text = ""
    }

    private fun getDogBreedMap(): Map<String, String> {
        /**
         * We can create a map directly from the list by using `associate`
         */
        return assets.list(dogBreedFolderName)?.associate {
            val folderSplit = it.split("-")
            val dogBreedId = folderSplit[0]
            val dogBreedName = folderSplit[1]

            dogBreedId to dogBreedName
        } ?: mapOf()
    }
}