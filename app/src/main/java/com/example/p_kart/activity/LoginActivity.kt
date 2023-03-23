package com.example.p_kart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.p_kart.R
import com.example.p_kart.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    private lateinit var builder : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        binding.button4.setOnClickListener {
             if (binding.UserNumber.text!!.isEmpty()){
                 Toast.makeText(this, "Please enter Phone Number", Toast.LENGTH_SHORT).show()
             }else{
                 sendOtp(binding.UserNumber.text.toString())
             }
        }
    }

    private fun sendOtp(number: String) {
         builder = AlertDialog.Builder(this)
            .setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()

        builder.show()

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+91$number")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

   val  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {


        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            builder.dismiss()
           val intent = Intent(this@LoginActivity,OTPActivity::class.java)
            intent.putExtra("verificationId",verificationId)
            intent.putExtra("number",binding.UserNumber.text.toString())
            startActivity(intent)
        }
    }
}