package com.rcm.engineering.user.activities

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
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
    private var employees: List<Employee> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.RCM)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )
        binding.searchAutoComplete.setAdapter(adapter)

        binding.searchAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val selectedName = adapter.getItem(position)
            val employee = employees.find { it.name == selectedName || it.empCode == selectedName }
            employee?.let { bindEmployee(it) }
        }

        binding.buttonSearch.setOnClickListener {
            val query = binding.searchAutoComplete.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchEmployee(query)
            } else {
                Toast.makeText(this, "enter empCode ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchEmployee(query: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.searchEmployee(query)
                if (response.isSuccessful) {
                    val employee = response.body()
                    if (employee != null) {
                        bindEmployee(employee)
                        binding.cardEmployee.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(this@ProfilesActivity, "Employee not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProfilesActivity, "Search failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfilesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
