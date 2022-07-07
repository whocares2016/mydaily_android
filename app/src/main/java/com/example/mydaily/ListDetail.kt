package com.example.mydaily

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.daily_detail.*

class ListDetail: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_detail)

        btn_detail_edit.setOnClickListener {
            val id = intent.getIntExtra("id", 0)
            val intent = Intent(this, EditDaily::class.java)
            intent.putExtra("id", id)
            startActivity(intent);
        }

        btn_detail_delete.setOnClickListener {
            showSimpleConfirmDialog(this)
        }
    }

    override fun onResume() {
        super.onResume()
        val id = intent.getIntExtra("id", 0)
        var title2: String = "no title"
        var time2: String = "no time"
        var content2: String = "nothing"

        val dbHelper = MyDatabaseHelper(this, "daily.db", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("select * from daily where id = $id", null)
        if (cursor.moveToFirst()) {
            do {
                title2 = cursor.getString(cursor.getColumnIndex("title"))
                time2 = cursor.getString(cursor.getColumnIndex("time"))
                content2 = cursor.getString(cursor.getColumnIndex("content"))
            } while (cursor.moveToNext())
        }

        title_detail.text = title2
        time_detail.text = time2
        content_detail.text = content2
    }

    /**
     * 简单的确认对话框
     */
    private fun showSimpleConfirmDialog(activity: Activity) {
        AlertDialog.Builder(this).apply {
            setTitle("Ask for your decision")
            setMessage("您要删除当前List吗")
            setCancelable(false)
            setPositiveButton("OK"){ _, _ ->
                // 删除操作
                val id = intent.getIntExtra("id", 0)
                val dbHelper = MyDatabaseHelper(activity, "daily.db", 1)
                val db = dbHelper.writableDatabase
                db.delete("daily", "id = ?", arrayOf(id.toString()))

                Toast.makeText(activity, "List已删除", Toast.LENGTH_SHORT).show()
                activity.finish()
            }
            setNegativeButton("No"){ _, _ ->
                Toast.makeText(activity, "已撤销删除操作", Toast.LENGTH_SHORT).show()
            }
        }.show()
    }
}