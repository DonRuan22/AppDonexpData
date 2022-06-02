package com.don2.shopintelli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.don2.shopintelli.databinding.MoreShopItemBinding
import com.squareup.picasso.Picasso

class MoreShopView(viewGroup: ViewGroup) : ATViewHolder<MoreShop, MoreShopItemBinding>(
  MoreShopItemBinding::inflate,
  viewGroup
) {

  override fun bind(item: MoreShop) {
    Picasso.get()
      .load(item.bannerUrl)
      .into(binding.imgShop)

    binding.txtShop.text = item.text
    binding.txtStarClassify.text = item.rate.toString()
    //binding.txtSubtitle.text = itemView.context.getString(R.string.shop_category, item.category, item.distance)
    //binding.txtPrice.text = itemView.context.getString(R.string.last_contact,"17","12","21")

    itemView.setOnClickListener {
      val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("client_data",
        Context.MODE_PRIVATE
      )
      val edit = sharedPreferences.edit()
      edit.putString("client_id" ,item.id.toString())
      edit.apply()
      //val client_name = sharedPreferences.getString("client_id" , "No imported" )
      //Toast.makeText(it.context , client_name , Toast.LENGTH_SHORT).show()

      val intent = Intent(itemView.context, ClientInfoActivity::class.java).apply {
        putExtra("Client_name", "Ruan")
      }
      ContextCompat.startActivity(itemView.context, intent, null)
    }

    itemView.setOnLongClickListener {
      val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("client_data",
        Context.MODE_PRIVATE
      )
      val edit = sharedPreferences.edit()
      edit.putString("client_id" ,item.id.toString())
      edit.apply()
      //val client_name = sharedPreferences.getString("client_id" , "No imported" )
      //Toast.makeText(it.context , client_name , Toast.LENGTH_SHORT).show()

      val intent = Intent(itemView.context, UpdateClientActivity::class.java).apply {
        putExtra("Client_name", "Ruan")
      }
      ContextCompat.startActivity(itemView.context, intent, null)
      false
    }
  }

}