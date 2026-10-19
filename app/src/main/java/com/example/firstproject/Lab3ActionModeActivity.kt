package com.example.firstproject

import android.app.Activity
import android.os.Bundle
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem

/**
 * Lab 3 - experiment 4: the contextual action bar (ActionMode) over a ListView.
 *
 * The list is put in CHOICE_MODE_MULTIPLE_MODAL, which is the mode invented for
 * exactly this task: a long press on a row checks it, the contextual action bar
 * appears on top of the toolbar, and you can keep tapping more rows - each tap
 * checks / unchecks one row, and the title follows the count ("1 selected",
 * "2 selected", ...).
 *
 * The framework starts and finishes the action mode by itself in this mode; all
 * we have to implement is AbsListView.MultiChoiceModeListener:
 *
 *   onCreateActionMode()        inflate the delete icon into the bar
 *   onItemCheckedStateChanged() update the "N selected" title
 *   onActionItemClicked()       delete every checked row
 *   onDestroyActionMode()       repaint the list so no row stays highlighted
 *
 * The blue/red "this row is selected" background comes from
 * ?android:attr/activatedBackgroundIndicator in item_actionmode.xml.
 */
class Lab3ActionModeActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private val items = arrayListOf("One", "Two", "Three", "Four", "Five")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab3_actionmode)

        // 窗口的 ActionBar 在 targetSdk 37 的边到边窗口里会压住内容，补上它的高度
        fixContentUnderActionBar(findViewById(R.id.contentRoot))

        listView = findViewById(R.id.lvItems)
        // item_actionmode.xml 里有 @id/tvItem，ArrayAdapter 就用它填每行的文字
        adapter = ArrayAdapter(this, R.layout.item_actionmode, R.id.tvItem, items)
        listView.adapter = adapter

        // 多选 + 上下文操作模式：长按自动进入，不用自己写 OnItemLongClickListener
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        listView.setMultiChoiceModeListener(multiChoiceModeListener)
    }

    private val multiChoiceModeListener = object : AbsListView.MultiChoiceModeListener {

        /** 进入上下文操作模式：把删除图标装进去。 */
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.lab3_action_mode_menu, menu)
            updateTitle(mode)
            return true
        }

        /** 每次显示前调整菜单，这里不需要。 */
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

        /** 某一行被选中 / 取消选中：标题上的数字跟着变。 */
        override fun onItemCheckedStateChanged(
            mode: ActionMode,
            position: Int,
            id: Long,
            checked: Boolean
        ) {
            updateTitle(mode)
        }

        /** 点了操作栏上的垃圾桶：删掉所有选中的行。 */
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            if (item.itemId != R.id.action_delete) return false

            val checked = listView.checkedItemPositions
            val positions = (0 until checked.size())
                .filter { checked.valueAt(it) }
                .map { checked.keyAt(it) }
                .sortedDescending()   // 从后往前删，前面的下标才不会错位
            for (position in positions) {
                items.removeAt(position)
            }
            adapter.notifyDataSetChanged()
            mode.finish()
            return true
        }

        /** 退出上下文操作模式：重绘列表，去掉残留的选中底色。 */
        override fun onDestroyActionMode(mode: ActionMode) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun updateTitle(mode: ActionMode) {
        mode.title = getString(R.string.action_mode_title, listView.checkedItemCount)
    }
}
