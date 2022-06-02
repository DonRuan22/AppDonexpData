package com.don2.shopintelli

import android.view.ViewGroup
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.don2.shopintelli.databinding.ActivityClientInfoBinding

class ClientInfoView(viewGroup: ViewGroup) : ATViewHolder<ItemClientInfo, ActivityClientInfoBinding>(
  ActivityClientInfoBinding::inflate,
  viewGroup
) {

  override fun bind(item: ItemClientInfo) {
    binding.txtTicketValue.text = item.ticket_medio.toString()
    binding.txtNotaValue.text = item.nota.toString()
    binding.txtMediaProductsValue.text = item.media_products.toString()
    binding.txtLastOrderValue.text = item.last_order.toString()
  }

  fun getInfo(): MutableList<String> {
    val data = mutableListOf<String>()
    data.add(binding.txtTicketValue.text.toString())
    data.add(binding.txtTicketValue.text.toString())
    data.add(binding.txtNotaValue.text.toString())
    data.add(binding.txtMediaProductsValue.text.toString())
    data.add(binding.txtLastOrderValue.text.toString())
    return data
  }


}