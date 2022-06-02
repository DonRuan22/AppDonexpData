package com.don2.shopintelli

import android.view.ViewGroup
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.don2.shopintelli.databinding.MoreProductItemBinding
import com.squareup.picasso.Picasso

class MoreProductView(viewGroup: ViewGroup) : ATViewHolder<MoreProduct, MoreProductItemBinding>(
  MoreProductItemBinding::inflate,
  viewGroup
) {

  override fun bind(item: MoreProduct) {

    Picasso.get()
      .load(item.photoUrl)
      .into(binding.imgProduct)

    binding.txtProduct.text = item.text
    binding.txtPrice.text = item.price.toString()
    //binding.txtQuant.text = item.quant.toString()
    binding.txtBrand.text = item.brand
    //binding.txtCategory.text = item.category
    //binding.txtSubtitle.text = itemView.context.getString(R.string.shop_category, item.category, item.distance)
  }

}