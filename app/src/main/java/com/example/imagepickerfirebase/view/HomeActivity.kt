package com.example.imagepickerfirebase.view


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.imagepickerfirebase.R
import com.example.imagepickerfirebase.adapter.BooksAdapter
import com.example.imagepickerfirebase.databinding.ActivityHomeBinding
import com.example.imagepickerfirebase.model.Books
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Locale


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: DatabaseReference
    private lateinit var rvHome: RecyclerView
    private lateinit var mAdapter: BooksAdapter
    private lateinit var listBooks: ArrayList<Books>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToProfile.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }
        binding.btnToAddFile.setOnClickListener {
            startActivity(Intent(this, AddBooksActivity::class.java))
        }

        val listSlide = ArrayList<SlideModel>()

        listSlide.add(SlideModel(R.drawable.photo1))
        listSlide.add(SlideModel(R.drawable.photo2))
        listSlide.add(SlideModel(R.drawable.photo3))

        binding.imageSlider.setImageList(listSlide, ScaleTypes.CENTER_CROP)

        rvHome = binding.rvHome
        rvHome.setHasFixedSize(true)
        rvHome.layoutManager = LinearLayoutManager(this)

        listBooks = arrayListOf()
        getDataBooks()
    }
    private fun getDataBooks() {

        database.child("Books").get().addOnSuccessListener {
            binding.progressBar.visibility = View.VISIBLE
            val dataSnap = it
//            Log.i("firebase", "Got value ${it.value}")
            if (dataSnap != null) {
                binding.progressBar.visibility = View.GONE
                for (booksSnap in dataSnap.children) {
                    val booksData = booksSnap.getValue(Books::class.java)
                    listBooks.add(booksData!!)
                }
                mAdapter = BooksAdapter(listBooks, this)
                rvHome.adapter = mAdapter

                mAdapter.setOnItemClickListener(object : BooksAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {

                        val intent = Intent(this@HomeActivity, PdfViewActivity::class.java)
                        //put Extra
                        intent.putExtra("booksUrl", listBooks[position].booksUrl)
                        Log.d("HomeActivity", "listbook${listBooks[position].booksUrl}")

                        startActivity(intent)
                    }

                })

                binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        searchList(p0!!)
                        Log.d("HomeActivity", "ini adalah i $p0")
                        return true
                    }

                })
            }
            val emailUser = Firebase.auth.currentUser?.email
            if (emailUser != null) {
                Toast.makeText(baseContext, "Selamat datang $emailUser", Toast.LENGTH_SHORT).show()
            } else {
                binding.btnToAddFile.visibility = View.GONE
            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun searchList(text: String) {
        val filteredList = ArrayList<Books>()
        for (i in listBooks) {
            if (i.booksTitle?.capitalize(Locale.ROOT)!!.contains(text)) {
                filteredList.add(i)
                Log.d("HomeActivity", "ini adalah list $listBooks")
            }
            if (filteredList.isEmpty()) {
//                Toast.makeText(baseContext, "No data found", Toast.LENGTH_SHORT).show()
            } else {
                mAdapter.setFilteredList(filteredList)
            }
        }
    }


}