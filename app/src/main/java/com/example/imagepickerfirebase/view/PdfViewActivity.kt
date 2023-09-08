package com.example.imagepickerfirebase.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.imagepickerfirebase.R
import com.example.imagepickerfirebase.databinding.ActivityPdfViewBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.mindev.mindev_pdfviewer.MindevPDFViewer
import com.mindev.mindev_pdfviewer.PdfScope

class PdfViewActivity : AppCompatActivity() {
//testtt
    private lateinit var binding : ActivityPdfViewBinding
    private lateinit var pdfRef : StorageReference
    private lateinit var storage : FirebaseStorage
    private var url : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = Firebase.storage
        val storageRef = storage.reference

        val pdfUri = intent.getStringExtra("filePdf")

        pdfRef = storageRef.child("$pdfUri")
        Log.d("uri", "ini adalah pdfUri $pdfUri")

        storageRef.child("File/$pdfUri").downloadUrl.addOnSuccessListener {
            // Got the download URL for 'users/me/profile.png'
            url = it
            binding.pdf.initializePDFDownloader("$url", statusListener)
            lifecycle.addObserver(PdfScope())
            Toast.makeText(baseContext, "Pdf Berhasil Di Unduh", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            // Handle any errors
        }


    }

    private val statusListener = object : MindevPDFViewer.MindevViewerStatusListener {
        override fun onStartDownload() {
            binding.pbLoading.visibility = View.VISIBLE
        }

        override fun onPageChanged(position: Int, total: Int) {
            binding.tvPages.text = "${position + 1}/$total"
        }

        override fun onProgressDownload(currentStatus: Int) {
            binding.pbLoading.visibility = View.VISIBLE
        }

        override fun onSuccessDownLoad(path: String) {
            binding.pbLoading.visibility = View.GONE
            binding.pdf.fileInit(path)
        }

        override fun onFail(error: Throwable) {
            Toast.makeText(baseContext, error.toString(), Toast.LENGTH_SHORT).show()
        }

        override fun unsupportedDevice() {
            Toast.makeText(baseContext, "Device not supported", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        binding.pdf.pdfRendererCore?.clear()
        super.onDestroy()
    }
}