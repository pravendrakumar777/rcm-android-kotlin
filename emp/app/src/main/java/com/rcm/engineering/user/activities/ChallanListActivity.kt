package com.rcm.engineering.user.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
                downloadExcel(challan.id)
            },
            btnCSV = {
                challan ->
                downloadCsv(challan.id)
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

    // download challan pdf
    private fun downloadPdf(challanId: Long) {
        RetrofitClient.apiService.downloadPDFChallan(challanId).enqueue(object : Callback<ResponseBody> {
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

    // download challan excel
    private fun downloadExcel(challanId: Long) {
        RetrofitClient.apiService.downloadExcelChallan(challanId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        try {
                            val file = File(getExternalFilesDir(null), "challan_$challanId.xlsx")
                            body.byteStream().use { input ->
                                FileOutputStream(file).use { output ->
                                    input.copyTo(output)
                                }
                            }
                            openExcel(file)
                        } catch (e: Exception) {
                            Log.e("EXCEL_ERROR", "Failed to save EXCEL", e)
                        }
                    }
                } else {
                    Log.e("EXCEL_ERROR", "Response not successful")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("EXCEL_ERROR", "Download failed", t)
            }
        })
    }

    private fun openExcel(file: File) {
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
    }

    // download CSV
    private fun downloadCsv(challanId: Long) {
        RetrofitClient.apiService.downloadCSVChallan(challanId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        try {
                            val file = File(getExternalFilesDir(null), "challan_$challanId.csv")
                            body.byteStream().use { input ->
                                FileOutputStream(file).use { output ->
                                    input.copyTo(output)
                                }
                            }
                            openCsv(file)
                        } catch (e: Exception) {
                            Log.e("CSV_ERROR", "Failed to save CSV", e)
                        }
                    }
                } else {
                    Log.e("CSV_ERROR", "Response not successful")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("CSV_ERROR", "Download failed", t)
            }
        })
    }

    private fun openCsv(file: File) {
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "text/csv")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No app found to open CSV files", Toast.LENGTH_LONG).show()
        }
    }
}