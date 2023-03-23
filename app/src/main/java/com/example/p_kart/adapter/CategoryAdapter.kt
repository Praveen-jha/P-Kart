package com.example.p_kart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.p_kart.R
import com.example.p_kart.activity.CategoryActivity
import com.example.p_kart.databinding.LayoutCategoryItemBinding
import com.example.p_kart.model.CategoryModel

class CategoryAdapter(
    var context: Context, var list : ArrayList<CategoryModel>
) :RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){


    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding = LayoutCategoryItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
       return CategoryViewHolder(
           LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent,false)
       )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.textView2.text = list[position].cate
        Glide.with(context).load(list[position].img).into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("cate",list[position].cate)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}