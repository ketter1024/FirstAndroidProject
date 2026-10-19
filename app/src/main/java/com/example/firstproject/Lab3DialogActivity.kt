package com.example.firstproject

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

/**
 * Lab 3 - experiment 2: an AlertDialog with a custom layout.
 *
 * Two things are strictly separated here, which is what the experiment asks for:
 *
 *  1. the *content* comes from res/layout/dialog_login.xml, handed to the dialog
 *     with AlertDialog.Builder.setView();
 *  2. the two buttons are the dialog's own, created by
 *     setNegativeButton() / setPositiveButton() - they are NOT part of the
 *     custom layout.
 *
 * The extra block in the onShow listener only restyles those two system buttons:
 * on Android 5+ the button bar packs them to the right (and upper-cases them),
 * while the experiment screenshot shows two equal halves. Setting layout weight
 * 1 on each button - and 0 on the invisible Spacer the framework puts after them
 * - turns "packed right" into "50 / 50", without giving up the dialog's buttons.
 */
class Lab3DialogActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab3_dialog)

        findViewById<Button>(R.id.btnShowDialog).setOnClickListener { showLoginDialog() }
    }

    private fun showLoginDialog() {
        // 1. 先把自定义布局 inflate 成一个 View
        val content = layoutInflater.inflate(R.layout.dialog_login, null)

        // 2. 再把它交给 Builder.setView()；按钮仍是对话框自己的
        val dialog = AlertDialog.Builder(this)
            .setView(content)
            .setNegativeButton(R.string.dialog_cancel) { d: DialogInterface, _: Int -> d.dismiss() }
            .setPositiveButton(R.string.dialog_sign_in) { _: DialogInterface, _: Int ->
                // 注意：要在这个 View 上 findViewById，不是 Activity 上
                val username = content.findViewById<EditText>(R.id.etUsername).text.toString()
                val password = content.findViewById<EditText>(R.id.etPassword).text.toString()
                Toast.makeText(
                    this,
                    "Sign in: $username / 密码长度 ${password.length}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .create()

        // 3. 等对话框真正显示出来，再把系统画好的两个按钮改成各占一半
        dialog.setOnShowListener {
            splitButtonsEvenly(dialog)
        }

        dialog.show()
    }

    /**
     * 把对话框底部的 Cancel / Sign in 从"靠右排"改成"各占一半"。
     *
     * 框架的按钮面板是一个 LinearLayout：按钮后面跟着一个 weight=1 的空 Space，
     * 所以按钮被挤到右边。给两个按钮各设 weight=1、宽 0，再把那个 Space 的
     * weight 清成 0，两半就均分了。
     */
    private fun splitButtonsEvenly(dialog: AlertDialog) {
        val negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val panel = negative?.parent as? LinearLayout ?: return

        for (index in 0 until panel.childCount) {
            val child = panel.getChildAt(index)
            if (child === negative || child === positive) {
                child.layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
                (child as? Button)?.apply {
                    gravity = Gravity.CENTER
                    isAllCaps = false            // 讲义截图里是 "Cancel" / "Sign in"，不是全大写
                }
            } else {
                // 框架塞在末尾的占位 Space，压成 0 宽，否则它会分走一半宽度
                val params = child.layoutParams as? LinearLayout.LayoutParams ?: continue
                params.width = 0
                params.weight = 0f
                child.layoutParams = params
            }
        }
    }
}
