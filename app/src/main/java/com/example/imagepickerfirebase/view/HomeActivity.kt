package com.example.imagepickerfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagepickerfirebase.adapter.BooksAdapter
import com.example.imagepickerfirebase.databinding.ActivityHomeBinding
import com.example.imagepickerfirebase.model.Books
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var database : DatabaseReference
    private lateinit var rvHome : RecyclerView

    private lateinit var listBooks : ArrayList<Books>
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

        rvHome = binding.rvHome
        rvHome.setHasFixedSize(true)
        rvHome.layoutManager = LinearLayoutManager(this)

        listBooks = arrayListOf()

        getDataBooks()
    }

//    private fun getDataBooks() {
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
////                val post = dataSnapshot.getValue<Books>()
////                Toast.makeText(baseContext, "Got data value $post", Toast.LENGTH_SHORT).show()
//                // ...
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        database.addValueEventListener(postListener)
//    }

    private fun getDataBooks() {
        database.child("Books").get().addOnSuccessListener {

            val dataSnap = it
//            Log.i("firebase", "Got value ${it.value}")
            if (dataSnap != null){
                for (booksSnap in dataSnap.children){
                    val booksData =booksSnap.getValue(Books::class.java)
                    listBooks.add(booksData!!)
                }
                val mAdapter = BooksAdapter(listBooks)
                rvHome.adapter = mAdapter

                mAdapter.setOnItemClickListener(object : BooksAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {

                        val intent = Intent(this@HomeActivity, PdfViewActivity::class.java)
                        //put Extra
                        intent.putExtra("booksUrl", listBooks[position].booksUrl)
                        Log.d("HomeActivity", "listbook${listBooks[position].booksUrl}")


                        startActivity(intent)
                    }

                })
            }

            Toast.makeText(baseContext, "Get Data Berhasil  ${it.value}", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }




}