package com.rcm.engineering.user.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcm.engineering.user.MainActivity
import com.rcm.engineering.user.R
import com.rcm.engineering.user.adapter.EmployeeAdapter
import com.rcm.engineering.user.databinding.ActivityEmployeeSearchBinding
import com.rcm.engineering.user.service.RetrofitClient
import kotlinx.coroutines.launch

class EmployeeSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeSearchBinding
    private lateinit var adapter: EmployeeAdapter
    private val api = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.RCM)
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        adapter = EmployeeAdapter(
            mutableListOf(),
            { employee ->
                val intent = Intent(this, ProfilesActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_EMPLOYEE, employee)
                startActivity(intent)
            },
            { employee ->
                Toast.makeText(this, "Delete ${employee.name}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerEmployees.layoutManager = LinearLayoutManager(this)
        binding.recyclerEmployees.adapter = adapter

        lifecycleScope.launch {
            try {
                val response = api.fetchAllEmployee()
                if (response.isSuccessful) {
                    adapter.setList(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@EmployeeSearchActivity, "Failed to fetch employees", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EmployeeSearchActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchInput.addTextChangedListener { query ->
            adapter.filter(query.toString())
        }
    }
}