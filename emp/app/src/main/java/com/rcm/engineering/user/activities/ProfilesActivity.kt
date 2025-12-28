package com.rcm.engineering.user.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rcm.engineering.user.R
import com.rcm.engineering.user.databinding.ActivityProfileBinding
import com.rcm.engineering.user.models.Employee
import com.rcm.engineering.user.service.RetrofitClient
import kotlinx.coroutines.launch

class ProfilesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.RCM)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Fetch from API
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.fetchAllEmployee()
                if (response.isSuccessful) {
                    val employees: List<Employee> = response.body() ?: emptyList()
                    if (employees.isNotEmpty()) {
                        val employee = employees.first()
                        bindEmployee(employee)
                    } else {
                        Toast.makeText(this@ProfilesActivity, "No employees found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@ProfilesActivity, "Failed to fetch employees", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfilesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun bindEmployee(employee: Employee) {
        binding.textName.text = employee.name
        binding.textEmail.text = employee.email
        binding.textCode.text = employee.empCode
        binding.textMobile.text = employee.mobile
        binding.textDepartment.text = employee.department
        binding.textDesignation.text = employee.designation
        binding.textManager.text = employee.manager
        binding.textDob.text = employee.dateOfBirth
        binding.textDoj.text = employee.dateOfJoining
        binding.textAadhaar.text = employee.aadhaarNumber
        binding.textCity.text = employee.city
        binding.textState.text = employee.state
    }
}