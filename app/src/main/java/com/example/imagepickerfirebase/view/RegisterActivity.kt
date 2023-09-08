package com.example.imagepickerfirebase.view

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.imagepickerfirebase.databinding.ActivityRegisterBinding
import com.example.imagepickerfirebase.model.Profile
import com.example.imagepickerfirebase.preference.ProfileSharePreference
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var profile : Profile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        val profilePref = ProfileSharePreference(baseContext)

        profile = profilePref.getProfile()

        profile.nama?.let {
            binding.etNama.setText(profile.nama)
        }

        binding.btnRegister.setOnClickListener {
            var email = binding.etEmailRegister?.text.toString()
            var password = binding.etPasswordRegister?.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                binding.etEmailRegister.error = "Email harus diisi"
                binding.etPasswordRegister.error = "Password harus diisi"

            } else {
                createRegister(email, password)
            }

            profile.nama = binding.etNama.text.toString()
            profilePref.setProfile(profile)
        }

        setContentView(binding.root)
    }

    private fun createRegister(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Register Telah Berhasil", Toast.LENGTH_SHORT)
                        .show()

                    var nama = binding.etNama?.text.toString()

                    val intent = Intent(this, LoginActivity::class.java)

                    intent.putExtra("nama", nama)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            reload()
//        }
//    }
//
//    private fun reload() {
//        TODO("Not yet implemented")
//    }
}