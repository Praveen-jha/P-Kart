package com.example.p_kart.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.p_kart.R
import com.example.p_kart.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding
    private lateinit var preferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserInfo()
        preferences = this.getSharedPreferences("users", MODE_PRIVATE)

        binding.proceed.setOnClickListener {
            validateData(
                binding.UserName.text.toString(),
                binding.UserNumber.text.toString(),
                binding.UserVillage.text.toString(),
                binding.UserCity.text.toString(),
                binding.UserPin.text.toString(),
                binding.UserState.text.toString()

            )
        }
    }

    private fun validateData(
        number: String,
        name: String,
        pinCode: String,
        city: String,
        state: String,
        village: String
    ) {

        if (number.isEmpty() || state.isEmpty() || name.isEmpty() || pinCode.isEmpty() || city.isEmpty() || village.isEmpty()){
            Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
        }else{
            storeData(pinCode,city,village,state)
        }
    }

    private fun storeData(pinCode: String, city: String, village: String, state: String) {
        
        val map = hashMapOf<String,Any>()
        map["village"] = village
        map["state"] = state
        map["city"] = city
        map["pinCode"]  = pinCode
        
        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .update(map).addOnSuccessListener { 
                startActivity(Intent(this,CheckoutActivity::class.java))

            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }

    private fun loadUserInfo() {

        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
                binding.UserName.setText(it.getString("userName"))
                binding.UserNumber.setText(it.getString("userPhoneNumber"))
                binding.UserVillage.setText(it.getString("village"))
                binding.UserCity.setText(it.getString("state"))
                binding.UserState.setText(it.getString("city"))
                binding.UserPin.setText(it.getString("pinCode"))

            }
    }
}