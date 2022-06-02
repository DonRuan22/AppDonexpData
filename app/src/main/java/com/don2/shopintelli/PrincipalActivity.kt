package com.don2.shopintelli

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.billingclient.api.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.don2.shopintelli.databinding.ActivityPrincipalBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import okhttp3.internal.wait
import org.json.JSONObject

class PrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding
    private lateinit var mAuth: FirebaseAuth
    private  var billingClient: BillingClient?=null
    private  var skuDetails: SkuDetails?=null
    private  var user_state: String ="normal"
    lateinit var mAdView : AdView

    lateinit var mGoogleSignInClient: GoogleSignInClient
    // val auth is initialized by lazy
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrincipalBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        MobileAds.initialize(this) {}
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        var res_user : String


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_new))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
        val edit = sharedPreferences.edit()
        edit.putString("user_id", "null")
        edit.putString("id_loja", "null")
        edit.apply()

        binding.logout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent= Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        //sharedPreferences.edit().remove("user_id").commit()
        val user_id = sharedPreferences.getString("user_id" , "No imported" )
        if(user_id == "No imported" || user_id == "null") {
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
                        setupViews()
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
        else{
            setupViews()
        }
        val client_id_new = sharedPreferences.getString("user_id" , "No imported" )
        Toast.makeText(this, client_id_new.toString(), Toast.LENGTH_LONG).show()
        binding.nameUser.text = user?.displayName

        //setupViews()
        setUpBillingClient()
        initListeners()
        GlobalScope.launch {
            //startConnection()
            listPurchase()
            withContext (Dispatchers.Main) {
                //update the UI
                user_state = sharedPreferences.getString("user_state", "normal").toString()
                if(user_state == "premium") {
                    showUIElements()
                }
            }
        }




    }


    private suspend fun listPurchase(){
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        Thread.sleep(500)
        val queryPurchasesAsync = billingClient?.queryPurchasesAsync(BillingClient.SkuType.SUBS)
        Log.v("TAG_LIST",queryPurchasesAsync?.purchasesList.toString())
        if (queryPurchasesAsync != null) {
            if(!queryPurchasesAsync.purchasesList.isEmpty()) {
                val userStatePurchase = queryPurchasesAsync?.purchasesList?.get(0)?.purchaseState
                val userStatePurchase_state = queryPurchasesAsync?.purchasesList?.get(0)?.isAutoRenewing
                Log.v("TAG_LIST2", userStatePurchase.toString())
                Log.v("TAG_LIST_CAN", userStatePurchase_state.toString())
                if (userStatePurchase == 1 && userStatePurchase_state == true) {
                    edit.putString("user_state", "premium")
                    edit.apply()
                } else {
                    edit.putString("user_state", "normal")
                    edit.apply()
                }
            }
            else{
                edit.putString("user_state", "normal")
                edit.apply()
            }
        }



    }


    private fun setupViews() {
        val tabLayout = binding.addTab
        val viewPager = binding.addViewpager
        val adapter = TabViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(adapter.tabs[position])
        }.attach()
    }

    private fun initListeners() {
        binding.premium?.setOnClickListener {
            // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
            // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
            Log.v("TAG_PREM","Click Premium")
            skuDetails?.let {
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(it)
                    .build()
                billingClient?.launchBillingFlow(this, billingFlowParams)?.responseCode
            }?:noSKUMessage()

        }
    }

    private fun noSKUMessage() {

    }

    private fun startConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    Log.v("TAG_INAPP","Setup Billing Done")
                    // The BillingClient is ready. You can query purchases here.
                    queryAvaliableProducts()
                }
            }
            override fun onBillingServiceDisconnected() {
                Log.v("TAG_INAPP","Billing client Disconnected")
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun setUpBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
        startConnection()
    }

    private fun queryAvaliableProducts() {
        val skuList = ArrayList<String>()
        skuList.add("subs_ilimited_users")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        billingClient?.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            // Process the result.
            Log.v("TAG_ERROR",skuDetailsList.toString())
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                for (skuDetails in skuDetailsList) {
                    Log.v("TAG_INSUB","skuDetailsList : ${skuDetailsList}")
                    //This list should contain the products added above
                    updateUI(skuDetails)
                }
            }
        }
    }

    private fun updateUI(skuDetails: SkuDetails?) {
        skuDetails?.let {
            this.skuDetails = it
            binding.catUser?.text = "sub"
            //showUIElements()
        }
    }

    private fun showUIElements() {
        binding.catUser?.text = "Premium"
        binding.catUser?.visibility = View.VISIBLE
        binding.premium?.visibility = View.GONE
        binding.adView?.visibility = View.GONE
    }


    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            Log.v("TAG_INAPP","billingResult responseCode : ${billingResult.responseCode}")

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    handleNonConsumablePurchase(purchase)
                    //handleConsumedPurchases(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }

    private fun handleConsumedPurchases(purchase: Purchase) {
        Log.d("TAG_INAPP", "handleConsumablePurchasesAsync foreach it is $purchase")
        val params =
            ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        billingClient?.consumeAsync(params) { billingResult, purchaseToken ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    // Update the appropriate tables/databases to grant user the items
                    Log.d(
                        "TAG_INAPP",
                        " Update the appropriate tables/databases to grant user the items"
                    )
                    val sharedPreferences: SharedPreferences = this.getSharedPreferences("client_data", MODE_PRIVATE)
                    val edit = sharedPreferences.edit()
                    edit.putString("user_state", "premium")
                    edit.apply()
                    user_state = sharedPreferences.getString("user_state" , "0" ).toString()
                }
                else -> {
                    Log.w("TAG_INAPP", billingResult.debugMessage)
                }
            }
        }
    }

    private fun handleNonConsumablePurchase(purchase: Purchase) {
        Log.v("TAG_INAPP","handlePurchase : ${purchase}")
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    val billingResponseCode = billingResult.responseCode
                    val billingDebugMessage = billingResult.debugMessage

                    Log.v("TAG_INAPP","response code: $billingResponseCode")
                    Log.v("TAG_INAPP","debugMessage : $billingDebugMessage")

                }
            }
        }
    }



}


class TabViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    val tabs = arrayOf(R.string.clients, R.string.orders, R.string.products)
    val fragments = arrayOf(RestaurantFragment(), OrderFragment(), ProductFragment())

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}


class MarketplaceFragment : Fragment() {}




    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
    }
}
*/
