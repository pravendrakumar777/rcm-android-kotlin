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
    val empCode: String
) : Serializable
