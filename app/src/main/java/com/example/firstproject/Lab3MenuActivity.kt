package com.example.firstproject

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

/**
 * Lab 3 - experiment 3: an options menu defined in res/menu/lab3_options_menu.xml.
 *
 * The action bar on this screen is the android.widget.Toolbar from the layout,
 * promoted by setActionBar(). Because it is the activity's action bar,
 * onCreateOptionsMenu() fills it just like a window action bar would, and the
 * three items show up in its overflow button (the three dots).
 *
 * Three methods do all the work:
 *
 *  onCreateOptionsMenu()   - inflate the XML into the action bar's menu;
 *  onOptionsItemSelected() - react to a click;
 *  (onPrepareOptionsMenu() - only needed when the menu must change every time it
 *                            is opened, so it is not used here.)
 *
 * 字体大小 and 字体颜色 are submenus: clicking one of their children delivers
 * the *child* id here, never the parent id.
 */
class Lab3MenuActivity : Activity() {

    private lateinit var etTest: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab3_menu)

        // 窗口的 ActionBar 在 targetSdk 37 的边到边窗口里会压住内容，补上它的高度
        fixContentUnderActionBar(findViewById(R.id.contentRoot))

        etTest = findViewById(R.id.etTest)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lab3_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 字体大小：10 / 16 / 20 号字
            R.id.menu_font_small -> etTest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            R.id.menu_font_medium -> etTest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            R.id.menu_font_large -> etTest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

            // 普通菜单项：弹一个 Toast
            R.id.menu_normal ->
                Toast.makeText(this, R.string.menu_normal_toast, Toast.LENGTH_SHORT).show()

            // 字体颜色：红 / 黑
            R.id.menu_color_red -> etTest.setTextColor(Color.RED)
            R.id.menu_color_black -> etTest.setTextColor(Color.BLACK)

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
