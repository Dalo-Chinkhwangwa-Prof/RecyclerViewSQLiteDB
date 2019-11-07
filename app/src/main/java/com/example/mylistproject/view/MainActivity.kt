package com.example.mylistproject.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mylistproject.R
import com.example.mylistproject.adapter.GuestAdapter
import com.example.mylistproject.adapter.GuestRAdapter
import com.example.mylistproject.database.MyDatabaseHelper
import com.example.mylistproject.model.Guest
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), GuestAdapter.GuestAdapterDelegate {

    private val fileName = "MyGuestList.txt"
    private var guestList = mutableListOf<Guest>()
    private lateinit var myDatabaseHelper: MyDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val itemDecoration = DividerItemDecoration(this, VERTICAL)
        hotel_list_view.addItemDecoration(itemDecoration)

        myDatabaseHelper = MyDatabaseHelper(this)

        check_in_button.setOnClickListener { _ ->
            //            writeToFile()
            saveToDatabase()
        }
    }

    override fun onResume() {
        super.onResume()
        readFromDatabase()
    }

    private fun writeToFile() {
        val fileOutputStream = openFileOutput(fileName, Context.MODE_APPEND)
        val inputString =
            "\n${name_edittext.text}:${room_number_edit_text.text}:${price_edit_text.text}"
        fileOutputStream.write(inputString.toByteArray())
        Toast.makeText(
            this,
            "Room #${room_number_edit_text.text} checked into!",
            Toast.LENGTH_SHORT
        ).show()

        clearFields()
        readFromExternal()
    }

    private fun readFromExternal() {
        val fileInputStream = openFileInput(fileName)
        val fileInputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(fileInputStreamReader)

        var readSting: String? = null
        val delimiter = ":"

        guestList = mutableListOf()
        while ({ readSting = bufferedReader.readLine(); readSting }() != null) {
            val myInput = readSting?.split(delimiter)
            if (myInput?.size ?: 0 > 1) {
                val readGuest = Guest(
                    myInput?.get(0),
                    Integer.parseInt(myInput?.get(1) ?: "0"),
                    Integer.parseInt(myInput?.get(2) ?: "0")
                )
                guestList.add(readGuest)
            }
        }

        displayUsers()
    }

    private fun clearFields() {
        name_edittext.text.clear()
        room_number_edit_text.text.clear()
        price_edit_text.text.clear()
    }

    private fun displayUsers() {
//        with BaseAdapter and ListView
//        val myBaseAdapter = GuestAdapter(guestList, this)
//        hotel_list_view.adapter = myBaseAdapter

//        With a RecyclerViewAdapter and ListView
        val recyclerAdapter = GuestRAdapter(guestList)
        hotel_list_view.adapter = recyclerAdapter
        val layoutMgr = LinearLayoutManager(this)
        hotel_list_view.layoutManager = layoutMgr

    }

    override fun deleteBooking(guestPosition: Int) {
        //deleteItem(guestPosition)
    }

    private fun deleteItem(position: Int) {
        guestList.removeAt(position)
        var fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
        for (i in 0 until guestList.size) {
            val guestAsString =
                "${guestList.get(i).name}:${guestList.get(i).roomNumber}:${guestList.get(i).price}:"
            if (i == 0)
                fileOutputStream.write(guestAsString.toByteArray())
            else {
                fileOutputStream = openFileOutput(fileName, Context.MODE_APPEND)
                fileOutputStream.write(guestAsString.toByteArray())
            }
        }
        Toast.makeText(this, "Guest checked out!", Toast.LENGTH_SHORT).show()
        readFromExternal()
    }

    private fun saveToDatabase() {
        val guestName = name_edittext.text.toString()
        val guestRoom = room_number_edit_text.text.toString()
        val roomPrice = price_edit_text.text.toString()
        val newGuest = Guest(guestName, Integer.parseInt(guestRoom), Integer.parseInt(roomPrice))
        myDatabaseHelper.insertGuest(newGuest)
        Toast.makeText(this, "Guest added to database.", Toast.LENGTH_SHORT).show()
        clearFields()
        readFromDatabase()
    }

    private fun readFromDatabase() {
        guestList = mutableListOf()

        val cursor = myDatabaseHelper.readAllGuests()
        cursor.moveToFirst()

        if (cursor.count > 0) {
            val guest = Guest(
                cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_ROOMNUMBER)),
                cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_PRICE))
            )
            guestList.add(guest)
        }
        while (cursor.moveToNext()) {
            val guestName = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NAME))
            val guestRoom =
                cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_ROOMNUMBER))
            val roomPrice = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_PRICE))
            val readGuest = Guest(guestName, Integer.parseInt(guestRoom), roomPrice)
            guestList.add(readGuest)
        }
        displayUsers()
        getTotalPrice()
    }

    private fun getTotalPrice() {
        Toast.makeText(this, myDatabaseHelper.getTotalPrice().toString(), Toast.LENGTH_SHORT).show()
        Log.d("TAG_X", myDatabaseHelper.getTotalPrice().toString())
    }

}
