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
import com.don2.shopintelli.databinding.ActivityClientOrderBinding
import org.json.JSONArray

class ClientOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientOrderBinding
    private val clientOrderAdapter = ATAdapter({ MoreClientOrderView(it) })
    private lateinit var client_list: ArrayList<MoreClientOrder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sendToGetId()

        /*
        clientOrderAdapter.items = arrayListOf(
            MoreClientOrder(31, "https://img.myloview.com.br/adesivos/client-icon-vector-male-person-profile-avatar-with-speech-bubble-symbol-for-discussion-and-information-in-flat-color-glyph-pictogram-illustration-400-176705670.jpg", "Pizza Crek", 4.4, "Pizza", 11.2, "60-70", 26.00),
            MoreClientOrder(32, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/bb3ad636-7c36-4ae2-a1db-14cd35695350/202001271029_rK15_i.png", "Fábrica de Esfiha", 4.3, "Esfiha", 12.2, "60-70", 9.00),
            MoreClientOrder(33, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/2fd863ac-4cc2-476c-8896-99aedfdaeb5f/201911150948_Z9QG_i.jpg", "Pecorino", 4.9, "Grill", 17.2, "60-70", 10.00),
            MoreClientOrder(34, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/86b58685-a7dc-4596-be26-2c4037b4d591/202006051304_JuRt_i.jpg", "Barbacoa Grill", 4.9, "Grill", 12.2, "70-90", 40.00),
            MoreClientOrder(5, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/e2f3424a-06fb-46dd-89c3-f7b039e2b1f0_BOLOD_PPIN02.jpeg", "Bolo de Madre", 4.7, "Bolo", 11.0, "80-90", 30.00),
            MoreClientOrder(6, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/201901021647_8066dc64-9383-46d1-aa2d-56b9492e27ed.png", "Uau Esfiha", 4.4, "Esfiha", 11.2, "60-70", 8.00),
            MoreClientOrder(7, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/201705131248_0ca51a98-ee95-48ac-b193-48066c8f20cc.png", "Bar do Juarez", 4.9, "Bar", 17.2, "40-50", 13.00),
        )
         */
        binding.btnBack.setOnClickListener{
            super.onBackPressed()
        }


        binding?.let {

            it.rvMoreShops.layoutManager = LinearLayoutManager(this)
            it.rvMoreShops.adapter = clientOrderAdapter
        }
    }

    private fun sendToGetId() {
        // Instantiate the RequestQueue.
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        //val category_order = sharedPreferences.getString("category_order" , "No imported" )
        val id_loja = sharedPreferences.getString("id_loja" , "No imported" )

        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/client/shop/" +id_loja
        var id: Int;

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("get client", response.toString())
                //id = response["data"].toString().toInt()
                //Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
                client_list = createArrayClient(response)
                clientOrderAdapter.items = client_list
                binding?.progressBar1?.setVisibility(View.GONE)
                clientOrderAdapter.notifyDataSetChanged()
                Log.d("client-list", client_list.toString())
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

    private fun createArrayClient(arrayRequest: JSONArray): ArrayList<MoreClientOrder> {
        var arrayElements = arrayListOf<MoreClientOrder>()
        for (i in 0 until arrayRequest.length()) {
            var item_list = arrayRequest.getJSONArray(i)
            var elem = MoreClientOrder(
                item_list[0].toString().toInt(),
                "https://img.myloview.com.br/adesivos/client-icon-vector-male-person-profile-avatar-with-speech-bubble-symbol-for-discussion-and-information-in-flat-color-glyph-pictogram-illustration-400-176705670.jpg",
                item_list[1].toString(),
                item_list[2].toString().toDouble(),
                item_list[2].toString(),
                0.0,
                "0",
                0.0
            )
            arrayElements.add(elem)
        }
        return arrayElements
    }
}
