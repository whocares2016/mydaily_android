package com.example.mydaily

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val dailyList = ArrayList<List>()

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, AddList::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        dailyList.clear()
        val layoutManager = LinearLayoutManager(this)
        // 从数据库中读取数据
        // 若没有这个数据库，SQLiteOpenHelper会自动帮我们创建
        val dbHelper = MyDatabaseHelper(this, "daily.db", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("select * from daily order by id DESC", null)
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val time = cursor.getString(cursor.getColumnIndex("time"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                dailyList.add(List(id, title, time, content))
            } while (cursor.moveToNext())
        }

        // 展示在主页
        recycleView.layoutManager = layoutManager
        val adapter = ListAdapter(this, dailyList)
        recycleView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_item -> {
                val intent = Intent(this, AddList::class.java)
                startActivity(intent)
            }
            R.id.search_item -> Toast.makeText(this, "该功能还在开发中...敬请期待", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun initDaily(){
            dailyList.add(List(1, "第一天日记", "11.20", "今天好开心"))
            dailyList.add(List(2,"第二天日记", "11.19", "今天超级开心"))
            dailyList.add(List(3,"第三天日记", "11.18", "今天好开心"))
            dailyList.add(List(4,"第四天日记", "11.17", "今天非常开心"))
            dailyList.add(List(5,"第五天日记", "11.16", "今天好开心"))
            dailyList.add(List(6,"第六天日记", "11.15", "今天特别特别开心"))
            dailyList.add(List(7,"第七天日记", "11.14", "今天好开心"))
            dailyList.add(List(8,"第八天日记", "11.13", "今天狠开心"))
            dailyList.add(List(9,"第九天日记", "11.12", "今天好开心"))

    }
}