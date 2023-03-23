package com.example.p_kart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.p_kart.MainActivity
import com.example.p_kart.databinding.ActivityProductDetailBinding
import com.example.p_kart.roomdb.AppDatabase
import com.example.p_kart.roomdb.ProductDao
import com.example.p_kart.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProductDetail(intent.getStringExtra("id"))
    }

    private fun getProductDetail(productId: String?) {
        Firebase.firestore.collection("products").document(productId!!).get().addOnSuccessListener {


            val list = it.get("productImage") as ArrayList<String>

            val name = it.getString("productName")
            val productSp = it.getString("productSp")
            val productDescription = it.getString("productDescription")
            binding.textView6.text = name
            binding.textView7.text = productSp
            binding.textView8.text = productDescription

            val slideList = ArrayList<SlideModel>()
            for (data in list) {
                slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
            }

            cartAction(productId, name, productSp, it.getString("productCoverImage"))

            binding.imageSlider.setImageList(slideList)


        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }

    }

    private fun cartAction(
        productId: String,
        name: String?,
        productSp: String?,
        coverImg: String?
    ) {

        val productDao = AppDatabase.getInstance(this).productDao()

        lifecycleScope.launch(Dispatchers.IO) {

            if (productDao.isExit(productId) != null) {
                binding.textView9.text = "Go to Cart"
            } else {
                binding.textView9.text = "Add to Cart"
            }
        }

        binding.textView9.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                if (productDao.isExit(productId) != null) {
                    openCart()
                } else {
                    addToCart(productDao, productId, name, productSp, coverImg)
                }
            }

        }

    }

    private fun addToCart(
        productDao: ProductDao,
        productId: String,
        name: String?,
        productSp: String?,
        coverImage: String?
    ) {

        val data = ProductModel(productId, name, coverImage, productSp)
        lifecycleScope.launch(Dispatchers.IO) {

            productDao.insertProduct(data)
            binding.textView9.text = "Go to Cart"
        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}