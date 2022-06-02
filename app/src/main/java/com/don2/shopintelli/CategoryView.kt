package com.don2.shopintelli

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.don2.shopintelli.databinding.CategoryItemBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CategoryView(viewGroup: ViewGroup) : ATViewHolder<Category, CategoryItemBinding>(
  CategoryItemBinding::inflate,
  viewGroup
) {

  override fun bind(item: Category) {
    binding.txtCategory.text = item.name

    Picasso.get()
      .load(item.logoUrl)
      .into(binding.imgCategory, object : Callback {
        override fun onSuccess() {
          val shape = GradientDrawable()
          shape.cornerRadius = 10f

          shape.setColor(item.color.toInt())
          shape.setBounds(1000,100,1000,100)

          binding.bgCategory.background = shape
        }

        override fun onError(e: Exception?) {
        }
      })

    binding.bgCategory.setOnClickListener{
      if(item.name == "Clientes"){
        var intent = Intent(binding.bgCategory.context, com.don2.shopintelli.CadastroClientActivity::class.java).apply {
          putExtra("Message", "Load activity client")
        }
        startActivity(binding.bgCategory.context,intent,null)
      }
      else if(item.name == "Produtos"){
        var intent = Intent(binding.bgCategory.context, CategoryProductsActivity::class.java).apply {
          putExtra("Message", "Load activity client")
        }
        startActivity(binding.bgCategory.context,intent,null)
      }
      else if(item.name == "Vendas"){
        var intent = Intent(binding.bgCategory.context, ClientOrderActivity::class.java).apply {
          putExtra("Message", "Load activity client")
        }
        startActivity(binding.bgCategory.context,intent,null)
      }
    }
  }



}