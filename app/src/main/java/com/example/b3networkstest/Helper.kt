package com.example.b3networkstest

import android.content.Context
import android.content.Intent
import android.widget.Toast

object Helper {

    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.startActivity(moveTo: Class<*>) {
        val intent = Intent(this, moveTo)
        startActivity(intent)
    }
}