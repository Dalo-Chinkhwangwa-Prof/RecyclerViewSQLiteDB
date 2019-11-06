package com.example.mylistproject.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION, null){


    companion object{
        const val DATABASE_NAME = "guests.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "guests"
        const val COLUMN_NAME = "name"
        const val COLUMN_ROOMNUMBER = "roomnumber"
        const val COLUMN_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createStatement = "CREATE TABLE $TABLE_NAME ()"
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}