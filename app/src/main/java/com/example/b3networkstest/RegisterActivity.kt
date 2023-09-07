package com.example.b3networkstest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.b3networkstest.Helper.startActivity
import com.example.b3networkstest.Helper.toast
import com.example.b3networkstest.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding: ActivityRegisterBinding
private lateinit var auth: FirebaseAuth

private lateinit var userEmail: String
private lateinit var userPassword: String

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userEmail = binding.edtEmail.text.toString()
                userPassword = binding.edtPassword.text.toString()

                with(binding.btnRegister) {
                    isEnabled = userEmail.isNotEmpty() && userPassword.isNotEmpty()
                    setOnClickListener { register() }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        with(binding) {
            edtEmail.addTextChangedListener(textWatcher)
            edtPassword.addTextChangedListener(textWatcher)

            btnBack.setOnClickListener { onBackPressed() }
        }
    }

    private fun register() {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.signOut()
                    toast(resources.getString(R.string.text_new_account))
                    startActivity(LoginActivity::class.java)
                    finish()
                } else toast(resources.getString(R.string.text_register_failed))
            }
    }
}