package com.don2.shopintelli

import android.view.ViewGroup
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.don2.shopintelli.databinding.MoreOrderItemBinding

class MoreOrderView(viewGroup: ViewGroup) : ATViewHolder<MoreOrder, MoreOrderItemBinding>(
  MoreOrderItemBinding::inflate,
  viewGroup
) {

  override fun bind(item: MoreOrder) {
    /*
    Picasso.get()
      .load(item.bannerUrl)
      .into(binding.imgShop)
  */
    binding.txtClient.text = item.client
    binding.txtStatus.text = item.status
    binding.txtDate.text = item.date
    binding.txtTotalValue.text = item.total_value.toString()
    //binding.txtSubtitle.text = itemView.context.getString(R.string.shop_category, item.category, item.distance)
  }

}