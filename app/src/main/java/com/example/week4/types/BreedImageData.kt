package com.example.week4.types

import android.graphics.Bitmap

data class BreedImageData(val fileName:String, val image: Bitmap)

data class Breed(val id: String, val name: String?, val imageData: BreedImageData?)

data class QuestionData(val dogs: Array<Breed>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuestionData

        if (!dogs.contentEquals(other.dogs)) return false

        return true
    }

    override fun hashCode(): Int {
        val result = dogs.contentHashCode()
        return result
    }
}