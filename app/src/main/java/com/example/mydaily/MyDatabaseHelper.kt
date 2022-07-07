package com.example.mydaily

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name: String, version: Int):
        SQLiteOpenHelper(context, name, null, version){

    private val createDaily = "create table daily (" +
            "id integer primary key autoincrement," +
            "title text," +
            "time text," +
            "content text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createDaily)
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
//        TODO("Not yet implemented")
    }
}