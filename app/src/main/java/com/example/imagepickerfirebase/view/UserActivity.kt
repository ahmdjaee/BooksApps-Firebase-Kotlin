package com.example.imagepickerfirebase.view

import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.imagepickerfirebase.R
import com.example.imagepickerfirebase.databinding.ActivityUserBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding
    private lateinit var imagesRef : StorageReference
    private lateinit var  storage : FirebaseStorage
    private var fileUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = Firebase.storage
        val storageRef = storage.reference
        imagesRef = storageRef.child("images/profile")
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
            fileUri = uri
            binding.profileImage.setImageURI(fileUri)
            if ( fileUri != null) uploadImageUri()

        }

        storageRef.child("images/profile").downloadUrl.addOnSuccessListener {
            // Got the download URL for 'users/me/profile.png'
            Glide.with(this).load(it).into(binding.profileImage);
            Log.d("MainActivity", "ImageUrl : $it")
        }

        binding.tvEditProfile.setOnClickListener {
            galleryLauncher.launch("image/*")

        }
//        val user = Firebase.auth.currentUser
//        user?.let {
//            // Name, email address, and profile photo Url
//            val name = it.displayName
//            val email = it.email
//            val photoUrl = it.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = it.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getIdToken() instead.
//            val uid = it.uid
//
//            binding.tvEmail.setText(name.toString())
//            binding.tvEmail.setText(email.toString())
//            binding.tvEmail.setText(email.toString())
//        }
//
//        val ONE_MEGABYTE: Long = 1024 * 1024
//        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
//            // Data for "images/island.jpg" is returned, use this as needed
//        }.addOnFailureListener {
//            // Handle any errors
//        }
    }
    private fun uploadImageUri(){
        // Get the data from an ImageView as bytes
        binding.profileImage.isDrawingCacheEnabled = true
        binding.profileImage.buildDrawingCache()
        val bitmap = (binding.profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,     100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(baseContext, "Photo Profile gagal dirubah", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(baseContext, "" +
                    "Photo Profile berhasil dirubah", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "Ini adalah $taskSnapshot")
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }
}