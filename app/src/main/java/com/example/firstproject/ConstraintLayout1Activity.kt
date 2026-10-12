package com.example.firstproject

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

/**
 * Lab 2 - ConstraintLayout experiment 1 (calculator).
 *
 * The layout is res/layout/activity_constraint_layout1.xml. This activity only
 * wires the keys up to a small calculator: the keys append digits, the four
 * operator keys remember a pending operation and "=" evaluates it.
 */
class ConstraintLayout1Activity : Activity() {

    private lateinit var display: TextView

    /** The number as typed by the user, or the last result. */
    private var entry = "0"

    /** Left hand side of the pending operation. */
    private var accumulator: Double? = null

    /** The operator waiting for its right hand side, null when none is pending. */
    private var pendingOp: Char? = null

    /** True when the next key press should start a fresh number. */
    private var startNewEntry = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout1)

        display = findViewById(R.id.tvDisplay)

        val digitKeys = mapOf(
            R.id.btn0 to '0',
            R.id.btn1 to '1',
            R.id.btn2 to '2',
            R.id.btn3 to '3',
            R.id.btn4 to '4',
            R.id.btn5 to '5',
            R.id.btn6 to '6',
            R.id.btn7 to '7',
            R.id.btn8 to '8',
            R.id.btn9 to '9'
        )
        digitKeys.forEach { (id, digit) ->
            findViewById<Button>(id).setOnClickListener { onDigit(digit) }
        }

        val operatorKeys = mapOf(
            R.id.btnPlus to '+',
            R.id.btnMinus to '-',
            R.id.btnMul to '*',
            R.id.btnDiv to '/'
        )
        operatorKeys.forEach { (id, op) ->
            findViewById<Button>(id).setOnClickListener { onOperator(op) }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener { onDot() }
        findViewById<Button>(R.id.btnEq).setOnClickListener { onEquals() }
    }

    private fun onDigit(digit: Char) {
        entry = if (startNewEntry || entry == "0") digit.toString() else entry + digit
        startNewEntry = false
        show()
    }

    private fun onDot() {
        when {
            startNewEntry -> {
                entry = "0."
                startNewEntry = false
            }

            !entry.contains('.') -> entry += "."
        }
        show()
    }

    private fun onOperator(op: Char) {
        val value = entry.toDoubleOrNull() ?: 0.0
        accumulator = if (pendingOp == null) {
            value
        } else {
            evaluate(accumulator ?: 0.0, value, pendingOp!!)
        }
        pendingOp = op
        startNewEntry = true
    }

    private fun onEquals() {
        val op = pendingOp ?: return
        val value = entry.toDoubleOrNull() ?: 0.0
        val result = evaluate(accumulator ?: 0.0, value, op)

        entry = format(result)
        accumulator = null
        pendingOp = null
        startNewEntry = true
        show()
    }

    private fun evaluate(left: Double, right: Double, op: Char): Double = when (op) {
        '+' -> left + right
        '-' -> left - right
        '*' -> left * right
        '/' -> if (right == 0.0) 0.0 else left / right
        else -> right
    }

    /** Prints whole numbers without a trailing ".0". */
    private fun format(value: Double): String =
        if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

    private fun show() {
        display.text = entry
    }
}
