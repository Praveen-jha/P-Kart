package com.example.p_kart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.p_kart.R
import com.example.p_kart.databinding.ActivityRegisterBinding
import com.example.p_kart.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.logInBtn.setOnClickListener {
           openLogin()
        }

        binding.button4.setOnClickListener {
            validateUser()
        }
    }

    private fun validateUser() {
        if (binding.UserName.text!!.isEmpty() || binding.UserNumber.text!!.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }else{
            storeData()
        }
    }

    private fun storeData() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()

        builder.show()

        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val editor = preference.edit()

        editor.putString("number",binding.UserNumber.text.toString())
        editor.putString("name",binding.UserName.text.toString())

        editor.apply()

        val data = UserModel(userName = binding.UserName.text.toString() , userPhoneNumber = binding.UserNumber.text.toString() )

        Firebase.firestore.collection("users").document(binding.UserNumber.text.toString())
            .set(data).addOnSuccessListener {

                Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show()
                builder.dismiss()
                openLogin()

            }.addOnFailureListener {
                builder.dismiss()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }

    private fun openLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}