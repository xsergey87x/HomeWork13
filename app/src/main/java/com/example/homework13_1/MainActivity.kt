package com.example.homework13_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.homework13_1.db.AppBase
import com.example.homework13_1.db.FilmDataO
import com.example.homework13_1.db.Films
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var filmData: FilmDataO
    private var newList: ArrayList<Films> = arrayListOf<Films>()
    private var launcher: ActivityResultLauncher<Intent>? = null
    lateinit var films: ArrayList<Films>
    var tempFilms: ArrayList<Films> = arrayListOf<Films>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.idRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, 1))
        val searchV = findViewById<SearchView>(R.id.searchViewID)

        searchV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                lifecycleScope.launch(Dispatchers.IO) {

                    films = filmData.getAll() as ArrayList<Films>

                    val searchText = newText!!.toLowerCase(Locale.getDefault())

                    if (searchText.isNotEmpty()) {

                        tempFilms.clear()

                        films.forEach {

                            if (it.filmName?.toLowerCase(Locale.getDefault())!!.contains(searchText)) {

                                tempFilms.add(Films(it.filmName, it.filmAuthor, it.filmYear, it.filmDescription))

                            }
                        }
                    }

                    withContext(Dispatchers.Main)
                    {
                        recyclerView.adapter = RecyclerAdapter(tempFilms)
                    }
                }
                return false
            }

        })

        searchV.setOnCloseListener(object : androidx.appcompat.widget.SearchView.OnCloseListener, SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                recyclerView.adapter = RecyclerAdapter(films)
                return false
            }
        })

        val db = Room.databaseBuilder(
                applicationContext,
                AppBase::class.java, "my-app-database"
        ).build()
        filmData = db.filmDataO()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val nameF = result.data?.getStringExtra("filmName")
                val authorF = result.data?.getStringExtra("filmAuthor")
                val yearF = result.data?.getStringExtra("filmYear")
                val descriptionF = result.data?.getStringExtra("filmDescription")
                val clearF = result.data?.getStringExtra("baseClear")

                if (nameF != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val newFilm = Films(nameF, authorF, yearF, descriptionF)

                        filmData.insertAll(newFilm)
                        val oldFilm = filmData.getAll() as ArrayList<Films>

                        withContext(Dispatchers.Main)
                        {
                            recyclerView.adapter = RecyclerAdapter(oldFilm)
                        }
                    }
                }

                if ((clearF != null) && (clearF == "Yes")) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        filmData.removeAll()
                        val oldFilm = filmData.getAll() as ArrayList<Films>
                        withContext(Dispatchers.Main)
                        {
                            recyclerView.adapter = RecyclerAdapter(oldFilm)
                        }
                    }
                }

            }
        }

        lifecycleScope.launch(Dispatchers.IO) {

            films = filmData.getAll() as ArrayList<Films>

            if (films.isNullOrEmpty()) {
                filmData.insertAll(ItemsDB[0], ItemsDB[1], ItemsDB[2], ItemsDB[3], ItemsDB[4],
                        ItemsDB[5], ItemsDB[6], ItemsDB[7], ItemsDB[8], ItemsDB[9])
                films = filmData.getAll() as ArrayList<Films>
            }
            withContext(Dispatchers.Main)
            {
                recyclerView.adapter = RecyclerAdapter(films)
            }
        }

        findViewById<Button>(R.id.btEdit).setOnClickListener {

            launcher?.launch(Intent(this, EditBaseActivity::class.java))
        }
    }
}

public class RecyclerAdapter(private val items: ArrayList<Films>) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val filmNameV: TextView = view.findViewById(R.id.textName)
        val filmYearV: TextView = view.findViewById(R.id.textYear)
        val filmAuthorV: TextView = view.findViewById(R.id.textAuthor)
        val filmDescriptionV: TextView = view.findViewById(R.id.textDescription)

    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycle_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        items[position].apply {
            viewHolder.filmNameV.text = filmName
            viewHolder.filmAuthorV.text = filmAuthor
            viewHolder.filmYearV.text = filmYear
            viewHolder.filmDescriptionV.text = filmDescription

        }
    }

}
