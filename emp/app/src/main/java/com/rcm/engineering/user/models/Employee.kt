package com.rcm.engineering.user.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Employee(
    val id: Long? = null,
    val name: String,
    val email: String,
    val mobile: String,
    val gender: String,
    val manager: String,
    val dateOfBirth: String,
    val dateOfJoining: String,
    val address: String,
    val postalCode: String,
    val department: String,
    val designation: String,
    val ohr: String,
    val salary: String,
    //
    val city: String,
    val state: String,
    val country: String,
    val panNumber: String,
    val aadhaarNumber: String,
    val bankName: String,
    val bankAccountNumber: String,
    val ifscCode: String,
    val photo: String? = null,
    val workingDays: Int? = null,
    val presentDays: Int? = null,
    val absentDays: Int? = null,
    val privilegeLeaves: Int? = null,
    val optionalLeaves: Int? = null

) : Serializable {
    val formattedDob: String
        get() = formatDate(dateOfBirth)

    val formattedJoining: String
        get() = formatDate(dateOfJoining)

    private fun formatDate(dateString: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val output = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            output.format(input.parse(dateString)!!)
        } catch (e: Exception) {
            dateString
        }
    }
}
