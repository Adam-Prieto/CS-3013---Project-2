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
import java.util.*
import java.lang.Exception

class CreateUpdateActivity : AppCompatActivity(), View.OnClickListener
{

    var op = CREATE_OP
    var id = 0
    lateinit var db: SQLiteDatabase
    lateinit var edtDescription: EditText
    lateinit var spnStatus: Spinner

    companion object
    {
        const val CREATE_OP = 0
        const val UPDATE_OP = 1
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update)

        // TODOd #8: get references to the view objects
        edtDescription = findViewById(R.id.edtDescription)
        val spnCategory: Spinner = findViewById(R.id.spnCategory)
        spnStatus = findViewById(R.id.spnCategory)


        // TODOd #9: define the spinner's adapter as an ArrayAdapter of String
        spnStatus.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            Item.STATUS_DESCRIPTIONS)

        // TODOd #10: get a reference to the "CREATE/UPDATE" button and sets its listener
        val btnCreateUpdate: Button = findViewById(R.id.btnCreateEvent)
        btnCreateUpdate.setOnClickListener(this)

        // TODOd #11: get a "writable" db connection
        val dbHelper = DBHelper(this)
        db = dbHelper.writableDatabase
        op = intent.getIntExtra("op", CREATE_OP)

        // TODOd #12: set the button's text to "CREATE"; make sure the spinner's selection is Item.SCHEDULED and the spinner is not enabled
        if (op == CREATE_OP)
        {
            btnCreateUpdate.text = "CREATE"
            spnStatus.setSelection(Item.SCHEDULED)
            spnStatus.isEnabled = false

        } // End if
        
        // TODOd #13: set the button's text to "UPDATE";
        //  extract the item's id from the intent;
        //  use retrieveItem to retrieve the item's info;
        //  use the info to update the description and status view components
        else
        {
            btnCreateUpdate.text = "UPDATE"
            
            val id = intent.getStringExtra("id") ?: ""
            val item = retrieveItem(id.toInt())
            
            edtDescription.setText(item.description)
            edtDescription.isEnabled = false
            
            spnStatus.setSelection(item.status)
        } // End else
    } // End onCreate

    // TODOd #14: return the item based on the given id;
    //  this function should query the database for the bucket list item identified by the given id;
    //  an item object should be returned
    fun retrieveItem(id: Int): Item
    {
        val cursor = db.query(
            "bucketlist",
            arrayOf("rowid, description, creation_date, update_date, status"),
            "rowid = $id",
            null,
            null,
            null,
            null)
        
        
        with(cursor) {
            cursor.moveToNext()
            val description = getInt(1).toString()
            val creation_date = Date(2)
            val update_date = Date(3)
            val status = getInt(4)
            return Item(id, description, creation_date, update_date, status)
        }
    }

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?)
    {
        /* TODOd #15: add a new item to the bucket list based on the information provided by the user
         both created_date and update_date should be set to current's date (use ISO format)
         status should be set to Item.SCHEDULED
         */

        // Create placeholder variables
        //val id = intent.getStringExtra("id") ?: ""
        val id = findViewById<TextView>(R.id.txtId)
        val description = findViewById<EditText>(R.id.edtDescription).text.toString()
        val current = Date()
        val creationDate = DBHelper.ISO_FORMAT.format(current)
        val updatedDate = DBHelper.ISO_FORMAT.format(current)
        val status =
            findViewById<Spinner>(R.id.spnCategory).selectedItemPosition
        
        


        if (op == CREATE_OP)
        {
            try
            {
                db.execSQL("""
                        INSERT INTO bucketlist VALUES
                            ("$description", "$creationDate", "$updatedDate", $status)
                        """)
                Toast.makeText(this,
                    "New bucket list entry successfully created!",
                    Toast.LENGTH_SHORT).show()
                
                finish()
            } // End try
            catch (ex: Exception)
            {
                print(ex.toString())
                Toast.makeText(
                    this,
                    "Exception when trying to create a new bucket list!",
                    Toast.LENGTH_SHORT).show()
            } // End catch
        } // End if
        
        // TODOd #16: update the item identified by "id"
        // update_date should be set to current's date (use ISO format)
        else
        {
            try
            {
                db.execSQL(
                    """
                        UPDATE bucketlist SET
                            description = "$description",
                            creation_date = "$creationDate",
                            update_date = "$updatedDate",
                            status = $status
                        WHERE rowid = "$id"
                    """
                          )
                Toast.makeText(
                    this,
                    "Bucket list item successfully updated!",
                    Toast.LENGTH_SHORT).show()
            }
            catch (ex: Exception)
            {
                print(ex.toString())
                Toast.makeText(
                    this,
                    "Exception when trying to update the Bucket list item!",
                    Toast.LENGTH_LONG).show()
            }

        }
    }
}