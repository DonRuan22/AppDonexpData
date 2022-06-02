package com.don2.shopintelli

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.atway.ui.adapter.ATAdapter
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.FragmentOrderBinding
import org.json.JSONArray

class OrderFragment : Fragment(R.layout.fragment_order) {

    private var binding: FragmentOrderBinding? = null

    //private val categoryAdapter = ATAdapter({ CategoryView(it) })
    //private val bannerAdapter = ATAdapter({ BannerView(it) })
    //private val shopAdapter = ATAdapter({ ShopView(it) })
    private val moreShopAdapter = ATAdapter({ MoreOrderView(it) })
    private lateinit var order_list: ArrayList<MoreOrder>

    /*
    private var filters = arrayOf(
        FilterItem(1, "Ordenar por nota"),
        FilterItem(2, "Ordenar por ticket médio"),
        FilterItem(3, "Tempo desde de última compra"),
        /*
        FilterItem(4, "Vale-refeição", closeIcon = R.drawable.ic_baseline_keyboard_arrow_down_24),
        FilterItem(5, "Distância", closeIcon = R.drawable.ic_baseline_keyboard_arrow_down_24),
        FilterItem(6, "Entrega Parceria"),
        FilterItem(7, "Super Restaurante"),
        FilterItem(8, "Filtros", closeIcon = R.drawable.ic_baseline_filter_list_24),
         */
    )
    */

    override fun onResume() {
        sendToGetId()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        categoryAdapter.items = arrayListOf(
            Category(1, "https://findicons.com/files/icons/126/sleek_xp_basic/128/clients.png", "Cadastro de clientes", 0xFF950245),
            Category(2, "https://findicons.com/files/icons/42/basic/64/buy.png", "Cadastro de vendas", 0xFFE91D2D),
            Category(3, "https://heartofcodes.com/wp-content/uploads/2018/08/box-1.png", "Cadastro de produtos", 0xFF7a457d),
        )

        bannerAdapter.items = arrayListOf(
            Banner(1, "https://www.fbvcursos.com/blog/wp-content/uploads/2020/09/ilustracao-do-conceito-de-suporte-ativo_114360-577.jpg"),
            Banner(2, "https://i0.wp.com/makeitsimple.com.br/wp-content/uploads/2020/08/makeitsimple.com.br-atendimento20automatizado20via20whatsapp.png?resize=535%2C356&ssl=1"),
            Banner(3, "https://canalpromo.com.br/blog/uploads/images/image_650x433_6015b29a2606d.jpg"),
        )
/*
        shopAdapter.items = arrayListOf(
            Shop(1, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/46ebd05c-116e-41cd-b3de-7a05c5bc730a/201811071958_30656.jpg", "Pizza Crek"),
            Shop(2, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/bb3ad636-7c36-4ae2-a1db-14cd35695350/202001271029_rK15_i.png", "Fábrica de Esfiha"),
            Shop(3, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/2fd863ac-4cc2-476c-8896-99aedfdaeb5f/201911150948_Z9QG_i.jpg", "Pecorino"),
            Shop(4, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/86b58685-a7dc-4596-be26-2c4037b4d591/202006051304_JuRt_i.jpg", "Barbacoa Grill"),
            Shop(5, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/e2f3424a-06fb-46dd-89c3-f7b039e2b1f0_BOLOD_PPIN02.jpeg", "Bolo de Madre"),
            Shop(6, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/201901021647_8066dc64-9383-46d1-aa2d-56b9492e27ed.png", "Uau Esfiha"),
            Shop(7, "https://static-images.ifood.com.br/image/upload/t_high/logosgde/201705131248_0ca51a98-ee95-48ac-b193-48066c8f20cc.png", "Bar do Juarez"),
        )
*/
*/
        /*
        moreShopAdapter.items = arrayListOf(
            MoreOrder(1,"Ruan Donino","Pago",100.0,"20/12/21"),
            MoreOrder(2,"Donino","Aguardando pagamento",100.0,"20/12/21"),
            MoreOrder(3,"Valdair","Aguardando pagamento",1000.0,"30/12/21")
        )
         */
        sendToGetId()

        binding = FragmentOrderBinding.bind(view)

        binding?.let {
            /*
            it.rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.rvCategory.adapter = categoryAdapter

            it.rvBanners.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.rvBanners.adapter = bannerAdapter
            */
            //it.rvShops.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //it.rvShops.adapter = shopAdapter

            it.rvMoreOrders.layoutManager = LinearLayoutManager(requireContext())
            it.rvMoreOrders.adapter = moreShopAdapter
            /*
            it.rvBanners.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        notifyPositionChanged(recyclerView)
                    }
                }
            })
            */
            //addDots(it.dots, bannerAdapter.items.size, 0)
            /*
            filters.forEach { filter ->
                it.chipGroupFilter.addView(filter.toChip(requireContext()))
            }
            it.chipGroupFilter.setOnCheckedChangeListener { chipGroup, checkedId ->
                val titleOrNull = chipGroup.findViewById<Chip>(checkedId)?.text
                Toast.makeText(chipGroup.context, titleOrNull ?: "No Choice", Toast.LENGTH_LONG).show()
            }
             */
        }

    }

    private fun addDots(container: LinearLayout, size: Int, position: Int) {
        container.removeAllViews()

        Array(size) {
            val textView = TextView(context).apply {
                text = getString(R.string.dotted)
                textSize = 20f
                setTextColor(
                    if (position == it) ContextCompat.getColor(context, android.R.color.black)
                    else ContextCompat.getColor(context, android.R.color.darker_gray)
                )
            }
            container.addView(textView)
        }
    }

    private fun sendToGetId() {
        // Instantiate the RequestQueue.
        val sharedPreferences: SharedPreferences? = context?.getSharedPreferences("client_data",
            AppCompatActivity.MODE_PRIVATE
        )
        //val category_order = sharedPreferences.getString("category_order" , "No imported" )
        val id_loja = sharedPreferences?.getString("id_loja" , "No imported" )

        val queue = Volley.newRequestQueue(context)
        val url = "https://backend-proj-app-vc5xcezzwa-uc.a.run.app/api/v1/order/list/" +id_loja
        var id: Int;

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("get order", response.toString())
                //id = response["data"].toString().toInt()
                //Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
                order_list = createArrayOrder(response)
                moreShopAdapter.items = order_list
                binding?.progressBar1?.setVisibility(View.GONE)
                moreShopAdapter.notifyDataSetChanged()
                Log.d("order-list", order_list.toString())
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

    private fun createArrayOrder(arrayRequest: JSONArray): ArrayList<MoreOrder> {
        var arrayElements = arrayListOf<MoreOrder>()
        for (i in 0 until arrayRequest.length()) {
            var item_list = arrayRequest.getJSONArray(i)
            var elem = MoreOrder(
                item_list[0].toString().toInt(),
                item_list[6].toString(),
                "CONCLUIDA",
                item_list[3].toString().toDouble(),
                item_list[2].toString()
            )
            arrayElements.add(elem)
        }
        return arrayElements
    }

    private var position: Int? = RecyclerView.NO_POSITION
    private val snapHelper = LinearSnapHelper()

    /*
    private fun notifyPositionChanged(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        val view = snapHelper.findSnapView(layoutManager)
        val position = if (view == null) RecyclerView.NO_POSITION else layoutManager?.getPosition(view)

        val positionChanged = this.position != position
        if (positionChanged) {
            addDots(binding!!.dots, bannerAdapter.items.size, position ?: 0)
        }
        this.position = position

    }
     */

}
