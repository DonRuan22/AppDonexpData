package com.don2.shopintelli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.atway.ui.adapter.ATAdapter
import com.don2.shopintelli.databinding.ActivityCategoryProductsBinding

class CategoryProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryProductsBinding
    private val categoryProductAdapter = ATAdapter({ CategoryProductView(it) })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryProductAdapter.items = arrayListOf(
            CategoryProduct(1,"https://static.netshoes.com.br/produtos/tenis-nike-revolution-6-next-nature-masculino/26/2IC-4365-026/2IC-4365-026_zoom1.jpg?ts=1631636825&","Calcado"),
            CategoryProduct(2,"https://static.netshoes.com.br/produtos/tenis-nike-revolution-6-next-nature-masculino/26/2IC-4365-026/2IC-4365-026_zoom1.jpg?ts=1631636825&","Calça"),
            CategoryProduct(3,"https://static.netshoes.com.br/produtos/tenis-nike-revolution-6-next-nature-masculino/26/2IC-4365-026/2IC-4365-026_zoom1.jpg?ts=1631636825&","Camiseta")
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