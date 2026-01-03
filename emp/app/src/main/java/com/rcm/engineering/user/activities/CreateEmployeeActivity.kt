package com.rcm.engineering.user.activities

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
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

        binding.etDateOfBirth.setOnClickListener {
            showDatePicker(binding.etDateOfBirth)
        }

        binding.etDateOfJoining.setOnClickListener {
            showDatePicker(binding.etDateOfJoining)
        }

        val managerList = listOf("MANAGERS", "Chandra Veer", "Kishan Kumar", "Pravendra Kumar", "Manish Kumar", "Kamlendra Kumar")
        val designationsList = listOf("DESIGNATIONS", "Production Assistant", "Accounts Manager", "Senior Traub Setter", "Traub Machine Specialist", "Quality Analyst", "Production & Logistics Executive")
        val departmentsList = listOf("DEPARTMENTS", "Production", "Engineering", "Quality Control", "Finance & Accounts", "Logistics & Dispatch")

        val spinnerAdapterForManagers = ArrayAdapter(this, android.R.layout.simple_spinner_item, managerList)
        val spinnerAdapterForDesignations = ArrayAdapter(this, android.R.layout.simple_spinner_item, designationsList)
        val spinnerAdapterForDepartments = ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentsList)

        // Managers
        spinnerAdapterForManagers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spManager.adapter = spinnerAdapterForManagers

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
        binding.spDesignations.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedDesignations = designationsList[position]
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

        binding.ivBack.setOnClickListener { finish() }

        vm = ViewModelProvider(this)[EmployeeViewModel::class.java]
        ingEmployee = intent.getSerializableExtra(MainActivity.EXTRA_EMPLOYEE) as? Employee

        if (ingEmployee != null) {
            binding.etName.setText(ingEmployee!!.name)
            binding.etEmail.setText(ingEmployee!!.email)
            binding.etMobile.setText(ingEmployee!!.mobile)
            binding.etGender.setText(ingEmployee!!.gender)
            binding.etAddress.setText(ingEmployee!!.address)
            binding.etPostalCode.setText(ingEmployee!!.postalCode)
            //binding.etEmpCode.visibility = View.VISIBLE
            //binding.etEmpCode.setText(ingEmployee!!.empCode)
            //binding.etEmpCode.isEnabled = false

            binding.btnSave.text = "Update"
        } else {
            //binding.etEmpCode.visibility = View.GONE
            binding.btnSave.text = "Save"
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val mobile = binding.etMobile.text.toString().trim()
            val gender = binding.etGender.text.toString().trim()
            val manager = selectedManager
            val dateOfBirth = binding.etDateOfBirth.text.toString().trim()
            val dateOfJoining = binding.etDateOfJoining.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val postalCode = binding.etPostalCode.text.toString().trim()
            val department = selectedDepartments
            val designation = selectedDesignations
            val salary = binding.etSalary.text.toString().trim()
            val city = binding.etCity.text.toString().trim()
            val state = binding.etState.text.toString().trim()
            val country = binding.etCountry.text.toString().trim()
            val panNumber = binding.etPanNumber.text.toString().trim()
            val aadhaarNumber = binding.etAadhaarNumber.text.toString().trim()
            val bankAccountNumber = binding.etBankAccountNumber.text.toString().trim()
            val bankName = binding.etBankName.text.toString().trim()
            val ifscCode = binding.etIfscCode.text.toString().trim()

            if (manager == "MANAGERS") {
                Toast.makeText(this, "Please select Manager", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (designation == "DESIGNATIONS") {
                Toast.makeText(this, "Please select Designations", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (department == "DEPARTMENTS") {
                Toast.makeText(this, "Please select Departments", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Name & Email required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ohr = ingEmployee?.ohr ?: ""
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
                ohr = ohr,
                salary = salary,
                city = city,
                state = state,
                country = country,
                panNumber = panNumber,
                aadhaarNumber = aadhaarNumber,
                bankName = bankName,
                bankAccountNumber = bankAccountNumber,
                ifscCode = ifscCode
            )

            if (ingEmployee == null) vm.createUser(employee)
            else vm.updateEmployee(ohr, employee)

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

    private fun showDatePicker(target: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, { _, y, m, d ->
            val formatted = String.format("%02d-%02d-%04d", d, m + 1, y)
            target.setText(formatted)
        }, year, month, day).show()
    }
}