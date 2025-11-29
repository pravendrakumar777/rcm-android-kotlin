package com.rcm.engineering.user.activities

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rcm.engineering.user.MainActivity
import com.rcm.engineering.user.databinding.ActivityCreateEditEmployeeBinding
import com.rcm.engineering.user.models.Employee
import com.rcm.engineering.user.views.EmployeeViewModel
import java.util.Locale

class CreateEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEditEmployeeBinding
    private lateinit var vm: EmployeeViewModel
    private var ingEmployee: Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val managerList = listOf("Select Manager", "Chandra Veer", "Kishan Kumar", "Pravendra Kumar", "Manish Rajput", "Kamlendra Kumar")
        val designationsList = listOf("Select Designations", "Production Assistant", "Accounts Manager", "Senior Traub Setter", "Traub Machine Specialist", "Quality Analyst", "Production & Logistics Executive")
        val departmentsList = listOf("Select Departments", "Production", "Engineering", "Quality Control", "Finance & Accounts", "Logistics & Dispatch")

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, managerList)
        val spinnerAdapterForDesignations = ArrayAdapter(this, android.R.layout.simple_spinner_item, designationsList)
        val spinnerAdapterForDepartments = ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentsList)

        // Managers
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spManager.adapter = spinnerAdapter

        var selectedManager = ""
        binding.spManager.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedManager = managerList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Designations
        spinnerAdapterForDesignations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDesignations.adapter = spinnerAdapterForDesignations

        var selectedDesignations = ""
        binding.spManager.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedManager = designationsList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Departments
        spinnerAdapterForDepartments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDepartments.adapter = spinnerAdapterForDepartments

        var selectedDepartments = ""
        binding.spDepartments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedDepartments = departmentsList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        vm = ViewModelProvider(this)[EmployeeViewModel::class.java]
        ingEmployee = intent.getSerializableExtra(MainActivity.EXTRA_EMPLOYEE) as? Employee

        if (ingEmployee != null) {
            binding.etName.setText(ingEmployee!!.name)
            binding.etEmail.setText(ingEmployee!!.email)
            binding.etMobile.setText(ingEmployee!!.mobile)
            binding.etGender.setText(ingEmployee!!.gender)
            binding.etDateOfBirth.setText(ingEmployee!!.dateOfBirth)
            binding.etDateOfJoining.setText(ingEmployee!!.dateOfJoining)
            binding.etAddress.setText(ingEmployee!!.address)
            binding.etPostalCode.setText(ingEmployee!!.postalCode)
            binding.etEmpCode.visibility = View.VISIBLE
            binding.etEmpCode.setText(ingEmployee!!.empCode)
            binding.etEmpCode.isEnabled = false

            binding.btnSave.text = "Update"
        } else {
            binding.etEmpCode.visibility = View.GONE
            binding.btnSave.text = "Save"
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val mobile = binding.etMobile.text.toString().trim()
            val gender = binding.etGender.text.toString().trim()
            val manager = selectedManager
            val dateOfBirth = convertToApiFormat(binding.etDateOfBirth.text.toString().trim())
            val dateOfJoining = convertToApiFormat(binding.etDateOfJoining.text.toString().trim())
            val address = binding.etAddress.text.toString().trim()
            val postalCode = binding.etPostalCode.text.toString().trim()
            val department = selectedDepartments
            val designation = selectedDesignations
            val salary = binding.etSalary.text.toString().trim()

            if (manager == "Select Manager") {
                Toast.makeText(this, "Please select Manager", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (designation == "Select Designations") {
                Toast.makeText(this, "Please select Designations", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (department == "Select Departments") {
                Toast.makeText(this, "Please select Departments", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Name & Email required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val empCode = ingEmployee?.empCode ?: ""
            val employee = Employee(
                id = ingEmployee?.id,
                name = name,
                email = email,
                mobile = mobile,
                gender = gender,
                manager = manager,
                dateOfBirth = convertToApiFormat(dateOfBirth),
                dateOfJoining = convertToApiFormat(dateOfJoining),
                address = address,
                postalCode = postalCode,
                department = department,
                designation = designation,
                empCode = empCode,
                salary = salary
            )

            if (ingEmployee == null) vm.createUser(employee)
            else vm.updateEmployee(empCode, employee)

            finish()
        }
    }

    private fun convertToApiFormat(date: String): String {
        return try {
            val input = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            output.format(input.parse(date)!!)
        } catch (e: Exception) {
            date
        }
    }
}