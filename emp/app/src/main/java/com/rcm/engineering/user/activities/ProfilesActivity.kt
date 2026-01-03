package com.rcm.engineering.user.activities

import android.os.Bundle
import android.util.Log
import android.view.View
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
            val employee = employees.find { it.name == selectedName || it.ohr == selectedName }
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
                Log.d("ProfilesActivity", "fetchEmployee() called with query: $query")

                val response = RetrofitClient.apiService.searchEmployee(query)
                Log.d("ProfilesActivity", "Response received. isSuccessful=${response.isSuccessful}, code=${response.code()}")

                if (response.isSuccessful) {
                    val employee = response.body()
                    Log.d("ProfilesActivity", "Response body: $employee")

                    if (employee != null) {
                        Log.i("ProfilesActivity", "Employee found: id=${employee.id}, name=${employee.name}")
                        bindEmployee(employee)
                        binding.cardEmployee.visibility = View.VISIBLE
                    } else {
                        Log.w("ProfilesActivity", "Employee body is null for query=$query")
                        Toast.makeText(this@ProfilesActivity, "Employee not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("ProfilesActivity", "Search failed. HTTP ${response.code()} - ${response.message()}")
                    Toast.makeText(this@ProfilesActivity, "Search failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("ProfilesActivity", "Exception in fetchEmployee(query=$query)", e)
                Toast.makeText(this@ProfilesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindEmployee(employee: Employee) {
        binding.textName.text = employee.name
        binding.textEmail.text = employee.email
        binding.textOhr.text = employee.ohr
        binding.textMobile.text = employee.mobile
        binding.textDepartment.text = employee.department
        binding.textDesignation.text = employee.designation
        binding.textCity.text = employee.city
    }
}
