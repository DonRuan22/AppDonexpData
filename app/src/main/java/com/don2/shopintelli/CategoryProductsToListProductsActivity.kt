package com.don2.shopintelli

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.atway.ui.adapter.ATAdapter
import com.don2.shopintelli.databinding.ActivityCategoryProductsBinding

class CategoryProductsToListProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryProductsBinding
    private val categoryProductAdapter = ATAdapter({ CategoryProductToListView(it) })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras = intent.extras
        if (extras != null) {
            val client_name = extras.getString("Client_name")
            Toast.makeText(this, client_name, Toast.LENGTH_LONG).show()
            //The key argument here must match that used in the other activity
        }
        categoryProductAdapter.items = arrayListOf(
            CategoryProduct(1,"https://st4.depositphotos.com/3822321/25497/v/1600/depositphotos_254976222-stock-illustration-sneakers-icon-vector-sign-symbol.jpg","Calcado"),
            CategoryProduct(2,"https://image.shutterstock.com/image-vector/pants-icon-vector-illustration-flat-260nw-1809661777.jpg","Calça"),
            CategoryProduct(3,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5EKS7yA7NIfblD_b_4Png_vyGrNw14wttFlWuAqBAT52RoTTFX7VATOlk5PtfznseOQ4&usqp=CAU","Camiseta")
        )
        binding.btnBack.setOnClickListener{
            super.onBackPressed()
        }


        binding?.let {

            it.rvMoreProduct.layoutManager = LinearLayoutManager(this)
            it.rvMoreProduct.adapter = categoryProductAdapter
        }

    }
}