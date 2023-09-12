package com.example.imagepickerfirebase.view

import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.imagepickerfirebase.R
import com.example.imagepickerfirebase.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var imagesRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private var user: FirebaseUser? = null
    private var fileUri: Uri? = null
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayData()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                fileUri = uri
                binding.profileImage.setImageURI(fileUri)
                if (fileUri != null) uploadImageUri()
            }

        binding.tvEditProfile.setOnClickListener {
            if (user != null) galleryLauncher.launch("image/jpeg")
            else Toast.makeText(baseContext, "Anda belum melakukan login", Toast.LENGTH_SHORT).show()
        }

        binding.progressBar4.visibility = View.GONE
        binding.btnLogout.setOnClickListener {
            logout()
        }

    }
    private fun logout() {
        AlertDialog.Builder(this).setTitle("Logout").setMessage("Are you sure want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                binding.progressBar4.visibility = View.VISIBLE
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(baseContext, "Logout success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()

            }.setNegativeButton("No") { _, _ ->
            }.create().show()
    }

    private fun displayData() {
        user = Firebase.auth.currentUser
        user?.let {
            val email = it.email
            val uid = it.uid

            storage = Firebase.storage
            val storageRef = storage.reference
            imagesRef = storageRef.child("Users/$uid")

            storageRef.child("Users/$uid").downloadUrl.addOnSuccessListener {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(this).load(it).into(binding.profileImage);
                Log.d("MainActivity", "ImageUrl : $it")
            }

            binding.tvName.setText(uid)
            binding.tvEmail.setText(email)
        }
    }

    private fun uploadImageUri() {
        // Get the data from an ImageView as bytes
        binding.profileImage.isDrawingCacheEnabled = true
        binding.profileImage.buildDrawingCache()
        val bitmap = (binding.profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(baseContext, "Photo Profile gagal dirubah", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(
                baseContext, "" + "Photo Profile berhasil dirubah", Toast.LENGTH_SHORT
            ).show()
            Log.d("MainActivity", "Ini adalah $taskSnapshot")
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        }

    }
}