package com.example.homework13_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.homework13_1.db.FilmDataO

class EditBaseActivity : AppCompatActivity() {

    lateinit var filmData2: FilmDataO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_base)

        val editName = findViewById<EditText>(R.id.etName)
        val editAuthor = findViewById<EditText>(R.id.etAuthor)
        val editYear = findViewById<EditText>(R.id.etYear)
        val editDescription = findViewById<EditText>(R.id.etDescription)


        findViewById<Button>(R.id.btADD).setOnClickListener {
            if (editName.text.toString().isNotEmpty() && editAuthor.text.toString().isNotEmpty()
                    && editYear.text.toString().isNotEmpty() && editDescription.text.toString().isNotEmpty()) {
                Toast.makeText(this, "You information added", Toast.LENGTH_LONG).show()

                val intent = Intent()
                intent.putExtra("filmName", editName.text.toString())
                intent.putExtra("filmAuthor", editAuthor.text.toString())
                intent.putExtra("filmYear", editYear.text.toString())
                intent.putExtra("filmDescription", editDescription.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        findViewById<Button>(R.id.btClear).setOnClickListener {

            Toast.makeText(this, "You base clear", Toast.LENGTH_LONG).show()

            val intent = Intent()
            intent.putExtra("baseClear", "Yes")
            setResult(RESULT_OK, intent)
            finish()

        }


    }
}