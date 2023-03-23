package com.example.p_kart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.p_kart.R
import com.example.p_kart.activity.AddressActivity
import com.example.p_kart.activity.CategoryActivity
import com.example.p_kart.adapter.CartAdapter
import com.example.p_kart.databinding.FragmentCartBinding
import com.example.p_kart.roomdb.AppDatabase
import com.example.p_kart.roomdb.ProductModel


class CartFragment : Fragment() {


    private lateinit var binding: FragmentCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()


        val dao = AppDatabase.getInstance(requireContext()).productDao()
        dao.getAllProducts().observe(requireActivity()){
            binding.cardRecycler.adapter = CartAdapter(requireContext(),it)

            totalCost(it)
        }

        return binding.root
    }

    private fun totalCost(data: List<ProductModel>?) {
        var total = 0
        for (item in data!!){
            total +=  item.productSp!!.toInt()
        }
        binding.textView12.text  = "Total item in cart is ${data.size}"
        binding.textView13.text = "Total Cost : $total"

        binding.checkout.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra("totalCost",total)
            startActivity(intent)
        }
    }


}