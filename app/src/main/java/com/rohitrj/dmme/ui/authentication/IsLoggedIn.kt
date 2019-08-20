package com.rohitrj.dmme.ui.authentication

import android.content.Context
import android.content.SharedPreferences

const val FILE_NAME = "newFile"
const val EMAIL = "email"

class IsLoggedIn{

    fun isLoggedIn(context: Context): Boolean{
        val sharedPreferences: SharedPreferences =  context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val email = sharedPreferences.getString(EMAIL,null)
        return !email.isNullOrEmpty()
    }

    fun loginUser(email: String, context: Context){
        val sharedPreferences: SharedPreferences =  context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString(EMAIL,email)
        editor.apply()
        editor.commit()
    }

    fun logOut(context: Context){
        val sharedPreferences: SharedPreferences =  context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString(EMAIL,null)
        editor.apply()
        editor.commit()
    }

    fun getEmailID(context: Context): String{
        val sharedPreferences: SharedPreferences =  context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val email = sharedPreferences.getString(EMAIL,null)
        return email.toString()
    }

}