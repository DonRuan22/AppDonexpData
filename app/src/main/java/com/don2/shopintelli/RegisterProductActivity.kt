package com.don2.shopintelli

import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.ActivityRegisterProductBinding
import com.google.android.material.chip.Chip
import org.json.JSONObject

class RegisterProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterProductBinding.inflate(layoutInflater)
        binding.idBtnRegister.setOnClickListener{
            val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
            //sharedPreferences.edit().remove("user_id").commit()
            val id_loja = sharedPreferences.getString("id_loja" , "No imported" )

            if(id_loja != "No imported" && !TextUtils.isEmpty(binding.productName.text) &&
                !TextUtils.isEmpty(binding.price.text.toString())&&
                !TextUtils.isEmpty(binding.brand.text.toString())&&
                !TextUtils.isEmpty(id_loja)) {
                    val json = JSONObject()
                    json.put("produto", "no")
                    json.put("name", binding.productName.text)
                    json.put("price", binding.price.text.toString().toFloat())
                    json.put("brand", binding.brand.text.toString().capitalize())
                    json.put("id_shop", id_loja)
                    json.put("others", binding.others.text.toString().capitalize())

                    Log.d("JSON-data", json.toString())
                    sendClient(json)
                    SystemClock.sleep(1000)
                super.onBackPressed()
            }
            else{
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
            }
            //Toast.makeText(this, "Test", Toast.LENGTH_LONG).show()
        }
        binding.btnBack.setOnClickListener{
            super.onBackPressed()
        }
        setContentView(binding.root)
    }

    private fun sendClient(json: JSONObject) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/calcado"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, json,
            Response.Listener { response ->
                Log.d("Register-product", "Registrado com sucesso")
                //Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { Toast.makeText(this, "Erro", Toast.LENGTH_LONG).show() })
        jsonObjectRequest.setRetryPolicy(DefaultRetryPolicy(100000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
        queue.start()
    }
}