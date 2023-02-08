package com.example.week2

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout

// https://medium.com/android-dev-hacks/a-kotlin-based-introduction-to-compound-components-on-android-part-1-2d28323b9ef9
class NumberGenerator
    @JvmOverloads
    constructor(ctx: Context, attributeSet: AttributeSet?, defStyleAttr:Int = 0)
    : LinearLayout(ctx, attributeSet,defStyleAttr) {

    init {
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        this.gravity = Gravity.CENTER_VERTICAL

        inflater.inflate(R.layout.number_generator, this)
    }
}