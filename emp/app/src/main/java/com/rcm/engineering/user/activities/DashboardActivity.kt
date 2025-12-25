package com.rcm.engineering.user.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rcm.engineering.user.databinding.ActivityDashboardBinding
import com.rcm.engineering.user.response.DashboardResponse
import com.github.mikephil.charting.data.*
import com.rcm.engineering.user.service.RetrofitClient.apiService
import kotlinx.coroutines.launch
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { finish() }
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        lifecycleScope.launch {
            try {
                val response: Response<DashboardResponse> = apiService.getDashboard()
                val data = response.body()
                if (response.isSuccessful && data != null && data.totalEmployees > 0) {
                    updateUI(data)
                } else {
                    showMockDashboard()
                }
            } catch (e: Exception) {
                showMockDashboard()
            }
        }
    }

    private fun showMockDashboard() {
        val mockData = DashboardResponse(
            totalEmployees = 33,
            present = 33,
            absent = 0,
            checkInData = mapOf("L" to 4f, "M" to 6f, "M" to 5f, "J" to 7f, "V" to 3f, "S" to 2f),
            checkOutData = mapOf("L" to 3f, "M" to 5f, "M" to 4f, "J" to 6f, "V" to 2f, "S" to 1f),
            statusDistribution = mapOf("Active" to 30, "Inactive" to 2, "On Leave" to 1)
        )
        updateUI(mockData)
        //Toast.makeText(this, "Showing mock dashboard data", Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(data: DashboardResponse) {
        binding.tvTotalEmployees.text = "Total: ${data.totalEmployees}"
        binding.tvPresent.text = "Present: ${data.present}"
        binding.tvAbsent.text = "Absent: ${data.absent}"
        setupPieChart(data.present, data.absent)
        setupBarChart(data.checkInData, data.checkOutData)
        setupStatusChart(data.statusDistribution)
    }

    private fun setupPieChart(present: Int, absent: Int) {
        val entries = mutableListOf<PieEntry>()
        if (present > 0) entries.add(PieEntry(present.toFloat(), "Present"))
        if (absent > 0) entries.add(PieEntry(absent.toFloat(), "Absent"))

        if (entries.isEmpty()) {
            binding.pieChart.clear()
            binding.pieChart.setNoDataText("No attendance data available")
            return
        }

        val darkGreen = Color.parseColor("#388E3C")
        val dataSet = PieDataSet(entries, "Attendance Summary").apply {
            colors = when {
                absent == 0 -> listOf(darkGreen)
                present == 0 -> listOf(Color.RED)
                else -> listOf(darkGreen, Color.RED)
            }
            valueTextColor = Color.BLACK
            valueTextSize = 14f
            sliceSpace = 2f
        }

        binding.pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            centerText = "Attendance"
            setCenterTextSize(16f)
            setEntryLabelColor(Color.BLACK)
            legend.isEnabled = true
            legend.textColor = Color.BLACK
            animateY(1000)
            invalidate()
        }
    }

    private fun setupBarChart(checkIn: Map<String, Float>, checkOut: Map<String, Float>) {
        val checkInEntries = checkIn.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value)
        }
        val checkOutEntries = checkOut.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value)
        }

        val darkGreen = Color.parseColor("#388E3C")
        val amberYellow = Color.parseColor("#FFC107")
        val dataSet1 = BarDataSet(checkInEntries, "Check-In").apply { color = darkGreen }
        val dataSet2 = BarDataSet(checkOutEntries, "Check-Out").apply { color = amberYellow }

        binding.barChart.apply {
            data = BarData(dataSet1, dataSet2)
            description.isEnabled = false
            setDrawGridBackground(false)
            setFitBars(true)
            legend.isEnabled = true
            animateY(1000)
            invalidate()
        }
    }

    val darkGreen = Color.parseColor("#388E3C")
    val amberYellow = Color.parseColor("#FFC107")
    private fun setupStatusChart(status: Map<String, Int>) {
        val entries = status.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }
        val dataSet = BarDataSet(entries, "Employee Status").apply {
            colors = listOf(darkGreen, amberYellow, Color.GRAY)
        }

        binding.statusChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            setDrawGridBackground(false)
            setFitBars(true)
            legend.isEnabled = true
            animateY(1000)
            invalidate()
        }
    }
}