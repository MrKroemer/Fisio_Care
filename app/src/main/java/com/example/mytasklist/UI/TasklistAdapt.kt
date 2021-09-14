package com.example.mytasklist.UI

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasklist.R
import com.example.mytasklist.databinding.ItemTaskBinding
import com.example.mytasklist.model.Task

class TasklistAdapt: ListAdapter<Task, TasklistAdapt.TaskViewHolder>(DiffCallBack()){
    var listenerEdit : (Task) -> Unit = {}
    var listenerDelete : (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Task) {
            binding.tvQueroSaber.text = item.title
            binding.tvDate.text = "${item.date} ${item.hour}"
            binding.imageV.setOnClickListener {
                showPopup(item)
            }
        }

        private fun showPopup(item: Task) {
            val imageV = binding.imageV
            val popupMenu = PopupMenu(imageV.context, imageV)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId){
                    R.id.menu_edit -> listenerEdit(item)
                    R.id.menu_delete -> listenerDelete(item)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }

    }

}

class DiffCallBack : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

}

