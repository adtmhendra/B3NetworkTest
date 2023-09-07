package com.example.b3networkstest

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.b3networkstest.Helper.startActivity
import com.example.b3networkstest.Helper.toast
import com.example.b3networkstest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


private lateinit var binding: ActivityMainBinding
private lateinit var auth: FirebaseAuth

private lateinit var viewModel: MainViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CALL = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initResponse()

        checkInputTelephone()

        with(binding) {
            val telephone = edtTelephone.text.toString()
            val message = edtMessage.text.toString()

            btnCall.setOnClickListener { callUser(telephone, message) }
            btnLogout.setOnClickListener {
                auth.signOut()
                startActivity(LoginActivity::class.java)
                finish()
            }

        }
    }

    private fun initResponse() {
        initResponseCall()
    }

    private fun callUser(telephone: String, message: String = "") {
        getDataAsync(telephone, message)
    }

    private fun getDataAsync(destination: String, message: String) {
        viewModel.getDataAsync(destination, message)
    }

    private fun initResponseCall() {
        viewModel.response.observe(this) { response ->
            when (response) {
                is Resource.LOADING -> showLoading(response.state)

                is Resource.SUCCESS -> {
                    when (response.data) {
                        200, 201 -> makePhoneCall()
                        else -> toast(resources.getString(R.string.text_something_went_wrong))
                    }
                }

                is Resource.FAILURE -> toast(resources.getString(R.string.text_something_went_wrong))
                else -> toast(resources.getString(R.string.text_something_went_wrong))
            }
        }
    }

    private fun checkInputTelephone() {
        binding.edtTelephone.doOnTextChanged { text, _, _, _ ->
            with(binding.txtTelephone) {
                if (text?.length!! < 5) {
                    isErrorEnabled = true
                    error = resources.getString(R.string.text_number)
                    binding.btnCall.isEnabled = false
                } else {
                    isErrorEnabled = false
                    error = null
                    binding.btnCall.isEnabled = true
                }

                if (text.isEmpty()) {
                    isErrorEnabled = true
                    error = resources.getString(R.string.text_data_not_null)
                    binding.btnCall.isEnabled = false
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall()
                } else {
                    Snackbar.make(
                        binding.btnCall,
                        resources.getString(R.string.text_call_permission_required),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(resources.getString(R.string.action_settings)) {
                        showAppInfo()
                    }.show()
                }
            }
        }
    }

    private fun showAppInfo() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
            startActivity(intent)
        }
    }

    private fun makePhoneCall() {
        val number = binding.edtTelephone.text.toString()
        when {
            ContextCompat.checkSelfPermission(
                this@MainActivity, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL
                )
            }

            else -> {
                val dial = "tel:$number"
                val intent = Intent(Intent.ACTION_CALL, Uri.parse(dial))
                startActivity(intent)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.progressCircular.visibility = View.VISIBLE
            binding.textWelcoming.visibility = View.INVISIBLE
            binding.textWelcomingDesc.visibility = View.INVISIBLE
            binding.txtTelephone.visibility = View.INVISIBLE
            binding.txtMessage.visibility = View.INVISIBLE
            binding.btnCall.visibility = View.INVISIBLE
        } else {
            binding.progressCircular.visibility = View.INVISIBLE
            binding.textWelcoming.visibility = View.VISIBLE
            binding.textWelcomingDesc.visibility = View.VISIBLE
            binding.txtTelephone.visibility = View.VISIBLE
            binding.txtMessage.visibility = View.VISIBLE
            binding.btnCall.visibility = View.VISIBLE
        }
    }
}