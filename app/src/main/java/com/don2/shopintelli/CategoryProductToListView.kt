package com.don2.shopintelli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.don2.shopintelli.databinding.CategorieProductItemBinding
import com.squareup.picasso.Picasso

class CategoryProductToListView(viewGroup: ViewGroup) : ATViewHolder<CategoryProduct, CategorieProductItemBinding>(
  CategorieProductItemBinding::inflate,
  viewGroup
) {

  override fun bind(item: CategoryProduct) {
    Picasso.get()
      .load(item.photoUrl)
      .into(binding.imgProduct)

    binding.txtProduct.text = item.text
    //binding.txtCategory.text = item.category
    //binding.txtSubtitle.text = itemView.context.getString(R.string.shop_category, item.category, item.distance)
    val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("client_data",
      Context.MODE_PRIVATE
    )


    itemView.setOnClickListener{
      if(item.text.toString() == "Calcado"){
        val edit = sharedPreferences.edit()
        edit.putString("category_order" ,"calcado")
        edit.apply()

        val intent = Intent(itemView.context, CategoryProductsOrdersActivity::class.java).apply {
          putExtra("name_item", "Load activity client")
        }
        ContextCompat.startActivity(itemView.context, intent, null)
      }
      if(item.text.toString() == "Calça"){
        val edit = sharedPreferences.edit()
        edit.putString("category_order" ,"calca")
        edit.apply()

        val intent = Intent(itemView.context, CategoryProductsOrdersActivity::class.java).apply {
          putExtra("Message", "Load activity client")
        }
        ContextCompat.startActivity(itemView.context, intent, null)
      }
      if(item.text.toString() == "Camiseta"){
        val edit = sharedPreferences.edit()
        edit.putString("category_order" ,"camiseta")
        edit.apply()

        val intent = Intent(itemView.context, CategoryProductsOrdersActivity::class.java).apply {
          putExtra("Message", "Load activity client")
        }
        ContextCompat.startActivity(itemView.context, intent, null)
      }
    }
  }

}