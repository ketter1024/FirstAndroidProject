package com.example.firstproject

import android.app.Activity
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets

/**
 * 让"窗口 ActionBar"不再压住内容。
 *
 * 背景：targetSdk 37 起窗口强制"边到边"（edge-to-edge），内容视图是从 y=0 开始
 * 排的，而 ActionBar 是画在内容之上的。于是列表的第一行、菜单页的测试文本会被
 * 操作栏挡住 —— 这不是布局写错了，是窗口没有给操作栏留位置。
 *
 * 做法：等布局完成，看一下内容视图在窗口里的 y 坐标。如果它是 0（说明确实被压住
 * 了），就把"状态栏高度 + 操作栏高度"补成它的 top padding。在老系统上窗口本来
 * 就会给操作栏让出位置，此时 y > 0，这段代码什么都不做，所以不需要判断版本。
 */
fun Activity.fixContentUnderActionBar(root: View) {
    root.post {
        val location = IntArray(2)
        root.getLocationInWindow(location)
        if (location[1] > 0) return@post      // 系统已经让出位置了，不用管

        val statusBarHeight = root.rootWindowInsets?.let { insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsets.Type.statusBars()).top
            } else {
                @Suppress("DEPRECATION")
                insets.systemWindowInsetTop
            }
        } ?: 0

        root.setPadding(
            root.paddingLeft,
            statusBarHeight + actionBarHeight(),
            root.paddingRight,
            root.paddingBottom
        )
    }
}

/** ?android:attr/actionBarSize 换算成像素。 */
private fun Activity.actionBarHeight(): Int {
    val value = TypedValue()
    theme.resolveAttribute(android.R.attr.actionBarSize, value, true)
    return TypedValue.complexToDimensionPixelSize(value.data, resources.displayMetrics)
}
