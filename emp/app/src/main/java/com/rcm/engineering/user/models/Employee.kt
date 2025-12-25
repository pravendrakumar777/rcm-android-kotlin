package com.rcm.engineering.user.models

import java.io.Serializable

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
    val empCode: String,
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
    val photoUrl: String? = null,

    val workingDays: Int? = null,
    val presentDays: Int? = null,
    val absentDays: Int? = null,
    val privilegeLeaves: Int? = null,
    val optionalLeaves: Int? = null

) : Serializable
