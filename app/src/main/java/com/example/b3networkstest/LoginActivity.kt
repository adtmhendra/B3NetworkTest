package com.example.b3networkstest

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.b3networkstest.Helper.startActivity
import com.example.b3networkstest.Helper.toast
import com.example.b3networkstest.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding: ActivityLoginBinding
private lateinit var auth: FirebaseAuth

private lateinit var userEmail: String
private lateinit var userPassword: String

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userEmail = binding.edtEmail.text.toString()
                userPassword = binding.edtPassword.text.toString()

                with(binding.btnLogin) {
                    isEnabled = userEmail.isNotEmpty() && userPassword.isNotEmpty()
                    setOnClickListener { login() }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        with(binding) {
            edtEmail.addTextChangedListener(textWatcher)
            edtPassword.addTextChangedListener(textWatcher)

            tvRegister.setOnClickListener { startActivity(RegisterActivity::class.java) }

            btnGenerate.setOnClickListener {
                edtEmail.setText("admin@test.com")
                edtPassword.setText("adminadmin")
                btnLogin.isEnabled = true
            }
        }
    }

    private fun login() {
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast(resources.getString(R.string.text_login_success))
                    startActivity(MainActivity::class.java)
                    finish()
                } else toast(resources.getString(R.string.text_login_failed))
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}