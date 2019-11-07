package com.example.mylistproject.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mylistproject.model.Guest

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION, null) {


    companion object {
        const val DATABASE_NAME = "guests.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "guests"
        const val GUEST_ID = "guest_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_ROOMNUMBER = "roomnumber"
        const val COLUMN_PRICE = "price"
        const val TOTAL_PRICE = "total"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createStatement =
            "CREATE TABLE $TABLE_NAME ($GUEST_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_ROOMNUMBER TEXT, $COLUMN_PRICE INTEGER)"
        db.execSQL(createStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertGuest(newGuest: Guest) {
        val guestValues = ContentValues()
        guestValues.put(COLUMN_NAME, newGuest.name)
        guestValues.put(COLUMN_ROOMNUMBER, newGuest.roomNumber.toString())
        guestValues.put(COLUMN_PRICE, newGuest.price)
        val db = writableDatabase
        db.insert(TABLE_NAME, null, guestValues)
        db.close()
    }

    fun readAllGuests(): Cursor {
        val query = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        return db.rawQuery(query, null)
    }

    fun getTotalPrice(): Int {
        val sumQuery = "SELECT SUM($COLUMN_PRICE) FROM $TABLE_NAME"
        val cursor = readableDatabase.rawQuery(sumQuery, null)
        cursor.moveToFirst()
        return  cursor.getInt(0)
    }
}