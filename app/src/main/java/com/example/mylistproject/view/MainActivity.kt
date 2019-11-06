package com.example.mylistproject.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mylistproject.R
import com.example.mylistproject.adapter.GuestAdapter
import com.example.mylistproject.model.Guest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity(), GuestAdapter.GuestAdapterDelegate {

    private val fileName = "MyGuestList.txt"
    private var guestList = mutableListOf<Guest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        check_in_button.setOnClickListener { _ ->
            writeToFile()
        }
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

        val myBaseAdapter = GuestAdapter(guestList, this)
        hotel_list_view.adapter = myBaseAdapter
    }

    private fun clearFields() {
        name_edittext.text.clear()
        room_number_edit_text.text.clear()
        price_edit_text.text.clear()
    }

    override fun deleteBooking(guestPosition: Int) {
        deleteItem(guestPosition)
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


}
