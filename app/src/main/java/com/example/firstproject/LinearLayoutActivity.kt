package com.example.firstproject

import android.app.Activity
import android.os.Bundle

/**
 * Lab 2 - LinearLayout experiment.
 *
 * Shows the 4x4 grid built from nested LinearLayouts, see
 * res/layout/activity_linear_layout.xml.
 */
class LinearLayoutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_layout)
    }
}
