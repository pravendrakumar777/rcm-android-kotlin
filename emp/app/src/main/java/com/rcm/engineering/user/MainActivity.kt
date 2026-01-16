package com.rcm.engineering.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcm.engineering.user.activities.AttendanceActivity
import com.rcm.engineering.user.activities.ChallanListActivity
import com.rcm.engineering.user.activities.CreateEmployeeActivity
import com.rcm.engineering.user.activities.DashboardActivity
import com.rcm.engineering.user.activities.ProfilesActivity
import com.rcm.engineering.user.adapter.EmployeeAdapter
import com.rcm.engineering.user.databinding.ActivityMainBinding
import com.rcm.engineering.user.models.Employee
import com.rcm.engineering.user.views.EmployeeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var vm: EmployeeViewModel
    private lateinit var adapter: EmployeeAdapter

    companion object {
        const val EXTRA_EMPLOYEE = "extra_employee"
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[EmployeeViewModel::class.java]
        Log.d(TAG, "EmployeeViewModel initialized")

        // RecyclerView + Adapter setup
        adapter = EmployeeAdapter(
            mutableListOf(),
            on = { employee -> open(employee) },
            onDelete = { employee -> vm.deleteEmployee(employee.id!!) }
        )
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        // add new emp
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, CreateEmployeeActivity::class.java))
        }

        // Swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            vm.fetchAllEmployees()
            binding.swipeRefresh.isRefreshing = false
        }

        // Observers
        vm.users.observe(this) { list ->
            adapter.setList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            binding.tvTotalEmployees.text = "Total Employees: ${list.size}"
        }

        vm.error.observe(this) { err ->
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
        }

        vm.responseMessage.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Back button
        binding.ivBack.setOnClickListener { finish() }

        // Bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_attendance -> {
                    startActivity(Intent(this, AttendanceActivity::class.java)); true
                }
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java)); true
                }
                R.id.nav_reports -> {
                    startActivity(Intent(this, ChallanListActivity::class.java)); true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfilesActivity::class.java)); true
                }
                else -> false
            }
        }

        // Load employees
        vm.fetchAllEmployees()
    }

    private fun open(employee: Employee) {
        val i = Intent(this, CreateEmployeeActivity::class.java)
        i.putExtra(EXTRA_EMPLOYEE, employee)
        startActivity(i)
    }
}