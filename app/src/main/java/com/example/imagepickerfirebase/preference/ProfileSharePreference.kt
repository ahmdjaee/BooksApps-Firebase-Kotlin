package com.example.imagepickerfirebase.preference

import android.content.Context
import com.example.imagepickerfirebase.model.Profile

class ProfileSharePreference(context: Context) {

    companion object{
        private const val PREF_NAME = "profile_preference"

        private const val NAME = "name"
    }

   private val sharedPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

    fun setProfile (value : Profile){
        val editor = sharedPref.edit()
        editor.putString(NAME, value.nama)
        editor.apply()
    }

    fun getProfile () : Profile{
        val data = Profile()
        data.nama = sharedPref.getString(NAME, "")

        return data
    }

}