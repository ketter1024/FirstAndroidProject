package com.example.firstproject

import android.app.Activity
import android.os.Bundle

/**
 * Lab 2 - TableLayout experiment.
 *
 * Shows the "Hello TableLayout" menu built from a TableLayout, see
 * res/layout/activity_table_layout.xml.
 */
class TableLayoutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_layout)
    }
}
