package com.example.firstproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

/**
 * Lab 2 entry screen.
 *
 * Each experiment lives in its own activity, so this screen just offers one
 * button per experiment and starts the matching activity. See
 * res/layout/activity_main.xml.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openOnClick(R.id.btnLinear, LinearLayoutActivity::class.java)
        openOnClick(R.id.btnTable, TableLayoutActivity::class.java)
        openOnClick(R.id.btnConstraint1, ConstraintLayout1Activity::class.java)
        openOnClick(R.id.btnConstraint2, ConstraintLayout2Activity::class.java)
    }

    private fun openOnClick(buttonId: Int, target: Class<*>) {
        findViewById<Button>(buttonId).setOnClickListener {
            startActivity(Intent(this, target))
        }
    }
}
