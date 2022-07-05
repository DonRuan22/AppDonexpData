package com.don2.shopintelli

import android.R
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.atway.ui.adapter.ATAdapter
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.ActivityClientInfoBinding
import org.json.JSONArray
import kotlin.math.log


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
                binding.txtTicketValue.text = "R$ "+ response[0].toString()
                binding.txtNotaValue.text = response[1].toString()
                binding.txtMediaProductsValue.text = response[2].toString()+ " Itens"
                binding.txtLastOrderValue.text = "Em: "+ response[3].toString()
                binding?.txtLastOrderValueTotal.text = "R$ "+ response[4].toString()
                num_whatsapp = response[6].toString()

                binding?.txtName.text = "Nome: " + response[5].toString()
                binding?.txtEmail.text = "Email: " + response[6].toString()
                binding?.txtPhone.text = "Telefone: " + response[7].toString()
                binding?.txtBirthday.text = "Nascimento: " + response[8].toString()

                var item_list = response.getJSONArray(9)
                for (i in 0 until item_list.length()) {
                    var prod =  item_list.getJSONArray(i)
                    if(i ==0) {
                        //Log.d("ARRAY TEST",item_list[i].toString())
                        binding?.lastproduct1?.visibility = View.VISIBLE
                        binding?.lastproductBrand1?.visibility = View.VISIBLE
                        binding?.lastproduct1.text = "Produto: "+prod[12].toString()
                        binding?.lastproductBrand1.text = "Marca: "+prod[14].toString()
                    }
                    if(i ==1) {
                        //Log.d("ARRAY TEST",item_list[i].toString())
                        //val prod =  item_list.getJSONArray(i)
                        binding?.lastproduct2?.visibility = View.VISIBLE
                        binding?.lastproductBrand2?.visibility = View.VISIBLE
                        binding?.lastproduct2.text = "Produto: "+prod[12].toString()
                        binding?.lastproductBrand2.text = "Marca: "+prod[14].toString()
                    }
                    if(i ==2) {
                        //Log.d("ARRAY TEST",item_list[i].toString())
                        //val prod =  item_list.getJSONArray(i)
                        binding?.lastproduct3?.visibility = View.VISIBLE
                        binding?.lastproductBrand3?.visibility = View.VISIBLE
                        binding?.lastproduct3.text = "Produto: "+prod[12].toString()
                        binding?.lastproductBrand3.text = "Marca: "+prod[14].toString()
                    }
                    if(i ==3) {
                        //Log.d("ARRAY TEST",item_list[i].toString())
                        //val prod =  item_list.getJSONArray(i)
                        binding?.lastproduct4?.visibility = View.VISIBLE
                        binding?.lastproductBrand4?.visibility = View.VISIBLE
                        binding?.lastproduct4.text = "Produto: "+prod[12].toString()
                        binding?.lastproductBrand4.text = "Marca: "+prod[14].toString()
                    }
                    if(i ==4) {
                        //Log.d("ARRAY TEST",item_list[i].toString())
                        //val prod =  item_list.getJSONArray(i)
                        binding?.lastproduct5?.visibility = View.VISIBLE
                        binding?.lastproductBrand5?.visibility = View.VISIBLE
                        binding?.lastproduct5.text = "Produto: "+prod[12].toString()
                        binding?.lastproductBrand5.text = "Marca: "+prod[14].toString()
                    }
                }



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

