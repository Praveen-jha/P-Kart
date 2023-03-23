package com.example.p_kart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.p_kart.MainActivity
import com.example.p_kart.R
import com.example.p_kart.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.verifyOtp.setOnClickListener {
            if (binding.UserOtp.text!!.isEmpty()){
                Toast.makeText(this, "Please provide OTP", Toast.LENGTH_SHORT).show()

            }else{
                verifyUser(binding.UserOtp.text.toString())
            }
        }
    }

    private fun verifyUser(otp: String) {
        val verificationId = intent.getStringExtra("verificationId")
        val credential = PhoneAuthProvider.getCredential(verificationId!! , otp)
        signInWithPhoneAuthCredential(credential)


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    val preference = this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preference.edit()

                    editor.putString("number",intent.getStringExtra("number")!!)

                    editor.apply()
                    
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                    val user = task.result?.user
                } else {

                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }
}