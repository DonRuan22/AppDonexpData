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
            val chip_year = binding.chipGroupYear.findViewById<Chip>(binding.chipGroupYear.checkedChipId)?.text

            val chip_ids_size = binding.chipGroupSize.checkedChipIds
            val list_sizes: MutableList<String> = mutableListOf<String>()
            for (id in chip_ids_size) {
                list_sizes.add(binding.chipGroupSize.findViewById<Chip>(id).text.toString())
            }

            val chip_category = binding.chipGroupCategory.findViewById<Chip>(binding.chipGroupCategory.checkedChipId)?.text

            val chip_material = binding.chipGroupMaterial.findViewById<Chip>(binding.chipGroupMaterial.checkedChipId)?.text

            val chip_gender = binding.chipGroupGender.findViewById<Chip>(binding.chipGroupGender.checkedChipId)?.text

            val chip_ids_color = binding.chipGroupColor.checkedChipIds
            val list_colors: MutableList<String> = mutableListOf<String>()
            for (id in chip_ids_color) {
                list_colors.add(binding.chipGroupColor.findViewById<Chip>(id).text.toString())
            }
            if(id_loja != "No imported" && !TextUtils.isEmpty(binding.productName.text) &&
                !TextUtils.isEmpty(binding.price.text.toString())&&
                !TextUtils.isEmpty(binding.brand.text.toString())&&
                !TextUtils.isEmpty(chip_year)&&
                !TextUtils.isEmpty(chip_category)&&
                !TextUtils.isEmpty(chip_material)&&
                !TextUtils.isEmpty(chip_gender)&&
                !TextUtils.isEmpty(id_loja)&&
                !list_sizes.isEmpty()&&
                !list_colors.isEmpty()) {
                for (tam in list_sizes) {
                    for (color in list_colors) {
                        val json = JSONObject()
                        json.put("produto", "no")
                        json.put("name", binding.productName.text)
                        json.put("price", binding.price.text.toString().toFloat())
                        json.put("brand", binding.brand.text.toString().capitalize())
                        json.put("date_model", chip_year)
                        json.put("categorie", chip_category)
                        json.put("material", chip_material)
                        json.put("gender", chip_gender)
                        json.put("id_shop", id_loja)
                        json.put("size", tam)
                        json.put("color", color)
                        json.put("type", "CALCADO")
                        Log.d("JSON-data", json.toString())
                        sendClient(json)
                        SystemClock.sleep(1000)
                    }
                }
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