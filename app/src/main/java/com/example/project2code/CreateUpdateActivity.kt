package com.example.project2code

/*
 * CS3013 - Mobile App Dev. - Summer 2022
 * Instructor: Thyago Mota
 * Student(s): Adam Prieto and Bishnu Bhusal
 * Description: App 02 - CreateUpdateActivity (controller) class
 */

import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.util.*
import java.text.SimpleDateFormat

class CreateUpdateActivity : AppCompatActivity(), View.OnClickListener {

    val dateFormat = SimpleDateFormat("MM/dd/yyyy")

    var op = CREATE_OP
    var id = 0
    lateinit var db: SQLiteDatabase
    lateinit var edtDescription: EditText
    lateinit var edtCreationDate: EditText
    lateinit var edtUpdatedDate: EditText
    lateinit var spnStatus: Spinner

    companion object {
        const val CREATE_OP = 0
        const val UPDATE_OP = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update)

        // TODOd #8: get references to the view objects
        edtDescription = findViewById(R.id.edtTask)
        edtCreationDate = findViewById(R.id.edtCreationDate)
        edtUpdatedDate = findViewById(R.id.edtLastUpdated)






        // Send email asking whether or not we actually need a spinner in the project.
        // TODO #9: define the spinner's adapter as an ArrayAdapter of String
        //spnCategory.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Item.CATEGORY_DESCRIPTIONS)





        // TODOd #10: get a reference to the "CREATE/UPDATE" button and sets its listener
        val btnCreateUpdate: Button = findViewById(R.id.btnCreateEvent)
        btnCreateUpdate.setOnClickListener(this)






        // TODOd #11: get a "writable" db connection
        val dbHelper = DBHelper(this)
        db = dbHelper.writableDatabase


        op = intent.getIntExtra("op", CREATE_OP)

        // TODO #12: set the button's text to "CREATE"; make sure the spinner's selection is Item.SCHEDULED and the spinner is not enabled
        if (op == CREATE_OP) {
            btnCreateUpdate.text = "CREATE"



        } // End if

        // TODO #13: set the button's text to "UPDATE"; extract the item's id from the intent; use retrieveItem to retrieve the item's info; use the info to update the description and status view components
        else {

            btnCreateUpdate.text = "UPDATE"
            val id = intent.getStringExtra("id") ?: ""
            edtDescription.setText(id)
            edtDescription.isEnabled = false



        }
    }

    // TODO #14: return the item based on the given id
    // this function should query the database for the bucket list item identified by the given id; an item object should be returned
    fun retrieveItem(id: Int): Item {

        val cursor = db.query(
            "items",
            null,
            "id = \"${id}\"",
            null,
            null,
            null,
            null) // End cursor

        with (cursor) {
            cursor.moveToNext()
            val description = cursor.getString(1).toString()
            val creationDate = Date(2)
            val updateDate     = Date(3)
            val status = getInt(4)
            val item = Item(id, description, creationDate, updateDate, status)
            return item
        } // End with

    } // End retrieveItem

    override fun onClick(view: View?) {

        // TODO #15: add a new item to the bucket list based on the information provided by the user
        // both created_date and update_date should be set to current's date (use ISO format)
        // status should be set to Item.SCHEDULED

        val taskName: EditText = findViewById(R.id.edtTask)
        val creationDate: EditText = findViewById(R.id.edtCreationDate)
        val updatedDate: EditText = findViewById(R.id.edtLastUpdated)





        if (op == CREATE_OP)
        {
            try
            {
                db.execSQL(
                    """
                        INSERT INTO items VALUES
                            ("$taskName", ${creationDate}, ${updatedDate})
                        """
                          )
                Toast.makeText(
                    this,
                    "New bucket list entry successfully created!",
                    Toast.LENGTH_SHORT
                              ).show()
            } // End try
            catch (ex: Exception)
            {
                print(ex.toString())
                Toast.makeText(
                    this,
                    "Exception when trying to create a new entry!",
                    Toast.LENGTH_SHORT
                              ).show()
            } // End catch
        } // End if
        // TODO #16: update the item identified by "id"
        // update_date should be set to current's date (use ISO format)
        else
        {
            try
            {
                db.execSQL(
                    """
                        UPDATE items SET
                            description = ${taskName},
                            creationDate = ${creationDate}, 
                            updatedDate = "${updatedDate}" 
                        WHERE id = "${id}"
                    """
                          )
                Toast.makeText(
                    this,
                    "New bucket list entry successfully created!",
                    Toast.LENGTH_SHORT
                              ).show()
            } // End try
            catch (ex: Exception)
            {
                print(ex.toString())
                Toast.makeText(
                    this,
                    "Exception when trying to create a new entry!",
                    Toast.LENGTH_SHORT
                              ).show()
            } // End catch

        }
        finish()
    }
}