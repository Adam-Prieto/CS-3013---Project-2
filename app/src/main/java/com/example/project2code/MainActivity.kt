package com.example.project2code

/*
 * CS3013 - Mobile App Dev. - Summer 2022
 * Instructor: Thyago Mota
 * Student(s): Adam Prieto and Bishnu Bhusal
 * Description: App 02 - MainActivity (controller) class
 */

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import android.annotation.SuppressLint
import android.widget.*
import com.google.android.material.textfield.*

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    val dateFormat = SimpleDateFormat("MM/dd/yyyy")


    lateinit var recyclerView: RecyclerView
    lateinit var dbHelper: DBHelper

    // TODOd #1: create the ItemHolder inner class
    // a holder object saves the references to view components of a recycler view item
    private inner class ItemHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtID: TextView = view.findViewById(R.id.txtID)
        val txtDescription: TextView = view.findViewById(R.id.txtDescription)
        val txtCreationDate: TextView = view.findViewById(R.id.txtCreatedDate)
        val txtLastUpdated: TextView = view.findViewById(R.id.txtLastUpdated)

    }

    // TODOd #2: create the ItemAdapter inner class
    // an item adapter binds items from a list to holder objects in a recycler view
    private inner class ItemAdapter(var bucketlist: List<Item>, var onClickListener: View.OnClickListener, var onLongClickListener: View.OnLongClickListener): RecyclerView.Adapter<ItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ItemHolder(view, )
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            val item = bucketlist[position]
            holder.txtID.text = item.id.toInt().toString()
            holder.txtDescription.text = item.description
            holder.txtCreationDate.text = dateFormat.format(item.creationDate)
            holder.txtLastUpdated.text = dateFormat.format(item.updateDate)

            holder.itemView.setOnClickListener(onClickListener)
        }

        override fun getItemCount(): Int {
            return bucketlist.size
        }
    }


    // TODOd #3: populate the recycler view
    // this function should query the database for all of the bucket list items; then use the list to update the recycler view's adapter
    // don't forget to call "sort()" on your list so the items are displayed in the correct order
    fun populateRecyclerView() {

        val db = dbHelper.readableDatabase
        val items = mutableListOf<Item>()
        val cursor = db.query(
            "items",
            null,
            null,
            null,
            null,
            null,
            null)

        with (cursor) {
            while (moveToNext()) {
                val id    = getInt(0)
                val description = getString(1)
                val creationDate = Date(2)
                val updateDate = Date(3)
                val status = getInt(4)
                val item = Item(id, description, creationDate, updateDate, status)
                items.add(item)
                items.sort()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODOd #4: create and populate the recycler view
        dbHelper = DBHelper(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        populateRecyclerView()




        // TODOd #5: initialize the floating action button
        val fabCreate: FloatingActionButton = findViewById(R.id.fabCreate)
        fabCreate.setOnClickListener {
            // calls CreateUpdateActivity for create
            val intent = Intent(this, CreateUpdateActivity::class.java)
            intent.putExtra("op", CreateUpdateActivity.CREATE_OP)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        populateRecyclerView()
    }

    // TODOd #6: call CreateUpdateActivity for update
    // don't forget to pass the item's id to the CreateUpdateActivity via the intent
    override fun onClick(view: View?) {
        if (view != null) {

            val id = view.findViewById<TextView>(R.id.txtID).text
            val intent = Intent(this, CreateUpdateActivity::class.java)
            intent.putExtra("op", CreateUpdateActivity.UPDATE_OP)
            intent.putExtra("ID", id)
            startActivity(intent)

        }
    }

    // TODO #7: delete the long tapped item after a yes/no confirmation dialog
    override fun onLongClick(view: View?): Boolean {

        class MyDialogInterfaceListener(val id: Int): DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, which: Int) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    try {

                    } catch (ex: Exception) {






                    }
                }
            }
        }

        if (view != null) {

            return true
        }
        return false
    } // End onLongClick

} // End MainActivity class