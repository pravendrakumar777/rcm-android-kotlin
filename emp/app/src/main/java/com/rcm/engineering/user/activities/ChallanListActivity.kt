package com.rcm.engineering.user.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcm.engineering.user.adapter.ChallanAdapter
import com.rcm.engineering.user.databinding.ActivityChallanListBinding
import com.rcm.engineering.user.models.Challan
import com.rcm.engineering.user.service.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ChallanListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallanListBinding
    private lateinit var adapter: ChallanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallanListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter = ChallanAdapter(mutableListOf(),
            onView = { challan ->
                Log.d("ChallanList", "View challan: ${challan.challanNo}")
            },
            btnPDF = { challan ->
                downloadPdf(challan.id)
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

    private fun downloadPdf(challanId: Long) {
        RetrofitClient.apiService.downloadChallan(challanId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        try {
                            val file = File(getExternalFilesDir(null), "challan_$challanId.pdf")
                            body.byteStream().use { input ->
                                FileOutputStream(file).use { output ->
                                    input.copyTo(output)
                                }
                            }
                            openPdf(file)
                        } catch (e: Exception) {
                            Log.e("PDF_ERROR", "Failed to save PDF", e)
                        }
                    }
                } else {
                    Log.e("PDF_ERROR", "Response not successful")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("PDF_ERROR", "Download failed", t)
            }
        })
    }

    private fun openPdf(file: File) {
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
    }
}