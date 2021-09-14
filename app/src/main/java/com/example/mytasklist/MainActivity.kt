package com.example.mytasklist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mytasklist.UI.AddTaskActivity
import com.example.mytasklist.UI.TasklistAdapt
import com.example.mytasklist.databinding.ActivityMainBinding
import com.example.mytasklist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TasklistAdapt() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners(){
        binding.btnPlus.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)

        }

        adapter.listenerEdit = {

            val intent = Intent(this, AddTaskActivity::class.java)
            val putExtra = intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)

        }
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK)
            updateList()
    }

    private fun updateList(){
        val list = TaskDataSource.getList()
        if(list.isEmpty()){
            binding.included.emptyState.visibility = View.VISIBLE
        } else {
            binding.included.emptyState.visibility = View.GONE
        }
        adapter.submitList(list)
    }
    companion object{
        private const val CREATE_NEW_TASK = 1000
    }
}