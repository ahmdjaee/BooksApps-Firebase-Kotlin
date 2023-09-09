package com.example.imagepickerfirebase.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.imagepickerfirebase.databinding.ActivityAddBooksBinding
import com.example.imagepickerfirebase.model.Books
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class AddBooksActivity : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var pdfRef: StorageReference
    private var pdfUri: Uri? = null

    // Configure realtime database
    private lateinit var database: DatabaseReference

    private lateinit var binding: ActivityAddBooksBinding
    private val PICK_PDF_FILE = 2
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = Firebase.storage

        // Configure realtime database
        database = Firebase.database.reference

        binding.btnAddPdf.setOnClickListener {
            openFile()
        }

        binding.btnSaveData.setOnClickListener {

            createBooksData()
        }

    }

    private fun createBooksData() {
        val id = database.push().key
        val title = binding.etTitle.text.toString()
        val author = binding.etAuthor.text.toString()
        val books = Books(id, title, author, "File/${pdfUri?.lastPathSegment}")
        if (title.isEmpty() && author.isEmpty() && binding.btnAddPdf == null){
            binding.etTitle.error = "Title Required"
            binding.etAuthor.error = "Author Required"
            binding.btnAddPdf.error = "Books File Required"
        }else if (title.isNotEmpty() && author.isNotEmpty() &&  pdfUri != null){
            database.child("Books").child("$id").setValue(books)
                .addOnCompleteListener {
                    // Clear Teks jika data berhasil di upload
                    binding.etTitle.text?.clear()
                    binding.etAuthor.text?.clear()
                    binding.btnAddPdf.setText("Insert Your File Here")
                    uploadPdf()
                }
        }else{
            Toast.makeText(baseContext, "Data Harus Lengkap", Toast.LENGTH_SHORT).show()
        }




    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, PICK_PDF_FILE)
    }

//    private fun openCamera (){
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        } catch (e: ActivityNotFoundException) {
//            // display error state to the user
//        }
//    }

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->
                pdfUri = uri
                binding.btnAddPdf.text = pdfUri.toString()

                Log.d("MainActivity", "ini adalah uri text $resultCode")
                // Perform operations on the document using its URI.

            }
        }
    }

    private fun uploadPdf() {
        val storageRef = storage.reference
        pdfRef = storageRef.child("File/${pdfUri?.lastPathSegment}")
        pdfUri?.let {
            pdfRef.putFile(it).addOnSuccessListener {
                Toast.makeText(baseContext, "Upload pdf berhasil", Toast.LENGTH_SHORT).show()
            }
        }

    }


}