package com.example.firstproject

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat

/**
 * Lab 3 - experiment 1: ListView + SimpleAdapter.
 *
 * SimpleAdapter needs four things and nothing more:
 *
 *  1. the data - a List of Maps, one Map per row, e.g. {"name": "Lion", "img": R.drawable.lion};
 *  2. the row layout - res/layout/item_animal.xml;
 *  3. `from` - the Map keys to read, in order;
 *  4. `to` - the view ids to write those values into, in the same order.
 *
 * A Map value that is an Int is handed to ImageView.setImageResource(), which
 * is the trick that lets R.drawable ids sit next to plain Strings in the Map.
 */
class Lab3ListActivity : Activity() {

    private companion object {
        const val KEY_NAME = "name"
        const val KEY_ICON = "img"
        const val CHANNEL_ID = "lab3_list"
        const val NOTIFICATION_ID = 1
        const val REQ_POST_NOTIFICATIONS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab3_list)

        // The six animal pictures come from the 实验三图片 folder of the lab handout.
        val animals = arrayListOf(
            animal("Lion", R.drawable.lion),
            animal("Tiger", R.drawable.tiger),
            animal("Monkey", R.drawable.monkey),
            animal("Dog", R.drawable.dog),
            animal("Cat", R.drawable.cat),
            animal("Elephant", R.drawable.elephant)
        )

        val listView = findViewById<ListView>(R.id.lvAnimals)
        listView.adapter = SimpleAdapter(
            this,
            animals,
            R.layout.item_animal,
            arrayOf(KEY_NAME, KEY_ICON),
            intArrayOf(R.id.tvName, R.id.ivAnimal)
        )

        listView.setOnItemClickListener { _, _, position, _ ->
            val name = animals[position][KEY_NAME] as String
            // (3) Toast 显示选中的列表项信息
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
            // (4) 再发一条通知
            showNotification(name)
        }

        askForNotificationPermission()
    }

    private fun animal(name: String, iconRes: Int): Map<String, Any> =
        mapOf(KEY_NAME to name, KEY_ICON to iconRes)

    /**
     * Android 13 (API 33) 起 POST_NOTIFICATIONS 是运行时权限，不申请的话
     * notify() 会被系统直接丢掉，而且不会有任何报错。
     */
    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQ_POST_NOTIFICATIONS)
        }
    }

    /**
     * 通知四要素：小图标 / 标题 / 正文 / 点击行为。
     *
     * 小图标用 R.mipmap.ic_launcher（就是“应用程序图标”）。注意：如果通知栏里
     * 显示成一个灰白方块，说明自适应图标（adaptive icon）没被正确裁切，这时把
     * 它换成 R.drawable.ic_launcher_foreground 即可。
     */
    private fun showNotification(title: String) {
        val manager = getSystemService(NotificationManager::class.java)

        // Android 8.0 (API 26) 起必须先建通知渠道，否则通知不显示。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "实验三列表项通知",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(getString(R.string.notification_text, title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, R.string.notification_no_permission, Toast.LENGTH_SHORT).show()
            return
        }
        manager.notify(NOTIFICATION_ID, notification)
    }
}
