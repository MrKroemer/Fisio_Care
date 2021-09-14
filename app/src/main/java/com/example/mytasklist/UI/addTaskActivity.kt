package com.example.mytasklist.UI

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mytasklist.databinding.ActivityAddTaskBinding
import com.example.mytasklist.datasource.TaskDataSource
import com.example.mytasklist.extensions.format
import com.example.mytasklist.extensions.text
import com.example.mytasklist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.titulo.text = it.title
                binding.date.text = it.date
                binding.hour.text = it.hour
                binding.descricao.text = it.descricao
                binding.age.text = it.idade
                binding.addresse.text = it.endereco
            }
        }

        insertListeners()
    }

    private fun insertListeners(){
        binding.date.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .build()

            val timeZone = TimeZone.getDefault()
            val offSet = timeZone.getOffset(Date().time) * -1

            datePicker.addOnPositiveButtonClickListener {
                binding.date.text = Date(it + offSet).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")

        }

        binding.hour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {

                val hour = if(timePicker.hour in 0..9){"0${timePicker.hour}"} else{timePicker.hour}
                val minute = if(timePicker.minute in 0..9){"0${timePicker.minute}"} else{timePicker.minute}

                binding.hour.text = "$hour : $minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.button2.setOnClickListener {
            finish()
        }

        binding.button1.setOnClickListener {

            val task = Task(
                date = binding.date.text,
                hour = binding.hour.text,
                title = binding.titulo.text,
                descricao = binding.descricao.text,
                idade = binding.age.toString(),
                endereco = binding.addresse.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )

            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
            Log.e("TAG", "insertListeners" + TaskDataSource.getList())
        }
    }

    companion object{
        const val TASK_ID = "task_id"
    }

}