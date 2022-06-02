package com.don2.shopintelli

import android.view.ViewGroup
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.don2.shopintelli.databinding.BannerItemBinding
import com.squareup.picasso.Picasso

class BannerView(viewGroup: ViewGroup) : ATViewHolder<com.don2.shopintelli.Banner, BannerItemBinding>(
  BannerItemBinding::inflate,
  viewGroup
) {

  override fun bind(item: com.don2.shopintelli.Banner) {
    Picasso.get()
      .load(item.bannerUrl)
      .into(binding.imgBanner)
  }

}