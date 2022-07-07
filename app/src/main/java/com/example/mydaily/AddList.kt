package com.example.mydaily

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_daily_activity.*
import java.security.AccessController.getContext

class AddList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_daily_activity)

        btn_sure.setOnClickListener {
            val title = et_title.text.toString()
            val time = et_time.text.toString()
            val content = et_content.text.toString()
            if (title.isNotEmpty() && time.isNotEmpty() && content.isNotEmpty()) {
                showSimpleConfirmDialog1(this, title, time, content)
            } else {
                if (title.isEmpty())
                    Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show()
                if (time.isEmpty())
                    Toast.makeText(this, "时间不能为空", Toast.LENGTH_SHORT).show()
                if (content.isEmpty())
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show()
            }
        }

        btn_cancel.setOnClickListener {
            val title = et_title.text.toString()
            val time = et_time.text.toString()
            val content = et_content.text.toString()
//            println("title: $title ${title.length} ${title.isNotEmpty()}")
//            println("time: $time ${time.length} ${title.isNotEmpty()}")
//            println("content: $content ${content.length} ${title.isNotEmpty()}")
            if (title.isNotEmpty() || time.isNotEmpty() || content.isNotEmpty()) {
                showSimpleConfirmDialog2(this, title, time, content)
            } else {
                this.finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val lastDailyTitle = prefs.getString("last_daily_title", "")
//        println("寻找lastDailyTitle... $lastDailyTitle")
        val lastDailyTime = prefs.getString("last_daily_time", "")
        val lastDailyContent = prefs.getString("last_daily_content", "")
        if (lastDailyTitle != null) {
            if (lastDailyTitle.isNotEmpty())
//                println("寻找lastDailyTitle... $lastDailyTitle")
            et_title.setText(lastDailyTitle)
        }
        if (lastDailyTime != null) {
            if (lastDailyTime.isNotEmpty())
//                println("lastDailyTime... $lastDailyTime")
            et_time.setText(lastDailyTime)
        }
        if (lastDailyContent != null) {
            if (lastDailyContent.isNotEmpty())
//                println("寻找lastDailyContent... $lastDailyContent")
            et_content.setText(lastDailyContent)
        }
    }

    private fun showSimpleConfirmDialog1(activity: Activity, title: String, time: String, content: String) {
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val editor = prefs.edit()
        AlertDialog.Builder(this).apply {
            setTitle("Ask for your decision")
            setMessage("您确认添加当前List吗")
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
                // 声明一个数据库帮助类
                val dbHelper = MyDatabaseHelper(activity, "daily.db", 1)
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("title", title)
                    put("time", time)
                    put("content", content)
                }
                db.insert("daily", null, values)

                editor.putString("last_daily_title", "")
                editor.putString("last_daily_time", "")
                editor.putString("last_daily_content", "")
                editor.apply()

                Toast.makeText(activity, "List已添加", Toast.LENGTH_SHORT).show()
                activity.finish()
            }
            setNegativeButton("No") { _, _ ->
                // do nothing
            }
        }.show()
    }

    /**
     * 简单的确认对话框
     */
    private fun showSimpleConfirmDialog2(activity: Activity, title: String, time: String, content: String) {
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val editor = prefs.edit()
        AlertDialog.Builder(this).apply {
            setTitle("Ask for your decision")
            setMessage("您要保留当前List吗")
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
                editor.putString("last_daily_title", title)
                editor.putString("last_daily_time", time)
                editor.putString("last_daily_content", content)
                editor.apply()
                Toast.makeText(activity, "List已缓存", Toast.LENGTH_SHORT).show()
                activity.finish()
            }
            setNegativeButton("No") { _, _ ->
                editor.putString("last_daily_title", "")
                editor.putString("last_daily_time", "")
                editor.putString("last_daily_content", "")
                editor.apply()
                activity.finish()
            }
        }.show()
    }
}