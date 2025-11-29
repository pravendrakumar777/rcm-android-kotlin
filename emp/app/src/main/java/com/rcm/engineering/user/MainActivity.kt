package com.rcm.engineering.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcm.engineering.user.activities.CreateEmployeeActivity
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[EmployeeViewModel::class.java]

        adapter = EmployeeAdapter(
            mutableListOf(),
            on = { open(it) },
            onDelete = { vm.deleteEmployee(it.id!!) })
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, CreateEmployeeActivity::class.java))
        }

        binding.swipeRefresh.setOnRefreshListener {
            vm.fetchAllEmployees()
            binding.swipeRefresh.isRefreshing = false
        }

        vm.users.observe(this) { list ->
            adapter.setList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        vm.error.observe(this) { err -> Toast.makeText(this, err, Toast.LENGTH_SHORT).show() }
        vm.responseMessage.observe(this) { msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }

        vm.users.observe(this) { list ->
            adapter.setList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

            binding.tvTotalEmployees.text = "Total Employees: ${list.size}"
        }
    }

    override fun onResume() {
        super.onResume()
        vm.fetchAllEmployees()
    }

    private fun open(employee: Employee) {
        val i = Intent(this, CreateEmployeeActivity::class.java)
        i.putExtra(EXTRA_EMPLOYEE, employee)
        startActivity(i)
    }
}