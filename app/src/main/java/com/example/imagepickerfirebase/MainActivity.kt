package com.example.imagepickerfirebase

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.imagepickerfirebase.databinding.ActivityMainBinding
import com.example.imagepickerfirebase.model.Menu
import com.example.imagepickerfirebase.view.PdfViewActivity
import com.example.imagepickerfirebase.view.UserActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.Random
import java.util.UUID
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private var pdfUri : Uri? = null

    private lateinit var  storage : FirebaseStorage
    private lateinit var imagesRef : StorageReference

    private lateinit var binding: ActivityMainBinding
    private val PICK_PDF_FILE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)

        storage = Firebase.storage
        val storageRef = storage.reference

//        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {uri : Uri? ->
//            pdfUri = uri
//            Log.d("MainActivity", "file uri $pdf")
//        }

        setContentView(binding.root)
        binding.btnAddPdf.setOnClickListener {
            openFile()
        }

        binding.btnToProfile.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }

        binding.cardView.setOnClickListener {
            startActivity(Intent(this, PdfViewActivity::class.java))
        }

    }



    fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
//            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(intent, PICK_PDF_FILE)


    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == PICK_PDF_FILE
            && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->
            pdfUri = uri
            binding.textView.text = pdfUri.toString()
                Log.d("MainActivity", "ini adalah uri text $pdfUri")
                // Perform operations on the document using its URI.

            }
        }
    }



}