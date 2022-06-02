package com.don2.shopintelli

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.ActivityCadastroClientBinding
import com.don2.shopintelli.databinding.ActivityUpdateClientBinding
import com.google.android.material.chip.Chip
import org.json.JSONObject

class UpdateClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateClientBinding
    private var quant_client: Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataClient()

        binding.idBtnRegister.setOnClickListener{
            val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
            //sharedPreferences.edit().remove("user_id").commit()
            val id_loja = sharedPreferences.getString("id_loja" , "No imported" )
            //val user_state = sharedPreferences.getString("user_state" , "normal" )
            //var user_hability = true
            val chip_selected = binding.chipGroupScore.findViewById<Chip>(binding.chipGroupScore.checkedChipId)?.text
            val whatsapp = binding.clientWhatsApp.text.toString()
            var whats_value = "55"+whatsapp.toString().replace(" ","")
            Log.d("Whatsapp",whats_value)
            if(id_loja != "No imported") {
                val json = JSONObject()
                json.put("name", binding.clientName.text)
                json.put("categorie", chip_selected)
                json.put("whats_app", whats_value)
                json.put("age", binding.clientDateBirthday.text)
                json.put("profession", binding.clientProfession.text)
                json.put("city", binding.clientCity.text)
                json.put("email", binding.clientEmail.text)
                json.put("id_shop", id_loja)

                //quant_client = getQuant(id_loja.toString().toInt())
                sendClient(json)
            }
            super.onBackPressed()
            //Toast.makeText(this, "Test", Toast.LENGTH_LONG).show()
        }
        binding.btnBack.setOnClickListener{
            super.onBackPressed()
        }
        binding.btnDelete.setOnClickListener{
            deleteClient()
            super.onBackPressed()
        }
    }

    private fun sendClient(json: JSONObject) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        val client_id = sharedPreferences.getString("client_id" , "No imported" )
        val id_loja = sharedPreferences.getString("id_loja" , "No imported" )

        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/client/"+client_id

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, json,
            Response.Listener { response ->
                Toast.makeText(this, "Atualizado com sucesso", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { Toast.makeText(this, "Erro", Toast.LENGTH_LONG).show() })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private fun getQuant(id_loja:Int): Int {


        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/client/shop/" +id_loja
        var id: Int;
        var len_client =0

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("get clients", response.toString())
                //id = response["data"].toString().toInt()
                //Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
                len_client = response.length()
                Log.d("client-list_length", len_client.toString())
            },
            Response.ErrorListener { error -> Log.d("Error request", error.toString()) })
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
        return len_client
    }


    private fun getDataClient() {
        // Instantiate the RequestQueue.
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        val client_id = sharedPreferences.getString("client_id" , "No imported" )
        val id_loja = sharedPreferences.getString("id_loja" , "No imported" )

        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/client/" + client_id
        var id: Int;

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
                binding.clientName.setText(response[1].toString())
                binding.clientProfession.setText(response[5].toString())
                binding.clientWhatsApp.setText(response[3].toString())
                binding.chipGroupScore.check(binding.chipGroupScore.get(response[2].toString().toInt()-1).id)
                //binding.chipGroupScore.check(response[0].toString().toInt())
                binding.clientDateBirthday.setText(response[4].toString().replace("/",""))
                binding.clientCity.setText(response[6].toString())
                binding.clientEmail.setText(response[7].toString())

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

    private fun deleteClient() {
        // Instantiate the RequestQueue.
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        val client_id = sharedPreferences.getString("client_id" , "No imported" )
        val id_loja = sharedPreferences.getString("id_loja" , "No imported" )

        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/client/" + client_id
        var id: Int;

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.DELETE, url, null,
            Response.Listener { response ->
                Toast.makeText(this, "Deletado com sucesso", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener {
                    error -> Log.d("Error request", error.toString()) })
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }


}