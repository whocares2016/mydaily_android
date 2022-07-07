package com.example.mydaily

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.daily_detail.*
import kotlinx.android.synthetic.main.edit_daily.*

class EditDaily : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_daily)

        btn_edit_sure.setOnClickListener {
            showSimpleConfirmDialog(this)
        }

        btn_edit_cancel.setOnClickListener {
            showSimpleConfirmDialog2(this)
        }
    }

    override fun onResume() {
        super.onResume()
        val id = intent.getIntExtra("id", 0)
        var title: String = "no title"
        var time: String = "no time"
        var content: String = "nothing"

        val dbHelper = MyDatabaseHelper(this, "daily.db", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("select * from daily where id = $id", null)
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex("title"))
                time = cursor.getString(cursor.getColumnIndex("time"))
                content = cursor.getString(cursor.getColumnIndex("content"))
            } while (cursor.moveToNext())
        }
        et_edit_title.setText(title)
        et_edit_time.setText(time)
        et_edit_content.setText(content)
    }

    private fun showSimpleConfirmDialog(activity: Activity) {
        AlertDialog.Builder(this).apply {
            setTitle("Ask for your decision")
            setMessage("您确认要修改当前List吗")
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
                val id = intent.getIntExtra("id", 0)
                val title = et_edit_title.text.toString()
                val time = et_edit_time.text.toString()
                val content = et_edit_content.text.toString()

                // 修改当前日记到数据库
                val dbHelper = MyDatabaseHelper(activity, "daily.db", 1)
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("title", title)
                    put("time", time)
                    put("content", content)
                }
                db.update("daily", values, "id = ?", arrayOf(id.toString()))

                Toast.makeText(activity, "List修改成功", Toast.LENGTH_SHORT).show()
                activity.finish()
            }
            setNegativeButton("No") { _, _ ->
//                Toast.makeText(activity, "已撤销删除操作", Toast.LENGTH_SHORT).show()
            }
        }.show()
    }

    private fun showSimpleConfirmDialog2(activity: Activity) {
        AlertDialog.Builder(this).apply {
            setTitle("Ask for your decision")
            setMessage("您确认要取消修改当前List吗")
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
//                Toast.makeText(activity, "List已删除", Toast.LENGTH_SHORT).show()
                activity.finish()
            }
            setNegativeButton("No") { _, _ ->
//                Toast.makeText(activity, "已撤销删除操作", Toast.LENGTH_SHORT).show()
            }
        }.show()
    }
}