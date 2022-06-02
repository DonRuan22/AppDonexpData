package com.don2.shopintelli

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.ActivityRegisterOrderCamisetaBinding
import com.google.android.material.chip.Chip
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class RegisterCamisetaOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterOrderCamisetaBinding
    private lateinit var product_id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val extras = intent.extras
        if (extras != null) {
            //product_id = extras.getString("product_id").toString()
            product_id = "121"
            //The key argument here must match that used in the other activity
        }
        binding = ActivityRegisterOrderCamisetaBinding.inflate(layoutInflater)



        binding.idBtnRegister.setOnClickListener{

            val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
            val client_id = sharedPreferences.getString("client_id" , "No imported" )
            val id_loja = sharedPreferences.getString("id_loja" , "No imported" )
            val chip_size = binding.chipGroupSize.findViewById<Chip>(binding.chipGroupSize.checkedChipId)?.text
            val chip_color = binding.chipGroupColor.findViewById<Chip>(binding.chipGroupColor.checkedChipId)?.text
            val json = JSONObject()

            if(id_loja != "No imported" && !TextUtils.isEmpty(chip_size) && !TextUtils.isEmpty(chip_color)){
                json.put("idProduct", product_id)
                json.put("tam", chip_size)
                json.put("color", chip_color)
                json.put("client_id", client_id)
                json.put("date", currentDate)
                json.put("shop_id", id_loja)
                json.put("quant", 2)
            }
            sendToGetId(json)
            //Toast.makeText(this, "Produto "+product_id+ " Client "+client_id, Toast.LENGTH_LONG).show()
        }
        binding.btnBack.setOnClickListener{
            super.onBackPressed()
        }
        setContentView(binding.root)
    }

    private fun sendToGetId(json: JSONObject) {
        // Instantiate the RequestQueue.
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        val category_order = sharedPreferences.getString("category_order" , "No imported" )

        val queue = Volley.newRequestQueue(this)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/"+ category_order + "/product"
        var id: Int;

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, json,
            Response.Listener { response ->
                Log.d("Register-product", response.toString())
                //id = response["data"].toString().toInt()
                //Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { Toast.makeText(this, "Erro produto não registrado", Toast.LENGTH_LONG).show() })
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
}