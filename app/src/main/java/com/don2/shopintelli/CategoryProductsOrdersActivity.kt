package com.don2.shopintelli

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.atway.ui.adapter.ATAdapter
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.ActivityProductsOrdersBinding
import org.json.JSONArray
import kotlinx.coroutines.*
import kotlin.system.*

class CategoryProductsOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsOrdersBinding
    private val ProductOrderAdapter = ATAdapter({ MoreProductOrderView(it) })
    private lateinit var calcados_list: ArrayList<MoreProduct>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsOrdersBinding.inflate(layoutInflater)
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
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/"+ category_order + "/shop/" +id_loja
        var id: Int;

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("get calcado", response.toString())
                //id = response["data"].toString().toInt()
                //Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
                calcados_list = createArrayCalcado(response)
                ProductOrderAdapter.items = calcados_list
                binding?.progressBar1?.setVisibility(View.GONE)
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

    private fun createArrayCalcado(arrayRequest:JSONArray): ArrayList<MoreProduct> {
        var arrayElements = arrayListOf<MoreProduct>()
        var price =0.0
        for (i in 0 until arrayRequest.length()) {
            var item_list = arrayRequest.getJSONArray(i)
            if(item_list.length() > 13){
                if(item_list[13] == "CALCA"){
                    price = item_list[10].toString().toDouble()
                }
                else{
                    price = item_list[2].toString().toDouble()
                }
            }
            else{
                price = item_list[2].toString().toDouble()
            }
            var elem = MoreProduct(
                item_list[0].toString().toInt(),
                "https://png.pngtree.com/png-clipart/20190920/original/pngtree-stacked-express-carton-hills-png-image_4672485.jpg",
                item_list[1].toString(),
                0,
                item_list[5].toString(),
                item_list[3].toString(),
                //item_list[2].toString().toDouble(),
               price
            )
            arrayElements.add(elem)
        }
        return arrayElements
    }
}