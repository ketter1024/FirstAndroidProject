package com.example.firstproject

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Lab 3 - experiment 5: the same animal list, this time done with RecyclerView.
 *
 * RecyclerView 自己不画任何东西，三件套必须手写：
 *
 *   recyclerView.layoutManager = LinearLayoutManager(this)  怎么排（竖着，一行一条）
 *   recyclerView.adapter       = AnimalAdapter(...)         数据怎么变成行
 *   (RecyclerView 只负责回收和复用行)
 *
 * 少了 layoutManager 这一行，界面会是空白 —— 这是最常见的"RecyclerView 不显示"的原因。
 */
class Lab3RecyclerActivity : Activity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimalAdapter

    private val animals = mutableListOf(
        AnimalAdapter.Animal("Lion", R.drawable.lion),
        AnimalAdapter.Animal("Tiger", R.drawable.tiger),
        AnimalAdapter.Animal("Monkey", R.drawable.monkey),
        AnimalAdapter.Animal("Dog", R.drawable.dog),
        AnimalAdapter.Animal("Cat", R.drawable.cat),
        AnimalAdapter.Animal("Elephant", R.drawable.elephant)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab3_recycler)

        recyclerView = findViewById(R.id.rvAnimals)

        adapter = AnimalAdapter(animals) { position ->
            val clicked = animals[position]
            // 单击：显示名称
            Toast.makeText(this, clicked.name, Toast.LENGTH_SHORT).show()
            // 再增加一条记录，列表立即更新
            adapter.addItem(
                AnimalAdapter.Animal("${clicked.name} #${animals.size + 1}", clicked.iconRes)
            )
            // 滚到新加的那一条，方便验收
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
