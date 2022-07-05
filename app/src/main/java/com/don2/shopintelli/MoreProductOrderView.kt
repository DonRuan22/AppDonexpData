package com.don2.shopintelli

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.MoreProductOrderItemBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

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
    binding.txtInfo.text = item.info

    val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("client_data",
      AppCompatActivity.MODE_PRIVATE
    )
    val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val category_order = sharedPreferences.getString("category_order" , "No imported" )
    //binding.txtCategory.text = item.category
    //binding.txtSubtitle.text = itemView.context.getString(R.string.shop_category, item.category, item.distance)
    itemView.setOnClickListener {
      /*
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
       */

      val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("client_data",
        AppCompatActivity.MODE_PRIVATE
      )
      val client_id = sharedPreferences.getString("client_id" , "No imported" )
      val id_loja = sharedPreferences.getString("id_loja" , "No imported" )
      val json = JSONObject()

      json.put("idProduct", item.id)
      json.put("client_id", client_id)
      json.put("date", currentDate)
      json.put("shop_id", id_loja.toString().toInt())
      json.put("quant", 1)
      sendToGetId(json)
    }

  }

  private fun sendToGetId(json: JSONObject) {
    // Instantiate the RequestQueue.
    val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("client_data",
      AppCompatActivity.MODE_PRIVATE
    )
    val category_order = sharedPreferences.getString("category_order" , "No imported" )

    val queue = Volley.newRequestQueue(itemView.context)
    val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/"+ "calcado" + "/order/product"
    var id: Int;

    // Request a string response from the provided URL.
    val jsonObjectRequest = JsonObjectRequest(
      Request.Method.POST, url, json,
      Response.Listener { response ->
        Log.d("Register-product", response.toString())
        //id = response["data"].toString().toInt()
        Toast.makeText(itemView.context, "Registrado com sucesso", Toast.LENGTH_LONG).show()
      },
      Response.ErrorListener { Toast.makeText(itemView.context, "Erro produto não registrado", Toast.LENGTH_LONG).show() })
    jsonObjectRequest.setRetryPolicy(
      DefaultRetryPolicy(5000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    )
    // Add the request to the RequestQueue.
    queue.add(jsonObjectRequest)
    val intent = Intent(itemView.context, PrincipalActivity::class.java).apply {
      putExtra("Message", "Load activity client")
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    ContextCompat.startActivity(itemView.context, intent, null)
  }

}