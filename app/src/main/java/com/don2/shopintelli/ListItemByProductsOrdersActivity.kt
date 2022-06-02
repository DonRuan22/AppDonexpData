package com.don2.shopintelli

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.atway.ui.adapter.ATAdapter
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.*
import org.json.JSONArray
import org.json.JSONObject

class ListItemByProductsOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListItemProductsOrdersBinding
    private val ProductOrderAdapter = ATAdapter({ ListItemByProductsOrderView(it) })
    private lateinit var calcados_list: ArrayList<ListItemProduct>

    private lateinit var product_id: String
    private lateinit var product_name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        if (extras != null) {
            product_id = extras.getString("product_id").toString()
            product_name = extras.getString("product_name").toString()
            //product_id = "110"
            //The key argument here must match that used in the other activity
        }
        binding = ActivityListItemProductsOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sendToGetId()





        /*
        ProductOrderAdapter.items = arrayListOf(
            MoreProduct(1,"https://static.netshoes.com.br/produtos/tenis-nike-revolution-6-next-nature-masculino/26/2IC-4365-026/2IC-4365-026_zoom1.jpg?ts=1631636825&","Tenis Nike",10,"Calcado","Nike",120.0),
            MoreProduct(2,"https://static.netshoes.com.br/produtos/tenis-nike-revolution-6-next-nature-masculino/26/2IC-4365-026/2IC-4365-026_zoom1.jpg?ts=1631636825&","Tenis Nike",10,"Calcado","Nike",120.0),
            MoreProduct(3,"https://static.netshoes.com.br/produtos/tenis-nike-revolution-6-next-nature-masculino/26/2IC-4365-026/2IC-4365-026_zoom1.jpg?ts=1631636825&","Tenis Nike",10,"Calcado","Nike",120.0),
            MoreProduct(4,"https://static.netshoes.com.br/produtos/tenis-nike-revolution-6-next-nature-masculino/26/2IC-4365-026/2IC-4365-026_zoom1.jpg?ts=1631636825&","Tenis Nike",10,"Calcado","Nike",120.0)
        )
         */

        binding.btnBack.setOnClickListener{
            super.onBackPressed()
        }


        binding?.let {

            it.rvMoreProduct.layoutManager = LinearLayoutManager(this)
            it.rvMoreProduct.adapter = ProductOrderAdapter
        }
    }

    private fun sendToGetId() {
        // Instantiate the RequestQueue.
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        val category_order = sharedPreferences.getString("category_order" , "No imported" )
        val id_loja = sharedPreferences.getString("id_loja" , "No imported" )

        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/"+ category_order + "/findproducts"
        var id: Int;

        val json = JSONObject()
        json.put("idProduct", product_id)

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, json,
            Response.Listener { response ->
                Log.d("get item calcados", response.toString())
                //id = response["data"].toString().toInt()
                //Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
                calcados_list = createArrayCalcado(response.getJSONArray("data"))
                ProductOrderAdapter.items = calcados_list
                ProductOrderAdapter.notifyDataSetChanged()
                Log.d("calcado-list", calcados_list.toString())
            },
            Response.ErrorListener { error -> Log.d("Error request", error.toString()) })
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private fun createArrayCalcado(arrayRequest:JSONArray): ArrayList<ListItemProduct> {
        var arrayElements = arrayListOf<ListItemProduct>()
        for (i in 0 until arrayRequest.length()) {
            var item_list = arrayRequest.getJSONArray(i)
            var elem = ListItemProduct(
                item_list[0].toString().toInt(),
                product_id.toString().toInt(),
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHHwjSNW0Hf36bcnwFL2M6pS6VjhIUZ20pbOdYpKCypKhT2gU7mSWDTWS9BMupMaZpHE8&usqp=CAU",
                product_name,
                1,
                item_list[2].toString(),
                item_list[1].toString()
            )
            arrayElements.add(elem)
        }
        return arrayElements
    }
}