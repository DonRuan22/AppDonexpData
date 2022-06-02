package com.don2.shopintelli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.don2.shopintelli.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth


class DashboardActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val textView_id_text: TextView = findViewById(R.id.id_txt) as TextView
        val textView_name_txt: TextView = findViewById(R.id.name_txt) as TextView
        val textView_email_txt: TextView = findViewById(R.id.email_txt) as TextView
        textView_id_text.text = currentUser?.uid
        textView_name_txt.text = currentUser?.displayName
        textView_email_txt.text = currentUser?.email

        var imageview_profile_image = findViewById(R.id.profile_image) as ImageView

        Glide.with(this).load(currentUser?.photoUrl).into(imageview_profile_image)

        binding.signOutBtn.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}