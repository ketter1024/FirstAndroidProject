package com.example.firstproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Lab 3 - experiment 5: the RecyclerView adapter.
 *
 * RecyclerView 把一个 Adapter 的工作拆成了三件事：
 *
 *  onCreateViewHolder() 造一个空壳（ViewHolder），只在这一行"不够用"时才调用；
 *  onBindViewHolder()   把第 position 条数据塞进那个壳里，滚动时不停地调用；
 *  getItemCount()       告诉 RecyclerView 一共有多少条。
 *
 * 关键点：行布局只 inflate 一次、findViewById 只做一次（在 ViewHolder 里），
 * 所以滚动多少屏都不会卡；反过来，如果数据只存在 View 上而没跟着 position 走，
 * 复用之后就会出现"错位"。
 */
class AnimalAdapter(
    private val items: MutableList<Animal>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    /** 列表项的数据：名字 + 图片资源 id。 */
    data class Animal(val name: String, val iconRes: Int)

    /**
     * ViewHolder 只干一件事：把一行里要用到的子 View 找一次，缓存起来。
     * 之后每次复用这一行都不必再 findViewById。
     */
    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val ivAnimal: ImageView = itemView.findViewById(R.id.ivAnimal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = items[position]
        holder.tvName.text = animal.name
        holder.ivAnimal.setImageResource(animal.iconRes)

        holder.itemView.setOnClickListener {
            // 用 bindingAdapterPosition，而不是上面那个 position：holder 会被
            // 复用，闭包里存下来的 position 在插入/删除之后可能已经过期。
            val current = holder.bindingAdapterPosition
            if (current != RecyclerView.NO_POSITION) {
                onItemClick(current)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    /**
     * 新增一条，并且只通知"最后一条插入了"。
     * 用 notifyDataSetChanged() 也能刷新，但那是"整屏重绘"，
     * 用 notifyItemInserted() 才能保持滚动位置、并且不会错位。
     */
    fun addItem(animal: Animal) {
        items.add(animal)
        notifyItemInserted(items.size - 1)
    }
}
