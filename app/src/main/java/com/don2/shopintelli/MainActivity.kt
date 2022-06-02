package com.don2.shopintelli

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject


class  MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        var res_user : String

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
            //sharedPreferences.edit().remove("user_id").commit()
        val user_id = sharedPreferences.getString("user_id" , "No imported" )
        if(user_id == "No imported" || user_id == "null" && user != null) {
            val json = JSONObject()
            Log.d("USER DATA", user.toString())
            json.put("id", user?.uid.hashCode())
            json.put("name", user?.displayName)
            json.put("email", user?.email)
            json.put("id_loja", 1)
            json.put("position", "admin")


            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/users/newUser"

            // Request a string response from the provided URL.
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, json,
                Response.Listener { response ->
                    // Display the first 500 characters of the response string.
                    //res_user = response.toString()
                    val sharedPreferences: SharedPreferences =
                        this.getSharedPreferences("client_data", MODE_PRIVATE)
                    val edit = sharedPreferences.edit()
                    if (response.optJSONObject("data")["user_id"] != null) {
                        edit.putString("user_id", response.optJSONObject("data")["user_id"].toString())
                        edit.putString("id_loja", response.optJSONObject("data")["shop_id"].toString())
                        edit.apply()
                        val user_id = sharedPreferences.getString("user_id" , "No imported" )
                        //setupViews()
                    }
                    else{
                        val user_id = sharedPreferences.getString("user_id" , "Null" )
                    }
                    /*
      else if (response.optJSONObject("data")["user_id"] != null) {
          edit.putString(
              "user_id",
              response.optJSONObject("data")["user_id"].toString()
          )
          edit.putString(
              "id_loja",
              response.optJSONObject("data")["shop_id"].toString()
          )
          edit.apply()
          val user_id = sharedPreferences.getString("user_id" , "No imported" )
      }
      */

                },
                Response.ErrorListener { Toast.makeText(this, "Error", Toast.LENGTH_LONG).show() })

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        }


        /**If user is not authenticated, send him to SignInActivity to authenticate first.
         * Else send him to DashboardActivity*/
        Handler().postDelayed({
            val client_id_new = sharedPreferences.getString("user_id" , "null" )
            Toast.makeText(this, client_id_new.toString(), Toast.LENGTH_LONG).show()
            if(client_id_new != "null"){

                val dashboardIntent = Intent(this, PrincipalActivity::class.java)
                startActivity(dashboardIntent)
                finish()
            }else{
                val signInIntent = Intent(this, SignInActivity::class.java)
                startActivity(signInIntent)
                finish()
            }
        }, 3000)

    }


}

