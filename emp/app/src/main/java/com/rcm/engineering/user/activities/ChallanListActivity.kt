package com.rcm.engineering.user.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcm.engineering.user.adapter.ChallanAdapter
import com.rcm.engineering.user.databinding.ActivityChallanListBinding
import com.rcm.engineering.user.models.Challan
import com.rcm.engineering.user.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallanListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallanListBinding
    private lateinit var adapter: ChallanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallanListBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.toolbar.setNavigationOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }

        adapter = ChallanAdapter(mutableListOf(),
            onView = { challan ->
                Log.d("ChallanList", "View challan: ${challan.challanNo}")
            },
            btnPDF = { challan ->
                Log.d("ChallanList", "PDF download: ${challan.challanNo}")
            },
            btnExcel = {
                challan ->
                Log.d("ChallanList", "Excel download: ${challan.challanNo}")
            },
            btnCSV = {
                challan ->
                Log.d("ChallanList", "CSV download: ${challan.challanNo}")
            }
        )

        binding.recyclerViewChallans.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChallans.adapter = adapter
        fetchChallans()
    }

    private fun fetchChallans() {
        RetrofitClient.apiService.getChallans().enqueue(object : Callback<List<Challan>> {
            override fun onResponse(call: Call<List<Challan>>, response: Response<List<Challan>>) {
                if (response.isSuccessful) {
                    adapter.setList(response.body() ?: emptyList())
                }
            }
            override fun onFailure(call: Call<List<Challan>>, t: Throwable) {
                Log.e("API_ERROR", "Failed to fetch challans", t)
            }
        })
    }
}