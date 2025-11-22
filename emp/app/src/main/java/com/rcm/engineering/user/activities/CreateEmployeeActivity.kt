package com.rcm.engineering.user.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rcm.engineering.user.MainActivity
import com.rcm.engineering.user.databinding.ActivityCreateEditEmployeeBinding
import com.rcm.engineering.user.models.Employee
import com.rcm.engineering.user.views.EmployeeViewModel

class CreateEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEditEmployeeBinding
    private lateinit var vm: EmployeeViewModel
    private var ingEmployee: Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[EmployeeViewModel::class.java]

        ingEmployee = intent.getSerializableExtra(MainActivity.EXTRA_EMPLOYEE) as? Employee
        ingEmployee?.let {
            binding.etName.setText(it.name)
            binding.etEmail.setText(it.email)
            binding.etMobile.setText(it.mobile)
            binding.etGender.setText(it.gender)

            binding.etManager.setText(it.manager)
            binding.etDateOfBirth.setText(it.dateOfBirth)
            binding.etDateOfJoining.setText(it.dateOfJoining)
            binding.etAddress.setText(it.address)
            binding.etPostalCode.setText(it.postalCode)
            binding.etDepartment.setText(it.department)
            binding.etDesignation.setText(it.designation)

            binding.btnSave.text = "Update"
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val mobile = binding.etMobile.text.toString().trim()
            val gender = binding.etGender.text.toString().trim()
            val manager = binding.etManager.text.toString().trim()
            val dateOfBirth = binding.etDateOfBirth.text.toString().trim()
            val dateOfJoining = binding.etDateOfJoining.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val postalCode = binding.etPostalCode.text.toString().trim()
            val department = binding.etDepartment.text.toString().trim()
            val designation = binding.etDesignation.text.toString().trim()
            val empCode = binding.etEmpCode.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Name & Email required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val employee = Employee(
                id = ingEmployee?.id,
                name = name,
                email = email,
                mobile = mobile,
                gender = gender,
                manager = manager,
                dateOfBirth = dateOfBirth,
                dateOfJoining = dateOfJoining,
                address = address,
                postalCode = postalCode,
                department = department,
                designation = designation,
                empCode = empCode

            )

            if (ingEmployee == null) vm.createUser(employee)
            else vm.updateEmployee(ingEmployee!!.empCode!!, employee)

            finish()
        }
    }
}