package com.don2.shopintelli

import android.content.Intent
import android.content.SharedPreferences
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.don2.shopintelli.databinding.MoreProductOrderItemBinding

class MoreProductOrderView(viewGroup: ViewGroup) : ATViewHolder<MoreProduct, MoreProductOrderItemBinding>(
  MoreProductOrderItemBinding::inflate,
  viewGroup
) {

  override fun bind(item: MoreProduct) {
    /*
    Picasso.get()
      .load(item.photoUrl)
      .into(binding.imgProduct)
  */
    binding.txtProduct.text = item.text
    binding.txtPrice.text = item.price.toString()
    //binding.txtQuant.text = item.quant.toString()
    binding.txtBrand.text = item.brand

    val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("client_data",
      AppCompatActivity.MODE_PRIVATE
    )
    val category_order = sharedPreferences.getString("category_order" , "No imported" )
    //binding.txtCategory.text = item.category
    //binding.txtSubtitle.text = itemView.context.getString(R.string.shop_category, item.category, item.distance)
    itemView.setOnClickListener {
      if(category_order == "camiseta") {
        val intent = Intent(itemView.context, ListItemByProductsOrdersActivity::class.java).apply {
          putExtra("product_id", item.id.toString())
          putExtra("product_name", item.text)
        }
        ContextCompat.startActivity(itemView.context, intent, null)
      }
      else {
        val intent = Intent(itemView.context, ListItemByProductsOrdersActivity::class.java).apply {
          putExtra("product_id", item.id.toString())
          putExtra("product_name", item.text)
        }
        ContextCompat.startActivity(itemView.context, intent, null)
      }
    }
  }

}