package com.rcm.engineering.user.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rcm.engineering.user.R
import com.rcm.engineering.user.databinding.ActivityAttendanceBinding
import com.rcm.engineering.user.models.Employee
import com.rcm.engineering.user.response.AttendanceResponse
import com.rcm.engineering.user.service.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceBinding
    private val api = RetrofitClient.apiService
    private var selectedOhr: String? = null
    private val attendanceState = mutableMapOf<String, Pair<LocalDateTime?, LocalDateTime?>>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }

        binding.btnCheckout.isEnabled = false
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        lifecycleScope.launch {
            val response = api.fetchAllEmployee()
            if (response.isSuccessful) {
                val employees = response.body() ?: emptyList()
                setupEmployeeSpinner(employees)
            }
        }

        binding.btnCheckin.setOnClickListener {
            if (selectedOhr == null) {
                Toast.makeText(this, "Select employee first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkInTime = LocalDateTime.now()
            attendanceState[selectedOhr!!] = Pair(checkInTime, null)
            setAttendanceState(true)
            binding.tvLastCheckIn.text = "Last Check-In: ${formatTime(checkInTime)}"
            binding.tvHoursWorked.text = "Hours Worked: 00:00 Hrs"
            callMarkAttendance("PRESENT", checkInTime, null)
        }

        binding.btnCheckout.setOnClickListener {
            if (selectedOhr == null) {
                Toast.makeText(this, "Select employee first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val state = attendanceState[selectedOhr!!]
            val checkInTime = state?.first
            if (checkInTime == null) {
                Toast.makeText(this, "No check-in found for this employee", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkOutTime = LocalDateTime.now()
            attendanceState[selectedOhr!!] = Pair(checkInTime, checkOutTime)

            setAttendanceState(false)
            binding.tvLastCheckOut.text = "Last Check-Out: ${formatTime(checkOutTime)}"

            val diff = Duration.between(checkInTime, checkOutTime)
            val hours = diff.toHours()
            val minutes = diff.toMinutes() % 60
            binding.tvHoursWorked.text = "Hours Worked: %02d:%02d Hrs".format(hours, minutes)
            callMarkAttendance("PRESENT", checkInTime, checkOutTime)
        }
    }

    private fun setupEmployeeSpinner(employees: List<Employee>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            employees.map { "${it.name} (${it.ohr})" }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEmployees.adapter = adapter

        binding.spinnerEmployees.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedOhr = employees[position].ohr
                updateUIForEmployee(selectedOhr!!)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedOhr = null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTime(time: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return time.format(formatter)
    }

    private fun setAttendanceState(isCheckedIn: Boolean) {
        binding.btnCheckin.isEnabled = !isCheckedIn
        binding.btnCheckout.isEnabled = isCheckedIn
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callMarkAttendance(status: String, checkIn: LocalDateTime?, checkOut: LocalDateTime?) {
        val today = LocalDate.now().toString()
        val checkInStr = checkIn?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        val checkOutStr = checkOut?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))

        val call: Call<AttendanceResponse> = api.markAttendance(
            ohr = selectedOhr!!,
            date = today,
            status = status,
            checkInDateTime = checkInStr,
            checkOutDateTime = checkOutStr
        )

        call.enqueue(object : Callback<AttendanceResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<AttendanceResponse>, response: Response<AttendanceResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val checkInApi = body?.checkIn?.let {
                        LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                    }
                    val checkOutApi = body?.checkOut?.let {
                        LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                    }

                    // Update UI with API times
                    if (checkInApi != null) {
                        binding.tvLastCheckIn.text = "Last Check-In: ${formatTime(checkInApi)}"
                    }
                    if (checkOutApi != null) {
                        binding.tvLastCheckOut.text = "Last Check-Out: ${formatTime(checkOutApi)}"
                        val diff = Duration.between(checkInApi, checkOutApi)
                        binding.tvHoursWorked.text =
                            "Hours Worked: %02d:%02d Hrs".format(diff.toHours(), diff.toMinutes() % 60)
                    } else {
                        binding.tvHoursWorked.text = "Hours Worked: 00:00 Hrs"
                    }

                    Toast.makeText(this@AttendanceActivity, "Marked: ${body?.status}", Toast.LENGTH_SHORT).show()
                } else {
                    handleError(response.code())
                }
            }

            override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                Toast.makeText(this@AttendanceActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun handleError(code: Int) {
        val message = when (code) {
            404 -> "Employee not found"
            409 -> "Attendance already marked"
            else -> "Error: $code"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUIForEmployee(empCode: String) {
        val state = attendanceState[empCode]
        if (state == null) {
            setAttendanceState(false)
            binding.tvLastCheckIn.text = "Last Check-In: --"
            binding.tvLastCheckOut.text = "Last Check-Out: N/A"
            binding.tvHoursWorked.text = "Hours Worked: 00:00 Hrs"

        } else {
            val (checkIn, checkOut) = state
            if (checkIn != null && checkOut == null) {
                binding.tvLastCheckIn.text = "Check-In Time: ${formatTime(checkIn)}"
                binding.tvLastCheckOut.text = "Check-Out Time: N/A"
                binding.tvHoursWorked.text = "Hours Worked: 00:00 Hrs"
                setAttendanceState(true)
            } else if (checkIn != null && checkOut != null) {
                binding.tvLastCheckIn.text = "Last Check-In: ${formatTime(checkIn)}"
                binding.tvLastCheckOut.text = "Last Check-Out: ${formatTime(checkOut)}"
                val diff = Duration.between(checkIn, checkOut)
                binding.tvHoursWorked.text =
                    "Hours Worked: %02d:%02d Hrs".format(diff.toHours(), diff.toMinutes() % 60)
                setAttendanceState(false)
            }
        }
    }
}