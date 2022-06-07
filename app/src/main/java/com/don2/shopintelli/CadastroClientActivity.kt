package com.don2.shopintelli

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.ActivityCadastroClientBinding
import com.google.android.material.chip.Chip
import org.json.JSONObject

class CadastroClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroClientBinding
    private var quant_client: Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idBtnRegister.setOnClickListener{
            val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
            //sharedPreferences.edit().remove("user_id").commit()
            val id_loja = sharedPreferences.getString("id_loja" , "No imported" )
            val user_state = sharedPreferences.getString("user_state" , "normal" )
            var user_hability = true
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

                quant_client = getQuant(id_loja.toString().toInt())
                //quant_client =51
                if(quant_client<51){
                    sendClient(json)
                }
                else if(user_state == "premium"){
                    sendClient(json)
                }
                else{
                    Toast.makeText(this, "Limite de cliente atingido vire PREMIUM", Toast.LENGTH_LONG).show()
                }

            }
            super.onBackPressed()
            //Toast.makeText(this, "Test", Toast.LENGTH_LONG).show()
        }
        binding.btnBack.setOnClickListener{
            super.onBackPressed()
        }
    }

    private fun sendClient(json: JSONObject) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/client"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, json,
            Response.Listener { response ->
                Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
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
}