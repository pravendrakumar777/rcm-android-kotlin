package com.rcm.engineering.user.activities

import android.os.Bundle
import android.view.View
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

        if (ingEmployee != null) {
            // ------- Edit Mode -------
            binding.etName.setText(ingEmployee!!.name)
            binding.etEmail.setText(ingEmployee!!.email)
            binding.etMobile.setText(ingEmployee!!.mobile)
            binding.etGender.setText(ingEmployee!!.gender)

            binding.etManager.setText(ingEmployee!!.manager)
            binding.etDateOfBirth.setText(ingEmployee!!.dateOfBirth)
            binding.etDateOfJoining.setText(ingEmployee!!.dateOfJoining)
            binding.etAddress.setText(ingEmployee!!.address)
            binding.etPostalCode.setText(ingEmployee!!.postalCode)
            binding.etDepartment.setText(ingEmployee!!.department)
            binding.etDesignation.setText(ingEmployee!!.designation)

            // Show EMP CODE & disable editing
            binding.etEmpCode.visibility = View.VISIBLE
            binding.etEmpCode.setText(ingEmployee!!.empCode)
            binding.etEmpCode.isEnabled = false

            binding.btnSave.text = "Update"
        } else {
            // ------- Create Mode -------
            binding.etEmpCode.visibility = View.GONE
            binding.btnSave.text = "Save"
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

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Name & Email required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // empCode: null for create | value for edit
            val empCode = ingEmployee?.empCode ?: ""

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
            else vm.updateEmployee(empCode, employee)

            finish()
        }
    }
}