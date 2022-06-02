package com.don2.shopintelli

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.atway.ui.adapter.ATAdapter
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.ActivityClientInfoBinding


class ClientInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientInfoBinding
    private val clientInfoAdapter = ATAdapter({ ClientInfoView(it) })
    private lateinit var num_whatsapp:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sendToGetId()

        binding.btnBack.setOnClickListener{
            super.onBackPressed()
        }
    }

    private fun sendToGetId() {
        // Instantiate the RequestQueue.
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        val client_id = sharedPreferences.getString("client_id" , "No imported" )
        val id_loja = sharedPreferences.getString("id_loja" , "No imported" )

        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/client/info/" + client_id
        var id: Int;

        binding.btnWhatsapp.setOnClickListener{
            Log.d("whats app", num_whatsapp)
            if(num_whatsapp != "null") {
                val i = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "https://api.whatsapp.com/send?phone=" + num_whatsapp +
                                "&text=" + "Olá!"
                    )
                )
                startActivity(i)
            }
            else{
                Toast.makeText(this, "Numero do cliente não encontrado", Toast.LENGTH_LONG).show()
            }
        }

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("get client info", response.toString())
                //id = response["data"].toString().toInt()
                //Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
                //calcados_list = createArrayCalcado(response)
                //clientInfoAdapter.items = arrayListOf(
                //    ItemClientInfo(response[0].toString().toInt(),response[1].toString().toInt(),response[2].toString().toInt(),response[3].toString(),response[4].toString())
                //)
                binding?.progressBar1?.setVisibility(View.GONE)
                binding.txtTicketValue.text = response[0].toString()
                binding.txtNotaValue.text = response[1].toString()
                binding.txtMediaProductsValue.text = response[2].toString()
                binding.txtLastOrderValue.text = response[3].toString()
                num_whatsapp = response[4].toString()
                Log.d("client info", response.toString())
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
}