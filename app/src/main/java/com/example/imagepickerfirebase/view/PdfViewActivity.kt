package com.example.imagepickerfirebase.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.imagepickerfirebase.R
import com.example.imagepickerfirebase.databinding.ActivityPdfViewBinding
import com.mindev.mindev_pdfviewer.MindevPDFViewer
import com.mindev.mindev_pdfviewer.PdfScope

class PdfViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPdfViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = "https://kotlinlang.org/docs/kotlin-reference.pdf"
        binding.pdf.initializePDFDownloader(url, statusListener)
        lifecycle.addObserver(PdfScope())
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